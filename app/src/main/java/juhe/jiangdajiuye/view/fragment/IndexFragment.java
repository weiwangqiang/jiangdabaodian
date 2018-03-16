package juhe.jiangdajiuye.view.fragment;


import android.os.Bundle;
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
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.consume.recyclerView.MyRecyclerView;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.utils.httpUtils.HttpHelper;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IDataListener;
import juhe.jiangdajiuye.utils.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.view.activity.Browse;
import juhe.jiangdajiuye.view.adapter.IndexFragmentAdapter;
import juhe.jiangdajiuye.view.xuanJiang.entity.MesItemHolder;

/**
 * Created by wangqiang on 2016/9/27.
 * 首界面的fragment
 */
public class IndexFragment extends Fragment implements MyRecyclerView.OnLoadMoreListener {
    public static final int XUANJIANG = 1;//宣讲会
    public static final int ZHAOPINZHIWEI = 2;//招聘职位
    public static final int XINXI = 3;//信息速递
    public static final int ZHAOPINGONGGAO = 4;//招聘公告
    public static final int ZHAOPINHUI = 5;//招聘会
    private View view, error;
    private String baseUrl;
    private String TAG;
    private int tab;
    private MyRecyclerView recyclerView;
    private LinearLayoutManager manager;
    private IndexFragmentAdapter adapter;
    //下拉刷新
    private Boolean isFirst = true;
    private int page = 1; //当前页面数
    private SwipeRefreshLayout swipeRefreshLayout;
    private HttpHelper httpHelper ;
    private StringBuilder str = new StringBuilder();
    private MesItemHolder holder = new MesItemHolder();
    public static IndexFragment newInstance(String url, String TAG, int tab) {
        IndexFragment f = new IndexFragment();
        Bundle b = new Bundle();
        b.putString("url", url);
        b.putString("TAG", TAG);
        b.putInt("tab", tab);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment, container, false);
        init();
        return view;
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        unBindNetState();
    }

    private void init() {
        httpHelper = HttpHelper.getInstance() ;
        Bundle bundle = getArguments();
        baseUrl = bundle.getString("url");
        TAG = bundle.getString("TAG");
        tab = bundle.getInt("tab");//控制用哪个解析方法
        holder.setTab(tab);
        findId();
        initRefresh();
        initList();
        bindNetState();
    }

    /**
     * 绑定网络监听
     */
    private void bindNetState() {
        NetStateReceiver.addNetLister(receiver);
    }

    private void unBindNetState() {
        NetStateReceiver.removeLister(receiver);
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

    private void findId() {
        error = view.findViewById(R.id.error);
        recyclerView = (MyRecyclerView) view.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnLoadMoreListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

    }

    private void initRefresh() {
        swipeRefreshLayout.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL);
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setOverScrollMode(View.OVER_SCROLL_NEVER);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(getActivity(), R.color.white));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.themeColor),
                ContextCompat.getColor(getActivity(), R.color.themeColor));
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
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (tab == XUANJIANG) {
                    recyclerView.setStatus(MyRecyclerView.STATUS_PULL_TO_REFRESH);
                    swipeRefreshLayout.setRefreshing(true);
                    getMessage();
                }
            }
        });
    }

    private IDataListener iDataListener = new IDataListener<List<MessageItem>>() {
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
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
            showError();
        }
    } ;
    private void initList() {
        adapter = new IndexFragmentAdapter(getActivity(), R.layout.main_list_item);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new IndexFragmentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MessageItem item) {
                Browse.StartActivity(getActivity(), item);
            }
        });
    }

    private void getMessage() {
        httpHelper.get(getUrl(), holder, iDataListener, HttpTask.Type.MessageItem);
    }

    private void showError() {
        if (adapter.getDataSize() == 0) {
            error.setVisibility(View.VISIBLE);
            return;
        } else {
            isFirst = false;
            error.setVisibility(View.GONE);
        }
    }

    private void upDate(List<MessageItem> list) {
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
            recyclerView.setStatus(MyRecyclerView.STATUS_END);
            return;
        }
        recyclerView.setStatus(MyRecyclerView.STATUS_DEFAULT);
        page++;
        showError();
    }

    private String getUrl() {
        str.setLength(0);
        if (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH){
            page = 1;
        }
        str.append(baseUrl);
        str.append("page=" + page);
        Log.i(TAG, "getUrl: " + str.toString());
        return str.toString();
    }

    @Override
    public void onLoadMore() {
        if (recyclerView.getStatus() == MyRecyclerView.STATUS_DEFAULT) {
            recyclerView.setStatus(MyRecyclerView.STATUS_REFRESHING);
            getMessage();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst && isVisibleToUser && adapter != null && adapter.getDataSize() == 0) {
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

}
