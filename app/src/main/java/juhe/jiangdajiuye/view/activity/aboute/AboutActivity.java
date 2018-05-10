package juhe.jiangdajiuye.view.activity.aboute;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.utils.AppConfigUtils;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.net.NetStateUtils;
import juhe.jiangdajiuye.utils.versionUpGrade.CheckUpgrade;

/**
 * class description here
 * <p>
 * 关于
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jiangdajiuye
 * @since 2017-03-27
 */
public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";
    private Toolbar toolbar;
    private TextView textViewUp;
    private TextView currentVersion;
    private static final int REQUEST_EXTERNAL_STORAGE = 10;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus){
            return;
        }
        if((null != textViewUp) && (null != CheckUpgrade.targetBean)){
            textViewUp.setText(CheckUpgrade.targetBean.getUpgradeMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aboute, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_disclaimer) {
            startActivitySlideInRight(this,DisclaimerActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        findId();
        initToolbar();
        CheckUpgrade.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUpgradeInfo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CheckUpgrade.checkUpgrade();
            } else {
                ToastUtils.showToast(ResourceUtils.getString(R.string.toast_permission_error));
            }
        }
    }

    public void checkUpData(View view) {
        if(!NetStateUtils.getNetWorkAvailable()){
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
            return;
        }
        if (verifyStoragePermissions()) {
            CheckUpgrade.checkUpgrade();
        }
    }

    public boolean verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//            }
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }

    private void loadUpgradeInfo() {
        if (CheckUpgrade.targetBean == null) {
            textViewUp.setText(ResourceUtils.getString(R.string.has_not_upgrade_information));
            return;
        }
        textViewUp.setText(CheckUpgrade.targetBean.
                getUpgradeMessage());
    }

    public void initToolbar() {
        toolbar.setTitle(ResourceUtils.getString(R.string.about_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void findId() {
        toolbar = (Toolbar) findViewById(R.id.aboute_toolbar);
        textViewUp = (TextView) findViewById(R.id.textViewUp);
        currentVersion = (TextView) findViewById(R.id.currentVersion);
        currentVersion.setText(ResourceUtils.getString(R.string.current_version)
                + AppConfigUtils.getVersionName());
    }

    @Override
    public void onClick(View v) {

    }
}