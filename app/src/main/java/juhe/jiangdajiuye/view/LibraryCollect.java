package juhe.jiangdajiuye.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import juhe.jiangdajiuye.InterFace.MyItemLister;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.LibraryCollectAdapter;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.consume.LibraryRecyclerView;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.sql.repository.LibraryRepository;

/**
 * Created by wangqiang on 2016/10/6.
 */
public class LibraryCollect extends BaseActivity {
    private String TAG = "LibraryCollect";
    private Boolean first = true;
    public LibraryRecyclerView recyclerView;
    private Toolbar toolbar;
    private View nothing;
    private LinearLayoutManager manager;
    public  LibraryCollectAdapter adapter;
    private LibraryRepository libraryRepository ;
    private ArrayList<BookBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_fragment);
       initView();
    }

    public void initView(){
        libraryRepository = LibraryRepository.getInstance() ;
        findId();
        initList();
        initToolbar();
    }
    public void findId(){
        recyclerView = (LibraryRecyclerView)findViewById(R.id.Collect_listView);
        toolbar = (Toolbar)findViewById(R.id.Collect_toolbar);
        nothing = findViewById(R.id.nothing);
    }
    public void initList(){
        initDate();
        adapter = new LibraryCollectAdapter(this,R.layout.library_listitem,data);
        manager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setLister(new MyItemLister() {
            @Override
            public void ItemLister(int position) {
                Intent intent = new Intent(LibraryCollect.this,SearchBook.class);
                intent.putExtra("url",data.get(position).getUrl());
//                intent.putExtra("from",2);
                intent.putExtra("book",data.get(position).getBook());
                intent.putExtra("editor",data.get(position).getEditor());
                intent.putExtra("available",data.get(position).getAvailable());
                intent.putExtra("number",data.get(position).getNumber());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.hold);
            }
        });
    }
    public void initToolbar(){
        toolbar.setTitle("图书收藏");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    public void initDate(){
        data.clear();
        data = libraryRepository.selectAll();
        first = false;
        if(data.size()==0){
            nothing.setVisibility(View.VISIBLE);
        }
        else
            nothing.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                 finish();
                overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        if(first){
            initList();
        }else
        {
            initDate();
            adapter.RefreshDate(data);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }
}
