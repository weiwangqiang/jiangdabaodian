package juhe.jiangdajiuye.util;

import android.support.annotation.StringRes;

import juhe.jiangdajiuye.core.MyApplication;

/**
 * class description here
 * 获取string 资源等信息的utils
 * @author wangqiang
 * @since 2017-12-31
 */

public class StringUtils {
    public static String getString(@StringRes int stringId){
        return MyApplication.getContext().getResources().getString(stringId);
    }
}
