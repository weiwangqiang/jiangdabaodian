package juhe.jiangdajiuye.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by wangqiang on 2016/10/1.
 */
public class MainLinearLayout extends LinearLayout {

    public MainLinearLayout(Context context) {
        super(context);
    }

    public MainLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MainLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return true;
    }
}
