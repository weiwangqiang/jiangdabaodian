package juhe.jiangdajiuye.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.MyExpandableListAdapter;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.bean.BookMesBean;
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.dialog.ProgressDialog;
import juhe.jiangdajiuye.sql.repository.LibraryRepository;
import juhe.jiangdajiuye.util.ParseUtils;
import juhe.jiangdajiuye.util.HttpConnection;
import juhe.jiangdajiuye.util.ResourceUtils;

/** 搜索图书界面
 * Created by wangqiang on 2016/10/2.
 */
public class SearchBook extends BaseActivity{
    private final int UPDATE_AND_CANCEL = 0x10 ;
    private final int CANCEL = 0x20 ;
    private Button back;
    private TextView titleTextView,editerTextView,bookMesTextView;
    private View v,footer;
    private RadioButton collect;
    private Boolean ischeck = false;
    private ExpandableListView listview;
    private MyExpandableListAdapter adapter;
    private ProgressDialog myprogress;
    private String TAG = "SearchBook";
    private String title = "",editer = "",bookMessage = "";
    private BookBean bookBean ;
    private BookMesBean bookMesBean ;
    public static List<List<String>> list = new ArrayList<>();
    private ParseUtils parseTools =  ParseUtils.getInstance() ;
    private LibraryRepository libraryRepository ;

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case UPDATE_AND_CANCEL:
                    showDate();
                    myprogress.cancel();
                    break;
                case CANCEL:
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
        libraryRepository = LibraryRepository.getInstance() ;
        getParam();
        initView();
    }
    public void initView(){
        findId();
        changeView();
        getSearch();
    }
    public void findId(){
        back = (Button)findViewById(R.id.search_back);
        listview = (ExpandableListView)findViewById(R.id.search_list);
        collect = (RadioButton)findViewById(R.id.browse_collect);

        back.setOnClickListener(this);
        collect.setOnClickListener(this);

        ischeck = libraryRepository.hasURL(bookBean.getUrl());
        collect.setChecked(ischeck);
    }
    /**
     * 获取参数信息
     */
    public void getParam(){
        Intent intent = getIntent();
        bookBean = new BookBean();
        bookBean.setBook(intent.getStringExtra("book"));
        bookBean.setUrl(intent.getStringExtra("url"));
        bookBean.setEditor(intent.getStringExtra("editor"));
        bookBean.setAvailable(intent.getStringExtra("available"));
        bookBean.setNumber(intent.getStringExtra("number"));
    }
    public void getSearch(){
        HttpConnection connection = new HttpConnection(this);
        connection.setNetListener(new HttpConnection.NetListener(){

            @Override
            public void success(List<MessageItem> data, int code) {

            }

            @Override
            public void success(String result, int code) {
                bookMesBean = parseTools.parseBookMessage(result);
                list = parseTools.parseSearchBookAvailable(result);
                handler.sendEmptyMessage(UPDATE_AND_CANCEL);
            }

            @Override
            public void failure(Exception e, String Error, int code) {
                Toast.makeText(SearchBook.this, ResourceUtils.getString(R.string.error_try_again),Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(CANCEL);
            }
        });
        connection.get(bookBean.getUrl());
    }
    public void showDate(){
        findFooter();
        title = bookMesBean.getBook();
        titleTextView.setText(title);
        editer = bookMesBean.getAuthor();
        editerTextView.setText(editer);
        bookMessage = bookMesBean.getBookMessage();
        bookMesTextView.setText(bookMessage);
        listview.addHeaderView(v);
        listview.addFooterView(footer);
        adapter = new MyExpandableListAdapter(SearchBook.this,title,list);
        listview.setAdapter(adapter);
    }
    public void findFooter(){
        v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.library_search_list_header,null);
        footer = LayoutInflater.from(getApplicationContext()).inflate(R.layout.search_book_footer,null);
        titleTextView = (TextView)v.findViewById(R.id.library_search_book_message_book);
        editerTextView = (TextView)v.findViewById(R.id.library_search_book_message_editer);
        bookMesTextView = (TextView)v.findViewById(R.id.library_search_book_message_bookMessage);
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
        boolean containUrl = libraryRepository.hasURL(bookBean.getUrl()) ;
        if(!containUrl&&collect.isChecked()){
            libraryRepository.add(bookBean);
        }else if (containUrl&&!collect.isChecked()){
            //取消收藏
            libraryRepository.delete(bookBean);
        }
    }
}
