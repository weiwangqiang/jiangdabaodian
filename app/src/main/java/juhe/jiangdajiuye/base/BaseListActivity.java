package juhe.jiangdajiuye.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.ui.recyclerView.LoadMoreRecyclerView;
import juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter;

/**
 * class description here
 * <p>
 * the xml should include <br>include_load_more_recycler</> resource
 *
 * @author wangqiang
 * @since 2018-04-27
 */
public abstract class BaseListActivity<T> extends BaseActivity {
    private static final String TAG = "BaseListActivity";
    protected LoadMoreRecyclerView loadMoreRecyclerView;
    protected AbsAdapter<T> adapter;
    protected SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvError = findViewById(R.id.include_load_more_error_textview);
        loadMoreRecyclerView = findViewById(R.id.include_load_more_recycler);
        loadMoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadMoreRecyclerView.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!loadMoreRecyclerView.isRefreshing() && canLoadMore()) {
                    loadMoreRecyclerView.setPullDownToRefresh();
                    requestMes();
                } else {
                    loadMoreRecyclerView.setCanLoadMoreRefresh(false);
                }
            }
        });
        adapter = getAdapter();
        loadMoreRecyclerView.setAdapter(adapter);
        initRefresh();
    }

    private void initRefresh() {
        swipeRefreshLayout = findViewById(R.id.include_load_more_SwipeRefreshLayout);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL);
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setOverScrollMode(View.OVER_SCROLL_NEVER);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(this, R.color.white));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.baseColor),
                ContextCompat.getColor(this, R.color.baseColor));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!loadMoreRecyclerView.isRefreshing() && canPullRefresh()) {
                    loadMoreRecyclerView.setPullDownToRefresh();
                    resetParams();
                    requestMes();
                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (!needInitRefresh()) {
                    return;
                }
                if (loadMoreRecyclerView.isRefreshing()) {
                    return;
                }
                loadMoreRecyclerView.setPullDownToRefresh();
                swipeRefreshLayout.setRefreshing(true);
                resetParams();
                requestMes();
            }
        });
    }

    public void showError(boolean error) {
        tvError.setVisibility(error ? View.VISIBLE : View.GONE);
        loadMoreRecyclerView.setVisibility(error ? View.GONE : View.VISIBLE);
    }

    /**
     * 更新list 必须调用
     *
     * @param data
     */
    public void upDateList(List<T> data) {
        if (data == null || data.size() == 0) {
            if (adapter.getDataSize() == 0) {
                showError(true);
                return;
            } else if (loadMoreRecyclerView.isPullDownToRefresh()) {
                adapter.upDate(data);
            }
            loadMoreRecyclerView.setCanLoadMoreRefresh(false);
            return;
        }
        showError(false);
        if (loadMoreRecyclerView.isPullDownToRefresh()) {
            adapter.upDate(data);
        } else {
            adapter.appendData(data);
        }
        //数据不够直接显示无更多
        if (adapter.getDataSize() < 5) {
            loadMoreRecyclerView.setCanLoadMoreRefresh(false);
        } else {
            loadMoreRecyclerView. setDefaultStatus();
        }
    }

    /**
     * 是否需要初始化刷新
     *
     * @return
     */
    protected boolean needInitRefresh() {
        return false;
    }

    /**
     * @param isDownToRefresh 是否是下拉刷新
     */
    protected void requestStart(boolean isDownToRefresh) {
        if (isDownToRefresh) {
            swipeRefreshLayout.setRefreshing(true);
            loadMoreRecyclerView.isPullDownToRefresh();
        } else {
            loadMoreRecyclerView.setPullUpToRefresh();
        }
    }

    protected void requestEnd() {
        swipeRefreshLayout.setRefreshing(false);
        if (loadMoreRecyclerView.isRefreshing()) {
            loadMoreRecyclerView.setDefaultStatus();
        }
    }

    /**
     * 返回可以上拉刷新的其他条件
     *
     * @return 默认返回 true
     */
    protected boolean canLoadMore() {
        return true;
    }

    /**
     * 其他因素
     *
     * @return 默认返回true
     */
    protected boolean canPullRefresh() {
        return true;
    }

    @NonNull
    protected abstract AbsAdapter<T> getAdapter();


    /**
     * 重置请求参数
     *
     * @return
     */
    protected abstract void resetParams();

    /**
     * 进行网络请求工作
     * 有3处：上拉，下拉，初始化（needInitRefresh返回true的情况）
     */
    protected abstract void requestMes();

}
