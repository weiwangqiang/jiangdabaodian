package juhe.jiangdajiuye.core;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.util.ToastUtils;

/**
 * 基础activity
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    public static String EXIT_APP_ACTION = "com.joshua.exit";
    public final int NET_SUCCESS = 0x1;
    public final int NET_ERROR = 0x2;
    private String TAG = "BaseActivity";
    public static ToastUtils toastUtils;

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScreen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();

//        if (!isFinished) {
//            //括号内部的代码请单独提成一个方法  我这里是为了视觉 懒了
//            String packageName="com.joshua.a51bike";//我们自己的应用的包名
//            String topActivityClassName= UiUtils.getTopActivityInfo(this);
//            if (packageName!=null&&topActivityClassName!=null && !topActivityClassName.startsWith(packageName))
//            {
//                //app已经后台
//                UiUtils.setBackstage(true);
//                Log.e(TAG,"------>> is to backStage<<<<<-------------");
//            }
//        }
    }
    /**
     * 初始化屏幕方向
     */
    private void initScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
    }

    //通用的启动activity动画
    protected void startActivitySlideInRight(Context mCtx , Class target) {
        startActivity(new Intent(mCtx, target));
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
    }
}
