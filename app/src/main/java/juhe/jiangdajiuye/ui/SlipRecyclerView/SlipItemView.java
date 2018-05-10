package juhe.jiangdajiuye.ui.SlipRecyclerView;

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
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ScreenSizeUtils;

/**
 * class description here
 * <p>
 * slipRecyclerView的 item布局
 *
 * @author wangqiang
 * @since 2017-12-31
 */

public class SlipItemView extends HorizontalScrollView {
    private final String TAG = "SlipItemView";
    private int viewWith = ScreenSizeUtils.getScreenWith() / 6;//subView的默认宽度
    private OnSlipItemViewClickListener listener;
    //id常量
    private final int ID_ITEMVIEW = 0x10;
    private final int ID_DELETE = 0x20;
    private final int ID_COLLECT = 0x30;

    private final int CLICK_DISTANCE = 5;//默认点击的触摸距离小于此
    //手指第一次点击的X位置
    private float downX;
    private int subViewWith = 0; //需要滑动的距离
    private LinearLayout parenView = null;
    private boolean close = true; //是否处于关闭状态

    //获取主view
    public View getItemView() {
        return itemView;
    }

    private View itemView = null; // 即主View

    public SlipItemView(Context context) {
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

    public SlipItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlipItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                if (!close && (downX < (ScreenSizeUtils.getScreenWith() - subViewWith))) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int cur = (int) ev.getRawX();
                if (Math.abs(cur - downX) > CLICK_DISTANCE) {
                    return true ;
                }
                break;
            default:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (subViewWith == 0)
            return super.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                break;
            case MotionEvent.ACTION_UP:
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
        if(Math.abs(distance)<CLICK_DISTANCE){
            close();
            return true ;
        }
        //满足关闭条件 1、用户向一个方向移动，满足close状态，2、用户来回移动，但最终位置满足close状态
        if (((getScrollX() != subViewWith)&& Math.abs(getScrollX() - subViewWith) >=(subViewWith / 3 ))) {
            close();
        } else {  //打开
            open();
        }
        return true;
    }

    private boolean tryToOpen(MotionEvent ev) {
        float distance = downX - ev.getRawX();
        //满足打开条件 1、用户向一个方向移动，满足open状态，2、用户来回移动，但最终位置满足open状态
        if (((getScrollX() != 0 )&& getScrollX() >= (subViewWith / 3))) {
            open();
        } else { //关闭
            close();
        }
        return true;
    }

    public void addItemView(View view) {
        LayoutParams layoutParams = new LayoutParams(ScreenSizeUtils.getScreenWith(),
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        parenView.addView(view, 0);
        itemView = view;
        itemView.setTag(ID_ITEMVIEW);
        itemView.setOnClickListener(new SubViewClickListener());
    }

    public void addDeleteSubView() {
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

    //添加收藏的SubView
    public void addCollectSubView() {
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

    //添加自定义的SubView
    public void addSubView(View view) {
        LayoutParams deleteParam = new LayoutParams(viewWith, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(deleteParam);
        view.setOnClickListener(new SubViewClickListener());
        parenView.addView(view);
        subViewWith += viewWith;
    }

    /**
     * 关闭
     */
    public void close() {
        Log.i(TAG, "close: ");
        ScrollTo(0);
        close = true;
        if (listener != null)
            listener.onStateChange(true);
    }


    public void open() {
        ScrollTo(subViewWith);
        Log.i(TAG, "open: ");
        close = false;
        if (listener != null)
            listener.onStateChange(false);
    }


    private void ScrollTo(final int distance) {
        Log.i(TAG, "ScrollTo: " + distance);
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

    //slipItemView 的点击事件
    public void setOnSlipItemViewClickListener(OnSlipItemViewClickListener listener) {
        this.listener = listener;
    }

    private class SubViewClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (null == listener)
                return;
            listener.onItemClick(v);

        }
    }

    public interface OnSlipItemViewClickListener {
        /**
         * 点击
         *
         * @param view
         */
        void onItemClick(View view);

        /**
         * 状态改变的时候触发
         *
         * @param isClose 是否处于关闭状态
         */
        void onStateChange(boolean isClose);

    }
}
