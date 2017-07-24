package juhe.jiangdajiuye.core;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.xutils.x;

import juhe.jiangdajiuye.util.MyApplication;
import juhe.jiangdajiuye.util.Uiuilts;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    public static String EXIT_APP_ACTION = "com.joshua.exit";
    private Boolean isFinished = false;
    public final int NET_SUCCESS = 0x1;
    public final int NET_ERROR = 0x2;
    private String TAG = "BaseActivity";
    public static Uiuilts uiutils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiutils = new Uiuilts(MyApplication.getApplication());
        initScreen();
        initXUtils();
        initReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(b){
//            UiUtils.setBackstage(false);
//            Log.e(TAG,"------>> is  backStage and start welcome <<<<<-------------");
//            Intent intent = new Intent(BaseActivity.this, WelCome.class);
//            startActivity(intent);
//        }
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

    @Override
    public void finish() {
        super.finish();
        isFinished = true;
    }

    /**
     * 初始化屏幕方向
     */
    private void initScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
    }

    /**
     * 初始化xUtils
     */
    private void initXUtils() {
        x.view().inject(this);
        x.Ext.init(getApplication());
    }

    /**
     * 初始化退出广播监听
     */
    private void initReceiver() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
