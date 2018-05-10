package juhe.jiangdajiuye.ui.recyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter;

import static juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter.STATUS_DEFAULT;
import static juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter.STATUS_END;
import static juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter.STATUS_ERROR;
import static juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter.STATUS_PULL_DOWN_TO_REFRESH;
import static juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter.STATUS_PULL_UP_TO_REFRESH;


/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-08
 */

public class LoadMoreRecyclerView extends RecyclerView {
    public static String TAG = "LoadMoreRecyclerView";

    public int getStatus() {
        return mStatus;
    }

    //是否处于刷新状态
    public boolean isRefreshing() {
        return mStatus == STATUS_PULL_UP_TO_REFRESH || mStatus == STATUS_PULL_DOWN_TO_REFRESH;
    }

    //是否是下拉刷新
    public boolean isPullDownToRefresh() {
        return mStatus == STATUS_PULL_DOWN_TO_REFRESH;
    }
    public boolean isPullUpToRefresh(){
        return mStatus == STATUS_PULL_UP_TO_REFRESH;
    }

    //设置默认状态
    public void setDefaultStatus(){
      setStatus(STATUS_DEFAULT );
    }
    public boolean isErrorStatus(){
        return mStatus == STATUS_ERROR ;
    }
    private int mStatus = 0;


    @Override
    public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener) {

    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
        addOnScrollListener(new ScrollListener());
    }

    public OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCanLoadMoreRefresh(boolean refresh) {
        setStatus(refresh ? STATUS_DEFAULT : STATUS_END);
    }
    public void setErrorStatus(){
        setStatus(STATUS_ERROR);
    }
    private void setStatus(int status) {
        mStatus = status;
        if (getAdapter() instanceof AbsAdapter) {
            ((AbsAdapter) getAdapter()).stateChange(mStatus);
        }
    }
    //设置上拉刷新状态
    public void setPullUpToRefresh(){
        setStatus(STATUS_PULL_UP_TO_REFRESH);
    }
    //设置下拉刷新状态
    public void setPullDownToRefresh(){
        setStatus(STATUS_PULL_DOWN_TO_REFRESH);
    }
    private class ScrollListener extends OnScrollMoreListener {

        @Override
        public void onLoadMore(RecyclerView recyclerView) {
            if (mOnLoadMoreListener != null && mStatus == STATUS_DEFAULT) {
                mOnLoadMoreListener.onLoadMore();
            }
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
