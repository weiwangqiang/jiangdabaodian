package juhe.jiangdajiuye.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by wangqiang on 2017/4/23.
 *
 * 不要在application 中完成第三方的初始化工作
 *  可以开一个service完成所有第三方的初始化工作
 *
 *
 *  静态注册广播有bug，可能无法监听到广播事件，推荐使用动态注册方式
 */

public class BaseApplication extends Application{
    private static final String TAG = "BaseApplication";
    protected static BaseApplication application;
    public static Context getContext() {
        return context;
    }
    public BaseApplication() {
    }
    public static Context context ;
//    private static RefWatcher refWatcher ;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = this;
        /**
         *  开一个service完成所有第三方的初始化工作
         */
//        MyExceptionHolder holder = new MyExceptionHolder(this);
//        Stetho.initializeWithDefaults(this);//facebook的调试工具
        InitWorker.initSDK(this);
//        refWatcher = LeakCanary.install(this);
    }

    public static BaseApplication getApplication(){
        return application;
    }

//    public static RefWatcher getRefWatcher() {
//        return refWatcher;
//    }
}
