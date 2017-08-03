package juhe.jiangdajiuye.util;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;
import juhe.jiangdajiuye.MainActivity;
import juhe.jiangdajiuye.R;

/**
 * Created by wangqiang on 2017/4/23.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public static final String APP_ID = "19ecb1a49a"; // TODO 替换成bugly上注册的appid
    private String Bmob_AppId = "f1a3949757fdc914a823e15eef961ce6";//bmob

    protected static MyApplication context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        Bmob.initialize(this, Bmob_AppId);

        Log.i(TAG, "onCreate: init jpush ");

        /***** Beta高级设置 *****/
        /**
         * true表示app启动自动初始化升级模块; false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = false;

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;
        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 10 * 1000;
        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.mipmap.logo;
        /**
         * 已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

        /***** Bugly高级设置 *****/
        BuglyStrategy strategy = new BuglyStrategy();
        /**
         * 设置app渠道号
         */
//        strategy.setAppChannel("96267489-18e1-46ae-8f93-307d23705396");

        /***** 统一初始化Bugly产品，包含Beta *****/
        Beta.smallIconId = R.mipmap.logo;
        Beta.defaultBannerId = R.mipmap.logo;
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
//        CrashReport.initCrashReport(getApplicationContext(), APP_ID, true);
        Bugly.init(this, APP_ID, true, strategy);
    }

    public static MyApplication getApplication(){
        return context;
    }
}
