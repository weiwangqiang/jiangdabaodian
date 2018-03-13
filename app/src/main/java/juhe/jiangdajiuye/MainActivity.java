package juhe.jiangdajiuye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import juhe.jiangdajiuye.bean.bmobAppMes.BootPicture;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.core.BaseApplication;
import juhe.jiangdajiuye.user.UserManager;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.SharePreUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.utils.imageUtils.ImageLocalLoad;
import juhe.jiangdajiuye.utils.netUtils.NetStateUtils;
import juhe.jiangdajiuye.utils.userInforUtils.UserActionRecordUtils;
import juhe.jiangdajiuye.utils.userInforUtils.UserBrowseRecordUtils;
import juhe.jiangdajiuye.utils.userInforUtils.UserShareUtils;
import juhe.jiangdajiuye.utils.versionUpGrade.CheckUpgrade;
import juhe.jiangdajiuye.view.About;
import juhe.jiangdajiuye.view.Browse;
import juhe.jiangdajiuye.view.Collect;
import juhe.jiangdajiuye.view.FeedBack;
import juhe.jiangdajiuye.view.Game;
import juhe.jiangdajiuye.view.Library;
import juhe.jiangdajiuye.view.LoginActivity;
import juhe.jiangdajiuye.view.adapter.FragmentAdapter;
import juhe.jiangdajiuye.view.constant.AppConstant;
import juhe.jiangdajiuye.view.constant.FileConstant;
import juhe.jiangdajiuye.view.dialog.ShareDialog;
import juhe.jiangdajiuye.view.fragment.IndexFragment;
import juhe.jiangdajiuye.view.xuanJiang.XuanEntrance;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private String TAG = "MainActivity";
    private Long exitTime = 0L;
    private List<Fragment> list = new ArrayList<>();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FragmentAdapter adapter;
    private ViewPager viewPager;
    private Dialog dialog;
    private DrawerLayout drawer;
    private ShareDialog sharedialog;
    private String APP_ID = "1105550872";
    private Tencent tencent;
    private baseUiLister baseuiLister;
    //微信
    private final String WEI_ID = "wxc306137ab1a20319";
    private final String MESSAGE_RECEIVED_ACTION
            = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    private final String content = "这里有很多宣讲信息，你也来看看吧";

    private IWXAPI api;
    private WXWebpageObject webpager;
    private WXMediaMessage message;
    private SendMessageToWX.Req req;

    private String pictureUri = null;
    private String kind = null;
    private Bitmap myBitmap = null;
    private boolean getAdvert = false;
    private NavigationView navigationView;
    private NetStateReceiver receiver;

    private BmobQuery<BootPicture> query = new BmobQuery<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            return;
        }
        registerNetStateReceiver();
        bindNetState();
        initView();
        initPush();
        CheckUpgrade.getUpgradeInfo(false);
    }

    private void registerNetStateReceiver() {
        receiver = new NetStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    private void initPush() {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
    }

    public void initView() {
        findId();
        initShare();
//        initDrawerLayout();
        initViewPager();
        initTabLayout();
        initNavigationView();
    }

    private void initNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.main_my_icn).setOnClickListener(this);
    }




    /**
     * 初始化微信
     */
    private void initShare() {
        sharedialog = new ShareDialog();
        sharedialog.setItemLister(new myItemList());
        dialog = sharedialog.getDialog(this,ResourceUtils.getString(R.string.title_share_soft));
        api = WXAPIFactory.createWXAPI(this, WEI_ID, true);
        api.registerApp(WEI_ID);
        baseuiLister = new baseUiLister();
        tencent = Tencent.createInstance(APP_ID, this);
    }

    private void initViewPager() {
        list.add(IndexFragment.newInstance("http://ujs.91job.gov.cn/teachin/index?",
                "xuanJiang", IndexFragment.XUANJIANG));
        list.add(IndexFragment.newInstance("http://ujs.91job.gov.cn/campus/index?",
                "zhaoPinGongGao", IndexFragment.ZHAOPINGONGGAO));
        list.add(IndexFragment.newInstance("http://ujs.91job.gov.cn/jobfair/index?",
                "zhaoPinHui", IndexFragment.ZHAOPINHUI));

        list.add(IndexFragment.newInstance("http://ujs.91job.gov.cn/job/search?",
                "zhaoPinZhiWei", IndexFragment.ZHAOPINZHIWEI));
        list.add(IndexFragment.newInstance("http://ujs.91job.gov.cn/news/index?tag=tzgg&",
                "xinXi", IndexFragment.XINXI));
        //刚开始只会加载前两个fragment
        adapter = new FragmentAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new pagerList());
        viewPager.setCurrentItem(0);
    }
    private void initDrawerLayout() {
        //实现左右滑动
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void initTabLayout() {
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        tabLayout = findViewById(R.id.tab_layout);
        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tabLayout.addTab(tab);
        }
        tabLayout.setupWithViewPager(viewPager);
    }

    public void findId() {
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        switch (item.getItemId()) {
            case R.id.nav_home:
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.nav_library:
                UserBrowseRecordUtils.setmLibrary(1);
                startActivitySlideInRight(this, Library.class);
                break;
            case R.id.nav_favorite:
                startActivitySlideInRight(this, Collect.class);
                break;
            case R.id.nav_xuanjianghui:
                UserBrowseRecordUtils.setmXuanJiangCollect(1);
                startActivitySlideInRight(this, XuanEntrance.class);
                break;
            case R.id.nav_send:
                startActivitySlideInRight(this, FeedBack.class);

                break;
            case R.id.nav_game:
                UserBrowseRecordUtils.setmOffLineGame(1);
                startActivitySlideInRight(this, Game.class);
                break;
            case R.id.nav_about:
                UserBrowseRecordUtils.setmAboute(1);
                startActivitySlideInRight(this, About.class);
                break;
            case R.id.nav_login:
                startActivitySlideInRight(this, LoginActivity.class);
                break;
            case R.id.nav_calender:
                Browse.StartActivity(this, "http://ehall.ujs.edu.cn/calendar/index#panel0");
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_my_icn:
                startActivitySlideInRight(this, LoginActivity.class);
                break;
        }
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

    private void showShare() {
        dialog.show();
        sharedialog.setItemLister(new myItemList());
    }

    //--------获取起始屏------------- start
    public void getAdvert() {
        if (getAdvert) {
            return;
        }
        if (NetStateUtils.getNetWorkState() != NetStateUtils.TYPE_WIFI) {
            return;
        }
        query.findObjects(new FindListener<BootPicture>() {
            @Override
            public void done(List<BootPicture> object, BmobException e) {
                if (e == null && object.size() != 0) {
                    BootPicture picture = object.get(object.size() - 1);
                    getAdvert = true;
                    pictureUri = picture.getUrl();
                    Glide.with(BaseApplication.getContext()) // could be an issue!
                            .load(pictureUri)
                            .asBitmap()   //强制转换Bitmap
                            .into(target);
                }
            }
        });
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            myBitmap = bitmap;
            if (pictureUri != null)
                kind = pictureUri.substring(pictureUri.lastIndexOf(".") + 1, pictureUri.length());
            final ImageLocalLoad load = new ImageLocalLoad();
            final String saveFile = FileConstant.BootAdvertSaveRootFile;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    load.saveBitmapToLocal(saveFile, FileConstant.BootAdvertSavePictureName, "jpg", myBitmap);
                }
            }).start();

        }
    };
    //--------获取起始屏------------- end

    /**
     * popupwind的Item 监听
     */
    private class myItemList implements ShareDialog.ItemLister {

        @Override
        public void shareToQzone() {
            UserShareUtils.setQQZone(1);
            ToQzone();
        }

        @Override
        public void shareToQQ() {
            UserShareUtils.setQQ(1);
            ToQQ();
        }

        @Override
        public void shareTowei() {
            UserShareUtils.setWeiXin(1);
            req = new SendMessageToWX.Req();
            req.scene = SendMessageToWX.Req.WXSceneSession;
            shareToWX();
        }

        @Override
        public void shareTopyq() {
            UserShareUtils.setWXPenYou(1);
            req = new SendMessageToWX.Req();
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            shareToWX();
        }
    }

    private void shareToWX() {
        ToastUtils.showToast("正在跳转");
        webpager = new WXWebpageObject();
        webpager.webpageUrl = AppConstant.AppDownLoad;
        message = new WXMediaMessage(webpager);
        message.title = AppConstant.AppName;
        message.description = content;
        req.transaction = "webPager";
        req.message = message;
        Boolean get = api.sendReq(req);
//        api.handleIntent(getIntent(),this);
        dialog.cancel();
    }

    private void ToQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, AppConstant.AppName);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, AppConstant.AppDownLoad);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, AppConstant.AppIcnUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, AppConstant.AppName);
        dialog.cancel();
        tencent.shareToQQ(this, params, baseuiLister);
    }

    private void ToQzone() {
        Bundle params = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add(AppConstant.AppIcnUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, AppConstant.AppName);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, AppConstant.AppDownLoad);//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        dialog.cancel();
        tencent.shareToQzone(this, params, baseuiLister);
    }

    /**
     * 腾讯的监听回调
     */
    private class baseUiLister implements IUiListener {

        @Override
        public void onComplete(Object o) {
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
        }
    }

    //    //要想调用IUiListener 必须重写此函数
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, baseuiLister);
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE) {
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, new baseUiLister());
            }
        }
        switch (requestCode) {
            case REQUESTCODE_GETLOGIN_RESULT:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.i(TAG, "onActivityResult: ok");
                        if (UserManager.getInStance().isLogin()) {
                            navigationView.getMenu()
                                    .findItem(R.id.nav_login)
                                    .setTitle(ResourceUtils.getString(
                                            R.string.title_fragment_logout));
                        }
                        break;
                    case RESULT_CANCELED:
                        Log.i(TAG, "onActivityResult: canceled ");
                        if (!UserManager.getInStance().isLogin()) {
                            navigationView.getMenu()
                                    .findItem(R.id.nav_login)
                                    .setTitle(ResourceUtils.getString(
                                            R.string.title_fragment_login));
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private class pagerList implements ViewPager.OnPageChangeListener {
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
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                long secondTime = System.currentTimeMillis();
                if (secondTime - exitTime > 2500) {
                    //如果两次按键时间间隔大于2秒，则不退出
                    ToastUtils.showToast("再按一次退出");
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

    private void showSharePrompt() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("亲~~")
                .setMessage("若觉得软件不错，就分享给好友吧~")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showShare();
                    }
                })
                .setNegativeButton("再看看吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        getAdvert();
        if (SharePreUtils.getInt(SharePreUtils.visitFrequency, 0) > 3) {
            if (!SharePreUtils.getBoolean(SharePreUtils.hasShowSharePrompt, false)) {
                showSharePrompt();
                SharePreUtils.setBoolean(SharePreUtils.hasShowSharePrompt, true);
            }
        }
    }

    /**
     * 绑定网络监听
     */
    public void bindNetState() {
        NetStateReceiver.addNetLister(new NetStateReceiver.NetLister() {
            @Override
            public void OutInternet() {
                Log.i(TAG, "OutInternet: ");
            }

            @Override
            public void GetInternet(int type) {
                Log.i(TAG, "GetInternet: ");
                if (type == NetStateReceiver.TYPE_WIFI) {
                    getAdvert();
                }
                UserActionRecordUtils.setIP();
            }
        });
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
        UserActionRecordUtils.pushMes();
        UserBrowseRecordUtils.save();
        UserShareUtils.save();
    }
}
