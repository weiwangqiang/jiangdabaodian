package juhe.jiangdajiuye.util;

import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;

import juhe.jiangdajiuye.core.MyApplication;

/**
 * class description here
 * 获取系统资源的utils
 * @author wangqiang
 * @since 2017-12-31
 */

public class ResourceUtils {
    public static String getString(@StringRes int stringId) {
        return MyApplication.getContext().getResources().getString(stringId);
    }

    public static  @ColorInt int getColor(int colorId){
        return MyApplication.getContext().getResources().getColor(colorId);
    }
}