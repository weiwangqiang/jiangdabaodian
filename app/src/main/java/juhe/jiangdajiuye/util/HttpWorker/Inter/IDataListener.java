package juhe.jiangdajiuye.util.HttpWorker.Inter;

/**
 * class description here
 *
 *   给应用层回调的接口，从中获取返回的数据
 *
 * @author wangqiang
 * @since 2018-02-14
 */

public interface IDataListener<T> {
    void onSuccess(T t);
    void onFail(Exception exception,int responseCode);
}
