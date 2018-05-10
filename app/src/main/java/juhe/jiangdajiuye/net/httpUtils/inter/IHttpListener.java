package juhe.jiangdajiuye.utils.httpUtils.Inter;

import java.io.BufferedReader;

/**
 * class description here
 *  从connection中获取 BufferedReader  ，进一步解析数据
 * @author wangqiang
 * @since 2018-02-14
 */

public interface IHttpListener {
    //请求成功，返回inputstream
    void onSuccess(BufferedReader bufferedReader);
    void onFail(Exception exception,int responseCode);
}
