package juhe.jiangdajiuye.consume;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.utils.SizeConvert;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-03-18
 */

public class UserCenterItem extends RelativeLayout {
    private TextView leftTV ;
    private String leftText ;
    private int leftTextColor ;
    private int leftTextSize ;
    private LayoutParams leftParams ;

    private TextView rightTV ;
    private String rightText ;
    private int rightTextColor ;
    private int rightTextSize ;
    private LayoutParams rightParams ;

    private ImageView mRightImage;
    private Drawable rightDrawable;

    public UserCenterItem(Context context) {
        super(context);
    }

    public UserCenterItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }


    public UserCenterItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.userCenterItem);
        leftText = typedArray.getText(R.styleable.userCenterItem_leftText).toString();

        leftText = typedArray.getText(R.styleable.userCenterItem_leftText).toString();

        leftTextColor = typedArray.getColor(R.styleable.userCenterItem_leftTextColor,
                getResources().getColor(R.color.gray));

        leftTextSize = typedArray.getDimensionPixelSize(R.styleable.userCenterItem_leftTextSize,15);
        rightText = typedArray.getText(R.styleable.userCenterItem_rightText).toString();
        rightTextColor = typedArray.getColor(R.styleable.userCenterItem_rightTextColor,
                getResources().getColor(R.color.gray));
        rightTextSize = typedArray.getDimensionPixelSize(R.styleable.userCenterItem_rightTextSize,15);
        rightDrawable = typedArray.getDrawable(R.styleable.userCenterItem_rightImage);
        typedArray.recycle();//回收
        leftTV = new TextView(context);
        rightTV = new TextView(context);
        mRightImage = new ImageView(context);

        leftTV.setText(leftText);
        leftTV.setGravity(CENTER_IN_PARENT);
        rightTV.setText(rightText);
        rightTV.setGravity(CENTER_IN_PARENT);
        leftParams  = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams  = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(ALIGN_PARENT_LEFT);
        leftParams.setMargins(getTextMargin(),getTextMargin(),getTextMargin(),getTextMargin());

        rightParams.setMargins(getTextMargin(),getTextMargin(),getTextMargin(),getTextMargin());
        rightParams.addRule(ALIGN_PARENT_RIGHT);

        addView(leftTV,leftParams);
        addView(rightTV,rightParams);
    }
    public int getTextMargin(){
        return SizeConvert.dip2px(8);
    }
}
