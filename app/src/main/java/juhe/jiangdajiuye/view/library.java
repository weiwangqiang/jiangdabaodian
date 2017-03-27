package juhe.jiangdajiuye.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import juhe.jiangdajiuye.InterFace.myitemLister;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.ly_recyclerAdapter;
import juhe.jiangdajiuye.tool.ProgressDialog;
import juhe.jiangdajiuye.tool.parseTools;
import juhe.jiangdajiuye.util.urlConnection;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import static juhe.jiangdajiuye.R.id.toolbar;

/**
 * Created by wangqiang on 2016/10/6.
 */
public class library extends AppCompatActivity  implements Toolbar.OnMenuItemClickListener,View.OnClickListener{
    private View view,main;
    public EditText edit;
    private String TAG = "fragmentLibrary";
    private OkHttpClient mOkHttpClient ;
    public ExecutorService service;

    //下拉刷新
    private Boolean isSearch = false;//是否是重新搜索
    private Boolean refreshing = false;
    private Boolean isfirst = true;//第一次加载数据
    private int page = 1;
    private int totalPage = 0 ;//搜索返回的结果数
    public TextView search;
    private ProgressDialog myprogress;
    public RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private Toolbar toolbar;
    private InputMethodManager imm;
    private ly_recyclerAdapter adapter;
    private parseTools parsetools =  parseTools.getparseTool() ;
    public static String url = "http://huiwen.ujs.edu.cn:8080/opac/openlink.php?" +
            "location=ALL&doctype=ALL&lang_code=ALL&match_flag=forward" +
            "&displaypg=10&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no";

    private ArrayList<Map<String,String>> data = new ArrayList<>();

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 0x1:
                    if(isfirst){
                        recyclerView.setAdapter(adapter);
                        isfirst = false;
                    }
                    adapter.RefreshDate(data);
                    if(isSearch){
                        isSearch = false;
                        try{
                            recyclerView.scrollToPosition(0);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    refreshing = false;
                    myprogress.cancel();
                    break;
                case 0x2:
                    refreshing = false;
                    myprogress.cancel();
                    break;
                case 0x3:
                    Toast.makeText(library.this,"没有你要找的书哦！",0).show();
                    refreshing = false;
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
        setContentView(R.layout.library_fragment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mOkHttpClient = new OkHttpClient();
        service = Executors.newFixedThreadPool (4);//线程池50
        initView();
    }
    public void initView(){
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        findid();
        initlist();
        initToolbar();
//        initTabLayout();
        setLister();
    }
    public void findid(){
        edit = (EditText) findViewById(R.id.library_edit);
        search = (TextView)findViewById(R.id.library_search);
        recyclerView = (RecyclerView)findViewById(R.id.library_listView);
        toolbar = (Toolbar)findViewById(R.id.Library_toolbar);
    }
    public void initlist(){
        adapter = new ly_recyclerAdapter(this,R.layout.library_listitem,data);
        manager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter.setLister(new myitemLister() {
            @Override
            public void ItemLister(int position) {
                if(position<data.size()){
                    Intent intent = new Intent(library.this,searchBook.class);
                    intent.putExtra("url",data.get(position).get("url"));
                    intent.putExtra("book",data.get(position).get("book"));
                    intent.putExtra("editor",data.get(position).get("editor"));
                    intent.putExtra("available",data.get(position).get("available"));
                    intent.putExtra("number",data.get(position).get("number"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.hold);

                }
            }
        });
    }
    public void setLister(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // dy>0 代表向下滚动
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                int itemCount = manager.getItemCount();
                // 如果最后一个可见的View的position 等于 itemCount-1 代表滚动到底部
                if((itemCount-1)==lastVisibleItemPosition){
                    getSearch();
                }
            }
        });
        search.setOnClickListener(this);
    }

    public void initToolbar(){
        toolbar.setTitle("图书馆");
//        toolbar.setNavigationIcon(R.drawable.menue);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.library_search:
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                if(edit.getText().length()==0)
                {
                    Toast.makeText(this,"请输入搜索词",Toast.LENGTH_SHORT).show();
                    return;
                }
                page=1;
                isSearch = true;
                changeView();
                getSearch();
//                getMessage();
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getSearch(){
        Log.e(TAG,"Current page is "+page + "totalPage is "+totalPage);
        if(refreshing) {
            Log.e(TAG," is refreshing ");
            return;
        }
        if(!isSearch&& (page * 10)>=totalPage){
            Toast.makeText(this,"没有更多了",Toast.LENGTH_SHORT).show();
            return;
        }
        refreshing = true;
        //解决中文乱码问题
        HttpUrl parsed = HttpUrl.parse(getUrl());
        final urlConnection connection= new urlConnection();
        connection.setgetLister(new urlConnection.getLister() {
            @Override
            public void success(final String response, int code) {
                if(isSearch){
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            totalPage = parsetools.parseSearchNumber(response);
                        }
                    });
                }
                upData(parsetools.parseSearch(response));
            }
            @Override
            public void failure(Exception e,String Error, int code) {
                e.printStackTrace();
                Log.e(TAG," Error response is "+ Error+"code is "+code);
                handler.sendEmptyMessage(0x2);
            }
        });
        connection.get(parsed.toString());
    }
    public void changeView(){
        myprogress = new ProgressDialog(this,R.drawable.waiting);
        myprogress.show();
    }
    public void upData(ArrayList<Map<String,String>> d){
        Log.e(TAG,"response list size is "+ d.size());
        if(d.size()==0){
            handler.sendEmptyMessage(0x3);
            return ;
        }
        if(isSearch){
            data.clear();
            data.addAll(d);
        }
        else{
            for(int i = 0;i<d.size();i++){
                data.add(d.get(i));
            }
        }
        handler.sendEmptyMessage(0x1);
        Log.e(TAG," lis size is "+data.size());
    }
    public String  getUrl(){
        String str = "";
        if(isSearch){
            str = url +"&page="+page+"&title="+edit.getText().toString();
        }else
        {
            page++;
            str = url +"&page="+page+"&title="+edit.getText().toString();
        }
        return str;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.librarymian, menu);
        return true;
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.library_collect:
                to();
                break;
            default:
                break;
        }
        return false;
    }
    public void to(){
        Intent intent =  new Intent(this,Librarycollect.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);

    }
    @Override
    public void onResume(){
        super.onResume();
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * This method will be invoked when a menu item is clicked if the item itself did
     * not already handle the event.
     *
     * @param item {@link MenuItem} that was clicked
     * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
     */

}
