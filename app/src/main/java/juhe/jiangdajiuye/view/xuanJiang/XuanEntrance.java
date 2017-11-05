package juhe.jiangdajiuye.view.xuanJiang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.view.xuanJiang.constant.XuanEntranceData;

public class XuanEntrance extends AppCompatActivity {
    ListView listView ;
    SimpleAdapter adapter ;
    XuanEntranceData data ;
    private Toolbar toolbar;
    private String TAG = "XuanEntrance" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuan_entrance);
        initView();
    }

    private void initView() {
        findId();
        toolbar.setTitle("宣讲省份");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        data = new XuanEntranceData();
        adapter = new SimpleAdapter(this,data.getData(),
                R.layout.activity_xuan_entrance_item,
                new String[]{XuanEntranceData.province},
                new int[]{R.id.entrance_text});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int provinceId = (int) data.getData().get(position).get("provinceId");
                Log.i(TAG, "onItemClick: "+provinceId);
                Intent intent = new Intent(XuanEntrance.this,xuanjiang.class);
                intent.putExtra("provinceId",provinceId);
                startActivity(intent);
            }
        });
    }

    private void findId() {
        listView = (ListView) findViewById(R.id.xuan_entrance_listView);
        toolbar = (Toolbar)findViewById(R.id.xuan_entrance_toolbar);
    }
}
