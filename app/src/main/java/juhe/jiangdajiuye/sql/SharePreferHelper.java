package juhe.jiangdajiuye.sql;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-02-05
 */

public class SharePerferHelper {
    private static final SharePerferHelper ourInstance = new SharePerferHelper();

    public static SharePerferHelper getInstance() {
        return ourInstance;
    }

    private SharePerferHelper() {
    }
}
