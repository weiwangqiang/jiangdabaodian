package juhe.jiangdajiuye.view.activity.JobFair.parse;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-03-13
 */

public class BaseParse {
    protected static String getRootUrl(String string) {
        String[] strings = string.split("/");
        return strings[0] + "//" + strings[2];
    }
}
