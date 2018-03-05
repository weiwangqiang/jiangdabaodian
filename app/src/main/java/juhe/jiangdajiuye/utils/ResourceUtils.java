package juhe.jiangdajiuye.utils;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import juhe.jiangdajiuye.core.BaseApplication;

/**
 * class description here
 * <p>
 * 获取系统资源的utils
 *
 * @author wangqiang
 * @since 2017-12-31
 */

public class ResourceUtils {
    public static String getString(@StringRes int stringId) {
        return BaseApplication.getContext().getResources().getString(stringId);
    }

    public static int getColor(@ColorRes int colorId) {
        return BaseApplication.getContext().getResources().getColor(colorId);
    }
}