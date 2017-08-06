package juhe.jiangdajiuye.util;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import juhe.jiangdajiuye.entity.recordEntity.UserBrowseRecord;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-06
 */

public class UserBrowseRecordUtils {
    public static UserBrowseRecord userBrowseRecord = new UserBrowseRecord();
    private static int mLibrary;
    private static int mXuanJiangCollect;
    private static int mOffLineGame;
    private static int mAboute ;

    public static void setmLibrary(int Library) {
        mLibrary += Library;
        userBrowseRecord.setmLibrary(mLibrary);
    }

    public static void setmXuanJiangCollect(int XuanJiangCollect) {
        mXuanJiangCollect += XuanJiangCollect;
        userBrowseRecord.setmXuanJiangCollect(mXuanJiangCollect);
    }

    public static void setmOffLineGame(int OffLineGame) {
        mOffLineGame += OffLineGame;
        userBrowseRecord.setmOffLineGame(mOffLineGame);
    }

    public static void setmAboute(int Aboute) {
        mAboute += Aboute;
        userBrowseRecord.setmAboute(mAboute);
    }


    private UserBrowseRecordUtils(){}
    public UserBrowseRecord getUserBrowseRecord(){
        return userBrowseRecord ;
    }

    /**
     * 提交浏览记录
     */
    public static void save(){
        userBrowseRecord.save(new SaveListener<String>(){

            @Override
            public void done(String s, BmobException e) {

            }
        });
    }
}