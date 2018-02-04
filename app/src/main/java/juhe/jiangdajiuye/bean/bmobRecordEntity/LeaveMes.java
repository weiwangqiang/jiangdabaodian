package juhe.jiangdajiuye.bean.bmobRecordEntity;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 * 用户留言
 *
 * @author wangqiang
 * @since 2017-07-31
 */

public class LeaveMes extends BmobObject{
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content ;

    public String getDeviceLevel() {
        return deviceLevel;
    }
    private String deviceLevel; //android 系统级别

    public void setDevice(String device) {
        this.device = device;
    }

    public void setDeviceLevel(String deviceLevel) {
        this.deviceLevel = deviceLevel;
    }

    public String getAppLevel() {
        return appLevel;
    }

    public void setAppLevel(String appLevel) {
        this.appLevel = appLevel;
    }

    private String appLevel ;//软件级别
    private String device; //手机厂家

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
}
