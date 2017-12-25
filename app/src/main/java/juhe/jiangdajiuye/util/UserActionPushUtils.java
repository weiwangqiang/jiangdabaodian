package juhe.jiangdajiuye.util;

import android.util.Log;

import juhe.jiangdajiuye.entity.recordEntity.visitCount;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-02
 */

public class UserActionPushUtils {
    private static String TAG = "UserActionPushUtils";
    public static void pushVisitCount(visitCount count){
        Log.i(TAG, "pushVisitCount: ");
//        count.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.i(TAG, "done: 已经提交 count "+s);
//            }
//        });
    }


}
