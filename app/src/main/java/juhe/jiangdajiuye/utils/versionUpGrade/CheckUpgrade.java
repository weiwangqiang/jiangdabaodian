package juhe.jiangdajiuye.utils.versionUpGrade;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.bmobAppMes.AppVersionBean;
import juhe.jiangdajiuye.view.dialog.UpgradeDialog;
import juhe.jiangdajiuye.utils.AppConfigUtils;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;

/**
 * class description here
 * 检查更新
 *
 * @author wangqiang
 * @since 2017-12-30
 */

public class CheckUpgrade {
    private  final String TAG = "CheckUpgrade";
    public static String downLoadFilePath;//apk下载 存放的包路径
    public  static File ApkFile;//apk 完整的file
    private static Context mCtx;
    public static AppVersionBean targetBean;
    private static BmobQuery<AppVersionBean> bmobQuery = new BmobQuery<>();
    private static UpgradeDialog dialog ;
    //检查更新
    public static void init(Context mCtx) {
        String defaultPath = Environment.getExternalStorageDirectory().toString()
                + File.separator + "DownLoad";
        init(mCtx, defaultPath);
    }

    public static void init(Context context, String FilePath) {
        mCtx = context;
        downLoadFilePath = FilePath;
        File file = new File(downLoadFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        getUpgradeInfo(false);
    }

    public static void checkUpgrade() {
        if (null == targetBean) {
            getUpgradeInfo(true);
        } else {
            showDialog(mCtx, targetBean);
        }
    }

    public static void getUpgradeInfo(final boolean showToast) {
        if (null != targetBean)
            return;
        if (showToast){
            ToastUtils.showToast(ResourceUtils.getString(R.string.check_upgrade_ing));
        }
        bmobQuery.findObjects(new FindListener<AppVersionBean>() {
            @Override
            public void done(List<AppVersionBean> object, BmobException e) {
                if (e != null || object.size() == 0) {
                    return;
                }
                for (AppVersionBean appVersionBean : object) {
                    if (AppConfigUtils.getVersionName().compareTo(appVersionBean.getVersion()) < 0) {
                        targetBean = appVersionBean;
                        if (showToast)
                            showDialog(mCtx, targetBean);
                        return;
                    }
                }
                if (showToast){
                    ToastUtils.showToast(ResourceUtils.getString(R.string.be_last_version));
                }
            }
        });
    }

    private static void showDialog(Context mCtx, AppVersionBean bean) {
        if(null == dialog){
            dialog = new UpgradeDialog(mCtx);
        }
        dialog.setBean(bean);
        dialog.show();
    }
}
