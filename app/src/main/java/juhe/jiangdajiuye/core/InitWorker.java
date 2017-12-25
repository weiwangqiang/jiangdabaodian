package juhe.jiangdajiuye.core;

import android.content.Context;
import android.util.Log;

import com.tencent.bugly.Bugly;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import juhe.jiangdajiuye.util.NetWork.NetStateUtils;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-11-05
 */

public class InitWorker {

    private static final String TAG = "InitWorker ";

    public static void initSDK(Context mCtx) {

        final String Bugly_APPId = "a40ad72c38 "; // TODO 替换成bugly上注册的appid
//        final String Bugly_APPId = "f928f6fe1c "; // TODO 替换成bugly上注册的appid
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

        NetStateUtils.initNetWorkState(mCtx);
//        /***** Beta高级设置 *****/
//        /**
//         * true表示app启动自动初始化升级模块; false不会自动初始化;
//         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
//         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
//         */
//        Beta.autoInit = true;
//
//        /**
//         * true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
//         */
//        Beta.autoCheckUpgrade = true;
//
//        /**
//         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
//         */
////        Beta.upgradeCheckPeriod = 60 * 1000;
//        /**
//         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
//         */
//        Beta.initDelay = 5 * 1000;
//        /**
//         * 设置通知栏大图标，largeIconId为项目中的图片资源;
//         */
//        Beta.largeIconId = R.mipmap.logo;
//        /**
//         * 已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
//         */
//        Beta.showInterruptedStrategy = true;
//        /**
//         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
//         */
//        Beta.canShowUpgradeActs.add(MainActivity.class);
//
//        /***** Bugly高级设置 *****/
//        BuglyStrategy strategy = new BuglyStrategy();
//        /**
//         * 设置app渠道号
//         */
////        strategy.setAppChannel("96267489-18e1-46ae-8f93-307d23705396");
//
//        /***** 统一初始化Bugly产品，包含Beta *****/
//        Beta.smallIconId = R.mipmap.logo;
//        Beta.defaultBannerId = R.mipmap.logo;
//        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
//        Bugly.init(mCtx, Bugly_APPId, true, strategy);
        Bugly.init(mCtx,Bugly_APPId, true);
        Log.i(TAG, "onStartCommand: 初始化完成 ！ ");
    }
}
