package juhe.jiangdajiuye.utils;

import android.content.Context;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-01-03
 */

public class MyExceptionHolder implements Thread.UncaughtExceptionHandler   {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext ;
    public MyExceptionHolder(Context context){
       init(context);
    }
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    /**
     * Method invoked when the given thread terminates due to the
     * given uncaught exception.
     * <p>Any exception thrown by this method will be ignored by the
     * Java Virtual Machine.
     *
     * @param t the thread
     * @param e the exception
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        mDefaultHandler.uncaughtException(t, e);

    }
}
