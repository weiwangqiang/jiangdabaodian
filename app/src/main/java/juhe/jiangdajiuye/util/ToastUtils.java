package juhe.jiangdajiuye.util;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import juhe.jiangdajiuye.core.MyApplication;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jiangdajiuye
 * @since 2017-04-23
 */
public class ToastUtils {
    private String TAG = "ToastUtils";

    public static Toast toast ;
    public ToastUtils(Context context){
        toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
    }
    public static void showToast(String content){
        toast.setText(content);
        toast.show();
    }
    public static Resources getResource() {
        return MyApplication.getApplication().getResources();
    }
}
