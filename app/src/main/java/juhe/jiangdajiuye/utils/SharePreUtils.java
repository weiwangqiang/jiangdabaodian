package juhe.jiangdajiuye.utils;

import android.content.Context;
import android.content.SharedPreferences;

import juhe.jiangdajiuye.core.BaseApplication;

/**
 * class description here
 * <p>
 * sharedPreferences utils
 *
 * @author wangqiang
 * @since 2018-03-08
 */

public class SharePreUtils {
    private static SharedPreferences sharedPreferences;
    private final String config_name = "config";
    public static final String visitFrequency = "visitFrequency";//用户访问次数
    public static final String hasShowSharePrompt = "hasShowSharePrompt";//是否提示分享给其他用户
    public static final String isFirst = "first";

    public static void setBoolean(String key, boolean val) {
        SharedPreferences.Editor editor = getSharedPreEditor();
        editor.putBoolean(key, val).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPre().getBoolean(key, defValue);
    }

    public static void setInt(String key, int val) {
        SharedPreferences.Editor editor = getSharedPreEditor();
        editor.putInt(key, val).commit();
    }

    public static int getInt(String key,int defValue) {
        return getSharedPre().getInt(key, defValue);
    }

    //get SharedPreferences
    private static SharedPreferences getSharedPre() {
        if (null == sharedPreferences) {
            sharedPreferences = BaseApplication
                    .getContext()
                    .getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    // get SharedPreferences#editor
    private static SharedPreferences.Editor getSharedPreEditor() {
        if (null == sharedPreferences) {
            sharedPreferences = getSharedPre();
        }
        return sharedPreferences.edit();
    }
}
