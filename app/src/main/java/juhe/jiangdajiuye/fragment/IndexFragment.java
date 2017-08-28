package juhe.jiangdajiuye.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.IndexFragmentAdapter;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.consume.recyclerView.OnLoadMoreListener;
import juhe.jiangdajiuye.consume.recyclerView.mRecyclerView;
import juhe.jiangdajiuye.entity.MessageItem;
import juhe.jiangdajiuye.tool.parseTools;
import juhe.jiangdajiuye.util.ToastUtils;
import juhe.jiangdajiuye.util.urlConnection;
import juhe.jiangdajiuye.view.browse;

/**
 * Created by wangqiang on 2016/9/27.
 */
public class IndexFragment extends Fragment implements OnLoadMoreListener {
    public static final int XUANJIANG = 1;
    public static final int ZHAOPIN = 2;
    public static final int XINXI = 3;

    private View view,error;
    private String baseurl;
    private String TAG;
    private int tab ;
    public mRecyclerView recyclerView;
    private LinearLayoutManager manager;
    private NetStateReceiver netState;
    public List<MessageItem> data = new ArrayList<>();
    public IndexFragmentAdapter adapter;
    //下拉刷新
    private Boolean isfirst = true;
    private int page = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private parseTools parsetools =  parseTools.getparseTool() ;
    private String string = "";

    public static IndexFragment newInstance(String url, String TAG, int tab) {
        IndexFragment f = new IndexFragment();
        Bundle b = new Bundle();
        b.putString("url",url);
        b.putString("TAG",TAG);
        b.putInt("tab",tab);
        f.setArguments(b);
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"  is onCreateView");
        if(view != null)
            return view ;
        view = inflater.inflate(R.layout.fragment,container,false);
        init();
        return view;
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        Log.i(TAG, "onStart: "+string);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    public void init(){
        Bundle bundle = getArguments();
        baseurl = bundle.getString("url");
        TAG = bundle.getString("TAG");
        tab = bundle.getInt("tab");//控制用哪个解析方法
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
                if(recyclerView != null){
                    if(recyclerView.getmStatus() == mRecyclerView.STATUS_ERROR){
                        recyclerView.setStatus(mRecyclerView.STATUS_DEFAULT);
                    }
                }
            }
        });
    }

    public void findId(){
        error = view.findViewById(R.id.error);
        recyclerView = (mRecyclerView) view.findViewById(R.id.recyclerView);
        manager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setmOnLoadMoreListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);

    }
    public void initRefresh(){
        swipeRefreshLayout.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL);
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setOverScrollMode( View.OVER_SCROLL_NEVER );
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(getActivity(),R.color.white));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.baseColor),
                ContextCompat.getColor(getActivity(),R.color.baseColor));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(recyclerView.getmStatus() == mRecyclerView.STATUS_DEFAULT ||
                        recyclerView.getmStatus() == mRecyclerView.STATUS_ERROR){
                    recyclerView.setStatus(mRecyclerView.STATUS_PULLTOREFRESH);
                    getMessage();
                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                    if(tab == XUANJIANG ){
                        recyclerView.setStatus(mRecyclerView.STATUS_PULLTOREFRESH);
                        swipeRefreshLayout.setRefreshing(true);
                        getMessage();
                    }
            }
        });
    }

    public void initList(){
        adapter = new IndexFragmentAdapter(R.layout.main_list_item);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new IndexFragmentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MessageItem item) {
                Intent intent = new Intent(getActivity(),browse.class);
                intent.putExtra("url",item.getUrl());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("time",item.getTime());
                intent.putExtra("company",item.getFrom());
                intent.putExtra("company",item.getFrom());
                intent.putExtra("location",item.getLocate());
                intent.putExtra("from",1);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }
        });
    }
    public void getMessage() {
        String url = getUrl();
//        Log.i(TAG,"url is "+url+" time :");
        urlConnection connection = new urlConnection(getActivity());
        connection.setNetListener(new urlConnection.NetListener(){

            @Override
            public void success(String response, int code) {
                upDate(parsetools.parseMainMes(response.trim(),tab));
                upDate(parsetools.parseMainMes(response.trim(),tab));
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.setStatus(mRecyclerView.STATUS_DEFAULT);
            }

            @Override
            public void failure(Exception e, String Error, int code) {
                recyclerView.setStatus(mRecyclerView.STATUS_ERROR);
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast("网络不太顺畅哦！");
                showError();
            }
        });
        connection.get(url);
    }

    private void showError() {
        if(adapter.getDataSize()==0){
            error.setVisibility(View.VISIBLE);
            return;
        }
        else
        {
            isfirst = false;
            error.setVisibility(View.GONE);
        }
    }
    public void upDate(List<MessageItem>  list){
        if(list.size() == 0 && adapter.getDataSize() != 0){
            recyclerView.setCanLoadMoreRefresh(false);
            return;
        }
        if(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH){
            adapter.clearAll();
            adapter.upDate(list);
        }else {
            adapter.appendDate(list);
        }
        page++;
        showError();
    }

    public String getUrl(){
        String str = "";
        if(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH)
            page = 1;
        str = baseurl +"page="+page;
        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }

    @Override
    public void onLoadMore() {
        if(recyclerView.getmStatus() == mRecyclerView.STATUS_DEFAULT){
            recyclerView.setStatus(mRecyclerView.STATUS_REFRESHING);
            getMessage();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint: "+isVisibleToUser +" isfirst ： "+isfirst);
        if(isfirst &&isVisibleToUser && adapter != null &&adapter.getDataSize() ==0  ){
            if(swipeRefreshLayout!=null && recyclerView.getmStatus()!=mRecyclerView.STATUS_ERROR){
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        recyclerView.setStatus(mRecyclerView.STATUS_PULLTOREFRESH);
                        getMessage();
                    }
                });
            }
        }
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
        string = "onAttach ";
    }
}
