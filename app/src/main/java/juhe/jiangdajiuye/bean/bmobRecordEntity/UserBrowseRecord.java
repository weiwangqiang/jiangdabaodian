package juhe.jiangdajiuye.bean.bmobRecordEntity;

import cn.bmob.v3.BmobObject;

/**
 * class description here
 *
 *   记录用户浏览历史
 *
 * @author wangqiang
 * @since 2017-08-06
 */

public class UserBrowseRecord extends BmobObject{
    private int mLibrary;
    private int mXuanJiangCollect;
    private int mOffLineGame;

    public int getmLibrary() {
        return mLibrary;
    }

    public void setmLibrary(int mLibrary) {
        this.mLibrary = mLibrary;
    }

    public int getmXuanJiangCollect() {
        return mXuanJiangCollect;
    }

    public void setmXuanJiangCollect(int mXuanJiangCollect) {
        this.mXuanJiangCollect = mXuanJiangCollect;
    }

    public int getmOffLineGame() {
        return mOffLineGame;
    }

    public void setmOffLineGame(int mOffLineGame) {
        this.mOffLineGame = mOffLineGame;
    }

    public int getmAboute() {
        return mAboute;
    }

    public void setmAboute(int mAboute) {
        this.mAboute = mAboute;
    }

        private int mAboute ;
    public static class Key{
        public static String mLibrary = "mLibrary";
        public static String mXuanJiangCollect = "mXuanJiangCollect";
        public static String mOffLineGame = "mOffLineGame";
        public static String mAboute  = "mAboute";
    }
}
