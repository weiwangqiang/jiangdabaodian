package juhe.jiangdajiuye.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.HashMap;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.view.adapter.GameAdapter;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.view.dialog.ShareDialog;
import juhe.jiangdajiuye.view.constant.AppConstant;


/**
 * Created by wangqiang on 2016/10/1.
 */
public class Game extends BaseActivity implements ListView.OnItemClickListener{
    private String TAG = "Game";
    private ArrayList<HashMap<String,String>> data;
    private ListView list;
    private GameAdapter adapter ;
    private Dialog dialog;
    private ShareDialog sharedialog;
    private String APP_ID = "1105550872";
    private Tencent tencent;
    private baseUiLister baseuiLister;
    //微信
    private static final String WEI_ID = "wxc306137ab1a20319";
    private IWXAPI api;
    private WXWebpageObject webpager ;
    private WXMediaMessage message;
    private SendMessageToWX.Req req;
    private String[] url = {
            "http://www.kkxyx.com/jianfengchazhen/",
            "http://www.kkxyx.com/chengganguohe/",
            "http://www.kkxyx.com/wuzi/",
            "http://www.kkxyx.com/cwdyj/",
            "http://www.kkxyx.com/glgs/",
            "http://www.kkxyx.com/50/",
            "http://www.kkxyx.com/zhaonimei/",
            "http://www.kkxyx.com/snrjx100/",
            "http://www.kkxyx.com/cube/",
            "http://www.kkxyx.com/memeda/",
            "http://www.kkxyx.com/selang/",
            "http://www.kkxyx.com/se/",
            "http://www.kkxyx.com/duimutou/ ",
            "http://www.kkxyx.com/shenjingcat2/",
            "http://www.kkxyx.com/xiaoniaofeifei/ ",
            "http://www.kkxyx.com/zqyl/ ",
    };
    private String[] image = {
            "http://kkxyx.com/jianfengchazhen/2000.jpg",
            "http://kkxyx.com/chengganguohe/2000.jpg",
            "http://www.kkxyx.com/wuzi/icon.png",
            "http://www.kkxyx.com/cwdyj/icon.png",
            "http://www.kkxyx.com/glgs/icon.png",
            "http://www.kkxyx.com/50/3.jpg",
            "http://www.kkxyx.com/zhaonimei/zhaonimei.jpg",
            "http://www.kkxyx.com/snrjx100/icon.png",
            "http://www.kkxyx.com/cube/share.png",
            "http://www.kkxyx.com/memeda/logo.png",
            "http://www.kkxyx.com/selang/icon.png",
            "http://www.kkxyx.com/se/logo.png",
            "http://www.kkxyx.com/duimutou/icon.png",
            "http://www.kkxyx.com/resources/images/icons/shenjingmao.png",
            "http://www.kkxyx.com/resources/images/icons/flappybird.png",
            "http://www.kkxyx.com/resources/images/icons/zqyl.png",
    };
    private String[] title = {
            "见缝插针",
            "撑杆过河",
            "五子棋  ",
            "宠物大营救  ",
            "灌篮高手   ",
            "比比谁更持久  ",
            "微信找你妹  ",
            "是男人就下100层  ",
            "变态方块游戏 ",
            "美女么么哒",
            "小心色狼 ",
            "看你有多色  ",
            "堆木头游戏 ",
            "神经猫2",
            "小鸟飞呀飞",
            "最强眼力 "
    };
    private String[] content = {
            "全世界只有四人能过17关",
            "据说IQ超过180的人，才能玩到第四十关",
            "五子棋！ ",
            "宠物大营救  ",
            "灌篮高手   ",
            "微信找你妹",
            "是男人就下100层！ ",
            "经典俄罗斯方块变态版 ",
            "不要害羞哦  ",
            "你懂的    ",
            "休闲益智 ",
            "休闲益智",
            "动作游戏",
            "休闲益智 ",
            "动作游戏 ",
            "动作游戏 "

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        init();
        findid();
        setlister();
        getMessage();
        upDate();
        initToolbar();
    }
    private void init(){
        initWEi();
        data = new ArrayList<>();
        sharedialog = new ShareDialog();
        baseuiLister = new baseUiLister();
        tencent = Tencent.createInstance(APP_ID, Game.this);
        dialog = sharedialog.getDialog(Game.this, ResourceUtils.getString(R.string.title_share_game));
    }
    private Toolbar toolbar;
    public void initToolbar(){
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    /**初始化微信
     */
    private void initWEi(){
        api = WXAPIFactory.createWXAPI(this,WEI_ID,true);
        api.registerApp(WEI_ID);
    }
    public void findid(){
        list = (ListView)findViewById(R.id.game_list);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

    }
    public void setlister(){

    }
    public void getMessage(){
            for(int i = 0;i<url.length;i++){
                HashMap<String,String> map = new HashMap<>();
                map.put("url",url[i]);
                map.put("title",title[i]);
                map.put("MessageItem",content[i]);
                map.put("image",image[i]);
                map.put("visit","12");
                data.add(map);
            }
    }
    public void upDate(){
        adapter = new GameAdapter(this,data,
                R.layout.game_item,
                new String[]{"image","title","MessageItem","visit"},
                new int[]{R.id.game_item_icn,R.id.game_item_title,
                        R.id.game_item_message,R.id.game_item_visit});
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            default:
                break;
        }
    }
    private void showShare(){
        dialog.show();
        sharedialog.setItemLister(new myItemListener());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_share:
                showShare();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Game.this,GameOnline.class);
        String url = data.get(position).get("url");
        intent.putExtra("url",url);
        intent.putExtra("title",data.get(position).get("title"));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
    }

    /**
     * popupwind的Item 监听
     */
    private class myItemListener implements ShareDialog.ItemLister{

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
        webpager.webpageUrl = AppConstant.AppDownLoad;
        message = new WXMediaMessage(webpager);
        message.title = AppConstant.AppName;
        message.description = AppConstant.AppDescription;
        req.transaction = "webPager";
        req.message = message;
        dialog.cancel();
    }
    private void ToQQ(){
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, AppConstant.AppName);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "我正在玩"+title);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, AppConstant.AppDownLoad);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,AppConstant.AppIcnUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, AppConstant.AppName);
        dialog.cancel();
        tencent.shareToQQ(Game.this, params,baseuiLister);
    }
    private void  ToQzone(){
        Bundle params = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add(AppConstant.AppIcnUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE,AppConstant.AppName);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,"我正在玩"+title);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, AppConstant.AppDownLoad);//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,list);
        dialog.cancel();
        tencent.shareToQzone(Game.this, params,baseuiLister);
    }
    /**
     * 腾讯的监听回调
     */
    private class baseUiLister implements IUiListener {

        @Override
        public void onComplete(Object o) {
//            Toast.makeText(Game.this,"分享成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
//            Toast.makeText(Game.this,"取消分享！", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onPause(){
        super.onPause();
    }
}
