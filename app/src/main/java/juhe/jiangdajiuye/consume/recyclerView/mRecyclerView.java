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

public class mRecyclerView extends RecyclerView {
    public static String TAG = "mRecyclerView";

    public int getmStatus() {
        return mStatus;
    }

    public int mStatus = 0;
    public static final int STATUS_DEFAULT = 0;//不在刷新或者end状态
    public static final int STATUS_REFRESHING = 1 ;
    public static final int STATUS_PULLTOREFRESH = 2;
    public static final int STATUS_LOADMORE = 3;
    public static final int STATUS_END = 4;
    public static final int STATUS_ERROR = 5;

    @Override
    public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener) {

    }

    public void setmOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
        addOnScrollListener( mOnLoadMoreScrollListener);
    }

    public OnLoadMoreListener mOnLoadMoreListener ;
    public mRecyclerView(Context context) {
        super(context);
    }

    public mRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public mRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private OnLoadMoreScrollListener mOnLoadMoreScrollListener = new OnLoadMoreScrollListener() {
        @Override
        public void onLoadMore(RecyclerView recyclerView) {
            Log.i(TAG, "onLoadMore: --------------");
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
            switch (status){
                case STATUS_DEFAULT:
                    ((AbsAdapter) getAdapter()).stateChange(AbsAdapter.STATUS_DEFAULT);
                    break;
                case STATUS_REFRESHING:
                    ((AbsAdapter) getAdapter()).stateChange(AbsAdapter.STATUS_REFRESHING);
                    break;
                case STATUS_END:
                    ((AbsAdapter) getAdapter()).stateChange(AbsAdapter.STATUS_END);
                    break;
                case STATUS_ERROR:
                    ((AbsAdapter) getAdapter()).stateChange(AbsAdapter.STATUS_ERROR);

            }
        }
    }
}
