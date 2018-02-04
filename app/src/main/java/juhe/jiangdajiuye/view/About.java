package juhe.jiangdajiuye.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.util.AppUtils;
import juhe.jiangdajiuye.util.ResourceUtils;
import juhe.jiangdajiuye.versionUpGrade.BmobCheckUpgrade;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        init();
        BmobCheckUpgrade.init(this);
        BmobCheckUpgrade.getUpgradeInfo(false);
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

    public boolean isClick = false;

    public void checkUpData(View view) {
        if (!isClick) {
            BmobCheckUpgrade.checkUpgrade();
        }
    }

    private void loadUpgradeInfo() {
        if (BmobCheckUpgrade.targetBean == null) {
            textViewUp.setText(ResourceUtils.getString(R.string.has_not_upgrade_information));
            return;
        }
        textViewUp.setText(BmobCheckUpgrade.targetBean.getUpgradeMessage());
    }

    public void initToolbar() {
        toolbar.setTitle(ResourceUtils.getString(R.string.about_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private TextView currentVersion;

    public void findId() {
        toolbar = (Toolbar) findViewById(R.id.aboute_toolbar);
        textViewUp = (TextView) findViewById(R.id.textViewUp);
        currentVersion = (TextView) findViewById(R.id.currentVersion);
        String s = "";
        try {
            s = ResourceUtils.getString(R.string.current_version) + AppUtils.getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentVersion.setText(s);
    }

    @Override
    public void onClick(View v) {

    }
}