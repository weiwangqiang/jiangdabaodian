package juhe.jiangdajiuye.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.widget.Toolbar;
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
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.bean.bmobRecordEntity.LeaveMes;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.sql.repository.CollectRepository;
import juhe.jiangdajiuye.utils.AppConfigUtils;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.view.constant.AppConstant;
import juhe.jiangdajiuye.view.dialog.ProgressDialog;
import juhe.jiangdajiuye.view.dialog.ShareDialog;

/**
 * Created by wangqiang on 2016/10/1.
 * 浏览器界面
 */
public class Browse extends BaseActivity {
    private static final String WEI_ID = "wxc306137ab1a20319";
    private static int VISIT_MESSAGE_ITEM = 0x1;
    private static int VISIT_CALENDER = 0x2;
    private static int visitKind = VISIT_MESSAGE_ITEM;
    private int SendCalendarCode = 0x12;
    private String TAG = "WebBrowse";
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
    private MessageItem messageItem;
    private CollectRepository collectRepository;

    /**
     * @param context 上下文
     * @param item    消息列表
     */
    public static void StartActivity(Context context, MessageItem item) {
        visitKind = VISIT_MESSAGE_ITEM;
        Intent intent = new Intent(context, Browse.class);
        intent.putExtra(MessageItem.keyVal.url, item.getUrl());
        intent.putExtra(MessageItem.keyVal.title, item.getTitle());
        intent.putExtra(MessageItem.keyVal.time, item.getTime());
        intent.putExtra(MessageItem.keyVal.from, item.getFrom());
        intent.putExtra(MessageItem.keyVal.locate, item.getLocate());
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
        Intent intent = new Intent(context, Browse.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);
        collectRepository = CollectRepository.getInstance();
        myProgress = new ProgressDialog(this, R.drawable.waiting);
        myProgress.show();
        init();
        findId();
        initToolbar();
        initParams();
        initWeb(messageItem.getUrl());
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
        ;
    }

    private void init() {
        initWEi();
        sharedialog = new ShareDialog();
        baseuiLister = new baseUiLister();
        tencent = Tencent.createInstance(APP_ID, Browse.this);
        dialog = sharedialog.getDialog(Browse.this,ResourceUtils.getString(R.string.title_share_message));
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
        messageItem = new MessageItem();
        if (visitKind == VISIT_MESSAGE_ITEM) {
            messageItem.setUrl(intent.getStringExtra(MessageItem.keyVal.url));
            messageItem.setTitle(intent.getStringExtra(MessageItem.keyVal.title));
            messageItem.setTime(intent.getStringExtra(MessageItem.keyVal.time));
            messageItem.setFrom(intent.getStringExtra(MessageItem.keyVal.from));
            messageItem.setLocate(intent.getStringExtra(MessageItem.keyVal.locate));
            isCollect = collectRepository.contain(messageItem);
        } else if (visitKind == VISIT_CALENDER) {
            messageItem.setUrl(intent.getStringExtra("url"));
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
                    myProgress.cancel();
                }
            }
        });
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void postBug(Exception e) {
        LeaveMes mes = new LeaveMes();
        mes.setEmail("report");
        mes.setContent("添加日历失败 mes：\n "+e.toString());
        mes.setAppLevel(AppConfigUtils.getVersionName());
        mes.setDevice(AppConfigUtils.getDevice());
        mes.setDeviceLevel(AppConfigUtils.getDeviceLevel());
        mes.save();
    }

    /**
     * 添加到日历中
     */
    private void calendar() {
        StringBuilder LOCATION = new StringBuilder();
        if (messageItem.getFrom() == null && messageItem.getLocate() == null) {
            LOCATION.append(ResourceUtils.getString(R.string.unKnowLocation));
        } else {
            if (messageItem.getFrom() != null)
                LOCATION.append(messageItem.getFrom());
            if (messageItem.getLocate() != null)
                LOCATION.append(" " + messageItem.getLocate());
        }
        String str = messageItem.getTime();
        if (str.indexOf("(") > 0) {
            messageItem.setTime(str.substring(0, str.indexOf("(")));
        }
        String[] times = messageItem.getTime().split("-|:| |\\n|：");
        for (String s : times) {
            System.out.println(" ; " + s);
        }
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
        calendarIntent.putExtra(CalendarContract.Events.TITLE, messageItem.getTitle());
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, LOCATION.toString());
        if(isIntentAvailable(calendarIntent)){
            startActivityForResult(calendarIntent, SendCalendarCode);
        }else{
            ToastUtils.showToast("添加日历出错");
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
        Uri content_url = Uri.parse(messageItem.getUrl());
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
        boolean contain = collectRepository.contain(messageItem);
        if (!contain) {
            collectRepository.add(messageItem);
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_add_collect));
            isCollect = true;
        } else {
            collectRepository.delete(messageItem);
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
        webpager.webpageUrl = messageItem.getUrl();
        message = new WXMediaMessage(webpager);
        message.title = AppConstant.AppName;
        message.description = "我正在" + AppConstant.AppName + "看" + messageItem.getTitle();
        req.transaction = "webPager";
        req.message = message;
        dialog.cancel();
    }

    private void ToQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, AppConstant.AppName);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "我正在看" + messageItem.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, messageItem.getUrl());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, AppConstant.AppIcnUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, AppConstant.AppName);
        dialog.cancel();
        tencent.shareToQQ(Browse.this, params, baseuiLister);
    }

    private void ToQzone() {
        Bundle params = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add(AppConstant.AppIcnUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, AppConstant.AppName);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "我正在看" + messageItem.getTitle());//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, messageItem.getUrl());//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        dialog.cancel();
        tencent.shareToQzone(Browse.this, params, baseuiLister);
    }

    /**
     * 腾讯的监听回调
     */
    private class baseUiLister implements IUiListener {

        @Override
        public void onComplete(Object o) {
//            Toast.makeText(Browse.this,"分享成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
//            Toast.makeText(Browse.this,"取消分享！", Toast.LENGTH_SHORT).show();
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
}
