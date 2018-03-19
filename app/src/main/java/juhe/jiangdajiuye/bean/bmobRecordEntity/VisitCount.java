package juhe.jiangdajiuye.bean.bmobRecordEntity;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 *  用户访问计数
 *
 * @author wangqiang
 * @since 2017-08-02
 */

public class VisitCount extends BmobObject {
    private String ip;

    private String versionName ;
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address ;
    public String getStayTime() {
        return stayTime;
    }

    public void setStayTime(String stayTime) {
        this.stayTime = stayTime;
    }

    private String stayTime ;  //用户存留时间
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    private String visitTime;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
