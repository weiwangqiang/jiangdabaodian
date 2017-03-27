package juhe.jiangdajiuye.view;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import juhe.jiangdajiuye.InterFace.myitemLister;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.CollectRceAdapter;
import juhe.jiangdajiuye.sql.CollectSqlHelper;

/**
 * Created by wangqiang on 2016/10/6.
 */
public class collect extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "fragmentCollect";
    private View view;
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
        adapter.setLister(new myitemLister() {
            @Override
            public void ItemLister(int position) {
                String url = date.get(position).get("url").toString();
                Intent intent = new Intent(collect.this,browse.class);
                intent.putExtra("url",url);
                intent.putExtra("from",3);
                intent.putExtra("title",date.get(position).get("title"));
                intent.putExtra("company",date.get(position).get("company"));
                intent.putExtra("location",date.get(position).get("place"));
                intent.putExtra("time",date.get(position).get("time"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.hold);
            }
        });
    }
    public void initToolbar(){
        toolbar.setTitle("收藏栏");
//        toolbar.setNavigationIcon(R.drawable.menue);
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
        Log.e(TAG,"init date first is false");
        date.clear();
        date = helper.selectAll();
//        for(int i =0;i<date.size();i++){
//            Log.e(TAG," title is "+date.get(i).get("title")+
//                    " location is "+date.get(i).get("location ")+
//                    "time is "+date.get(i).get("time ")+
//                    " url is "+date.get(i).get("url ")+
//                    " company is "+ date.get(i).get("company") );
//        }
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
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
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
