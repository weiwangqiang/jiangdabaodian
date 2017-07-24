package juhe.jiangdajiuye.util;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jiangdajiuye
 * @since 2017-04-23
 */
public class Uiuilts {
    private String TAG = "Uiuilts";

    public static Toast toast ;
    public Uiuilts(Context context){
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
