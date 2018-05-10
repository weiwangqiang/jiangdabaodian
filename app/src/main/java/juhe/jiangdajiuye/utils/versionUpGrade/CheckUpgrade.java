package juhe.jiangdajiuye.utils.versionUpGrade;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

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
    private static final String TAG = "CheckUpgrade";
    public static String downLoadFilePath;//apk下载 存放的包路径
    public static File ApkFile;//apk 完整的file
    private static Context mCtx;
    public static AppVersionBean targetBean;
    private static BmobQuery<AppVersionBean> bmobQuery = new BmobQuery<>();
    private static UpgradeDialog dialog;

    //检查更新
    public static void init(Context mCtx) {
        String defaultPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "DownLoad";
        init(mCtx, defaultPath);
    }

    public static void init(Context context, String FilePath) {
        mCtx = context;
        downLoadFilePath = FilePath;
        checkFile();
        getUpgradeInfo(false);
    }

    private static void checkFile() {
        File file = new File(downLoadFilePath);
        if (!file.exists()) {
            Log.i(TAG, "checkFile: 创建成功 ？ "+file.mkdirs());
        } else {
            Log.i(TAG, "checkFile: file  exit ");
        }
    }

    public static void checkUpgrade() {
        checkFile();
        if (null == targetBean) {
            getUpgradeInfo(true);
        } else {
            showDialog(mCtx, targetBean);
        }
    }

    public static void getUpgradeInfo(final boolean showToast) {
        if (null != targetBean) {
            return;
        }
        if (showToast) {
            ToastUtils.showToast(ResourceUtils.getString(R.string.check_upgrade_ing));
        }
        bmobQuery.findObjects(new FindListener<AppVersionBean>() {
            @Override
            public void done(List<AppVersionBean> object, BmobException e) {
                if (e != null || object.size() == 0) {
                    return;
                }
                for (AppVersionBean appVersionBean : object) {
                    if (!compareTo(AppConfigUtils.getVersionName(),appVersionBean.getVersion())) {
                        continue;
                    }
                    targetBean = appVersionBean;
                    if (showToast) {
                        showDialog(mCtx, targetBean);
                    }
                    return;
                }
                if (showToast) {
                    ToastUtils.showToast(ResourceUtils.getString(R.string.be_last_version));
                }
            }
        });
    }

    /**
     *
     * @param curVersion 当前版本
     * @param targetVersion 目标版本 可能为null
     * @return 是否有新版本
     */
    public static boolean compareTo(String curVersion, String targetVersion){
        if(TextUtils.isEmpty(targetVersion)){
            return false;
        }
        String[] cur = curVersion.split("\\.");
        String[] target = targetVersion.split("\\.");
        int n = Math.min(cur.length,target.length);
        for(int i = 0 ;i < n ;i++){
            int curN = Integer.parseInt(cur[i]);
            int targetN = Integer.parseInt(target[i]);
            if(curN==targetN){
                continue;
            }
            return curN < targetN ;
        }
        return cur.length < target.length ;
    }
    private static void showDialog(Context mCtx, AppVersionBean bean) {
        dialog = new UpgradeDialog(mCtx);
        dialog.setBean(bean);
        dialog.show();
    }
}
