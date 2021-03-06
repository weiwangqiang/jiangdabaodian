package juhe.jiangdajiuye.view.activity.browse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.push.XuanJiangPush;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.view.dialog.ProgressDialog;
import juhe.jiangdajiuye.view.dialog.ShareDialog;
import juhe.jiangdajiuye.listener.OnDownLoadListener;

/**
 * Created by wangqiang on 2016/10/1.
 */
public class PushBrowseActivity extends BaseActivity {
    private String TAG = "PushBrowseActivity";
    private String targetUrl, title;
    private WebView webView;
    private ProgressDialog myProgress;
    private Dialog dialog;
    private ShareDialog sharedialog;
    private String APP_ID = "1105550872";
    private Tencent tencent;
    private baseUiLister baseuiLister;
    //微信
    private static final String WEI_ID = "wxc306137ab1a20319";
    private IWXAPI api;
    private WXWebpageObject webpager;
    private WXMediaMessage message;
    private SendMessageToWX.Req req;
    private String IcnUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3084594445,4206732502&fm=96";

    public static Intent getActivityInt(Context context, XuanJiangPush mes) {
        Intent intent = new Intent(context, PushBrowseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", mes.getTargetUrl());
        bundle.putString("title", mes.getTitle());
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);
        myProgress = new ProgressDialog(this);
        myProgress.show();
        init();
    }

    private Toolbar toolbar;

    public void initToolbar() {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void init() {
        initWEi();
        findId();
        getParam();
        initToolbar();
        initWeb(targetUrl);
        sharedialog = new ShareDialog();
        baseuiLister = new baseUiLister();
        tencent = Tencent.createInstance(APP_ID, PushBrowseActivity.this);
        dialog = sharedialog.getDialog(PushBrowseActivity.this, ResourceUtils.getString(R.string.title_share_message));
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
    public void getParam() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        targetUrl = bundle.getString("url");
        title = bundle.getString("title");
    }

    public void initWeb(String targetUrl) {
        if (targetUrl == null)
            return;
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
//        wSet.setAppCacheEnabled(true);
        wSet.setSupportZoom(true);
        wSet.setBuiltInZoomControls(true);

        wSet.setDisplayZoomControls(false);
        wSet.setAllowFileAccess(true);//资源加载超时操作
        wSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应屏幕，内容将自动缩放
        wSet.setLoadWithOverviewMode(true);
        wSet.setUseWideViewPort(true);
        webView.loadUrl(targetUrl);
//        webView.setInitialScale(100);   //100代表不缩放
        webView.setDownloadListener(new OnDownLoadListener());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest resourceRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(resourceRequest.getUrl().toString());
                    return true;
                } else {
                    return false;
                }
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                webView.setVisibility(View.VISIBLE);
                myProgress.cancel();
//                Toast.makeText(PushBrowseActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.webbrowse, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
                return true;
            case R.id.browse_share:
                showShare();
                return true;
            case R.id.browse_open:
                openInBrowse();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openInBrowse() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri contentUrl = Uri.parse(targetUrl);
        intent.setData(contentUrl);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

    }

    private void showShare() {
        dialog.show();
        sharedialog.setItemLister(new myItemList());
    }

    /**
     * popupwind的Item 监听
     */
    private class myItemList implements ShareDialog.ItemLister {

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
        ToastUtils.showToast(ResourceUtils.getString(R.string.toast_jump_wait));
        webpager = new WXWebpageObject();
        webpager.webpageUrl = targetUrl;
        message = new WXMediaMessage(webpager);
        message.title = ResourceUtils.getString(R.string.app_name);
        message.description = title;
        req.transaction = "webPager";
        req.message = message;
        Boolean get = api.sendReq(req);
        dialog.cancel();
    }

    private void ToQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "我正在江大宝典看" + title);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, IcnUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ResourceUtils.getString(R.string.app_name));
        dialog.cancel();
        tencent.shareToQQ(PushBrowseActivity.this, params, baseuiLister);
    }

    private void ToQzone() {
        Bundle params = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add(IcnUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "我正在江大宝典看" + title);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        dialog.cancel();
        tencent.shareToQzone(PushBrowseActivity.this, params, baseuiLister);
    }

    /**
     * 腾讯的监听回调
     */
    private class baseUiLister implements IUiListener {

        @Override
        public void onComplete(Object o) {
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_share_success));
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_share_cancel));

        }
    }

    //    //要想调用IUiListener 必须重写此函数
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, baseuiLister);
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE) {
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, new baseUiLister());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
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
