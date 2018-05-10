package juhe.jiangdajiuye.view.activity.JobFair;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.view.activity.JobFair.constant.JobFairUrl;
import juhe.jiangdajiuye.adapter.MesFragmentAdapter;

/**
 * Created by wangqiang on 2016/10/1.
 * 省份里各个高校的宣讲会tab列表
 */
public class JobFTabActivity extends BaseActivity {
    private String TAG = "JobFTabActivity";
    private ViewPager viewPager;
    private TabLayout  tabLayout;
    private List<Fragment> list = new ArrayList<>();
    private MesFragmentAdapter adapter;
    public String[] college ;
    public String[] urls ;
    private int provinceId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuanjiang);
        Intent intent = getIntent();
        provinceId = intent.getIntExtra("provinceId",0);
        findId();
        initList();
        initTabLayout();
    }
    public void findId(){
        viewPager = (ViewPager) findViewById(R.id.xuanjiang_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.xuanjiang_tabLayout);
    }
    public void initList(){
        college = JobFairUrl.getInstance().getTitle(provinceId);
        urls = JobFairUrl.getInstance().getUrls(provinceId);
        for(int i =0;i<college.length;i++){
            Fragment f = JobFragment.newInstance(urls[i],college[i],i,provinceId);
            list.add(f);
        }
        adapter  = new MesFragmentAdapter(getSupportFragmentManager(), list,college);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new pagerlist());
        viewPager.setCurrentItem(0);
    }

    private void initTabLayout(){
        supportToolbar(R.id.xuanjiang_toolbar,ResourceUtils.getString(R.string.title_job_fair_tab));
        for(int i = 0;i<list.size();i++){
            TabLayout.Tab tabFirst = tabLayout.newTab();
            tabLayout.addTab(tabFirst);
        }
        tabLayout.setupWithViewPager(viewPager);
    }
    class pagerlist implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            adapter.getItem(position).onResume();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            default:
                break;
        }
    }
    @Override
    public void onPause(){
        super.onPause();
    }
}
