package juhe.jiangdajiuye.utils.versionUpGrade;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.bmobAppMes.AppVersionBean;
import juhe.jiangdajiuye.utils.ResourceUtils;

/**
 * class description here
 * 执行任务的service
 * @author wangqiang
 * @since 2017-12-30
 */

public class DownLoadService extends IntentService {
    private static final String TAG = "DownLoadService ";
    private final String FILE_NAME = "jiangdabaodian.apk";
    private int NOTIFICATION = 1;
    public static AppVersionBean bean = null;
    private NotificationManager mNM = null;
    private NotificationCompat.Builder builder = null  ;
    public DownLoadService() {
        super("DownLoadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setContentTitle(ResourceUtils.getString(R.string.download_ing));
        builder.setContentText(ResourceUtils.getString(R.string.download));
        builder.setProgress(100, 0, false);
    }

    public static void startDownLoadService(Context mCtx, AppVersionBean appVersionBean) {
        Intent intent = new Intent(mCtx, DownLoadService.class);
        bean = appVersionBean;
        mCtx.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mNM.notify(NOTIFICATION, builder.build());
        downLoad(bean.getDownLoadUrl());
    }

    public void downLoad(String urlPath) {
        final BmobFile bmobFile = new BmobFile(FILE_NAME,"",urlPath);
        if (bmobFile != null) {
            CheckUpgrade.ApkFile = new File(CheckUpgrade.downLoadFilePath,bmobFile.getFilename()) ;
            bmobFile.download(CheckUpgrade.ApkFile, new DownloadFileListener() {

                @Override
                public void onProgress(Integer integer, long l) {
                    builder.setProgress(100, integer, false);
                    builder.setContentText(ResourceUtils.getString(R.string.has_downloaded)+integer+"%");
                    mNM.notify(NOTIFICATION, builder.build());
                }

                @Override
                    public void done(String s, BmobException e) {
                    builder.setProgress(100, 100, false);
                    builder.setContentText(ResourceUtils.getString(R.string.downloaded));
                    mNM.notify(NOTIFICATION, builder.build());
                    install(CheckUpgrade.ApkFile);
                    }
            });
        }
    }
    //安装
    public void install(File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
