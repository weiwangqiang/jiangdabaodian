package juhe.jiangdajiuye.engine;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-04-11
 */
public class UrlManager {
    private String DOWN_LOAD_APP_URL = "http://app.qq.com/#id=detail&appid=1105550872";//app下载路径
    private static final UrlManager ourInstance = new UrlManager();

    public static UrlManager getInstance() {
        return ourInstance;
    }

    private UrlManager() {
    }
    public String getDownLoadAppUrl(){
        return DOWN_LOAD_APP_URL ;
    }
}
