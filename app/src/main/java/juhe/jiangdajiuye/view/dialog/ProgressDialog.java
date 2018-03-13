package juhe.jiangdajiuye.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import juhe.jiangdajiuye.R;

/**
 * Created by wangqiang on 2016/10/2.
 */
public class ProgressDialog extends Dialog {
    private AnimationDrawable mAnimation;
    private Context mContext;
    private ImageView mImageView;
    private int count = 0;
    private int mResid;//图片资源
    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, int id) {
        super(context);
        this.mContext = context;
        this.mResid = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        initView();
//        initData();
    }

    private void initData() {
        mImageView.setBackgroundResource(mResid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();

            }
        });

    }
    private void initView() {
        setContentView(R.layout.progress_dialog);
//        setProgressStyle(R.style.LodingDialog);
        //解决android 5以上dialog无法通过style 来设置透明背景的问题
        getWindow().setBackgroundDrawable(new ColorDrawable());
//        mImageView = (ImageView) findViewById(R.id.progress_image);
    }
}
