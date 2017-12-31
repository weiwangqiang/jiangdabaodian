package juhe.jiangdajiuye.bean.recordEntity;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-06
 */

public class ShareRecord extends BmobObject {
    public static int QQZone = 0;
    public static int QQ = 0;
    public static int WeiXin = 0;

    public static int getQQZone() {
        return QQZone;
    }

    public static void setQQZone(int QQZone) {
        ShareRecord.QQZone = QQZone;
    }

    public static int getQQ() {
        return QQ;
    }

    public static void setQQ(int QQ) {
        ShareRecord.QQ = QQ;
    }

    public static int getWeiXin() {
        return WeiXin;
    }

    public static void setWeiXin(int weiXin) {
        WeiXin = weiXin;
    }

    public static int getWXPenYou() {
        return WXPenYou;
    }

    public static void setWXPenYou(int WXPenYou) {
        ShareRecord.WXPenYou = WXPenYou;
    }

    public static int WXPenYou = 0;
}
