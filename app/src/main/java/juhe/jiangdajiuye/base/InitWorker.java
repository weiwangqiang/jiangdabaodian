package juhe.jiangdajiuye.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import juhe.jiangdajiuye.net.NetStateUtils;

/**
 * class description here
 * 初始化sdk 的worker
 * @author wangqiang
 * @since 2017-11-05
 */

public class InitWorker {

    private static final String TAG = "InitWorker ";

    public static void initSDK(Context mCtx) {

        final String Bugly_APPId = "a40ad72c38"; // TODO 替换成bugly上注册的appid
        String Bmob_AppId = "f1a3949757fdc914a823e15eef961ce6";//bmob
        // 初始化BmobSDK
        Bmob.initialize(mCtx, Bmob_AppId);
// 使用推送服务时的初始化操作
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {

            }
        });
        // 启动推送服务
        BmobPush.startWork(mCtx);
        //初始化当前网络状况
        NetStateUtils.initNetWorkState(mCtx);
        // 获取当前包名
        String packageName = mCtx.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mCtx);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        strategy.setAppChannel("debug");
        if(!isApkInDebug(mCtx)){
            Log.i(TAG, "initSDK: start bugly init ");
            CrashReport.initCrashReport(mCtx, Bugly_APPId,false);
            CrashReport.startCrashReport();
        }
        Log.i(TAG, "onStartCommand: 初始化完成 ！ ");

    }
    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
