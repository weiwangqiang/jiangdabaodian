package juhe.jiangdajiuye.ui.SlipRecyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * class description here
 * <p>
 * item具有侧滑功能的recyclerView
 *
 * @author wangqiang
 * @since 2017-12-31
 */

public class SlipRecyclerView extends RecyclerView {


    private static final String TAG = "SlipRecyclerView";
    private SlipRecyclerAdapter adapter;

    public SlipRecyclerView(Context context) {
        super(context);
    }

    public SlipRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        adapter = (SlipRecyclerAdapter) getAdapter();
        if (adapter.getItemCount() == 0)
            return super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (adapter.isHasChildOpen()) {
                    LayoutManager layoutManager = getLayoutManager();
                    View view = layoutManager.findViewByPosition(adapter.getOpenPosition());
                    if (DownOnTheDeleteButton(ev, view)) {
                        return super.dispatchTouchEvent(ev);
                    } else {
                        adapter.closeSlipItem();
                        return false;
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean DownOnTheDeleteButton(MotionEvent ev, View view) {
        int downX = (int) ev.getX();
        int downY = (int) ev.getY();
        if (null == view) {
            return false;
        }
        int left = view.getLeft();
        int top = view.getTop();
        int bottom = view.getBottom();
        boolean InTopAndBottom = (downY >= top && downY <= bottom);
        boolean InLeftAndRight = (downX > left);
        return InLeftAndRight && InTopAndBottom;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
