package juhe.jiangdajiuye.utils.httpUtils;

import android.os.Handler;
import android.os.Looper;

/**
 * class description here
 *
 * 线程切换，将子线程切换到主线程，为了避免引入过多第三方包，
 * 这里不考虑用rxjava
 *
 * @author wangqiang
 * @since 2018-02-25
 */

public class ThreadHelper {
    private static ThreadHelper instance = new ThreadHelper();
    private Handler handler ;
    private ThreadHelper(){}
    public static ThreadHelper getInstance(){
        return instance ;
    }
    public void init(){
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     *  提交任务到主线程
     * @param task
     */
    public void runOnMainThread(Runnable task){
        if(null == handler){
            throw new NullPointerException("HttpHelper must init first! ");
        }
        handler.post(task) ;
    }
}
