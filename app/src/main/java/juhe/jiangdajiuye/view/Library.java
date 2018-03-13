package juhe.jiangdajiuye.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.consume.recyclerView.MyRecyclerView;
import juhe.jiangdajiuye.consume.recyclerView.OnLoadMoreListener;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.utils.httpUtils.HttpHelper;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IDataListener;
import juhe.jiangdajiuye.utils.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.view.adapter.SearchLibraryAdapter;
import juhe.jiangdajiuye.view.dialog.ProgressDialog;

/**
 * Created by wangqiang on 2016/10/6.
 */
public class Library extends BaseActivity implements
        Toolbar.OnMenuItemClickListener, OnLoadMoreListener {
    private EditText edit;
    private String TAG = "fragmentLibrary";
    private int currentPage = 1;
    private int totalNum = 0;//搜索返回的结果数
    private String mTitle;
    private TextView search;
    private ProgressDialog mProgress;
    private MyRecyclerView recyclerView;
    private Toolbar toolbar;
    private InputMethodManager imm;
    private SearchLibraryAdapter adapter;
    private HttpHelper httpHelper ;
    private static String url = "http://huiwen.ujs.edu.cn:8080/opac/openlink.php?" +
            "location=ALL&doctype=ALL&lang_code=ALL&match_flag=forward" +
            "&displaypg=10&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no";
    private IDataListener iDataListener = new IDataListener<List<BookBean>>() {

        @Override
        public void onSuccess(List<BookBean> bookBeans) {
            upData(bookBeans);
            currentPage++;
            mProgress.cancel();
        }

        @Override
        public void onFail(Exception exception, int responseCode) {
            recyclerView.setStatus(MyRecyclerView.STATUS_DEFAULT);
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
            mProgress.cancel();
        }
    } ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_fragment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    public void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        findId();
        initList();
        initToolbar();
        setLister();
        bindNetState();
    }

    public void findId() {
        edit =  findViewById(R.id.library_edit);
        search = findViewById(R.id.library_search);
        recyclerView = (MyRecyclerView) findViewById(R.id.library_listView);
        toolbar = (Toolbar) findViewById(R.id.Library_toolbar);
    }

    public void initList() {
        adapter = new SearchLibraryAdapter(this, R.layout.library_listitem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new SearchLibraryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(BookBean data) {
                Intent intent = new Intent(Library.this, LibraryDetails.class);
                intent.putExtra("url", data.getUrl());
                intent.putExtra("book", data.getBook());
                intent.putExtra("editor", data.getEditor());
                intent.putExtra("available", data.getAvailable());
                intent.putExtra("number", data.getNumber());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }
        });
        //设置editText支持输入法搜索
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEND:
                    case EditorInfo.IME_ACTION_NEXT:
                    case EditorInfo.IME_ACTION_SEARCH:
                    case EditorInfo.IME_ACTION_GO:
                        Log.i(TAG, "onEditorAction: action id ");
                        prepareSearch(v);
                        return true ;
                    default:
                        break;
                }
                if (event == null) {
                    return false;
                }
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.ACTION_DOWN:
                        prepareSearch(v);
                        Log.i(TAG, "onEditorAction: key code ");
                        return true  ;
                }
                return false;
            }
        });
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
                if (recyclerView != null) {
                    if (recyclerView.getStatus() == MyRecyclerView.STATUS_ERROR) {
                        recyclerView.setStatus(MyRecyclerView.STATUS_DEFAULT);
                    }
                }
            }
        });
    }

    public void setLister() {
        search.setOnClickListener(this);
        httpHelper = HttpHelper.getInstance() ;
    }

    public void initToolbar() {
        toolbar.setTitle(ResourceUtils.getString(R.string.title_library));
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.library_search:
                prepareSearch(view);
                break;
            default:
                break;
        }
    }

    private void prepareSearch(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        if (edit.getText().length() == 0) {
            //数据为null
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_library_input_warn));
            return;
        }
        if(recyclerView.isRefresh()){
            //处于获取数据状态
            return;
        }
        mTitle = edit.getText().toString();
        recyclerView.setStatus(MyRecyclerView.STATUS_PULL_TO_REFRESH);
        showProgress();
        searchBook();
    }

    public void searchBook() {
        if (recyclerView.getStatus() != MyRecyclerView.STATUS_PULL_TO_REFRESH) {
            if ((currentPage * 10) >= totalNum && adapter.getDataSize() != 0) {
                recyclerView.setStatus(MyRecyclerView.STATUS_END);
                ToastUtils.showToast(ResourceUtils.getString(R.string.toast_library_content_not_more));
                return;
            }
        }
        //解决中文乱码问题
//        HttpUrl parsed = HttpUrl.parse(getUrl());
        Log.i(TAG, "searchBook: "+getUrl());
        httpHelper.get(getUrl(),null,iDataListener, HttpTask.Type.book);
    }
    //显示进度条
    public void showProgress() {
        if(null == mProgress){
            mProgress = new ProgressDialog(this, R.drawable.waiting);
        }
        mProgress.show();
    }
    //更新当前数据
    public void upData(List<BookBean> data) {
        Log.i(TAG, "upData: "+data);
        if (data.size() == 0) {
            if (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH) {
                ToastUtils.showToast(ResourceUtils.getString(R.string.toast_library_can_not_search_book));
                recyclerView.setStatus(MyRecyclerView.STATUS_DEFAULT);
            } else{
                recyclerView.setStatus(MyRecyclerView.STATUS_END);
            }
            return;
        }
        totalNum = data.get(0).getTotalNum() ; //获取搜索结果的数量
        Log.i(TAG, "upData: "+totalNum);
        if (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH) {
            adapter.upDate(data);
            recyclerView.scrollToPosition(0);
        } else {
            adapter.appendDate(data);
        }
        if (totalNum != 0 && (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH)
                && adapter.getDataSize() >= totalNum) {
            recyclerView.setStatus(MyRecyclerView.STATUS_END);
            Log.i(TAG, "upData: set end ");
            return;
        }
        Log.i(TAG, "upData: set STATUS_DEFAULT");
        recyclerView.setStatus(MyRecyclerView.STATUS_DEFAULT);
    }

    public String getUrl() {
        if (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH) {
            currentPage = 1;
        }
        return url + "&page=" + currentPage + "&title=" + mTitle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.librarymian, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.library_collect:
                to();
                break;
            default:
                break;
        }
        return false;
    }

    public void to() {
        Intent intent = new Intent(this, LibraryCollect.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);

    }

    @Override
    public void onResume() {
        super.onResume();
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0); //强制隐藏键盘
    }

    @Override
    public void onLoadMore() {
        if (recyclerView.getStatus() == MyRecyclerView.STATUS_DEFAULT) {
            recyclerView.setStatus(MyRecyclerView.STATUS_REFRESHING);
            searchBook();
        }
    }
}
