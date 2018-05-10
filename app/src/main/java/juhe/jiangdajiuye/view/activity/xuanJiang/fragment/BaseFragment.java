package juhe.jiangdajiuye.view.activity.xuanJiang.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.IndexAdapter;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.db.repository.BrowseDepository;
import juhe.jiangdajiuye.net.httpUtils.HttpHelper;
import juhe.jiangdajiuye.net.httpUtils.inter.IDataListener;
import juhe.jiangdajiuye.net.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.ui.recyclerView.LoadMoreRecyclerView;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.view.activity.browse.MesBrowseActivity;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;

/**
 * class description here
 * fragment 基类
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public abstract class BaseFragment extends Fragment implements LoadMoreRecyclerView.OnLoadMoreListener {
    private static final String TAG = "BaseFragment";
    private View view, error;
    private LoadMoreRecyclerView recyclerView;
    private LinearLayoutManager manager;
    private IndexAdapter adapter;
    //是否没有初始化数据
    private Boolean isFirst = true;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HttpHelper httpHelper;
    private MesItemHolder holder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            if (recyclerView.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fragment, container, false);
        init();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetStateReceiver.removeLister(receiver);
    }

    public void init() {
        Bundle bundle = getArguments();
        holder = new MesItemHolder();
        holder.setBaseUrl(bundle.getString("BaseUrl"));
        holder.setCollege(bundle.getString("college"));
        holder.setCollegeId(bundle.getInt("collegeId"));
        holder.setProvinceId(bundle.getInt("provinceId"));
        findId();
        initRefresh();
        initList();
        bindNetState();
        httpHelper = HttpHelper.getInstance();
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
                if (recyclerView.isErrorStatus()) {
                    recyclerView.setDefaultStatus();
                }
            }
        }
    };

    public void findId() {
        error = view.findViewById(R.id.error);
        recyclerView = view.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnLoadMoreListener(this);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
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
                if (!recyclerView.isErrorStatus()) {
                    recyclerView.setPullDownToRefresh();
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
                    recyclerView.setPullDownToRefresh();
                    getMessage();
                }
            });
        }
    }

    public void initList() {
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
            ToastUtils.showToast("网络不太顺畅哦！");
            showError(true);
        }
    };

    /**
     * http://career.hdu.edu.cn/module/getcareers?start_page=1&keyword=&type=inner&day=&count=10&start=1
     */
    public void getMessage() {
        String url = getUrl(recyclerView.isPullDownToRefresh()
                , holder);
        httpHelper.get(url, holder, iDataListener, HttpTask.Type.MessageItem);
    }

    /**
     * 判断当前是否需要显示Error
     */
    private void showError(boolean isError) {
        error.setVisibility(isError ? View.VISIBLE:View.GONE);
    }

    public void upDate(List<MessageBean> list) {
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
        if (adapter.getDataSize() < 6) {
            recyclerView.setCanLoadMoreRefresh(false);
            return;
        }
        recyclerView.setDefaultStatus();
        RequestSuccess();
        showError(adapter.getDataSize() ==0);
    }

    public abstract String getUrl(boolean isPullDownToRefresh, MesItemHolder holder);

    public abstract void RequestSuccess();

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
        if (isFirst && isVisibleToUser) {
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
