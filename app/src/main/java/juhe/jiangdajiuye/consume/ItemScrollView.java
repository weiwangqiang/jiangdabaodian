package juhe.jiangdajiuye.consume;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.util.ResourceUtils;
import juhe.jiangdajiuye.util.ScreenSizeUtil;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-12-31
 */

public class ItemScrollView extends HorizontalScrollView {
    private final String TAG = "ItemScrollView";
    private int viewWith = ScreenSizeUtil.getScreenWith() / 6;
    private OnSubItemClickListener listener;
    //id常量
    private final int ID_ITEMVIEW = 0x10;
    private final int ID_DELETE = 0x20;
    private final int ID_COLLECT = 0x30;

    private int subViewWith = 0; //需要滑动的距离
    private LinearLayout parenView = null;
    private boolean close = true; //是否处于关闭状态

    public View getItemView() {
        return itemView;
    }

    private View itemView = null;

    public ItemScrollView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        parenView = new LinearLayout(context);
        parenView.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parenView.setLayoutParams(layoutParams);
        addView(parenView);
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public ItemScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private final int CLICK_DISTANCE = 5 ;//默认点击的触摸距离小于此
    private final int TOUCH_DEFAULT = 0;
    private final int TOUCH_CLICK = 1;
    private final int TOUCH_SCROLL = 2;
    private int TOUCH_STATE = TOUCH_DEFAULT;
    private int upX = 0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                TOUCH_STATE = TOUCH_DEFAULT ;
                if(!close && (downX < (ScreenSizeUtil.getScreenWith() - subViewWith))) {
                    close();
                    Log.i(TAG, "onInterceptTouchEvent: close ");
                    return true ;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onInterceptTouchEvent: down ");
                int cur = (int) ev.getRawX();
                if (Math.abs(cur - downX) > CLICK_DISTANCE) {
                    TOUCH_STATE = TOUCH_SCROLL;
                }
                if(TOUCH_STATE == TOUCH_SCROLL){
                    return true ;
                }
                break;
            case MotionEvent.ACTION_UP:
                upX = (int) ev.getRawX();
                if ( (TOUCH_STATE == TOUCH_SCROLL ) ||  (Math.abs(upX - downX) > CLICK_DISTANCE)) {
                    return true;
                }

                    default:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }
    //手指第一次点击的X位置
    private float downX;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (subViewWith == 0)
            return super.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                Log.i(TAG, "onTouchEvent: down ");
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float distance = Math.abs(ev.getRawX() - downX);
                if(!close && (ScreenSizeUtil.getScreenWith() - downX < subViewWith)){
                    Log.i(TAG, "onInterceptTouchEvent: give to delete ");
                    return false  ;
                }
                if (close) {
                    return tryToOpen(ev);
                } else {
                    return tryToClose(ev);
                }
        }
        return super.onTouchEvent(ev);
    }

    private boolean tryToClose(MotionEvent ev) {
        float distance = ev.getRawX() - downX;
        Log.i(TAG, "tryToClose: " + distance);
        if (distance < 0) {
            return super.onTouchEvent(ev);
        }
        //满足关闭条件
        if (distance >= (subViewWith / 3)) {
            close();
        } else {  //打开
            open();
        }
        return true;
    }

    private boolean tryToOpen(MotionEvent ev) {
        float distance = downX - ev.getRawX();
        Log.i(TAG, "tryToOpen: " + distance);
        if (distance < 0) {
            return super.onTouchEvent(ev);
        }
        //满足打开条件
        if (distance >= (subViewWith / 3)) {
            open();
        } else { //关闭
            close();
        }
        return true;
    }

    public void addItemView(View view) {
        LayoutParams layoutParams = new LayoutParams(ScreenSizeUtil.getScreenWith(),
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        parenView.addView(view, 0);
        itemView = view;
        itemView.setTag(ID_ITEMVIEW);
        itemView.setOnClickListener( new SubViewClickListener());
    }

    public void addBoth() {
        addDeleteSubItem();
        addCollectSubItem();
    }

    public void addDeleteSubItem() {
        TextView delete = new TextView(getContext());
        delete.setTag(ID_DELETE);
        LayoutParams deleteParam = new LayoutParams(viewWith, ViewGroup.LayoutParams.MATCH_PARENT);
        delete.setLayoutParams(deleteParam);
        delete.setOnClickListener(new SubViewClickListener());
        parenView.addView(delete);
        delete.setBackgroundColor(ResourceUtils.getColor(R.color.red));
        delete.setText("删除");
        delete.setTextColor(ResourceUtils.getColor(R.color.white));
        delete.setGravity(Gravity.CENTER);
        subViewWith += viewWith;
    }

    public void addCollectSubItem() {
        TextView collect = new TextView(getContext());
        collect.setTag(ID_COLLECT);
        LayoutParams deleteParam = new LayoutParams(viewWith, ViewGroup.LayoutParams.MATCH_PARENT);
        collect.setLayoutParams(deleteParam);
        collect.setOnClickListener(new SubViewClickListener());
        parenView.addView(collect);
        collect.setBackgroundColor(ResourceUtils.getColor(R.color.gray));
        collect.setText("收藏");
        collect.setGravity(Gravity.CENTER);
        subViewWith += viewWith;
    }

    /**
     * 关闭
     */
    public void close() {
        ScrollTo(0);
        close = true;
        if(listener != null)
        listener.onStateChange(true);
    }


    public void open() {
        ScrollTo(subViewWith);
        close = false;
        Log.i(TAG, "open: ");
        if(listener != null)
            listener.onStateChange(false);
    }


    private void ScrollTo(final int distance) {
        this.post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(distance, 0);
            }
        });
    }
    //是否处于打开状态
    public boolean isClose() {
        return close;
    }

    public void setOnSubItemClickListener(OnSubItemClickListener listener) {
        this.listener = listener;
    }

    private class SubViewClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: ");
            if (null == listener)
                return;
            Log.i(TAG, "onClick: list is not null ");
            listener.onItemClick(v);

        }
    }

    public interface OnSubItemClickListener {
        /**
         * 点击
         *
         * @param view
         */
        void onItemClick(View view);

        /**
         *   状态改变的时候触发
         *
         * @param isClose 是否处于关闭状态
         */
        void onStateChange(boolean isClose);

    }
}
