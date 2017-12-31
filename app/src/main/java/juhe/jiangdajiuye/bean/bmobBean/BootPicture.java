package juhe.jiangdajiuye.bean.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 *  启动屏的图片
 *
 * @author wangqiang
 * @since 2017-08-16
 */

public class BootPicture extends BmobObject {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url = "";
}
