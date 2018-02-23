package juhe.jiangdajiuye.util.HttpWorker;

import juhe.jiangdajiuye.util.HttpWorker.Inter.IDataListener;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;

/**
 * class description here
 *
 * 提供给客户端调用的类
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
        }
        return instance ;
    }
    public void get(String url, T requestBean, IDataListener<T> iDataListener, HttpTask.Type type) {
        HttpTask httpTask = new HttpTask(url, requestBean, iDataListener, type);
        taskExecuteManager.execute(httpTask);
    }
    public void get(String url, XuanJiangMesHolder holder, IDataListener<T> iDataListener, HttpTask.Type type) {
        HttpTask httpTask = new HttpTask(url, holder, iDataListener, type);
        taskExecuteManager.execute(httpTask);
    }
}
