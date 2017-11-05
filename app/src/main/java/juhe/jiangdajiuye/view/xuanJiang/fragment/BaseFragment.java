package juhe.jiangdajiuye.view.xuanJiang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.IndexFragmentAdapter;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.consume.recyclerView.OnLoadMoreListener;
import juhe.jiangdajiuye.consume.recyclerView.mRecyclerView;
import juhe.jiangdajiuye.entity.MessageItem;
import juhe.jiangdajiuye.util.ToastUtils;
import juhe.jiangdajiuye.util.urlConnection;
import juhe.jiangdajiuye.view.browse;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanHolder;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public abstract class BaseFragment extends Fragment implements OnLoadMoreListener {
    private static final String TAG = "BaseFragment";
    private View view,error;
    private mRecyclerView recyclerView;
    private LinearLayoutManager manager;
    private IndexFragmentAdapter adapter;
    //是否没有初始化数据
    private Boolean isfirst = true;
    private SwipeRefreshLayout swipeRefreshLayout;
    private urlConnection connection ;
    private XuanHolder holder ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null)
            return view ;
        view = inflater.inflate(R.layout.fragment,container,false);
        connection = new urlConnection(getActivity());
        init();
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        NetStateReceiver.removeLister(receiver);
    }

    public void init(){
        Bundle bundle = getArguments();
        holder = new XuanHolder();
        holder.setBaseUrl(bundle.getString("BaseUrl"));
        holder.setCollege(bundle.getString("college"));
        holder.setCollegeId(bundle.getInt("collegeId"));
        holder.setProvinceId(bundle.getInt("provinceId"));
        Log.i(TAG, "init: "+holder.getProvinceId());
        findId();
        initRefresh();
        initList();
        bindNetState();
        initRequest();
    }

    private void initRequest() {
        connection.setNetListener(new urlConnection.NetListener(){

            @Override
            public void success(String response, int code) {
                upDate(parseMes(response.trim(),holder));
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
    }

    /**
     * 绑定网络监听
     */
    public void bindNetState(){
        NetStateReceiver.addNetLister(receiver);
    }
    private  NetStateReceiver.NetLister receiver = new NetStateReceiver.NetLister(){

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
    };
    public void findId(){
        error = view.findViewById(R.id.error);
        recyclerView = (mRecyclerView) view.findViewById(R.id.recyclerView);
        manager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setmOnLoadMoreListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        Log.i(TAG, "findId:  findid ");
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
        if(getUserVisibleHint() && isfirst){
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run:  post ");
                    swipeRefreshLayout.setRefreshing(true);
                    recyclerView.setStatus(mRecyclerView.STATUS_PULLTOREFRESH);
                    getMessage();
                }
            });
        }
    }

    public void initList(){
        adapter = new IndexFragmentAdapter(getActivity(),R.layout.main_list_item);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new IndexFragmentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MessageItem item) {
                browse.StartActivity(getActivity() ,item);
            }
        });
    }

    /**
     * http://career.hdu.edu.cn/module/getcareers?start_page=1&keyword=&type=inner&day=&count=10&start=1
     * http://career.hdu.edu.cn/module/getcareers?start_page=1&keyword=&type=inner&day=&count=10&start=1
     */
    public void getMessage() {
        String url = getUrl(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH
                  ,holder);
        Log.i(TAG, "getMessage: "+url);
        connection.get(url);
    }

    /**
     * 判断当前是否需要显示Error
     */
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
        RequestSuccess();
        showError();
    }
    public abstract String getUrl(boolean isPull,XuanHolder holder);
    public abstract List<MessageItem> parseMes(String result,XuanHolder holder);
    public abstract void RequestSuccess();
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
        if(isfirst && isVisibleToUser ){
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

}
