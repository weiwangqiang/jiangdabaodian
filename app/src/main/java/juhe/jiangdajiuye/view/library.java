package juhe.jiangdajiuye.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.SearchLibraryAdapter;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.consume.recyclerView.OnLoadMoreListener;
import juhe.jiangdajiuye.consume.recyclerView.mRecyclerView;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.tool.ProgressDialog;
import juhe.jiangdajiuye.tool.parseTools;
import juhe.jiangdajiuye.util.urlConnection;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by wangqiang on 2016/10/6.
 */
public class library extends BaseActivity implements
        Toolbar.OnMenuItemClickListener,OnLoadMoreListener {
    public EditText edit;
    private String TAG = "fragmentLibrary";
    private OkHttpClient mOkHttpClient ;
    public ExecutorService service;

    private int page = 1;
    private String title = "";
    private int totalPage = 0 ;//搜索返回的结果数
    public TextView search;
    private ProgressDialog myprogress;
    public mRecyclerView recyclerView;
    private LinearLayoutManager manager;
    private Toolbar toolbar;
    private InputMethodManager imm;
    private SearchLibraryAdapter adapter;
    private parseTools parsetools =  parseTools.getparseTool() ;
    public static String url = "http://huiwen.ujs.edu.cn:8080/opac/openlink.php?" +
            "location=ALL&doctype=ALL&lang_code=ALL&match_flag=forward" +
            "&displaypg=10&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_fragment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mOkHttpClient = new OkHttpClient();
        service = Executors.newFixedThreadPool (Runtime.getRuntime().availableProcessors());
        initView();
    }
    public void initView(){
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        findid();
        initList();
        initToolbar();
        setLister();
        bindNetState();
    }
    public void findid(){
        edit = (EditText) findViewById(R.id.library_edit);
        search = (TextView)findViewById(R.id.library_search);
        recyclerView = (mRecyclerView)findViewById(R.id.library_listView);
        toolbar = (Toolbar)findViewById(R.id.Library_toolbar);
    }
    public void initList(){
        adapter = new SearchLibraryAdapter(R.layout.library_listitem);
        manager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setmOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new SearchLibraryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Map<String,String> data) {
                    Intent intent = new Intent(library.this,searchBook.class);
                    intent.putExtra("url",data.get("url"));
                    intent.putExtra("book",data.get("book"));
                    intent.putExtra("editor",data.get("editor"));
                    intent.putExtra("available",data.get("available"));
                    intent.putExtra("number",data.get("number"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }
        });
    }
    /**
     * 绑定网络监听
     */
    public void bindNetState(){
        NetStateReceiver.addNetLister(new NetStateReceiver.NetLister() {
            @Override
            public void OutInternet() {
                Log.i(TAG, "OutInternet: ");
            }
            @Override
            public void GetInternet(int type) {
                Log.i(TAG, "GetInternet: ----------------");
                if(recyclerView != null){
                    if(recyclerView.getmStatus() == mRecyclerView.STATUS_ERROR){
                        Log.i(TAG, "GetInternet: set default value ");
                        recyclerView.setStatus(mRecyclerView.STATUS_DEFAULT);
                    }
                }
            }
        });
    }
    public void setLister(){
        search.setOnClickListener(this);
    }

    public void initToolbar(){
        toolbar.setTitle("图书馆");
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
                    uiutils.showToast("请输入搜索词");
                    return;
                }
                title = edit.getText().toString() ;
                recyclerView.setStatus(mRecyclerView.STATUS_PULLTOREFRESH);
                showProgress();
                getSearch();
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
        if(recyclerView.getmStatus() != mRecyclerView.STATUS_PULLTOREFRESH){
            if((page * 10)>=totalPage && adapter.getDataSize() != 0 ){
                recyclerView.setStatus(mRecyclerView.STATUS_END);
                uiutils.showToast("没有更多了");
                return;
            }
        }
        //解决中文乱码问题
        HttpUrl parsed = HttpUrl.parse(getUrl());
        final urlConnection connection= new urlConnection(this);
        connection.setNetListener(new urlConnection.NetListener() {
            @Override
            public void success(final String response, int code) {
                if(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH){
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
                recyclerView.setStatus(mRecyclerView.STATUS_ERROR);
                uiutils.showToast("网络不在服务区哦！请重试");
                myprogress.cancel();
            }
        });
        connection.get(parsed.toString());
    }
    public void showProgress(){
        myprogress = new ProgressDialog(this,R.drawable.waiting);
        myprogress.show();
    }
    public void upData(List<Map<String,String>> d){
        if(d.size()==0){
            if(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH){
                uiutils.showToast("没有你要找的书哦!");
                myprogress.cancel();
                recyclerView.setStatus(mRecyclerView.STATUS_DEFAULT);
            }else
                recyclerView.setStatus(mRecyclerView.STATUS_END);
            return;
        }
        if(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH){
            adapter.upDate(d);
            recyclerView.scrollToPosition(0);
        }
        else{
          adapter.appendDate(d);
        }
        page++;
        myprogress.cancel();
        if(totalPage != 0 && (recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH)
                && adapter.getDataSize() >= totalPage){
            recyclerView.setStatus(mRecyclerView.STATUS_END);
            return;
        }
         recyclerView.setStatus(mRecyclerView.STATUS_DEFAULT);
    }
    public String  getUrl(){
        String str = "";
        if(recyclerView.getmStatus() == mRecyclerView.STATUS_PULLTOREFRESH){
            page = 1 ;
        }
        str = url +"&page="+page+"&title="+title;
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

    @Override
    public void onLoadMore() {
        if(recyclerView.getmStatus() == mRecyclerView.STATUS_DEFAULT){
            recyclerView.setStatus(mRecyclerView.STATUS_REFRESHING);
            getSearch();

        }
    }
}
