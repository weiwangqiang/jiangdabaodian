package juhe.jiangdajiuye.utils;


import juhe.jiangdajiuye.base.BaseApplication;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-03-18
 */

public class SizeConvert {
    /** dip转换px */
    public static int dip2px(int dip) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /** px转换dip */

    public static int px2dip(int px) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
