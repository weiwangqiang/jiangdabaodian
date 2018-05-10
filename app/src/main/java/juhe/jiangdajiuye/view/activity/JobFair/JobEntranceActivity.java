package juhe.jiangdajiuye.view.activity.JobFair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.view.activity.JobFair.constant.JobEntranceData;
import juhe.jiangdajiuye.view.activity.xuanJiang.constant.XuanEntranceData;

/**
 * 招聘会入口
 */
public class JobFEntrance extends BaseActivity {
    ListView listView ;
    SimpleAdapter adapter ;
    private JobEntranceData data ;
    private String TAG = "JobFEntrance" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuan_entrance);
        initView();
    }

    private void initView() {
        findId();
        supportToolbar(R.id.xuan_entrance_toolbar,ResourceUtils.getString(R.string.title_job_fair_province));
        data = new JobEntranceData();
        adapter = new SimpleAdapter(this,data.getData(),
                R.layout.activity_xuan_entrance_item,
                new String[]{XuanEntranceData.province},
                new int[]{R.id.entrance_text});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int provinceId = (int) data.getData().get(position).get("provinceId");
                Intent intent = new Intent(JobFEntrance.this,JobFTab.class);
                intent.putExtra("provinceId",provinceId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }
        });
    }

    private void findId() {
        listView = (ListView) findViewById(R.id.xuan_entrance_listView);
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
