package juhe.jiangdajiuye.fragment;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.InterFace.myitemLister;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.SDrecyclerAdapter;
import juhe.jiangdajiuye.entity.MessageItem;
import juhe.jiangdajiuye.tool.NetState;
import juhe.jiangdajiuye.tool.parseTools;
import juhe.jiangdajiuye.util.NetMesManager;
import juhe.jiangdajiuye.util.UserActionRecordUtils;
import juhe.jiangdajiuye.util.urlConnection;
import juhe.jiangdajiuye.view.browse;

/**
 * Created by wangqiang on 2016/9/27.
 */
public class fragmentSD extends Fragment  {
    private View view,error;
    private NetState netState;
    private String TAG = "fragmentSD";
    private String url = "http://ujs.91job.gov.cn/news/index?tag=tzgg";
    public RecyclerView recyclerView;
    private LinearLayoutManager manager;

    public List<MessageItem>  data = new ArrayList<>();
    public SDrecyclerAdapter adapter;
    //下拉刷新
    private Boolean isPull = false;
    private Boolean refreshing = false;
    private Boolean isfirst = true;
    private Boolean swipInit = false;
    private boolean isVisibleToUser = false ;
    private int page = 1;

    private SwipeRefreshLayout swipeRefreshLayout;
    private parseTools parsetools =  parseTools.getparseTool() ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"fragmentSD  is onCreateView");
        view = inflater.inflate(R.layout.fragment,container,false);
        init();
        return view;
    }
    public void init(){
        findId();
        initList();
        bindNetState();
    }

    public void findId(){
        error = view.findViewById(R.id.error);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        manager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);

    }
    /**
     * 绑定网络监听
     */
    public void bindNetState(){
        netState = new NetState();
        netState.setNetLister(new NetState.NetLister() {
            @Override
            public void OutInternet() {
                Log.e(TAG,"error is visible");
                error.setVisibility(View.VISIBLE);
            }

            @Override
            public void GetInternet() {
                Log.e(TAG,"error is gone");
                error.setVisibility(View.GONE);
                if(UserActionRecordUtils.ipbean == null){
                    NetMesManager.setIP(getActivity());
                }
            }
        });
        IntentFilter filter = new IntentFilter();
        getActivity().registerReceiver(netState, filter);
        netState.onReceive(getActivity(), null);
    }


    public void initList(){
        Log.e(TAG,"init list");
        adapter = new SDrecyclerAdapter(getActivity(), R.layout.mainsd_list_item,data);
        recyclerView.setAdapter(adapter);
        adapter.setLister(new myitemLister() {
            @Override
            public void ItemLister(int position) {
                MessageItem item = data.get(position);
                Intent intent = new Intent(getActivity(),browse.class);
                intent.putExtra("url",item.getUrl());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("time",item.getTime());
                intent.putExtra("from",2);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                Log.i(TAG, "onScrolled: "+itemCount+"  :  "+lastVisibleItemPosition);
                if((itemCount-1)==lastVisibleItemPosition){
                    if(isVisibleToUser)
                       getMessage();
                }
            }
        });
        initRefresh();
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
                getMessage();
            }
        });
    }
    public void getMessage() {
        if(refreshing) {
            return;
        }
        refreshing = true;
        String url = getUrl();
        Log.e(TAG,"url is "+url);
        urlConnection connection = new urlConnection(getActivity());
        connection.setgetLister(new urlConnection.getLister(){

            @Override
            public void success(String response, int code) {
                upDate(parsetools.parseShudi(response.trim()));
                MySuccess();
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void failure(Exception e, String Error, int code) {
                Toast.makeText(getActivity(),"网络不太顺畅哦！",Toast.LENGTH_SHORT).show();

                swipeRefreshLayout.setRefreshing(false);
                changeRefreshState();
            }
        });
        connection.get(url);
    }
    private void MySuccess() {
        swipeRefreshLayout.setRefreshing(false);
        if(data.size()==0){
            error.setVisibility(View.VISIBLE);
            return;
        }
        else
        {
            isfirst = false;
            error.setVisibility(View.GONE);
        }
        adapter.RefreshDate(data);
        changeParam();
    }
    //更新数据
    public void upDate(List<MessageItem> list){
        Log.i(TAG, "upDate: list size()  "+list.size());
        if(isPull){
            data.clear();
            data = list;
        }
        else
            data.addAll(list);
    }
    public void changeParam(){
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
        if(isPull){
            str = url;
        }
        else {
            str = url +"&page="+page;
        }
        return str;
    }
    @Nullable
    @Override
    public View getView() {
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"Start");
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser ;
        Log.i(TAG, "setUserVisibleHint: "+isVisibleToUser);
        if(isfirst&&!swipInit &&isVisibleToUser ){
            if(swipeRefreshLayout!=null){
                swipInit = true;
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        getMessage();

                    }
                });
            }
        }
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
        getActivity().unregisterReceiver(netState);

    }
    @Override
    public void onHiddenChanged (boolean hidden){
        Log.d(TAG,"onHiddenChanged");

    }


}
