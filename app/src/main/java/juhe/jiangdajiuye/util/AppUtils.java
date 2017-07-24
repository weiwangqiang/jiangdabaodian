package juhe.jiangdajiuye.util;

import android.os.Environment;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jiangdajiuye
 * @since 2017-04-02
 */
public class AppUtils {
    private String TAG = "AppUtils";
    //应用图片存储的绝对路径
    public static final  String savePictureUrl = Environment.getExternalStorageDirectory()+"/game/";
    public AppUtils() {
    }
}
