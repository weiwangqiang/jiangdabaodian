package juhe.jiangdajiuye.utils.versionUpGrade;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.engine.UrlManager;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;

/**
 * class description here
 * 执行任务的service
 *
 * @author wangqiang
 * @since 2017-12-30
 */

public class DownLoadService extends IntentService {
    private static final String TAG = "DownLoadService ";
    private final String FILE_NAME = "jiangdabaodian.apk";
    private int NOTIFICATION = 9;
    private String channelId = "my_channel_01";
    public static String url;
    private NotificationManager mNM = null;
    private NotificationCompat.Builder builder = null;

    public DownLoadService() {
        super("DownLoadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //需要这样设置，不然无法弹出进度条
            mNM.createNotificationChannel(
                    new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH));
        }
        builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(ResourceUtils.getString(R.string.download_ing))
                .setContentText(ResourceUtils.getString(R.string.download))
                .setProgress(100, 0, false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(this,
                        0, new Intent(), 0))
                .setSound(Uri.parse("android.resource://"
                        + getPackageName() + "/"
                        + R.raw.silence));
    }

    public static void startDownLoadService(Context mCtx, String downLoadUrl) {
        Intent intent = new Intent(mCtx, DownLoadService.class);
        url = downLoadUrl;
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
        downLoad(url);
    }

    public void downLoad(String urlPath) {
        final BmobFile bmobFile = new BmobFile(FILE_NAME, "", urlPath);
        if (bmobFile == null) {
            downloadInBrowse();
            return;
        }
        CheckUpgrade.ApkFile = new File(CheckUpgrade.downLoadFilePath, bmobFile.getFilename());
        bmobFile.download(CheckUpgrade.ApkFile, new DownloadFileListener() {

            @Override
            public void onProgress(Integer integer, long l) {
                builder.setProgress(100, integer, false);
                builder.setContentText(ResourceUtils.getString(R.string.has_downloaded) + integer + "%");
                mNM.notify(NOTIFICATION, builder.build());

            }

            @Override
            public void done(String s, BmobException e) {
                if (null == e) {
                    builder.setContentText(ResourceUtils.getString(R.string.notify_download_finish))
                            // Removes the progress bar
                            .setProgress(0, 0, false);
//                        builder.setProgress(100, 100, false);
                    mNM.notify(NOTIFICATION, builder.build());
                    install(CheckUpgrade.ApkFile);
                } else {
                    downloadInBrowse();
                    e.printStackTrace();
                }
                mNM.cancel(NOTIFICATION);
            }
        });
    }

    //安装
    public void install(File file) {
        if (!file.exists()) {
            downloadInBrowse();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri fileUri = FileProvider.getUriForFile(getBaseContext(),
                    "juhe.jiangdajiuye.provider", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(fileUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }
    private void downloadInBrowse() {
        ToastUtils.showToast("下载失败，请到应用商店下载");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(UrlManager.getInstance().getDownLoadAppUrl());
        intent.setData(content_url);
        startActivity(intent);
    }
}
