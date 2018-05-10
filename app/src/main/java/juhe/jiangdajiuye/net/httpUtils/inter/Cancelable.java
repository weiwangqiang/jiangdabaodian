package juhe.jiangdajiuye.net.httpUtils.inter;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-03-26
 */

public interface Cancelable {
    void cancel();
    boolean isCanceled();
}
