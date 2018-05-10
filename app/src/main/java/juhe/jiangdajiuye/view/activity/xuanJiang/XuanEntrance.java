package juhe.jiangdajiuye.view.xuanJiang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.view.xuanJiang.constant.XuanEntranceData;

/**
 * 宣讲会入口
 */
public class XuanEntrance extends BaseActivity {
    ListView listView ;
    SimpleAdapter adapter ;
    XuanEntranceData data ;
    private Toolbar toolbar;
    private String TAG = "JobFEntrance" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuan_entrance);
        initView();
    }

    private void initView() {
        findId();
        toolbar.setTitle(ResourceUtils.getString(R.string.title_xuanjiang_province));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                Intent intent = new Intent(XuanEntrance.this,XuanJiangTab.class);
                intent.putExtra("provinceId",provinceId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }
        });
    }

    private void findId() {
        listView = (ListView) findViewById(R.id.xuan_entrance_listView);
        toolbar = (Toolbar)findViewById(R.id.xuan_entrance_toolbar);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
