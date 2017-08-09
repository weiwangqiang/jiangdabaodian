package juhe.jiangdajiuye.view;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.myExpandableListAdapter;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.sql.LibrarySqlHelper;
import juhe.jiangdajiuye.tool.ProgressDialog;
import juhe.jiangdajiuye.tool.parseTools;
import juhe.jiangdajiuye.util.urlConnection;

/**图书详情
 * Created by wangqiang on 2016/10/2.
 */
public class searchBook extends BaseActivity{
    private Button back;
    private TextView b,e,bm;
    private View v,footer;
    private RadioButton collect;
    private Boolean ischeck = false;
    private ExpandableListView listview;
    private myExpandableListAdapter adapter;
    private ProgressDialog myprogress;
    private String book,editor,available,number,url;
    private String TAG = "searchBook";
    private String title = "",editer = "",bookMessage = "";
    public HashMap<String,String> map  = new HashMap<>();
    public static List<List<String>> list = new ArrayList<>();
    private parseTools parsetools =  parseTools.getparseTool() ;

    private LibrarySqlHelper helper;
    private SQLiteDatabase sqLiteDatabase;

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 0x1:
                    showDate();
                    myprogress.cancel();
                    break;
                case 0x2:
                    myprogress.cancel();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book);
        helper = new LibrarySqlHelper(this);
        sqLiteDatabase = helper.getWritableDatabase();
        helper.setSQL(sqLiteDatabase);
        getParam();
        initView();
        Log.e(TAG,"------url--------------"+url);
    }
    public void initView(){
        findid();
        changeView();
        getSearch();
    }
    public void findid(){
        back = (Button)findViewById(R.id.search_back);
        listview = (ExpandableListView)findViewById(R.id.search_list);
        collect = (RadioButton)findViewById(R.id.browse_collect);

        back.setOnClickListener(this);
        collect.setOnClickListener(this);

        ischeck = helper.hasURL(url);
        collect.setChecked(ischeck);
    }
    /**
     * 获取参数信息
     */
    public void getParam(){
        Intent intent = getIntent();
        book  =  intent.getStringExtra("book");
        url  =  intent.getStringExtra("url");
        editor = intent.getStringExtra("editor");
        available = intent.getStringExtra("available");
        number = intent.getStringExtra("number");
        Log.e(TAG,book + " "+ url +" "+editor+" "+available+" "+number);
    }
    public void getSearch(){
        urlConnection connection = new urlConnection(this);
        connection.setNetListener(new urlConnection.NetListener(){

            @Override
            public void success(String result, int code) {
                Log.d(TAG, "onSuccess: "+result);
                map = parsetools.parseBookMessage(result);
                list = parsetools.parseSearchBookAvailabale(result);
                handler.sendEmptyMessage(0x1);
            }

            @Override
            public void failure(Exception e, String Error, int code) {
                Toast.makeText(searchBook.this,"失败，请重试",Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(0x2);
            }
        });
        connection.get(url);
//        RequestParams params = new RequestParams(url);
//        x.http().get(params,new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                ex.printStackTrace();
////                Log.e(TAG," Error response is "+ Error+"code is "+code);
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                handler.sendEmptyMessage(0x2);
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });




//        final urlConnection connection= new urlConnection();
//        connection.setgetLister(new urlConnection.getLister() {
//            @Override
//            public void success(String response, int code) {
//
//            }
//            @Override
//            public void failure(Exception e,String Error, int code) {
//
//            }
//        });
//        connection.get(url);
    }
    public void showDate(){
        findfooter();
        title = map.get("book");
        b.setText(title);
        editer = map.get("editer");
        e.setText(editer);
        bookMessage = map.get("bookMessage");
        bm.setText(bookMessage);
        listview.addHeaderView(v);
        listview.addFooterView(footer);
        adapter = new myExpandableListAdapter(searchBook.this,title,list);
        listview.setAdapter(adapter);
    }
    public void findfooter(){
        v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.library_search_list_header,null);
        footer = LayoutInflater.from(getApplicationContext()).inflate(R.layout.search_book_footer,null);
        b = (TextView)v.findViewById(R.id.library_search_book_message_book);
        e = (TextView)v.findViewById(R.id.library_search_book_message_editer);
        bm = (TextView)v.findViewById(R.id.library_search_book_message_bookMessage);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.search_back:
                finish();
                overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
                break;
            case R.id.browse_collect:
                showCollect();
                break;
        }
    }

    private void showCollect(){
        if(ischeck){
            Log.e(TAG,"is false");
            collect.setChecked(false);
        }
        else if(!ischeck){
            collect.setChecked(true);
        }
        ischeck = !ischeck;
    }
    public void changeView(){
        myprogress = new ProgressDialog(this,R.drawable.waiting);
        myprogress.show();
    }
    @Override
    public void onPause(){
        super.onPause();
        if(collect.isChecked()){
            helper.addCollect(title,editor,available,number,url);
        }
        else if (helper.hasURL(url)&&!collect.isChecked()){
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
