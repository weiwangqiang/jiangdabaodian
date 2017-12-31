package juhe.jiangdajiuye.view;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import juhe.jiangdajiuye.InterFace.MyItemLister;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.CollectRceAdapter;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.sql.CollectSqlHelper;

/**
 * Created by wangqiang on 2016/10/6.
 * 收藏夹
 */
public class Collect extends BaseActivity {
    private String TAG = "fragmentCollect";
    private Boolean first = true;
    public RecyclerView recyclerView;
    private Toolbar toolbar;
    private View nothing;
    private LinearLayoutManager manager;
    public CollectRceAdapter adapter;
    private CollectSqlHelper helper;
    private SQLiteDatabase sqLiteDatabase;
    public ArrayList<HashMap<String,String>> date = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_fragment);
       initView();
    }

    public void initView(){
        helper = new CollectSqlHelper(this);
        sqLiteDatabase = helper.getReadableDatabase();
        helper.setSQL(sqLiteDatabase);
        findid();
        initlist();
        initToolbar();
    }
    public void findid(){
        recyclerView = (RecyclerView)findViewById(R.id.Collect_listView);
        toolbar = (Toolbar)findViewById(R.id.Collect_toolbar);
        nothing = findViewById(R.id.nothing);
    }
    public void initlist(){
        initDate();
        adapter = new CollectRceAdapter(this,R.layout.main_list_item,date);
        manager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setLister(new MyItemLister() {
            @Override
            public void ItemLister(int position) {
                String url = date.get(position).get("url").toString();
                MessageItem item = new MessageItem();
                item.setUrl(url);
                item.setTitle(date.get(position).get(MessageItem.keyVal.title));
                item.setFrom(date.get(position).get(MessageItem.keyVal.from));
                item.setLocate(date.get(position).get(MessageItem.keyVal.locate));
                item.setTime(date.get(position).get(MessageItem.keyVal.time));
                Browse.StartActivity(Collect.this,item);
            }
        });
    }
    public void initToolbar(){
        toolbar.setTitle("收藏栏");
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
        date.clear();
        date = helper.selectAll();
        first = false;
        if(date.size()==0){
            nothing.setVisibility(View.VISIBLE);
        }
        else
            nothing.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
//                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                 finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        if(first){
            initlist();
        }else
        {
            initDate();
            adapter.RefreshDate(date);
        }
    }

}
