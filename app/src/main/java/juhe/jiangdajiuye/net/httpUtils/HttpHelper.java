package juhe.jiangdajiuye.net.httpUtils;

import juhe.jiangdajiuye.net.httpUtils.inter.IDataListener;
import juhe.jiangdajiuye.net.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.net.httpUtils.task.TaskExecuteManager;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;

/**
 * class description here
 *
 *  提供给客户端调用的类
 *
 * @author wangqiang
 * @since 2018-02-14
 */

public class HttpHelper<T> {
    private TaskExecuteManager taskExecuteManager = TaskExecuteManager.getInstance();
    private static HttpHelper instance ;
    public static HttpHelper getInstance(){
        if(null == instance){
            instance = new HttpHelper();
            instance.init();
        }
        return instance ;
    }

    /**
     * 在使用之前必须初始化，最好在application处
     */
    public void init(){
        ThreadHelper.getInstance().init();
    }
    public void get(String url, T requestBean, IDataListener<T> iDataListener, HttpTask.Type type) {
        HttpTask httpTask = new HttpTask(url, requestBean, iDataListener, type);
        taskExecuteManager.execute(httpTask);
    }
    public void get(String url, MesItemHolder holder, IDataListener<T> iDataListener, HttpTask.Type type) {
        HttpTask httpTask = new HttpTask(url, holder, iDataListener, type);
        taskExecuteManager.execute(httpTask);
    }
}
