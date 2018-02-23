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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.SearchLibraryAdapter;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.consume.recyclerView.MyRecyclerView;
import juhe.jiangdajiuye.consume.recyclerView.OnLoadMoreListener;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.dialog.ProgressDialog;
import juhe.jiangdajiuye.util.HttpManager;
import juhe.jiangdajiuye.util.ParseUtils;
import juhe.jiangdajiuye.util.ResourceUtils;
import juhe.jiangdajiuye.util.ToastUtils;

/**
 * Created by wangqiang on 2016/10/6.
 */
public class Library extends BaseActivity implements
        Toolbar.OnMenuItemClickListener, OnLoadMoreListener {
    public EditText edit;
    private String TAG = "fragmentLibrary";
    public ExecutorService service;

    private int mPage = 1;
    private String mTitle = "";
    private int totalPage = 0;//搜索返回的结果数
    public TextView search;
    private ProgressDialog mProgress;
    public MyRecyclerView recyclerView;
    private LinearLayoutManager manager;
    private Toolbar toolbar;
    private InputMethodManager imm;
    private SearchLibraryAdapter adapter;
    private HttpManager connection;
    private ParseUtils parseTools = ParseUtils.getInstance();
    public static String url = "http://huiwen.ujs.edu.cn:8080/opac/openlink.php?" +
            "location=ALL&doctype=ALL&lang_code=ALL&match_flag=forward" +
            "&displaypg=10&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_fragment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
        edit = (EditText) findViewById(R.id.library_edit);
        search = (TextView) findViewById(R.id.library_search);
        recyclerView = (MyRecyclerView) findViewById(R.id.library_listView);
        toolbar = (Toolbar) findViewById(R.id.Library_toolbar);
    }

    public void initList() {
        adapter = new SearchLibraryAdapter(this, R.layout.library_listitem);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new SearchLibraryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(BookBean data) {
                Intent intent = new Intent(Library.this, SearchBook.class);
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
                switch (actionId){
                    case EditorInfo.IME_ACTION_SEND:
                    case EditorInfo.IME_ACTION_SEARCH:
                    case EditorInfo.IME_ACTION_GO:
                        prepareSearch(v);
                        break;
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
        connection = new HttpManager(this);
        connection.setNetListener(new HttpManager.NetListener() {
            @Override
            public void success(List<MessageItem> data, int code) {

            }

            @Override
            public void success(final String response, int code) {
                if (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH) {
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            totalPage = parseTools.parseSearchNumber(response);
                        }
                    });
                }
                upData(parseTools.parseSearch(response));
            }

            @Override
            public void failure(Exception e, String Error, int code) {
                e.printStackTrace();
                recyclerView.setStatus(MyRecyclerView.STATUS_ERROR);
                ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
                mProgress.cancel();
            }
        });
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
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_library_input_warn));
            return;
        }
        mTitle = edit.getText().toString();
        recyclerView.setStatus(MyRecyclerView.STATUS_PULL_TO_REFRESH);
        showProgress();
        searchBook();
    }

    public void searchBook() {
        if (recyclerView.getStatus() != MyRecyclerView.STATUS_PULL_TO_REFRESH) {
            if ((mPage * 10) >= totalPage && adapter.getDataSize() != 0) {
                recyclerView.setStatus(MyRecyclerView.STATUS_END);
                ToastUtils.showToast(ResourceUtils.getString(R.string.toast_library_content_not_more));
                return;
            }
        }
        //解决中文乱码问题
//        HttpUrl parsed = HttpUrl.parse(getUrl());

        connection.get(getUrl());
    }

    public void showProgress() {
        mProgress = new ProgressDialog(this, R.drawable.waiting);
        mProgress.show();
    }

    public void upData(List<BookBean> d) {
        if (d.size() == 0) {
            if (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH) {
                ToastUtils.showToast(ResourceUtils.getString(R.string.toast_library_can_not_search_book));
                mProgress.cancel();
                recyclerView.setStatus(MyRecyclerView.STATUS_DEFAULT);
            } else
                recyclerView.setStatus(MyRecyclerView.STATUS_END);
            return;
        }
        if (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH) {
            adapter.upDate(d);
            recyclerView.scrollToPosition(0);
        } else {
            adapter.appendDate(d);
        }
        mPage++;
        mProgress.cancel();
        if (totalPage != 0 && (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH)
                && adapter.getDataSize() >= totalPage) {
            recyclerView.setStatus(MyRecyclerView.STATUS_END);
            return;
        }
        recyclerView.setStatus(MyRecyclerView.STATUS_DEFAULT);
    }

    public String getUrl() {
        if (recyclerView.getStatus() == MyRecyclerView.STATUS_PULL_TO_REFRESH) {
            mPage = 1;
        }
        return url + "&page=" + mPage + "&title=" + mTitle;
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
