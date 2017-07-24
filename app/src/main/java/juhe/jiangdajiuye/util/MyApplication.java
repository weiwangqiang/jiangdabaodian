package juhe.jiangdajiuye.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by wangqiang on 2017/4/23.
 */

public class MyApplication extends Application {
    protected static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }

    public static Context getApplication(){
        return context;
    }
}
