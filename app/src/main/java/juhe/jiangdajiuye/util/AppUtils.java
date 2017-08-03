package juhe.jiangdajiuye.util;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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


    public static String getSavePictureUrl() {
        return savePictureUrl;
    }

    //应用图片存储的绝对路径
    public static final  String savePictureUrl = Environment.getExternalStorageDirectory()+"/game/";
    public AppUtils() {
    }

    /**
     * 获取app级别
     * @return
     */
    public static String getVersionName()
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getApplication().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getApplication().getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 获取手机型号信息
     * @return
     */
    public static String getDevice(){
        String   model= android.os.Build.MODEL;
        String carrier= android.os.Build.MANUFACTURER;
        return "型号："+model+ "\n  厂商 ：" +carrier ;
    }

    /**
     * 获取系统版本号
     * @return
     */
    public static String getDeviceLevel(){
        int currentapiVersion=android.os.Build.VERSION.SDK_INT;
        return "系统："+android.os.Build.VERSION.RELEASE+" \n sdk :"+currentapiVersion+"";
    }
    private static Application getApplication(){
        return MyApplication.getApplication();
    }
}
