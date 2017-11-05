package juhe.jiangdajiuye.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
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
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.entity.MessageItem;
import juhe.jiangdajiuye.sql.CollectSqlHelper;
import juhe.jiangdajiuye.tool.ProgressDialog;
import juhe.jiangdajiuye.tool.shareDialog;
import juhe.jiangdajiuye.util.ToastUtils;
import juhe.jiangdajiuye.view.constant.AppConstant;

/**
 * Created by wangqiang on 2016/10/1.
 */
public class browse extends BaseActivity {
    private String TAG = "WebBrowse";
    private Boolean isCheck = false;
    private WebView webView;
    private ProgressDialog myprogress;
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
    private MessageItem mItem;
    private CollectSqlHelper helper;
    private SQLiteDatabase sqLiteDatabase;

    /**
     *
     * @param context 上下文
     * @param item 消息列表
     *
     */
    public static void StartActivity(Context context,MessageItem item){
        Intent intent = new Intent(context,browse.class);
        intent.putExtra(MessageItem.keyVal.url,item.getUrl());
        intent.putExtra(MessageItem.keyVal.title,item.getTitle());
        intent.putExtra(MessageItem.keyVal.time,item.getTime());
        intent.putExtra(MessageItem.keyVal.from,item.getFrom());
        intent.putExtra(MessageItem.keyVal.locate,item.getLocate());
        context.startActivity(intent);
        if(context instanceof Activity)
        ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);
        myprogress = new ProgressDialog(this, R.drawable.waiting);
        helper = new CollectSqlHelper(this);
        sqLiteDatabase = helper.getWritableDatabase();
        helper.setSQL(sqLiteDatabase);
        myprogress.show();
        init();
        findid();
        initToolbar();
        getParam();
        initView();
        initWeb(mItem.getUrl());
    }
    private Toolbar toolbar;
    public void initToolbar(){
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void init(){
        initWEi();
        sharedialog = new shareDialog();
        baseuiLister = new baseUiLister();
        tencent = Tencent.createInstance(APP_ID, browse.this);
        dialog = sharedialog.getDialog(browse.this);
    }
    /**初始化微信
     */
    private void initWEi(){
        api = WXAPIFactory.createWXAPI(this,WEI_ID,true);
        api.registerApp(WEI_ID);
    }
    public void findid(){
        webView = (WebView)findViewById(R.id.webView);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
    }
    public void initView(){
        isCheck = helper.hasURL(mItem.getUrl());
    }

    /**
     * 获取参数信息
     */
    public void getParam(){
        Intent intent = getIntent();
        mItem = new MessageItem();
        mItem.setUrl(intent.getStringExtra(MessageItem.keyVal.url));
        mItem.setTitle(intent.getStringExtra(MessageItem.keyVal.title));
        mItem.setTime(intent.getStringExtra(MessageItem.keyVal.time));
        mItem.setFrom(intent.getStringExtra(MessageItem.keyVal.from));
        mItem.setLocate(intent.getStringExtra(MessageItem.keyVal.locate));
        Log.i(TAG, "getParam: "+mItem.getFrom()+mItem.getLocate());
    }
    public void initWeb(String url){
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
//        wSet.setAppCacheEnabled(true);
        wSet.setSupportZoom(true);
        wSet.setBuiltInZoomControls(true);
        wSet.setDisplayZoomControls(false);
        wSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN );//适应屏幕，内容将自动缩放
        wSet.setLoadWithOverviewMode(true);
        wSet.setUseWideViewPort(true);
        webView.loadUrl(url);
//        webView.setInitialScale(100);   //100代表不缩放
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                // MainActivity.this.setProgress(newProgress * 100);
                if(newProgress==100){
                    webView.setVisibility(View.VISIBLE);
                    myprogress.cancel();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browseitem, menu);
        return true;
    }
    private Menu menu ;
    private static String Menutitle = "";
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.menu = menu;
        Menutitle = helper.hasURL(mItem.getUrl()) ? "取消收藏":"收藏";
        menu.findItem(R.id.browse_collect).setTitle(Menutitle);
        invalidateOptionsMenu();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
      switch (id){
         case android.R.id.home:
             finish();
             overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
             return true;
         case R.id.browse_collect:
             showCollect();
             return true;
         case R.id.browse_share:
             showShare();
             return true;
          case R.id.browse_open:
              openInBrowse();
              return true;  
          case R.id.browse_calendar:
              calendar();
              return true;
     }
        return super.onOptionsItemSelected(item);
    }
    public int SendCalendarCode = 0x12;

    /**
     * 添加到日历中
     */
    private void calendar() {
        StringBuilder LOCATION = new StringBuilder();
        if(mItem.getFrom()== null  &&  mItem.getLocate() == null){
            LOCATION.append("地点未知");
        }
        else{
            if(mItem.getFrom() != null)
                LOCATION.append(mItem.getFrom());
            if(mItem.getLocate() != null)
                LOCATION.append(" "+mItem.getLocate()) ;
        }
        String[] times = mItem.getTime().split("-|:| |\\n|：");
        for(String s : times){
            System.out.println(" ; "+s);
        }
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        if(times.length >=3){
            int year = Integer.parseInt(times[0]);
            int month = Integer.parseInt(times[1]);
            month--;
            int day = Integer.parseInt(times[2]);
            int hourS = 0;
            int minuteS = 0 ;
            int hourE = 0 ;
            int minuteE = 0;
            if(times.length == 3){//没有 hh:mm:ss
            }else if(times.length == 5){//只有开始时间 的hh:mm:ss
                 hourS = Integer.parseInt(times[3]);
                 minuteS = Integer.parseInt(times[4]);
            }else{//有具体时间段 hh:mm:ss - hh:mm:ss
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
        calendarIntent.putExtra(CalendarContract.Events.TITLE,mItem.getTitle());
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, LOCATION.toString());
        startActivityForResult(calendarIntent,SendCalendarCode);
    }

    /**
     * 用浏览器打开
     */
    private void openInBrowse() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(mItem.getUrl());
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

    }

    private void showShare(){
        dialog.show();
        sharedialog.setItemlister(new myItemlist());
    }
    private void showCollect(){
        String showText = isCheck ? "取消成功":"收藏成功";
        Menutitle = isCheck ? "收藏" : "取消收藏";
        uiutils.showToast(showText);
        invalidateOptionsMenu();
        isCheck = !isCheck;
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
        Toast.makeText(this,"正在跳转",Toast.LENGTH_SHORT).show();
        webpager = new WXWebpageObject();
        webpager.webpageUrl = mItem.getUrl();
        message = new WXMediaMessage(webpager);
        message.title = AppConstant.AppName;
        message.description = "我正在"+AppConstant.AppName+"看"+mItem.getTitle();
        req.transaction = "webPager";
        req.message = message;
        Boolean get = api.sendReq(req);
        dialog.cancel();
        Log.e(TAG,"share return is "+get);
    }
    private void ToQQ(){
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, AppConstant.AppName);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "我正在看"+mItem.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mItem.getUrl());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, AppConstant.AppIcnUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  AppConstant.AppName);
        dialog.cancel();
        tencent.shareToQQ(browse.this, params,baseuiLister);
    }
    private void  ToQzone(){
        Bundle params = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add(AppConstant.AppIcnUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, AppConstant.AppName);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,"我正在看"+mItem.getTitle());//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, mItem.getUrl());//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,list);
        dialog.cancel();
        tencent.shareToQzone(browse.this, params,baseuiLister);
    }
    /**
     * 腾讯的监听回调
     */
    private class baseUiLister implements IUiListener {

        @Override
        public void onComplete(Object o) {
//            Toast.makeText(browse.this,"分享成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
//            Toast.makeText(browse.this,"取消分享！", Toast.LENGTH_SHORT).show();
        }
    }
    //    //要想调用IUiListener 必须重写此函数
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SendCalendarCode){
            if(resultCode == RESULT_OK){
                ToastUtils.showToast("添加成功!");
            }
            Log.i(TAG, "onActivityResult: "+resultCode+" ok "+RESULT_OK);
            return;
        }
        Tencent.onActivityResultData(requestCode, resultCode, data, baseuiLister);
//        if(requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE){
//            if (resultCode == Constants.ACTIVITY_OK) {
//                Log.i(TAG, "onActivityResult: --------------");
//                Tencent.handleResultData(data, new baseUiLister());
//            }
//        }

    }
    @Override
    public void onPause(){
        super.onPause();
        //收藏
        if(isCheck&& !helper.hasURL(mItem.getUrl())){
            helper.addCollect(mItem.getTitle(),
                    mItem.getFrom(),
                    mItem.getLocate(),
                    mItem.getTime(),
                    mItem.getUrl());
        }
        else if (helper.hasURL(mItem.getUrl())&&!isCheck){
            //取消收藏
            helper.delete(mItem.getUrl());
        }
}

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

}
