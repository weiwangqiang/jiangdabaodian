package juhe.jiangdajiuye.constant;

import android.os.Environment;

import java.io.File;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-16
 */

public class CacheConstant {
    public static final String projectName = "jiangDaBaoDian";//项目英文名
    public static final String SDRoot =  Environment.getExternalStorageDirectory().toString() ;
    //保存启动界面图片的路径
    public static final String BootAdvertSaveRootFile =SDRoot + File.separator+projectName+ File.separator+"BootAdvert"+File.separator;
    public static final String BootAdvertSavePictureName = "advert.jpg";//保存的起始屏图片名

    public static String AppIcnUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3084594445,4206732502&fm=96" ;
    public static String AppDownLoad = "http://app.qq.com/#id=detail&appid=1105550872" ;
}
