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
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.consume.recyclerView.MyRecyclerView;
import juhe.jiangdajiuye.consume.recyclerView.OnLoadMoreListener;
import juhe.jiangdajiuye.util.HttpManager;
import juhe.jiangdajiuye.util.HttpWorker.HttpHelper;
import juhe.jiangdajiuye.util.HttpWorker.HttpTask;
import juhe.jiangdajiuye.util.HttpWorker.Inter.IDataListener;
import juhe.jiangdajiuye.util.ToastUtils;
import juhe.jiangdajiuye.view.Browse;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;

/**
 * class description here
 * fragment 基类
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public abstract class BaseFragment extends Fragment implements OnLoadMoreListener {
    private static final String TAG = "BaseFragment";
    private View view, error;
    private MyRecyclerView recyclerView;
    private LinearLayoutManager manager;
    private IndexFragmentAdapter adapter;
    //是否没有初始化数据
    private Boolean isFirst = true;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HttpManager connection;
    private XuanJiangMesHolder holder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment, container, false);
        connection = new HttpManager(getActivity());
        init();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetStateReceiver.removeLister(receiver);
    }

    public void init() {
        Bundle bundle = getArguments();
        holder = new XuanJiangMesHolder();
        holder.setBaseUrl(bundle.getString("BaseUrl"));
        holder.setCollege(bundle.getString("college"));
        holder.setCollegeId(bundle.getInt("collegeId"));
        holder.setProvinceId(bundle.getInt("provinceId"));
        findId();
        initRefresh();
        initList();
        bindNetState();
    }

    /**
     * 绑定网络监听
     */
    public void bindNetState() {
        NetStateReceiver.addNetLister(receiver);
    }

    private NetStateReceiver.NetLister receiver = new NetStateReceiver.NetLister() {

        @Override
        public void OutInternet() {
            if (isFirst) {
                error.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void GetInternet(int type) {
            if (recyclerView != null) {
                if (recyclerView.getStatus() == MyRecyclerView.STATUS_ERROR) {
                    recyclerView.setStatus(MyRecyclerView.STATUS_DEFAULT);
                }
            }
        }
    };

    public void findId() {
        error = view.findViewById(R.id.error);
        recyclerView = (MyRecyclerView) view.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnLoadMoreListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
    }

    public void initRefresh() {
        swipeRefreshLayout.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL);
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setOverScrollMode(View.OVER_SCROLL_NEVER);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(getActivity(), R.color.white));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.baseColor),
                ContextCompat.getColor(getActivity(), R.color.baseColor));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if ( recyclerView.getStatus() != MyRecyclerView.STATUS_ERROR) {
                    recyclerView.setStatus(MyRecyclerView.STATUS_PULL_TO_REFRESH);
                    getMessage();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        if (getUserVisibleHint() && isFirst) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    recyclerView.setStatus(MyRecyclerView.STATUS_PULL_TO_REFRESH);
                    getMessage();
                }
            });
        }
    }

    public void initList() {
        adapter = new IndexFragmentAdapter(getActivity(), R.layout.main_list_item);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new IndexFragmentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MessageItem item) {
                Browse.StartActivity(getActivity(), item);
            }
        });
    }

    /**
     * http://career.hdu.edu.cn/module/getcareers?start_page=1&keyword=&type=inner&day=&count=10&start=1
     */
    public void getMessage() {
        String url = getUrl(recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH
                , holder);
        Log.i(TAG, "getMessage: " + url);
        HttpHelper httpManager = HttpHelper.getInstance()  ;
        httpManager.get(url,holder , new IDataListener<List<MessageItem>>(){
            @Override
            public void onSuccess(List<MessageItem> messageItems) {
                upDate(messageItems);
                swipeRefreshLayout.setRefreshing(false);
                messageItems.clear();
            }

            @Override
            public void onFail(Exception exception, int responseCode) {
                recyclerView.setStatus(MyRecyclerView.STATUS_ERROR);
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast("网络不太顺畅哦！");
                showError();
            }
        }, HttpTask.Type.MessageItem);
    }

    /**
     * 判断当前是否需要显示Error
     */
    private void showError() {
        if (adapter.getDataSize() == 0) {
            error.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(View.GONE);
        }
    }

    public void upDate(List<MessageItem> list) {
        //没有更多了
        if ((list.size() == 0 && adapter.getDataSize() != 0)) {
            recyclerView.setCanLoadMoreRefresh(false);
            return;
        }
        isFirst = false;
        //如果是下拉刷新就刷新adapter的数据，否则直接添加
        if (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH) {
            adapter.upDate(list);
        } else {
            adapter.appendDate(list);
        }
        //如果adapter的数据不够，说明没有更多的数据，直接提示没有更多数据
        if( adapter.getDataSize() <6){
            Log.i(TAG, "upDate: end ------- size is  "+list.size());
            recyclerView.setStatus(MyRecyclerView.STATUS_END);
            return;
        }
        recyclerView.setStatus(MyRecyclerView.STATUS_DEFAULT);
        RequestSuccess();
        showError();
    }

    public abstract String getUrl(boolean isPull, XuanJiangMesHolder holder);

    public abstract void RequestSuccess();

    @Override
    public void onLoadMore() {
        Log.i(TAG, "onLoadMore: ");
        if (recyclerView.getStatus() == MyRecyclerView.STATUS_DEFAULT) {
            recyclerView.setStatus(MyRecyclerView.STATUS_REFRESHING);
            getMessage();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst && isVisibleToUser) {
            if (swipeRefreshLayout != null && recyclerView.getStatus() != MyRecyclerView.STATUS_ERROR) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        recyclerView.setStatus(MyRecyclerView.STATUS_PULL_TO_REFRESH);
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
