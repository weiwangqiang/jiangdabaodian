package juhe.jiangdajiuye.bean.bmobUser;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-02-04
 */

public class UserCollect extends BmobObject {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId ; //用户ID
    private String url;//网页资源路径
    private String city;//举办城市
    private String theme;//主题
    private String title;//标题
    private String from;//消息来源
    private String locate;//消息地点
    private String industry;//职位类型
    private String time;//举办时间

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


    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
