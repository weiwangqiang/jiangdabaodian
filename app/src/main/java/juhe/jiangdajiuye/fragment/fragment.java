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
import android.text.BoringLayout;
import android.util.Log;
import android.util.TypedValue;
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
import juhe.jiangdajiuye.tool.parseOther;
import juhe.jiangdajiuye.util.urlConnection;
import juhe.jiangdajiuye.view.browse;
import okhttp3.OkHttpClient;

import static android.R.attr.data;
import static com.tencent.wxop.stat.common.k.e;

/**
 * Created by wangqiang on 2016/9/27.
 */
public class fragment extends Fragment {
    private View view,error;
    private String baseurl;
    private String TAG;
    private int from ;
    public RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private NetState netState;
    public ArrayList<HashMap<String,String>>  date = new ArrayList<>();
    public recyclerAdapter adapter;
    //下拉刷新
    private Boolean isPull = false;
    private Boolean refreshing = false;
    private Boolean isfirst = true;
    private Boolean swipInit = false;
    private int page = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    public OkHttpClient mOkHttpClient ;
    private parseOther parse = new parseOther();
    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 0x1:
                    //成功返回
                    if(date.size()==0){
                        Log.e(TAG,"addList have a error ,list is 0 ");
                        error.setVisibility(View.VISIBLE);
                        break;
                    }
                    else
                    {
                        isfirst = false;
                        Log.e(TAG," -->>isfirst is change to false ");
                        error.setVisibility(View.GONE);
                    }
                    adapter.RefreshDate(date);
                    changeToState();
                    break;
                case 0x2:
                    //返回有错误，
                    changeRefreshState();
                    break;
                default:
                    break;
            }
        }
    };
    public static fragment newInstance(String url,String TAG,int from) {
        fragment f = new fragment();
        Bundle b = new Bundle();
        b.putString("url",url);
        b.putString("TAG",TAG);
        b.putInt("from",from);
        f.setArguments(b);
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"fragmentJS  is onCreateView");
        view = inflater.inflate(R.layout.fragment,container,false);
        mOkHttpClient = new OkHttpClient();
        init();
        return view;
    }

    public void init(){
        Bundle bundle = getArguments();
        baseurl = bundle.getString("url");
        TAG = bundle.getString("TAG");
        from = bundle.getInt("from");//控制用哪个解析方法
        findId();
        initRefresh();
        initList();
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
                    isPull = true;
                    Log.e(TAG,"swiprefreshlayout getMessage");
                    getMessage();
                }
            });
//        setViewObserver();
    }
    public void setViewObserver(){
        ViewTreeObserver observer = swipeRefreshLayout.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(!swipInit){
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
                intent.putExtra("title",date.get(position).get("title"));
                intent.putExtra("time",date.get(position).get("time"));
                intent.putExtra("company",date.get(position).get("company"));
                intent.putExtra("location",date.get(position).get("place"));
                intent.putExtra("from",1);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when RecyclerView's scroll state changes.
             *
             * @param recyclerView The RecyclerView whose scroll state has changed.
             * @param newState     The updated scroll state. One of {@link #SCROLL_STATE_IDLE},
             *                     {@link #SCROLL_STATE_DRAGGING} or {@link #SCROLL_STATE_SETTLING}.
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // dy>0 代表向下滚动
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                int itemCount = manager.getItemCount();
                // 如果最后一个可见的View的position 等于 itemCount-1 代表滚动到底部
                if((itemCount-1)==lastVisibleItemPosition){
                    Log.e(TAG,"list 到底啦");
                    getMessage();
                }
            }
        });
    }

    public void getMessage() {
        if(refreshing) {
//            isPull = false;
            Log.e(TAG,"is refreshing and  return");
            return;
        }
        refreshing = true;
        String url = getUrl();
        Log.e(TAG,"------------------url is "+ url);
        final urlConnection connection= new urlConnection();
        connection.setgetLister(new urlConnection.getLister() {
            @Override
            public void success(String response, int code) {
                upDate(parse.parse(response,from));
                handler.sendEmptyMessage(0x1);
            }
            @Override
            public void failure(Exception e,String Error, int code) {
                e.printStackTrace();
                Log.e(TAG,"-->> error code is  "+ code);
                handler.sendEmptyMessage(0x2);
            }
        });
        connection.get(url);
    }    //更新数据
    public void upDate(ArrayList<HashMap<String,String>>  list){
        if(isPull){
            date.clear();
            date = list;
        }
        else{
            date.addAll(list);
        }

    }
    //预先处理下一次加载路径的参数
    public void changeToState(){
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
        String str = "";
        //下拉或者是第一次加载
        if(isPull||isfirst){
            str = getPullurl(from);
        }
        else {
            str = getNexturl(from);
        }
        return str;
    }
    //获取下拉url
    public String  getPullurl(int from){
        if(from==6){
            //南京大学的url 与其他的大学不一样
            page=1;
            return baseurl+"?type=zph&pageNow="+page;
        }else
            return baseurl;
    }
    //获取上拉url
    public String getNexturl(int from){
        if(from==6) {
            return baseurl+"?type=zph&pageNow="+page;
        }
        return  baseurl +"?page="+page;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onStop(){
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(netState);
    }
    @Override
    public void onHiddenChanged (boolean hidden){
    }
}
