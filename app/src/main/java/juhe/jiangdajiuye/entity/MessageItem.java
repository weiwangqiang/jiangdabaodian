package juhe.jiangdajiuye.entity;

/**
 * Created by wangqiang on 2016/10/19.
 */
public class MessageItem {
    private String url ;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String city ;
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

    private String title;
    private String from;
    private String locate;
    private String industry;
    private String time;

    public static keyVal getVal() {
        return val;
    }

    public static keyVal val = new keyVal();
    public static class keyVal{
        public static String url = "url";
        public static String title = "title";
        public static String from = "from";
        public static String locate = "locate";
        public static String industry = "industry";
        public static String time = "time";
    }
}
