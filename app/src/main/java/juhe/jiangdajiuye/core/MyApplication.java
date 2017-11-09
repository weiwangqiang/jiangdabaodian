package juhe.jiangdajiuye.core;

import android.app.Application;
import android.content.Context;

/**
 * Created by wangqiang on 2017/4/23.
 *
 * 不要在application 中完成第三方的初始化工作
 *  可以开一个service完成所有第三方的初始化工作
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    protected static MyApplication application;
    public static Context getContext() {
        return context;
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
        InitWoker.initSDK(this);
    }

    public static MyApplication getApplication(){
        return application;
    }
}
