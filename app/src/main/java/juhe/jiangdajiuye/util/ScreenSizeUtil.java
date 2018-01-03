package juhe.jiangdajiuye.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import juhe.jiangdajiuye.core.MyApplication;

/**
 * class description here
 * 屏幕的utils
 * @author wangqiang
 * @since 2017-08-03
 */

public class ScreenSizeUtil {
    public static ScreenSize screenSize  = null ;
    public static class ScreenSize{
        public final double size;
        public final int widthPixels;
        public final int heightPixels;
        private ScreenSize(double sizeParameter,int widthPixelsParameter,int heightPixelsParameter){
            size = sizeParameter;
            widthPixels = widthPixelsParameter;
            heightPixels = heightPixelsParameter;
        }
    }
    /**
     * get screensize
     * @return screensize
     */
    public static ScreenSize getScreenSize(){
        if(screenSize != null)
            return screenSize ;
        WindowManager w = (WindowManager) MyApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17){
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= 17){
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        DisplayMetrics dm = MyApplication.getApplication().getResources().getDisplayMetrics();
        double heightInch = heightPixels/(double)dm.densityDpi;
        double widthInch =  widthPixels/(double)dm.densityDpi;
        double inch = Math.sqrt(widthInch * widthInch + heightInch * heightInch);
        ScreenSize screenSize= new ScreenSize(inch,widthPixels,heightPixels);
        return screenSize;
    }

    public static int getAvailScreenHeight() {
        return ScreenSizeUtil.getScreenSize().heightPixels - getStatusBarHeight() - getNavBarHeight();
    }

    public static int getScreenWith(){
        return getScreenSize().widthPixels ;
    }

    public static int getStatusBarHeight() {
        Resources resources = MyApplication.getApplication().getResources();
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavBarHeight() {
        Context context = MyApplication.getApplication();
        Resources resources = context.getResources();
        int result = 0;
        int resourceId = 0;

        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasMenuKey = hasPermanentMenuKey();
        if (!hasBackKey && !hasMenuKey) {
            int orientation = resources.getConfiguration().orientation;
            switch(orientation) {
                case Configuration.ORIENTATION_LANDSCAPE:
                    resourceId = resources.getIdentifier("navigation_bar_height_landscape", "dimen", "android");
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                    break;
                default:
                    break;
            }
        }
        if (resourceId > 0) {
            result =  resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean hasPermanentMenuKey() {
        boolean hasMenuKey;
        ViewConfiguration vc = ViewConfiguration.get(MyApplication.getApplication());
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                Method m = vc.getClass().getMethod("hasPermanentMenuKey",
                        new Class<?>[] {});
                try {
                    hasMenuKey = (Boolean) m.invoke(vc,
                            new Object[] {});
                } catch (IllegalArgumentException e) {
                    hasMenuKey = false;
                } catch (IllegalAccessException e) {
                    hasMenuKey = false;
                } catch (InvocationTargetException e) {
                    hasMenuKey = false;
                }
            }catch (NoSuchMethodException e) {
                hasMenuKey = false;
            }
        } else {
            hasMenuKey = true;
        }
        return hasMenuKey;
    }
}
