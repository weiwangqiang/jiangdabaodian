package juhe.jiangdajiuye.constant;

import android.os.Environment;

import java.io.File;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-16
 */

public class FileConstant {
    public static final String projectName = "jiangDaBaoDian";
    public static final String Separator = File.separator+"" ;
    public static final String SDRoot =  Environment.getExternalStorageDirectory().toString() ;
    //保存启动界面图片的路径
    public static final String BootAdvertSaveRootFile =SDRoot + Separator+projectName+ Separator+"BootAdvert"+Separator;
    public static final String BootAdvertSavePictureName = "advert.jpg";
}
