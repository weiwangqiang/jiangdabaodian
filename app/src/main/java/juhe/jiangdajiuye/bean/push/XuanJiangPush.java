package juhe.jiangdajiuye.bean.push;

/**
 * class description here
 *
 *  推送消息的对象类
 *
 * @author wangqiang
 * @since 2017-11-05
 */

public class XuanJiangPush {
    Integer type =0 ;
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    String url ;
    String imageUrl;
    String content ;
    String targetUrl;

}
