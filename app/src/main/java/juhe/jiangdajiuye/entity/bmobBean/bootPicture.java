package juhe.jiangdajiuye.entity.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-16
 */

public class bootPicture extends BmobObject {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url = "";
}
