package juhe.jiangdajiuye.view.fragment;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.IndexAdapter;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.broadcast.NetStateReceiver;
import juhe.jiangdajiuye.db.repository.BrowseDepository;
import juhe.jiangdajiuye.net.httpUtils.HttpHelper;
import juhe.jiangdajiuye.net.httpUtils.inter.IDataListener;
import juhe.jiangdajiuye.net.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.ui.recyclerView.LoadMoreRecyclerView;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.view.activity.browse.MesBrowseActivity;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;

/**
 * Created by wangqiang on 2016/9/27.
 * 首界面的fragment
 */
public class IndexFragment extends BaseTooBarFragment implements LoadMoreRecyclerView.OnLoadMoreListener {
    public static final int XUANJIANG = 1;//宣讲会
    public static final int ZHAOPINZHIWEI = 2;//招聘职位
    public static final int XINXI = 3;//信息速递
    public static final int ZHAOPINGONGGAO = 4;//招聘公告
    public static final int ZHAOPINHUI = 5;//招聘会
    private View view, error;
    private String baseUrl;
    private String TAG;
    private int tab;
    private LoadMoreRecyclerView recyclerView;
    private LinearLayoutManager manager;
    private IndexAdapter adapter;
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
                if (recyclerView.isErrorStatus()) {
                    recyclerView.setCanLoadMoreRefresh(true);
                }
            }
        }
    };

    private void findId() {
        error = view.findViewById(R.id.error);
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
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
                if (! recyclerView.isErrorStatus()) {
                    recyclerView.setPullDownToRefresh();
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
                    recyclerView.setPullDownToRefresh();
                    swipeRefreshLayout.setRefreshing(true);
                    getMessage();
                }
            }
        });
    }

    private IDataListener iDataListener = new IDataListener<List<MessageBean>>() {
        @Override
        public void onSuccess(List<MessageBean> messageBeans) {
            upDate(messageBeans);
            swipeRefreshLayout.setRefreshing(false);
            messageBeans.clear();
        }

        @Override
        public void onFail(Exception exception, int responseCode) {
            recyclerView.setErrorStatus();
            swipeRefreshLayout.setRefreshing(false);
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
            showError();
        }
    } ;
    private void initList() {
        adapter = new IndexAdapter(getActivity(), R.layout.main_list_item);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new IndexAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MessageBean item,int position) {
                if(!item.getHasBrowse()){
                    item.setHasBrowse(true);
                    BrowseDepository.getInstance().add(item.getUrl());
                    adapter.notifyItemChanged(position);
                }
                MesBrowseActivity.StartActivity(getActivity(), item);
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

    private void upDate(List<MessageBean> list) {
        //没有更多了
        if ((list.size() == 0 && adapter.getDataSize() != 0)) {
            recyclerView.setCanLoadMoreRefresh(false);
            return;
        }
        isFirst = false;
        //如果是下拉刷新就刷新adapter的数据，否则直接添加
        if (recyclerView.isPullDownToRefresh()) {
            adapter.upDate(list);
        } else {
            adapter.appendData(list);
        }
        //如果adapter的数据不够，说明没有更多的数据，直接提示没有更多数据
        if( adapter.getDataSize() <6){
            recyclerView.setCanLoadMoreRefresh(false);
            return;
        }
        recyclerView.setDefaultStatus();
        page++;
        showError();
    }

    private String getUrl() {
        str.setLength(0);
        if (recyclerView.isPullDownToRefresh()){
            page = 1;
        }
        str.append(baseUrl);
        str.append("page=" + page);
        Log.i(TAG, "getUrl: " + str.toString());
        return str.toString();
    }

    @Override
    public void onLoadMore() {
        if (!recyclerView.isRefreshing()) {
            recyclerView.setPullUpToRefresh();
            getMessage();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst && isVisibleToUser && adapter != null && adapter.getDataSize() == 0) {
            if (swipeRefreshLayout != null && !recyclerView.isErrorStatus()) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        recyclerView.setPullDownToRefresh();
                        getMessage();
                    }
                });
            }
        }
    }

}
