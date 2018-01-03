package juhe.jiangdajiuye.core;

import android.app.Application;
import android.content.Context;

import juhe.jiangdajiuye.tool.MyExceptionHolder;

/**
 * Created by wangqiang on 2017/4/23.
 *
 * 不要在application 中完成第三方的初始化工作
 *  可以开一个service完成所有第三方的初始化工作
 */

public class MyApplication extends Application{
    private static final String TAG = "MyApplication";
    protected static MyApplication application;
    public static Context getContext() {
        return context;
    }
    public MyApplication() {
    }
    public static Context context ;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = this;
        /**
         *  开一个service完成所有第三方的初始化工作
         */
        InitWorker.initSDK(this);
        MyExceptionHolder holder = new MyExceptionHolder(this);
    }

    public static MyApplication getApplication(){
        return application;
    }
}
