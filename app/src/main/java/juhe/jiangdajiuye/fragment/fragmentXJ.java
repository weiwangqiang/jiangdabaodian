package juhe.jiangdajiuye.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.ImpAdapter;
import juhe.jiangdajiuye.consume.recyclerView.OnLoadMoreListener;
import juhe.jiangdajiuye.consume.recyclerView.mRecyclerView;
import juhe.jiangdajiuye.entity.MessageItem;
import juhe.jiangdajiuye.tool.NetState;
import juhe.jiangdajiuye.tool.parseTools;
import juhe.jiangdajiuye.util.urlConnection;
import juhe.jiangdajiuye.view.browse;

/**
 * 宣讲
 * Created by wangqiang on 2016/9/27.
 */
public class fragmentXJ extends Fragment implements OnLoadMoreListener {
    private View view,error;
    private String TAG = "fragmentXJ";
    private String url = "http://ujs.91job.gov.cn/teachin/index";
    private Boolean isfirst = true;
    private NetState netState;
    private int page = 1;
    public mRecyclerView recyclerView;
    private LinearLayoutManager manager;

    public ImpAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private parseTools parsetools =  parseTools.getparseTool() ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment,container,false);
        init();
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
    public void init(){
        findId();
        initList();
        bindNetState();
    }
    /**
     * 绑定网络监听
     */
    public void bindNetState(){
        NetState.addNetLister(new NetState.NetLister() {
            @Override
            public void OutInternet() {
                if(isfirst){
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void GetInternet(int type) {
                error.setVisibility(View.GONE);
            }
        });
        IntentFilter filter = new IntentFilter();
        getActivity().registerReceiver(netState, filter);
        netState.onReceive(getActivity(), null);
    }

    public void findId(){
        error = view.findViewById(R.id.error);
        recyclerView = (mRecyclerView) view.findViewById(R.id.recyclerView);
        manager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        initRefresh();
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
                if(recyclerView.getmStatus() == mRecyclerView.STATUS_DEFAULT){
                    getMessage();
                    recyclerView.setStatus(mRecyclerView.STATUS_PULLTOREFRESH);
                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(recyclerView.getmStatus() == mRecyclerView.STATUS_DEFAULT){
                    getMessage();
                    recyclerView.setStatus(mRecyclerView.STATUS_PULLTOREFRESH);
                    swipeRefreshLayout.setRefreshing(true);
                }

            }
        });
    }

    public void initList(){
        adapter = new ImpAdapter(R.layout.main_list_item);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImpAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MessageItem item) {
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
    }
    public void getMessage() {
        String url = getUrl();
        Log.i(TAG,"url is "+url+" time :");
        urlConnection connection = new urlConnection(getActivity());
        connection.setNetListener(new urlConnection.NetListener(){

            @Override
            public void success(String response, int code) {
                upDate(parsetools.parseXuanjiang(response.trim()));
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(Exception e, String Error, int code) {
                recyclerView.setStatus(mRecyclerView.STATUS_DEFAULT);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),"网络不太顺畅哦！",Toast.LENGTH_SHORT).show();
            }
        });
        connection.get(url);
    }
    private void MySuccess() {
        if(adapter.getDataSize()==0){
            error.setVisibility(View.VISIBLE);
            return;
        }
        else
        {
            isfirst = false;
            error.setVisibility(View.GONE);
        }
        changeParam();
    }
    public void upDate(List<MessageItem>  list){
         if(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH){
             adapter.clearAll();
             adapter.upDate(list);
         }else {
             adapter.appendDate(list);
         }
        MySuccess();
    }
    public void changeParam(){
        if(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH)
            page = 2;
        else
            page++;
        recyclerView.setStatus(mRecyclerView.STATUS_DEFAULT);
    }

    public String getUrl(){
        String str = "";
        if(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH)
            str = url;
        else
            str = url +"?page="+page;
        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(netState);
    }
    @Override
    public void onLoadMore() {
        if(recyclerView.getmStatus() == mRecyclerView.STATUS_DEFAULT){
            recyclerView.setStatus(mRecyclerView.STATUS_REFRESHING);
            getMessage();
        }
    }
}
