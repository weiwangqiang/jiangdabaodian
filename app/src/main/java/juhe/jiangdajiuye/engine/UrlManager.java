package juhe.jiangdajiuye.engine;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-04-11
 */
public class UrlManager {
    private static final UrlManager ourInstance = new UrlManager();

    public static UrlManager getInstance() {
        return ourInstance;
    }

    private UrlManager() {
    }
}
