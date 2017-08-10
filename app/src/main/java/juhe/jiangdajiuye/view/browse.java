package juhe.jiangdajiuye.view;

import android.app.Dialog;
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
import android.widget.Button;
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
import juhe.jiangdajiuye.sql.CollectSqlHelper;
import juhe.jiangdajiuye.tool.ProgressDialog;
import juhe.jiangdajiuye.tool.shareDialog;
import juhe.jiangdajiuye.util.ToastUtils;

/**
 * Created by wangqiang on 2016/10/1.
 */
public class browse extends BaseActivity {
    private String TAG = "WebBrowse";
    private String title,company,location,time;
    private int from;
    private Boolean ischeck = false;
    private Button back;
//    private Toolbar toolbar;
    private String url;
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

    private CollectSqlHelper helper;
    private SQLiteDatabase sqLiteDatabase;
    private String IcnUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3084594445,4206732502&fm=96";
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
        setlister();
        initView();
        initWeb(url);
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
    public void setlister(){
//        back.setOnClickListener(this);
//        collect.setOnClickListener(this);
//        share.setOnClickListener(this);

    }
    public void initView(){
        ischeck = helper.hasURL(url);
    }

    /**
     * 获取参数信息
     */
    public void getParam(){
        Intent intent = getIntent();
        url  =  intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        time = intent.getStringExtra("time");
        //1是从招聘会，宣讲会来的，2是从信息速递来的，3是从收藏栏来的
        from = intent.getIntExtra("from",-1);
        if(from != 2){
            company = intent.getStringExtra("company");
            location = intent.getStringExtra("location");
        }
       Log.e(TAG,"url is "+url+" title is "+title+ " time is "+time+" company is "+company+" location is "+location);
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
        Menutitle = helper.hasURL(url) ? "取消收藏":"收藏";
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
    private void calendar() {
        Log.i(TAG, "calendar: ");
        String LOCATION;

        if(company == null && location == null)
            LOCATION = "地点未知，请点击修改";
        else
        {
            LOCATION = company + "  "+ location ;
        }
        String TITLE = title ;
        String[] tiems = time.split("-|:| ");
        int size = tiems.length ;
        for(String s : tiems){
            System.out.println(" ; "+s);
        }
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        if(tiems.length >= 3){
            int year = Integer.parseInt(tiems[0]);
            int month = Integer.parseInt(tiems[1]);
            month--;
            int day = Integer.parseInt(tiems[2]);
            if(tiems.length == 3){
                beginTime.set(year, month, day, 0, 0);
                endTime.set(year, month, day+1, 0, 0);
            }else {
                int hourS = Integer.parseInt(tiems[3]);
                int minuteS = Integer.parseInt(tiems[4]);
                int hourE = Integer.parseInt(tiems[5]);
                int minuteE = Integer.parseInt(tiems[6]);
                beginTime.set(year, month, day, hourS, minuteS);
                endTime.set(year, month, day, hourE, minuteE);
            }
        }
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE,title);
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, LOCATION);
        startActivityForResult(calendarIntent,SendCalendarCode);
    }

    private void openInBrowse() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
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
        if(ischeck){
            uiutils.showToast("取消成功");
            Menutitle = "收藏";
            menu.findItem(R.id.browse_collect).setTitle("收藏");

        }
        else if(!ischeck){
            uiutils.showToast("收藏成功");
            Menutitle = "取消收藏";
            menu.findItem(R.id.browse_collect).setTitle("取消收藏");

        }
        invalidateOptionsMenu();
        ischeck = !ischeck;
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
        Log.e(TAG,"share to weixin");
        webpager = new WXWebpageObject();
        webpager.webpageUrl = url;
        message = new WXMediaMessage(webpager);
        message.title = "江大宝典";
        message.description = title;
        req.transaction = "webPager";
        req.message = message;
        Boolean get = api.sendReq(req);
        dialog.cancel();
        Log.e(TAG,"share return is "+get);
    }
    private void ToQQ(){
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "我正在江大宝典看"+title);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,IcnUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "江大宝典");
        dialog.cancel();
        tencent.shareToQQ(browse.this, params,baseuiLister);
    }
    private void  ToQzone(){
        Bundle params = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add(IcnUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,"我正在宝典看"+title);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
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
            Toast.makeText(browse.this,"分享成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
            Toast.makeText(browse.this,"取消分享！", Toast.LENGTH_SHORT).show();
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
        Log.i(TAG, "onActivityResult: requestCode "+requestCode );
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
        if(ischeck&&from!=3){
            helper.addCollect(title,company,location,time,url);
        }
        else if (helper.hasURL(url)&&!ischeck){
            //取消收藏
            helper.delete(url);
        }
}

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

}
