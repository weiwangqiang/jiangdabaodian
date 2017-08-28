package juhe.jiangdajiuye.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.XJFragmentAdapter;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.fragment.fragment;
import juhe.jiangdajiuye.util.XuanJiangData;

/**
 * Created by wangqiang on 2016/10/1.
 */
public class xuanjiang extends BaseActivity {
    private String TAG = "xuanjiang";
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout  tabLayout;
    private List<Fragment> list = new ArrayList<>();
    private XJFragmentAdapter adapter;
    public String[] college ;
    public String[] urls ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuanjiang);
        findid();
        initList();
        initTabLayout();
    }
    public void findid(){
        viewPager = (ViewPager) findViewById(R.id.xuanjiang_viewpager);
        toolbar = (Toolbar)findViewById(R.id.xuanjiang_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.xuanjiang_tabLayout);

    }
    public void initList(){
        XuanJiangData xuanJiangData = new XuanJiangData();
        college = xuanJiangData.getTitle();
        urls = xuanJiangData.getUrls();
        for(int i =0;i<college.length;i++){
            fragment f = fragment.newInstance(urls[i],college[i],i);
//            fragmentSD f = new fragmentSD();
            list.add(f);
        }
        adapter  = new XJFragmentAdapter(getSupportFragmentManager(), list,college);
        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(new pagerlist());
        viewPager.setCurrentItem(0);
    }

    private void initTabLayout(){
        toolbar.setTitle("宣讲大全");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
            Log.e(TAG,"pager is changed  position is "+position);
//            lister.showFrament(position);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause(){
        super.onPause();
    }
}
