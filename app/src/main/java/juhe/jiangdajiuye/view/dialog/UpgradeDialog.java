package juhe.jiangdajiuye.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.bmobAppMes.AppVersionBean;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.net.NetStateUtils;
import juhe.jiangdajiuye.utils.versionUpGrade.DownLoadService;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-12-30
 */

public class UpgradeDialog extends Dialog implements View.OnClickListener {
    public void setBean(AppVersionBean bean) {
        this.bean = bean;
    }

    private AppVersionBean bean = null;
    private Context mCtx = null;
    private boolean upGradeInApk = true ;
    public UpgradeDialog(@NonNull Context context) {
        super(context);
        this.mCtx = context;
    }


    public UpgradeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upgrade);

        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        ((TextView) findViewById(R.id.upgrade_dialog_title)).setText(bean.getUpgradeTitle());
        ((TextView) findViewById(R.id.upgrade_dialog_info)).setText(bean.getUpgradeMessage());
        findViewById(R.id.upgrade_dialog_cancel_button).setOnClickListener(this);
        findViewById(R.id.upgrade_dialog_confirm_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upgrade_dialog_cancel_button:
                cancel();
                break;
            case R.id.upgrade_dialog_confirm_button:
                showConfirmDialog();
                break;
            default:
                break;
        }
    }

    //显示确认dialog
    private void showConfirmDialog() {
        cancel();
        if (NetStateUtils.isWifiState()) {
            upGrade();
            return;
        }
        new AlertDialog.Builder(mCtx)
                .setTitle(ResourceUtils.getString(R.string.download_without_wifi))
                .setPositiveButton(ResourceUtils.getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               upGrade();
                                dialog.cancel();
                            }
                        })
                .setNeutralButton(ResourceUtils.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .create()
                .show();
    }

    private void upGrade() {
        if(upGradeInApk){
            DownLoadService.startDownLoadService(mCtx, bean.getDownLoadUrl());
        }else {
            upByMarket();
        }
    }

    //通过应用商店更新
    private void upByMarket() {
        Uri uri = Uri.parse("market://details?id=" + mCtx.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(intent);
    }
}
