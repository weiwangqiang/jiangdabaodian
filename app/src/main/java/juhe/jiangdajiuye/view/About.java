package juhe.jiangdajiuye.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.consume.ItemScrollView;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.util.AppUtils;
import juhe.jiangdajiuye.util.ResourceUtils;
import juhe.jiangdajiuye.versionUpDate.BmobCheckUpgrade;

/**
 * class description here
 *
 *  关于
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
    private String postFile ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        init();
        BmobCheckUpgrade.init(this);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        loadUpgradeInfo();
    }

    public void init() {
        findId();
        initToolbar();
    }
    public  boolean isClick = false ;
    public void checkUpData(View view){
        /**
         * 参数1：isManual 用户手动点击检查，非用户点击操作请传false
           参数2：isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
         */
//        Beta.checkUpgrade();
        if(!isClick){
//            isClick = true ;
            BmobCheckUpgrade.checkUpgrade();
        }
    }
    private void loadUpgradeInfo() {
            /***** 获取升级信息 *****/
//        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (BmobCheckUpgrade.targetBean == null) {
            textViewUp.setText("无升级信息");
            return;
        }
        textViewUp.setText(BmobCheckUpgrade.targetBean.getUpgradeMessage());

      /*  DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

        textViewUp.setText(info); */
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
    private ItemScrollView itemScrollView ;
    public void findId() {
        toolbar = (Toolbar)findViewById(R.id.aboute_toolbar);
        textViewUp = (TextView) findViewById(R.id.textViewUp);
        currentVersion = (TextView) findViewById(R.id.currentVersion);
        itemScrollView = (ItemScrollView) findViewById(R.id.ItemScrollView) ;
        TextView textView = new TextView(this);
        textView.setBackgroundColor(ResourceUtils.getColor(R.color.green_transparency_950));
        itemScrollView.addItemView(textView);
        itemScrollView.addBoth();
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {

    }
}