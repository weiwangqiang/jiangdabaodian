package juhe.jiangdajiuye.view.activity.browse;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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
import java.util.Calendar;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.bean.bmobRecordEntity.LeaveMes;
import juhe.jiangdajiuye.constant.CacheConstant;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.db.repository.CollectDepository;
import juhe.jiangdajiuye.view.dialog.ProgressDialog;
import juhe.jiangdajiuye.view.dialog.ShareDialog;
import juhe.jiangdajiuye.utils.AppConfigUtils;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.view.activity.CommentActivity;

/**
 * Created by wangqiang on 2016/10/1.
 * 浏览器界面
 */
public class MesBrowseActivity extends BaseActivity {
    private static final String WEI_ID = "wxc306137ab1a20319";
    private static int VISIT_MESSAGE_ITEM = 0x1;
    private static int VISIT_CALENDER = 0x2;
    private static int visitKind = VISIT_MESSAGE_ITEM;
    private int SendCalendarCode = 0x12;
    private String TAG = "PushBrowseActivity";
    private String APP_ID = "1105550872";
    private Boolean isCollect = false;//记录是否处于收藏状态
    private WebView webView;
    private ProgressDialog myProgress;
    private Dialog dialog;
    private ShareDialog sharedialog;
    private Tencent tencent;
    private baseUiLister baseuiLister;
    //微信
    private IWXAPI api;
    private WXWebpageObject webpager;
    private WXMediaMessage message;
    private SendMessageToWX.Req req;
    private MessageBean messageBean;
    private CollectDepository collectDepository;

    /**
     * @param context 上下文
     * @param item    消息列表
     */
    public static void StartActivity(Context context, MessageBean item) {
        visitKind = VISIT_MESSAGE_ITEM;
        Intent intent = new Intent(context, MesBrowseActivity.class);
        intent.putExtra(MessageBean.keyVal.url, item.getUrl());
        intent.putExtra(MessageBean.keyVal.title, item.getTitle());
        intent.putExtra(MessageBean.keyVal.time, item.getTime());
        intent.putExtra(MessageBean.keyVal.from, item.getFrom());
        intent.putExtra(MessageBean.keyVal.locate, item.getLocate());
        context.startActivity(intent);
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
    }

    /**
     * 只是打开一个链接
     *
     * @param context
     * @param url
     */
    public static void StartActivity(Context context, String url) {
        visitKind = VISIT_CALENDER;
        Intent intent = new Intent(context, MesBrowseActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);
        collectDepository = CollectDepository.getInstance();
        myProgress = new ProgressDialog(this);
        init();
        findId();
        initToolbar();
        initParams();
        initWeb(messageBean.getUrl());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            Log.i(TAG, "onBackPressed: back webview ");
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private Toolbar toolbar;

    public void initToolbar() {
        if (visitKind == VISIT_MESSAGE_ITEM) {
            toolbar.setTitle(ResourceUtils.getString(R.string.title_browse_message));
        } else if (visitKind == VISIT_CALENDER) {
            toolbar.setTitle(ResourceUtils.getString(R.string.title_browse_calender));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void init() {
        initWEi();
        sharedialog = new ShareDialog();
        baseuiLister = new baseUiLister();
        tencent = Tencent.createInstance(APP_ID, MesBrowseActivity.this);
        dialog = sharedialog.getDialog(MesBrowseActivity.this, ResourceUtils.getString(R.string.title_share_message));
    }

    /**
     * 初始化微信
     */
    private void initWEi() {
        api = WXAPIFactory.createWXAPI(this, WEI_ID, true);
        api.registerApp(WEI_ID);
    }

    public void findId() {
        webView = (WebView) findViewById(R.id.webView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 获取参数信息
     */
    public void initParams() {
        Intent intent = getIntent();
        messageBean = new MessageBean();
        if (visitKind == VISIT_MESSAGE_ITEM) {
            messageBean.setUrl(intent.getStringExtra(MessageBean.keyVal.url));
            messageBean.setTitle(intent.getStringExtra(MessageBean.keyVal.title));
            messageBean.setTime(intent.getStringExtra(MessageBean.keyVal.time));
            messageBean.setFrom(intent.getStringExtra(MessageBean.keyVal.from));
            messageBean.setLocate(intent.getStringExtra(MessageBean.keyVal.locate));
            isCollect = collectDepository.contain(messageBean);
        } else if (visitKind == VISIT_CALENDER) {
            messageBean.setUrl(intent.getStringExtra("url"));
        }
    }

    public void initWeb(String url) {
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
//        wSet.setAppCacheEnabled(true);
        wSet.setSupportZoom(true);
        wSet.setBuiltInZoomControls(true);
        wSet.setDisplayZoomControls(false);
        wSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应屏幕，内容将自动缩放
        wSet.setLoadWithOverviewMode(true);
        wSet.setUseWideViewPort(true);
        wSet.setAllowFileAccess(false);
        wSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);
//        webView.setInitialScale(100);   //100代表不缩放
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                } else {
                    return false;
                }
            }

            /**
             * Notify the host application that a page has started loading. This method
             * is called once for each main frame load so a page with iframes or
             * framesets will call onPageStarted one time for the main frame. This also
             * means that onPageStarted will not be called when the contents of an
             * embedded frame changes, i.e. clicking a link whose target is an iframe,
             * it will also not be called for fragment navigations (navigations to
             * #fragment_id).
             *
             * @param view    The WebView that is initiating the callback.
             * @param url     The url to be loaded.
             * @param favicon The favicon for this page if it already exists in the
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "onPageStarted: target url " + url);
                myProgress.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                cancelProgress();
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                // MainActivity.this.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    webView.setVisibility(View.VISIBLE);
                    cancelProgress();
                }
            }
        });
    }

    private void cancelProgress() {
        if (myProgress.isShowing()) {
            myProgress.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelProgress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (visitKind == VISIT_MESSAGE_ITEM) {
            getMenuInflater().inflate(R.menu.browseitem, menu);
        }
        return true;
    }

    private static StringBuilder collectTitle = new StringBuilder();

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (visitKind == VISIT_MESSAGE_ITEM) {
            collectTitle.setLength(0);
            collectTitle.append(isCollect ?
                    ResourceUtils.getString(R.string.cancel_collect) :
                    ResourceUtils.getString(R.string.collect));
            menu.findItem(R.id.browse_collect).setTitle(collectTitle.toString());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.browse_collect:
                changeCollectState();
                return true;
            case R.id.browse_share:
                showShare();
                return true;
            case R.id.browse_open:
                openInBrowse();
                return true;
            case R.id.browse_calendar:
                try {
                    calendar();
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showToast("添加失败");
                    postBug(e);
                }
                return true;
            case R.id.browse_comment:
                CommentActivity.StartActivity(this, messageBean);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void postBug(Exception e) {
        LeaveMes mes = new LeaveMes();
        mes.setEmail("report");
        mes.setContent("添加日历失败 mes：\n " + e.toString());
        mes.setAppLevel(AppConfigUtils.getVersionName());
        mes.setDevice(AppConfigUtils.getDevice());
        mes.setDeviceLevel(AppConfigUtils.getDeviceLevel());
        mes.save();
    }

    /**
     * 添加到日历中
     */
    private void calendar() {
        StringBuilder location = new StringBuilder();
        if (messageBean.getFrom() == null && messageBean.getLocate() == null) {
            location.append(ResourceUtils.getString(R.string.unKnowLocation));
        } else {
            if (messageBean.getFrom() != null)
                location.append(messageBean.getFrom());
            if (messageBean.getLocate() != null)
                location.append(" " + messageBean.getLocate());
        }
        String str = messageBean.getTime();
        if (str.indexOf("(") > 0) {//时间后面有“（周二..）”
            messageBean.setTime(str.substring(0, str.indexOf("(")));
        }
        String[] times = messageBean.getTime().split("-|:| |\\n|：");
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        if (times.length >= 3) {
            int year = Integer.parseInt(times[0]);
            int month = Integer.parseInt(times[1]);
            month--;
            int day = Integer.parseInt(times[2]);
            int hourS = 0;
            int minuteS = 0;
            int hourE = 0;
            int minuteE = 0;
            if (times.length == 3) {//没有 hh:mm:ss
            } else if (times.length <= 6) {
                //只有开始时间 的hh:mm:ss
                hourS = Integer.parseInt(times[3]);
                minuteS = Integer.parseInt(times[4]);
            } else if (times.length >= 7) {
                //有具体时间段 hh:mm:ss - hh:mm:ss
                hourS = Integer.parseInt(times[3]);
                minuteS = Integer.parseInt(times[4]);
                hourE = Integer.parseInt(times[5]);
                minuteE = Integer.parseInt(times[6]);
            }
            beginTime.set(year, month, day, hourS, minuteS);
            endTime.set(year, month, day, hourE, minuteE);
        }
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE, messageBean.getTitle());
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.toString());
        if (isIntentAvailable(calendarIntent)) {
            startActivityForResult(calendarIntent, SendCalendarCode);
        } else {
            ToastUtils.showToast("找不到日历~");
        }
    }

    public boolean isIntentAvailable(Intent intent) {
        return !getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty();
    }

    /**
     * 用浏览器打开
     */
    private void openInBrowse() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(messageBean.getUrl());
        intent.setData(content_url);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("打开失败");
        }
    }

    @Override
    public void onClick(View view) {

    }

    private void showShare() {
        dialog.show();
        sharedialog.setItemLister(new myItemListener());
    }

    private void changeCollectState() {
        boolean contain = collectDepository.contain(messageBean);
        if (!contain) {
            collectDepository.add(messageBean);
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_add_collect));
            isCollect = true;
        } else {
            collectDepository.delete(messageBean);
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_delete_collect));
            isCollect = false;
        }
    }

    /**
     * popupwind的Item 监听
     */
    private class myItemListener implements ShareDialog.ItemLister {

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
            req.scene = SendMessageToWX.Req.WXSceneSession;
            shareToWX();
        }

        @Override
        public void shareTopyq() {
            req = new SendMessageToWX.Req();
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            shareToWX();
        }
    }

    private void shareToWX() {
        Toast.makeText(this, ResourceUtils.getString(R.string.share_toast), Toast.LENGTH_SHORT).show();
        webpager = new WXWebpageObject();
        webpager.webpageUrl = messageBean.getUrl();
        message = new WXMediaMessage(webpager);
        message.title = ResourceUtils.getString(R.string.app_name);
        message.description = "我正在" + ResourceUtils.getString(R.string.app_name) + "看" + messageBean.getTitle();
        req.transaction = "webPager";
        req.message = message;
        dialog.cancel();
    }

    private void ToQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, ResourceUtils.getString(R.string.app_name));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "我正在看" + messageBean.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, messageBean.getUrl());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, CacheConstant.AppIcnUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ResourceUtils.getString(R.string.app_name));
        dialog.cancel();
        tencent.shareToQQ(MesBrowseActivity.this, params, baseuiLister);
    }

    private void ToQzone() {
        Bundle params = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add(CacheConstant.AppIcnUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, ResourceUtils.getString(R.string.app_name));//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "我正在看" + messageBean.getTitle());//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, messageBean.getUrl());//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        dialog.cancel();
        tencent.shareToQzone(MesBrowseActivity.this, params, baseuiLister);
    }

    /**
     * 腾讯的监听回调
     */
    private class baseUiLister implements IUiListener {

        @Override
        public void onComplete(Object o) {
//            Toast.makeText(MesBrowseActivity.this,"分享成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
//            Toast.makeText(MesBrowseActivity.this,"取消分享！", Toast.LENGTH_SHORT).show();
        }
    }

    //    //要想调用IUiListener 必须重写此函数
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SendCalendarCode) {
            if (resultCode == RESULT_OK) {
                ToastUtils.showToast(ResourceUtils.getString(R.string.add_success));
            }
            return;
        }
        Tencent.onActivityResultData(requestCode, resultCode, data, baseuiLister);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (myProgress != null && myProgress.isShowing()) {
            myProgress.dismiss();
        }
    }
}
