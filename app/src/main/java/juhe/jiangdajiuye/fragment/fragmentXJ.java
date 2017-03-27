package juhe.jiangdajiuye.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.HashMap;

import juhe.jiangdajiuye.InterFace.myitemLister;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.recyclerAdapter;
import juhe.jiangdajiuye.tool.NetState;
import juhe.jiangdajiuye.tool.parseTools;
import juhe.jiangdajiuye.util.urlConnection;
import juhe.jiangdajiuye.view.browse;

/**
 * 宣讲
 * Created by wangqiang on 2016/9/27.
 */
public class fragmentXJ extends Fragment{
    private View view,error,view1;
    private String TAG = "fragmentXJ";
    private String url = "http://ujs.91job.gov.cn/teachin/index";
    private Boolean isfirst = true;
    private NetState netState;
    private int page = 1;
    public RecyclerView recyclerView;
    private LinearLayoutManager manager;

    public ArrayList<HashMap<String,String>>  date = new ArrayList<>();
    public recyclerAdapter adapter;
    //下拉刷新
    private Boolean isPull = false;
    private Boolean swipInit = false;
    private Boolean refreshing = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private parseTools parsetools =  parseTools.getparseTool() ;
    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 0x1:
                    if(isfirst){
                        isfirst = false;
                        error.setVisibility(View.GONE);
                    }
                    if(date.size()==0){
                        error.setVisibility(View.VISIBLE);
                    }
                    else
                        error.setVisibility(View.GONE);
                    adapter.RefreshDate(date);
                    changeToState();
                    break;
                case 0x2:
                    changeRefreshState();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("onCreateView","fragmentJS  is onCreateView");
        view = inflater.inflate(R.layout.fragment,container,false);

                view1 = inflater.inflate( R.layout.main_list_item,container,false);

        init();
        return view;
    }
    @Override
    public void onStart(){
        Log.e(TAG,"onstart");
        super.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        if(isfirst&&!swipInit){
            if(swipeRefreshLayout!=null){
                swipInit = true;
                Log.e(TAG,"onResume");
//                swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
//                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
//                                .getDisplayMetrics()));
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG,"on post ");
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });
                getMessage();
            }
        }

    }
    public void init(){
        findId();
        initList();
        initRefresh();
        bindNetState();
    }
    /**
     * 绑定网络监听
     */
    public void bindNetState(){
        netState = new NetState();
        netState.setNetLister(new NetState.NetLister() {
            @Override
            public void OutInternet() {
                if(isfirst){
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void GetInternet() {
                error.setVisibility(View.GONE);
            }
        });
        IntentFilter filter = new IntentFilter();
        getActivity().registerReceiver(netState, filter);
        netState.onReceive(getActivity(), null);
    }


    public void findId(){

        error = view.findViewById(R.id.error);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        manager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);

    }
    public void initRefresh(){
        Log.e(TAG,"initRefesh");
        swipeRefreshLayout.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL);
        // 设置下拉多少距离之后开始刷新数据
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        // 设置进度条背景颜色
        swipeRefreshLayout.setOverScrollMode( View.OVER_SCROLL_NEVER );
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(getActivity(),R.color.white));
        // 设置刷新动画的颜色，可以设置1或者更多.
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.baseColor),
                ContextCompat.getColor(getActivity(),R.color.baseColor));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG,"init swiprefreshlayout getMessage");
                isPull = true;
                getMessage();
            }
        });
//        setViewObserver();
    }

    public void setViewObserver(){
        Log.e(TAG," set view Observer");
        final ViewTreeObserver observer = recyclerView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Log.e(TAG," in  view Observer");
                if(!swipInit){
                    Log.e(TAG,"change view Observer");
                    swipInit = true;

                    swipeRefreshLayout.setRefreshing(true);
                    isPull = true;
                    getMessage();
                }
                return false;
            }
        });
    }
    public void initList(){
        adapter = new recyclerAdapter(getActivity(), R.layout.main_list_item,date);
        recyclerView.setAdapter(adapter);
        adapter.setLister(new myitemLister() {
            @Override
            public void ItemLister(int position) {
                String url = date.get(position).get("url").toString();
                Intent intent = new Intent(getActivity(),browse.class);
                intent.putExtra("url",url);
                intent.putExtra("from",1);
                intent.putExtra("title",date.get(position).get("title"));
                intent.putExtra("company",date.get(position).get("company"));
                intent.putExtra("location",date.get(position).get("place"));
                intent.putExtra("time",date.get(position).get("time"));
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG,"state is "+newState+"== recyclerView "
                        +recyclerView.SCROLL_STATE_IDLE
                        +" SCROLL_STATE_DRAGGING is "+recyclerView.SCROLL_STATE_DRAGGING
                        +" SCROLL_STATE_SETTLING is "+recyclerView.SCROLL_STATE_SETTLING);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // dy>0 代表向下滚动
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                int itemCount = manager.getItemCount();
                Log.e(TAG,"在滚动中 itemCount is "+itemCount +" lastVisibleItem is "+lastVisibleItemPosition);
                // 如果最后一个可见的View的position 等于 itemCount-1 代表滚动到底部
                if((itemCount-1)==lastVisibleItemPosition){
                    Log.e(TAG,"到底啦");
                    getMessage();
                }
            }
        });

    }

    public void getMessage() {
        Log.e(TAG,"getMessage");
        if(refreshing) {
            isPull = false;
//            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        refreshing = true;
        String url =  getUrl();
        Log.e(TAG,"begin to get url is "+url);
        final urlConnection connection= new urlConnection();
        connection.setgetLister(new urlConnection.getLister() {
            @Override
            public void success(String response, int code) {
                upDate(parsetools.parseXuanjiang(response));
                Log.e(TAG," response code is "+code+"date size is "+date.size());
                handler.sendEmptyMessage(0x1);

            }
            @Override
            public void failure(Exception e,String Error, int code) {
                e.printStackTrace();

                Log.e(TAG," Error response is "+ Error+"code is "+code);
                handler.sendEmptyMessage(0x2);
            }
        });
        connection.get(url);
    }
    //更新数据
    public void upDate(ArrayList<HashMap<String,String>>  list){
        Log.e(TAG,"update");
        if(isPull){
            date.clear();
            date = list;
        }
        else{
            date.addAll(list);
        }
    }
    public void changeToState(){
        Log.e(TAG,"changeToState");
        if(isPull){
            page = 2;
        }
        else
        {
            page++;
        }
        changeRefreshState();
    }
    public void changeRefreshState(){
        isPull = false;
        refreshing = false;
        swipeRefreshLayout.setRefreshing(false);
    }
    //获取URL
    public String getUrl(){
        Log.e(TAG,"getURL");
        String str = "";
        if(isPull){
            str = url;
        }
        else {
            str = url +"?page="+page;
        }
        return str;
    }
    @Nullable
    @Override
    public View getView() {
        Log.e(TAG,"getView");
        return view;
    }

     @Override
     public void onPause(){
        super.onPause();
         Log.d(TAG,"onPause");

     }
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop");

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");

    }
    @Override
    public void onHiddenChanged (boolean hidden){
        Log.d(TAG,"onHiddenChanged");
    }

}
