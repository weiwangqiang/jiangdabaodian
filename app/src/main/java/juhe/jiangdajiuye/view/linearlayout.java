package juhe.jiangdajiuye.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by wangqiang on 2016/10/1.
 */
public class linearlayout extends LinearLayout {

    public linearlayout(Context context) {
        super(context);
    }

    public linearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public linearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return true;
    }
}
