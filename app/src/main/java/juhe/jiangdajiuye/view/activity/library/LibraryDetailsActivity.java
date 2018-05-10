package juhe.jiangdajiuye.view.activity.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.BookDetailsAdapter;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.bean.BookMesBean;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.view.dialog.ProgressDialog;
import juhe.jiangdajiuye.db.repository.LibraryRepository;
import juhe.jiangdajiuye.net.httpUtils.HttpHelper;
import juhe.jiangdajiuye.net.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.net.httpUtils.inter.IDataListener;
import juhe.jiangdajiuye.utils.JiangDaParseUtils;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;

/**
 * 图书详情界面
 * <p>
 * Created by wangqiang on 2016/10/2.
 */
public class LibraryDetails extends BaseActivity {

    private String TAG = "LibraryDetails";
    private String title, editer, bookMessage;
    private Button back;
    private TextView titleTextView, editorTextView, bookMesTextView;
    private View headView, footerView;
    private RadioButton collect;
    private Boolean isCheck = false;
    private ExpandableListView mListView;
    private BookDetailsAdapter adapter;
    private ProgressDialog myProgress;
    private BookBean bookBean;
    private BookMesBean bookMesBean;
    public static List<List<String>> list = new ArrayList<>();
    private JiangDaParseUtils parseTools = JiangDaParseUtils.getInstance();
    private LibraryRepository libraryRepository;
    private HttpHelper httpHelper;

    private IDataListener<String> dataListener = new IDataListener<String>() {
        @Override
        public void onSuccess(String s) {
            bookMesBean = parseTools.parseBookMessage(s);
            list = parseTools.parseSearchBookAvailable(s);
            showDate();
            myProgress.cancel();
        }

        @Override
        public void onFail(Exception exception, int responseCode) {
            ToastUtils.showToast(ResourceUtils.getString(R.string.error_try_again));
            myProgress.cancel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book);
        libraryRepository = LibraryRepository.getInstance();
        getParam();
        initView();

    }

    public void initView() {
        findId();
        changeView();
        httpHelper = HttpHelper.getInstance();
        httpHelper.get(bookBean.getUrl(), null, dataListener, HttpTask.Type.string);

    }

    public void findId() {
        back = (Button) findViewById(R.id.search_back);
        mListView = (ExpandableListView) findViewById(R.id.search_list);
        collect = (RadioButton) findViewById(R.id.browse_collect);

        back.setOnClickListener(this);
        collect.setOnClickListener(this);

        isCheck = libraryRepository.hasURL(bookBean.getUrl());
        collect.setChecked(isCheck);
    }

    /**
     * 获取参数信息
     */
    public void getParam() {
        Intent intent = getIntent();
        bookBean = new BookBean();
        bookBean.setBook(intent.getStringExtra("book"));
        bookBean.setUrl(intent.getStringExtra("url"));
        bookBean.setEditor(intent.getStringExtra("editor"));
        bookBean.setAvailable(intent.getStringExtra("available"));
        bookBean.setNumber(intent.getStringExtra("number"));
    }

    public void showDate() {
        findFooter();
        title = bookMesBean.getBook();
        titleTextView.setText(title);
        editer = bookMesBean.getAuthor();
        editorTextView.setText(editer);
        bookMessage = bookMesBean.getBookMessage();
        bookMesTextView.setText(bookMessage);
        mListView.addHeaderView(headView);
        mListView.addFooterView(footerView);
        adapter = new BookDetailsAdapter(LibraryDetails.this, title, list);
        mListView.setAdapter(adapter);
    }

    public void findFooter() {
        headView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.library_search_list_header, null);
        footerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.search_book_footer, null);
        titleTextView = headView.findViewById(R.id.library_search_book_message_book);
        editorTextView = headView.findViewById(R.id.library_search_book_message_editer);
        bookMesTextView =  headView.findViewById(R.id.library_search_book_message_bookMessage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
                break;
            case R.id.browse_collect:
                showCollect();
                break;
        }
    }

    private void showCollect() {
        collect.setChecked(!isCheck);
        isCheck = !isCheck;
    }

    public void changeView() {
        myProgress = new ProgressDialog(this);
        myProgress.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        boolean containUrl = libraryRepository.hasURL(bookBean.getUrl());
        if (!containUrl && collect.isChecked()) {
            libraryRepository.add(bookBean);
        } else if (containUrl && !collect.isChecked()) {
            //取消收藏
            libraryRepository.delete(bookBean);
        }
    }
}
