package juhe.jiangdajiuye.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.utils.AppConfigUtils;
import juhe.jiangdajiuye.utils.ResourceUtils;
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
    private boolean isClick = false;
    private TextView currentVersion;

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

    public void init() {
        findId();
        initToolbar();
    }

    public void checkUpData(View view) {
        if (!isClick) {
            CheckUpgrade.checkUpgrade();
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