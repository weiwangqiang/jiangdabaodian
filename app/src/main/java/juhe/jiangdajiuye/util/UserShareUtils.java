package juhe.jiangdajiuye.util;

import juhe.jiangdajiuye.bean.bmobRecordEntity.ShareRecord;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-06
 */

public class UserShareUtils {
    public static int QQZone = 0;
    public static int QQ = 0;
    public static int WeiXin = 0;
    public static ShareRecord shareRecord;
    public static ShareRecord getShareRecord(){
        if(shareRecord == null)
            shareRecord = new ShareRecord();
        return shareRecord ;
    }
    public static void setQQZone(int QQZone) {
        if(shareRecord == null)
            shareRecord = new ShareRecord();
        UserShareUtils.QQZone += QQZone;
        shareRecord.setQQZone(UserShareUtils.QQZone);
    }

    public static void setQQ(int QQ) {
        if(shareRecord == null)
            shareRecord = new ShareRecord();
        UserShareUtils.QQ += QQ;
        shareRecord.setQQ(UserShareUtils.QQ);
    }

    public static void setWeiXin(int weiXin) {
        if(shareRecord == null)
            shareRecord = new ShareRecord();
        UserShareUtils.WeiXin += weiXin;
        shareRecord.setWeiXin(UserShareUtils.WeiXin);
    }

    public static void setWXPenYou(int WXPenYou) {
        if(shareRecord == null)
            shareRecord = new ShareRecord();
        UserShareUtils.WXPenYou += WXPenYou;
        shareRecord.setWXPenYou(UserShareUtils.WXPenYou);
    }

    public static int WXPenYou = 0;
    public static void save(){
        if(shareRecord == null)
            return;
//        shareRecord.save(new SaveListener<String>(){
//
//            @Override
//            public void done(String s, BmobException e) {
//
//            }
//        });
    }

}
