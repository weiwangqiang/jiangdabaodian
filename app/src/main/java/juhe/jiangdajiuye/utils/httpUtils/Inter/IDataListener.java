package juhe.jiangdajiuye.utils.httpUtils.Inter;

/**
 * class description here
 *
 *   给应用层回调的接口，从中获取返回的数据
 *   其所有方法需要提交到主线程中
 * @author wangqiang
 * @since 2018-02-14
 */

public interface IDataListener<T> {
    void onSuccess(T t);
    void onFail(Exception exception,int responseCode);
}
