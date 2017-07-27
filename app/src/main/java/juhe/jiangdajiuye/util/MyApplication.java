package juhe.jiangdajiuye.util;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;
import juhe.jiangdajiuye.MainActivity;
import juhe.jiangdajiuye.R;

/**
 * Created by wangqiang on 2017/4/23.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public static final String APP_ID = "19ecb1a49a"; // TODO 替换成bugly上注册的appid

    protected static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        Log.i(TAG, "onCreate: init jpush ");
        Beta.autoCheckUpgrade = true;
        Beta.showInterruptedStrategy = false;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.largeIconId = R.mipmap.logo;
        Beta.smallIconId = R.mipmap.logo;
        Beta.defaultBannerId = R.mipmap.logo;
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.showInterruptedStrategy = false;
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;

        CrashReport.initCrashReport(getApplicationContext(), APP_ID, true);
        Bugly.init(getApplicationContext(),APP_ID , true);
    }

    public static Context getApplication(){
        return context;
    }
}
