package juhe.jiangdajiuye.core;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import juhe.jiangdajiuye.consume.MyExceptionHolder;

/**
 * Created by wangqiang on 2017/4/23.
 *
 * 不要在application 中完成第三方的初始化工作
 *  可以开一个service完成所有第三方的初始化工作
 */

public class BaseApplication extends Application{
    private static final String TAG = "BaseApplication";
    protected static BaseApplication application;
    private static Handler handler;
    public static Context getContext() {
        return context;
    }
    public BaseApplication() {
    }
    public static Context context ;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = this;
        handler = new Handler(Looper.getMainLooper());
        /**
         *  开一个service完成所有第三方的初始化工作
         */
        MyExceptionHolder holder = new MyExceptionHolder(this);
        InitWorker.initSDK(this);
    }

    public static BaseApplication getApplication(){
        return application;
    }
    //将任务提交给主线程
    public static void runOnMainThread(Runnable  task){
       handler.post(task);
    }
}
