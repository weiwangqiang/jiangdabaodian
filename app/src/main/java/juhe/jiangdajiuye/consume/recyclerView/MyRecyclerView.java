package juhe.jiangdajiuye.consume.recyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import juhe.jiangdajiuye.consume.recyclerView.adapter.AbsAdapter;


/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-08
 */

public class MyRecyclerView extends RecyclerView {
    public static String TAG = "MyRecyclerView";

    public int getStatus() {
        return mStatus;
    }

    private int mStatus = 0;

    public static final int STATUS_DEFAULT = 0;//不在刷新或者end状态
    public static final int STATUS_REFRESHING = 0x10; //正在刷新状态
    public static final int STATUS_PULL_TO_REFRESH = 0x11;//下拉刷新状态
    public static final int STATUS_END = 0x12;//没有更多状态
    public static final int STATUS_ERROR = 0x13;//出错状态

    @Override
    public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener) {

    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
        addOnScrollListener( mOnLoadMoreScrollListener);
    }

    public OnLoadMoreListener mOnLoadMoreListener ;
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private OnLoadMoreScrollListener mOnLoadMoreScrollListener = new OnLoadMoreScrollListener() {
        @Override
        public void onLoadMore(RecyclerView recyclerView) {
            if (mOnLoadMoreListener != null && mStatus == STATUS_DEFAULT) {
                mOnLoadMoreListener.onLoadMore();
            }
        }
    };
    public void setCanLoadMoreRefresh(boolean refresh){
        mStatus = refresh ? STATUS_DEFAULT : STATUS_END ;
        setStatus(mStatus);
    }

    public void setStatus(int status){
        mStatus = status ;
        if(getAdapter() instanceof AbsAdapter){
            Log.i(TAG, "setStatus: set state ");
            ((AbsAdapter) getAdapter()).stateChange(status);
        }
    }
}
