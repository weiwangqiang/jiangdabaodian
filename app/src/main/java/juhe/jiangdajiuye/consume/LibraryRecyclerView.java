package juhe.jiangdajiuye.consume;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import juhe.jiangdajiuye.adapter.LibraryCollectAdapter;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-12-31
 */

public class LibraryRecyclerView extends RecyclerView {


    private static final String TAG = "LibraryRecyclerView";
    private LibraryCollectAdapter adapter;

    public LibraryRecyclerView(Context context) {
        super(context);
    }

    public LibraryRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        adapter = (LibraryCollectAdapter) getAdapter();
        if(adapter.getItemCount()==0)
            return super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (adapter.isHasChildOpen()) {
                    LayoutManager layoutManager = getLayoutManager();
                    View view = layoutManager.findViewByPosition(adapter.getOpenPosition());
                    if (DownOnTheDeleteButton(ev, view)){
                        Log.i(TAG, "dispatchTouchEvent: give to delete ");
                        return super.dispatchTouchEvent(ev);
                    }
                    else {
                        Log.i(TAG, "dispatchTouchEvent: hasChildOpen  return false ");
                        adapter.closeSubItem();
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
        int left = view.getLeft();
        int top = view.getTop();
        int bottom = view.getBottom();
        Log.i(TAG, "DownOnTheDeleteButton  X :"+downX+" Y: "+downY);
        Log.i(TAG, "DownOnTheDeleteButton: left"+left+" top : "+top+" bottom : "+bottom);
        boolean InTopAndBottom = (downY >= top && downY <= bottom);
        boolean InLeftAndRight = (downX > left);
        return InLeftAndRight && InTopAndBottom;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onInterceptTouchEvent: down ");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onInterceptTouchEvent: up ");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
