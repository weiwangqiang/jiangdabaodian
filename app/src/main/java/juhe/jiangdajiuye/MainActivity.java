package juhe.jiangdajiuye;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import juhe.jiangdajiuye.adapter.FragmentAdapter;
import juhe.jiangdajiuye.broadCast.MyJushReceiver;
import juhe.jiangdajiuye.fragment.MainFragment;
import juhe.jiangdajiuye.tool.shareDialog;
import juhe.jiangdajiuye.tool.toast;
import juhe.jiangdajiuye.util.MyApplication;
import juhe.jiangdajiuye.util.TabLayoutUtils;
import juhe.jiangdajiuye.util.UserActionRecordUtils;
import juhe.jiangdajiuye.util.UserBrowseRecordUtils;
import juhe.jiangdajiuye.util.UserShareUtils;
import juhe.jiangdajiuye.util.lifecycle.AppLifeCycle;
import juhe.jiangdajiuye.view.about;
import juhe.jiangdajiuye.view.collect;
import juhe.jiangdajiuye.view.game;
import juhe.jiangdajiuye.view.library;
import juhe.jiangdajiuye.view.suggest;
import juhe.jiangdajiuye.view.xuanjiang;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        View.OnClickListener{
    private String TAG = "MainActivity";
    private Long exitTime = 0L;

    private List<Fragment> list = new ArrayList<>();
    private toast mytoast = new toast();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private DrawerLayout drawer;
    private FragmentAdapter adapter;
    private ViewPager viewPager;
    private Dialog dialog;
    private shareDialog sharedialog;
    private String APP_ID = "1105550872";
    private Tencent tencent;
    private baseUiLister baseuiLister;
    //微信
    private static final String WEI_ID = "wxc306137ab1a20319";
    private IWXAPI api;
    private WXWebpageObject webpager ;
    private WXMediaMessage message;
    private SendMessageToWX.Req req;
    //分享的信息
    private String APPName = "江大宝典";
    private String title = "我的分享";
    private String content = "我正在使用江大宝典，你也来看看吧";
    private String url = "http://fusion.qq.com/cgi-bin/qzapps/unified_jump?" +
            "appid=42384324&from=mqq&actionFlag=0&params=pname%3Djuhe.jiangdajiuye" +
            "%26versioncode%3D1%26actionflag%3D0%26channelid%3D";
    private String IcnUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3084594445,4206732502&fm=96";
    private String[] res = {"首页","图书馆","职位收藏"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppLifeCycle appLifeCycle =  AppLifeCycle.getInstance(MyApplication.getApplication());
        appLifeCycle.addListener(new AppLifeCycle.Listener() {
            @Override
            public void onBecameForeground(Activity activity) {
                Log.i(TAG, "onBecameForeground: ");
            }

            @Override
            public void onBecameBackground(Activity activity) {
                Log.i(TAG, "onBecameBackground: ");
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        //实现左右滑动
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initView();
        initPush();
    }
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    MyJushReceiver receiver ;
    private void initPush(){
        Log.i(TAG, "initPush: ");
        JPushInterface.init(MainActivity.this);
        receiver = new MyJushReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(receiver, filter);
    }
    public void initView(){
        sharedialog = new shareDialog();
        sharedialog.setItemlister(new myItemlist());
        dialog = sharedialog.getDialog(this);
        inithShare();
        findid();
        initViewPager();
        initTabLayout();
    }
    /**初始化微信
     */
    private void inithShare(){
        api = WXAPIFactory.createWXAPI(this,WEI_ID,true);
            api.registerApp(WEI_ID);
            baseuiLister = new baseUiLister();
        tencent = Tencent.createInstance(APP_ID, this);
    }
    private void initViewPager(){
        list.add(MainFragment.newInstance("http://ujs.91job.gov.cn/teachin/index?",
                "XUANJIANG",MainFragment.XUANJIANG));
        list.add(MainFragment.newInstance("http://ujs.91job.gov.cn/job/search?",
                "ZHAOPIN",MainFragment.ZHAOPIN));
        list.add(MainFragment.newInstance("http://ujs.91job.gov.cn/news/index?tag=tzgg&",
                "XINXI",MainFragment.XINXI));
        //刚开始只会加载前两个fragment
        adapter  = new FragmentAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new pagerlist());
        viewPager.setCurrentItem(0);
    }
    private void initTabLayout(){
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            for(int i = 0;i<3;i++){
                TabLayout.Tab tab = tabLayout.newTab();
                tabLayout.addTab(tab);
            }
            tabLayout.setupWithViewPager(viewPager);
        TabLayoutUtils.setIndicator(this,tabLayout,15,15);
        //给left mian 的item 设置监听事件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    public void findid(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: start");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Log.d(TAG, "onBackPressed: ");
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addCategory("android.intent.category.MONKEY");
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.nav_home:

                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.nav_library:
                UserBrowseRecordUtils.setmLibrary(1);
                startActivity(new Intent(MainActivity.this,library.class));
                break;
            case R.id.nav_favorite:

                startActivity(new Intent(MainActivity.this,collect.class));
                break;
            case R.id.nav_xuanjianghui:
                UserBrowseRecordUtils.setmXuanJiangCollect(1);
                startActivity(new Intent(MainActivity.this,xuanjiang.class));
                break;
            case R.id.nav_send:
                startActivity(new Intent(MainActivity.this,suggest.class));
                break;
            case R.id.nav_game:
                UserBrowseRecordUtils.setmOffLineGame(1);
                startActivity(new Intent(MainActivity.this,game.class));
                break;
            case R.id.nav_about:
                UserBrowseRecordUtils.setmAboute(1);
                startActivity(new Intent(MainActivity.this,about.class));
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_share) {
            showShare();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showShare(){
        dialog.show();
        sharedialog.setItemlister(new myItemlist());
    }

    /**
     * popupwind的Item 监听
     */
    private class myItemlist implements shareDialog.Itemlister{

        @Override
        public void shareToQzone() {
            ToQzone();
        }

        @Override
        public void shareToQQ() {
            ToQQ();
        }

        @Override
        public void shareTowei() {
            req = new SendMessageToWX.Req();
            req.scene = SendMessageToWX.Req.WXSceneSession ;
            shareToWX();
        }
        @Override
        public void shareTopyq() {
            req = new SendMessageToWX.Req();
            req.scene = SendMessageToWX.Req.WXSceneTimeline ;
            shareToWX();
        }
    }
    private void shareToWX(){
        mytoast.makeText(MainActivity.this,"正在跳转");
        Log.e(TAG,"share to weixin");
        webpager = new WXWebpageObject();
        webpager.webpageUrl = url;
        message = new WXMediaMessage(webpager);
        message.title = title;
        message.description = content;
        req.transaction = "webPager";
        req.message = message;
        Boolean get = api.sendReq(req);
//        api.handleIntent(getIntent(),this);
        dialog.cancel();
    }
    private void ToQQ(){
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE,title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,IcnUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, APPName);
        dialog.cancel();
        tencent.shareToQQ(this, params,baseuiLister);
    }
    private void  ToQzone(){
        Bundle params = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add(IcnUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,content);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,list);
        dialog.cancel();
        tencent.shareToQzone(this, params,baseuiLister);
    }
    /**
     * 腾讯的监听回调
     */
    private class baseUiLister implements IUiListener {

        @Override
        public void onComplete(Object o) {
            mytoast.makeText(MainActivity.this,"分享成功！");
            UserShareUtils.setQQ(1);
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
            mytoast.makeText(MainActivity.this,"取消分享");
        }
    }
    //    //要想调用IUiListener 必须重写此函数
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, baseuiLister);
        if(requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE){
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, new baseUiLister());
            }
        }
    }
    class pagerlist implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            adapter.getItem(position).onResume();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onKeyUp: ");
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.MAIN");
//        intent.addCategory("android.intent.category.HOME");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addCategory("android.intent.category.MONKEY");
//        startActivity(intent);

        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                long secondTime = System.currentTimeMillis();
                Log.e(TAG," toast time is "+Toast.LENGTH_SHORT);
                if (secondTime - exitTime > 2500) {
                    //如果两次按键时间间隔大于2秒，则不退出
                    mytoast.makeText(MainActivity.this,"再按一次退出");
                    exitTime = secondTime;
                    //更新firstTime
                    return true;
                } else {
                    //两次按键小于2秒时，退出应用
                    MainActivity.this.finish();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    //***********************生命周期*******************************************
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        unregisterReceiver(receiver);
        UserActionRecordUtils.setOutTime(System.currentTimeMillis());
        UserBrowseRecordUtils.save();
        UserShareUtils.save();
    }
//*******************异常退出保留数据方法*******************************
    /** 异常退出保留数据
     *
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
        outState.putInt("IntTest", 0);
//        unregisterReceiver(receiver);
        UserActionRecordUtils.setOutTime(System.currentTimeMillis());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState: ");
    }
}
