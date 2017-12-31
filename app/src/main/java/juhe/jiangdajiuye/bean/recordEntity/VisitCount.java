package juhe.jiangdajiuye.bean.recordEntity;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 * 用户访问计数
 * @author wangqiang
 * @since 2017-08-02
 */

public class VisitCount extends BmobObject {
    private String ip;

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
}
