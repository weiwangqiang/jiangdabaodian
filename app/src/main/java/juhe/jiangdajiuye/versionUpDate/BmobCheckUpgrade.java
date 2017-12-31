package juhe.jiangdajiuye.versionUpDate;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import juhe.jiangdajiuye.bean.bmobBean.AppVersionBean;
import juhe.jiangdajiuye.dialog.UpgradeDialog;
import juhe.jiangdajiuye.util.AppUtils;
import juhe.jiangdajiuye.util.ToastUtils;

/**
 * class description here
 * 检查更新
 * @author wangqiang
 * @since 2017-12-30
 */

public class BmobCheckUpgrade {
    private static final String TAG = "BmobCheckUpgrade";
    public static String downLoadFilePath ;//apk下载 存放的包路径
    public static File ApkFile ;//apk 完整的file
    public static Context mCtx ;
    public static AppVersionBean targetBean = null;
    //检查更新
    public static void init(Context mCtx ){
        String defaultPath = Environment.getExternalStorageDirectory().toString()
                + File.separator+"DownLoad" ;
        init(mCtx,defaultPath);
    }
    public static void init(Context context ,String FilePath){
        mCtx = context ;
        downLoadFilePath =  FilePath ;
        File file  = new File(downLoadFilePath);
        if(!file.exists()){
            file.mkdirs();
        }
    }
    public static void checkUpgrade(){
        ToastUtils.showToast("正在检查更新");
        if(null == targetBean){
            getUpgradeInfo();
        }else{
            showDialog(mCtx,targetBean);
        }
    }
    public static void getUpgradeInfo(){
        BmobQuery<AppVersionBean> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<AppVersionBean>() {
            @Override
            public void done(List<AppVersionBean> object, BmobException e) {
                if(e!=null || object.size() == 0){
                    return ;
                }
                for (AppVersionBean appVersionBean : object) {
                    if(AppUtils.getVersionName().compareTo(appVersionBean.getVersion()) < 0 ){
                        targetBean = appVersionBean ;
                        showDialog(mCtx,targetBean);
                        return;
                    }
                }
                ToastUtils.showToast("已经是最新了哦~");
            }
        });
    }
    private static void showDialog(Context mCtx,AppVersionBean bean){
        UpgradeDialog dialog = new UpgradeDialog(mCtx) ;
        dialog.setBean(bean);
        dialog.show();
    }
}
