package juhe.jiangdajiuye.utils.httpUtils.task;

import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * class description here
 *
 * 执行任务的类
 * 采用线程池并发，将任务提交给队列，再从队列中获取任务
 * @author wangqiang
 * @since 2018-02-14
 */

public class TaskExecuteManager {
    private static final TaskExecuteManager ourInstance = new TaskExecuteManager();
    private static final String TAG = "TaskExecuteManager";
    //请求线程池
    private ThreadPoolExecutor threadPoolExecutor;
    //请求队列
    private LinkedBlockingQueue<Future<?>> requestQueue = new LinkedBlockingQueue<>();

    public static TaskExecuteManager getInstance() {
        return ourInstance;
    }

    private TaskExecuteManager() {
        int available = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor = new ThreadPoolExecutor(available, 2 * available + 1,
                10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedHandler());
        threadPoolExecutor.execute(getExecuteTaskRunnable);
    }

    //执行任务
    public void execute(Runnable runnable) {
        if (null != runnable) {
            FutureTask<Object> futureTask = new FutureTask<>(runnable, null);
            try {
                requestQueue.put(futureTask);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable getExecuteTaskRunnable = new Runnable() {
        @Override
        public void run() {
            FutureTask futureTask = null;
            while (true) {
                try {
                    //取请求任务
                    boolean main = (Looper.getMainLooper() == Looper.myLooper()) ;
                    Log.i(TAG, "run: "+ main);
                    futureTask = (FutureTask) requestQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (null != futureTask) {
                    threadPoolExecutor.execute(futureTask);
                }
            }
        }
    };

    //线程池的拒绝策略
    class RejectedHandler implements RejectedExecutionHandler {


        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                requestQueue.put(new FutureTask<>(r, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
