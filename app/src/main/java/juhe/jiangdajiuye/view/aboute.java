package juhe.jiangdajiuye.view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jiangdajiuye
 * @since 2017-03-27
 */
@ContentView(R.layout.aboute)
public class aboute extends BaseActivity {
    private static final String TAG = "aboute";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        findId();
        initToolbar();
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
    private TextView tv;
    public void findId() {
        toolbar = (Toolbar)findViewById(R.id.aboute_toolbar);
        tv = (TextView) findViewById(R.id.textView3);
        String s = "";
            try{
                s = "当前版本："+getVersionName()+getResources().getString(R.string.aboute);
            }catch(Exception e){
                e.printStackTrace();
                s = getResources().getString(R.string.aboute);
            }
        tv.setText(s);
    }
    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
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
    }
}