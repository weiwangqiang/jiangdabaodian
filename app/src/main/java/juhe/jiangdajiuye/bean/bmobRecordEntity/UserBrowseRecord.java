package juhe.jiangdajiuye.bean.bmobRecordEntity;

import android.util.Log;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

/**
 * class description here
 *
 *   记录用户浏览历史
 *
 * @author wangqiang
 * @since 2017-08-06
 */

public class UserBrowseRecord extends BmobObject{
    private static final String TAG = "UserBrowseRecord";
    private static UserBrowseRecord instance = new UserBrowseRecord() ;
    private Integer mLibrary = 0;
    private Integer mXuanJiang= 0;
    private Integer mOffLineGame= 0 ;
    private Integer mJobFair = 0;
    private Integer mAboute = 0;
    private Integer mCalender = 0;
    private UserBrowseRecord(){}
    public static UserBrowseRecord getInstance(){
        return instance ;
    }
    public void inCalender(){
        mCalender++;
    }
    public void inJobFair() {
        mJobFair++;
    }

    public void inLibrary() {
        mLibrary ++;
    }

    public  void inXuanJiang() {
        mXuanJiang ++;
    }

    public void inOffLineGame() {
        mOffLineGame ++;
    }

    public void inAboute() {
        mAboute ++;
    }
    @Override
    public Subscription save() {
        if((mLibrary+mAboute+mCalender+mJobFair+mOffLineGame+mXuanJiang) !=0 ){
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
