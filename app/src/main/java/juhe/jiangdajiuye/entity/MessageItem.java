package juhe.jiangdajiuye.entity;

/**
 * Created by wangqiang on 2016/10/19.
 */
public class MessageItem {
    private String url ;//网页资源路径

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String title;//标题
    private String from;//消息来源
    private String locate;//消息地点
    private String industry;//职位类型
    private String time;//举办时间

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    private String theme ;//主题
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String city ;//举办城市
    public static class keyVal{

        public static String tableName = "collectTable";
        public static String url = "url";
        public static String title = "title";
        public static String from = "MesFrom";
        public static String locate = "locate";
        public static String industry = "industry";
        public static String time = "time";
    }
}
