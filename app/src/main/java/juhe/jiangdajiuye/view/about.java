package juhe.jiangdajiuye.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.util.AppUtils;
import juhe.jiangdajiuye.util.ToastUtils;
import juhe.jiangdajiuye.util.getImageUtils.GetImageFromLocate;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jiangdajiuye
 * @since 2017-03-27
 */
public class about extends BaseActivity {
    private static final String TAG = "about";
    private Toolbar toolbar;
    private TextView textViewUp;
    private String postFile ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUpgradeInfo();
    }

    public void init() {
        findId();
        initToolbar();
    }
    public void checkUpData(View view){
        Beta.checkUpgrade();
    }
    private void loadUpgradeInfo() {
        if (textViewUp == null)
            return;

        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

        if (upgradeInfo == null) {
            textViewUp.setText("无升级信息");
            return;
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(upgradeInfo.publishTime));
        StringBuilder info = new StringBuilder();
//        info.append("id: ").append(upgradeInfo.id).append("\n");
        info.append("标题: ").append(upgradeInfo.title).append("\n");
        info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
//        info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
//        info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
        info.append("发布时间: ").append(date).append("\n");
//        info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
//        info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
        info.append("安装包大小: ").append(upgradeInfo.fileSize/1000/1000.0 + "M").append("\n");
//        info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
//        info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
//        info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
//        info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType);

        textViewUp.setText(info);
    }
    public void initToolbar(){
        toolbar.setTitle("关于");
//        toolbar.setNavigationIcon(R.drawable.menue);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private TextView currentVersion;
    public void findId() {
        toolbar = (Toolbar)findViewById(R.id.aboute_toolbar);
        textViewUp = (TextView) findViewById(R.id.textViewUp);
        currentVersion = (TextView) findViewById(R.id.currentVersion);
        String s = "";
            try{
                s = "当前版本："+ AppUtils.getVersionName();
            }catch(Exception e){
                e.printStackTrace();
            }
        currentVersion.setText(s);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG, "onOptionsItemSelected: finished");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    /**
     * Called when an activity you launched exits
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
        switch (requestCode){
            case GetImageFromLocate.REQUEST_CODE:
                postFile(GetImageFromLocate.getResult(this,data));
                break;
        }
    }
    BmobFile bmobFile ;
    private void postFile(final File file) {
        Log.i(TAG, "postFile:   ------------- "+file.getPath());
        bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUtils.showToast("上传文件成功:");
                    Log.i(TAG, "done: "+  bmobFile.getFileUrl());
                }else{
                    ToastUtils.showToast("上传文件失败：");
                    e.printStackTrace();
                }

            }

            @Override
            public void onProgress(Integer value) {
                Log.i(TAG, "onProgress: "+value );
                // 返回的上传进度（百分比）
            }
        });
    }
}