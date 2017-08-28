package juhe.jiangdajiuye.fragment;


import android.content.Intent;
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
import juhe.jiangdajiuye.adapter.recyclerAdapter;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.entity.MessageItem;
import juhe.jiangdajiuye.tool.parseOther;
import juhe.jiangdajiuye.util.urlConnection;
import juhe.jiangdajiuye.view.browse;

/**
 * Created by wangqiang on 2016/9/27.
 */
public class fragment extends Fragment  {
    private View view,error;
    private String baseurl;
    private String TAG;
    private int from ;
    public RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private NetStateReceiver netState;
    public List<MessageItem> data = new ArrayList<>();
    public recyclerAdapter adapter;
    //下拉刷新
    private Boolean isPull = false;
    private Boolean refreshing = false;
    private Boolean isfirst = true;
    private Boolean swipInit = false;
    private int page = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private parseOther parse = new parseOther();


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
        if(view != null) return view ;
        view = inflater.inflate(R.layout.fragment,container,false);
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
        NetStateReceiver.addNetLister(new NetStateReceiver.NetLister() {
            @Override
            public void OutInternet() {
                if(isfirst){
                    error.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void GetInternet(int type) {
//                error.setVisibility(View.GONE);
            }
        });
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
                    getMessage();
                }
            });
        if(from == 0){
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    getMessage();

                }
            });
        }
    }
    public void initList(){
        adapter = new recyclerAdapter(getActivity(), R.layout.main_list_item,data);
        recyclerView.setAdapter(adapter);
        adapter.setLister(new myitemLister() {
            @Override
            public void ItemLister(int position) {
                MessageItem item = data.get(position);
                Intent intent = new Intent(getActivity(),browse.class);
                intent.putExtra("url",item.getUrl());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("time",item.getTime());
                intent.putExtra("company",item.getFrom());
                intent.putExtra("location",item.getLocate());
                intent.putExtra("from",1);
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
                if((itemCount-1)==lastVisibleItemPosition){
                    if(isVisibleToUser)
                        getMessage();
                }
            }
        });
    }

    public void getMessage() {
        if(refreshing) {
            return;
        }
        refreshing = true;
        String url = getUrl();

        urlConnection connection = new urlConnection(getActivity());
        connection.setNetListener(new urlConnection.NetListener(){

            @Override
            public void success(String response, int code) {
                upDate(parse.parse(response,from,baseurl));
                MySuccess();
            }

            @Override
            public void failure(Exception e, String Error, int code) {
                Toast.makeText(getActivity(),"网络不太顺畅哦！",Toast.LENGTH_SHORT).show();
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
    public void upDate(List<MessageItem>  list){
        if(list == null) return ;
        if(isPull){
            data.clear();
            data = list;
        }
        else{
            data.addAll(list);
        }
    }
    //预先处理下一次加载路径的参数
    public void changeParam(){
        if(isPull)
            page = 2;
        else
            page++;
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
        if(from==5){
            //南京大学的url 与其他的大学不一样
            page=1;
            return baseurl+"?type=xyzp&pageNow="+page;
        }else
            return baseurl;
    }
    //获取上拉url
    public String getNexturl(int from){
        if(from==5)
            return baseurl+"?type=xyzp&pageNow="+page;
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
    }
    @Override
    public void onHiddenChanged (boolean hidden){
    }

    private boolean isVisibleToUser = false ;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser  = isVisibleToUser ;
        Log.i(TAG, "setUserVisibleHint: ");
        if(isfirst&&!swipInit &&isVisibleToUser){
            if(swipeRefreshLayout!=null){
                swipInit = true;
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        getMessage();
                    }
                });
            }
        }
    }

}
