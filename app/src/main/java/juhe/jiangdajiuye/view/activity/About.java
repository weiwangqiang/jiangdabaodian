package juhe.jiangdajiuye.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.core.BaseApplication;
import juhe.jiangdajiuye.utils.AppConfigUtils;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.utils.netUtils.NetStateUtils;
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
public class About extends BaseActivity {
    private static final String TAG = "About";
    private Toolbar toolbar;
    private TextView textViewUp;
    private TextView currentVersion;
    private static final int REQUEST_EXTERNAL_STORAGE = 10;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    /**
     * Called when the current {@link Window} of the activity gains or loses
     * focus.  This is the best indicator of whether this activity is visible
     * to the user.  The default implementation clears the key tracking
     * state, so should always be called.
     * <p>
     * <p>Note that this provides information about global focus state, which
     * is managed independently of activity lifecycles.  As such, while focus
     * changes will generally have some relation to lifecycle changes (an
     * activity that is stopped will not generally get window focus), you
     * should not rely on any particular order between the callbacks here and
     * those in the other lifecycle methods such as {@link #onResume}.
     * <p>
     * <p>As a general rule, however, a resumed activity will have window
     * focus...  unless it has displayed other dialogs or popups that take
     * input focus, in which case the activity itself will not have focus
     * when the other windows have it.  Likewise, the system may display
     * system-level windows (such as the status bar notification panel or
     * a system alert) which will temporarily take window input focus without
     * pausing the foreground activity.
     *
     * @param hasFocus Whether the window of this activity has focus.
     * @see #hasWindowFocus()
     * @see #onResume
     * @see View#onWindowFocusChanged(boolean)
     */
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        init();
        CheckUpgrade.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUpgradeInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CheckUpgrade.checkUpgrade();
            } else {
                ToastUtils.showToast("无法获取权限");
            }
        }
    }



    public void init() {
        findId();
        initToolbar();
    }

    public void checkUpData(View view) {
        if(!NetStateUtils.getNetWorkAvailable()){
            ToastUtils.showToast("当前网络不可用");
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
        textViewUp.setText(CheckUpgrade.targetBean.getUpgradeMessage());
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