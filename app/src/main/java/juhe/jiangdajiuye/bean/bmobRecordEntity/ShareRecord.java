package juhe.jiangdajiuye.bean.bmobRecordEntity;

import android.util.Log;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

/**
 * class description here
 * 用户分享记录
 *
 * @author wangqiang
 * @since 2017-08-06
 */

public class ShareRecord extends BmobObject {
    private static final String TAG = "ShareRecord ";
    private static ShareRecord instance = new ShareRecord() ;
    private int QQZone = 0;
    private  int QQ = 0;
    private  int WeiXin = 0;
    private  int WXPenYou = 0;

    private ShareRecord(){}
    public static ShareRecord getInstance()
    {
        return instance ;
    }
    public  void toQQZone() {
        QQZone++;
    }


    public  void toQQ() {
        QQ++;
    }


    public  void toWeiXin() {
        WeiXin++;
    }


    public  void toWXPenYou() {
        WXPenYou++;
    }

    @Override
    public Subscription save() {
        if((QQZone+QQ+WeiXin+WXPenYou) !=0 ){
            return super.save(new SaveListener<String>(){
                @Override
                public void done(String s, BmobException e) {
                    Log.i(TAG, "done: finish ");
                }
            });
        }
        return null ;
    }
}
