package juhe.jiangdajiuye.view.activity.library;

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
import juhe.jiangdajiuye.adapter.LibraryAdapter;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.broadCast.NetStateReceiver;
import juhe.jiangdajiuye.ui.recyclerView.LoadMoreRecyclerView;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.net.httpUtils.HttpHelper;
import juhe.jiangdajiuye.net.httpUtils.inter.IDataListener;
import juhe.jiangdajiuye.net.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.view.dialog.ProgressDialog;

/**
 * Created by wangqiang on 2016/10/6.
 */
public class LibraryActivity extends BaseActivity implements
        Toolbar.OnMenuItemClickListener, LoadMoreRecyclerView.OnLoadMoreListener {
    private EditText edit;
    private String TAG = "fragmentLibrary";
    private int currentPage = 1;
    private int displaypg = 10;
    private int totalNum = 0;//搜索返回的结果数
    private String mTitle;
    private TextView search;
    private ProgressDialog mProgress;
    private LoadMoreRecyclerView recyclerView;
    private Toolbar toolbar;
    private InputMethodManager imm;
    private LibraryAdapter adapter;
    private HttpHelper httpHelper;
    private static String url = "http://huiwen.ujs.edu.cn:8080/opac/openlink.php?" +
            "location=ALL&doctype=ALL&lang_code=ALL&match_flag=forward" +
            "&displaypg=10&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no";
    private IDataListener iDataListener = new IDataListener<List<BookBean>>() {

        @Override
        public void onSuccess(List<BookBean> bookBeans) {
            upData(bookBeans);
            if (bookBeans.size() != 0) {
                totalNum = bookBeans.get(0).getTotalNum(); //获取搜索结果的数量
            }
            currentPage++;
            mProgress.cancel();
        }

        @Override
        public void onFail(Exception exception, int responseCode) {
            recyclerView.setErrorStatus();
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
            mProgress.cancel();
        }
    };

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
        edit = findViewById(R.id.library_edit);
        search = findViewById(R.id.library_search);
        recyclerView = findViewById(R.id.library_listView);
        toolbar = findViewById(R.id.Library_toolbar);
    }

    public void initList() {
        adapter = new LibraryAdapter(this, R.layout.library_listitem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new AbsAdapter.OnItemClickListener<BookBean>() {

            @Override
            public void onClick(View view, BookBean bookBean, int position) {
                Intent intent = new Intent(
                        LibraryActivity.this,
                        LibraryDetailsActivity.class);
                intent.putExtra("url", bookBean.getUrl());
                intent.putExtra("book", bookBean.getBook());
                intent.putExtra("editor", bookBean.getEditor());
                intent.putExtra("available", bookBean.getAvailable());
                intent.putExtra("number", bookBean.getNumber());
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
                        prepareSearch(v);
                        return true;
                    default:
                        return false;
                }
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
                    if (recyclerView.isErrorStatus()) {
                        recyclerView.setDefaultStatus();
                    }
                }
            }
        });
    }

    public void setLister() {
        search.setOnClickListener(this);
        httpHelper = HttpHelper.getInstance();
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
        if (recyclerView.isRefreshing()) {
            //处于获取数据状态
            return;
        }
        mTitle = edit.getText().toString();
        recyclerView.setPullUpToRefresh();
        showProgress();
        recyclerView.setPullDownToRefresh();
        searchBook();
    }

    public void searchBook() {
        if (recyclerView.isPullUpToRefresh()) {
            if (adapter.getDataSize() >= totalNum) {
                recyclerView.setCanLoadMoreRefresh(false);
                return;
            }
        }
        //解决中文乱码问题
//        HttpUrl parsed = HttpUrl.parse(getUrl());
        httpHelper.get(getUrl(), null, iDataListener, HttpTask.Type.book);
    }

    //显示进度条
    public void showProgress() {
        if (null == mProgress) {
            mProgress = new ProgressDialog(this);
        }
        mProgress.show();
    }

    //更新当前数据
    public void upData(List<BookBean> data) {
        if (data.size() == 0) {
            if (recyclerView.isPullDownToRefresh()) {
                ToastUtils.showToast(ResourceUtils.getString(R.string.toast_library_can_not_search_book));
                recyclerView.setDefaultStatus();
            } else {
                recyclerView.setCanLoadMoreRefresh(false);
            }
            return;
        }
        if (recyclerView.isPullDownToRefresh()) {
            adapter.upDate(data);
            recyclerView.scrollToPosition(0);
        } else {
            adapter.appendData(data);
        }
        if (totalNum != 0 && (recyclerView.isPullDownToRefresh())
                && adapter.getDataSize() >= totalNum) {
            recyclerView.setCanLoadMoreRefresh(false);
            return;
        }
        recyclerView.setDefaultStatus();
    }

    public String getUrl() {
        if (recyclerView.isPullDownToRefresh()) {
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
        Intent intent = new Intent(this, LibraryCollectActivity.class);
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
        if (!recyclerView.isRefreshing()) {
            recyclerView.setPullUpToRefresh();
            searchBook();
        }
    }
}
