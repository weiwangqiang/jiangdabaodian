package juhe.jiangdajiuye.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.InterFace.viewPagerChangeLister;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.FragmentAdapter;


/**
 * Created by wangqiang on 2016/10/1.
 */
public class fragmentMain extends Fragment implements View.OnClickListener{
    private String TAG = "fragementMain";
    private View view,main;
    private ViewPager viewPager;
    private TabLayout  tabLayout;
    private Toolbar toolbar;
    private FragmentAdapter adapter;
    private List<Fragment> list = new ArrayList<>();
    private viewPagerChangeLister lister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("onCreateView","fragmentJS  is onCreateView");
        view = inflater.inflate(R.layout.app_bar_main,container,false);
        main = inflater.inflate(R.layout.activity_main,container,false);
        initView();
        return view;
    }
    public void initView(){
        findid();
        initViewPager();
        initTabLayout();
    }
    public void findid(){
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
    }
    private void initViewPager(){
        list.add(new fragmentXJ());
        list.add(new fragmentZP());
        list.add(new fragmentSD());
        //刚开始只会加载前两个fragment
        adapter  = new FragmentAdapter(getActivity().getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new pagerlist());
        viewPager.setCurrentItem(0);
    }
    private void initTabLayout(){
//        toolbar.setTitle("江大资讯");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"id is "+view.getId(),Toast.LENGTH_SHORT).show();
            }
        });

        setHasOptionsMenu(true);
        DrawerLayout drawer = (DrawerLayout) main.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.openDrawer(Gravity.LEFT);
        toggle.syncState();
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        TabLayout.Tab tabFirst = tabLayout.newTab();
        tabLayout.addTab(tabFirst);

        TabLayout.Tab tabSecond = tabLayout.newTab();
        tabLayout.addTab(tabSecond);

        TabLayout.Tab tabThird = tabLayout.newTab();
        tabLayout.addTab(tabThird);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public void onClick(View view) {
        Log.e(TAG,"view id is "+view.getId());
        Toast.makeText(getActivity(),"id is "+view.getId(),Toast.LENGTH_SHORT).show();
        switch(view.getId()){
            case android.R.id.home:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) main.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    class pagerlist implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.e(TAG,"pager is changed  position is "+position);
//          lister.showFrament(position);
            adapter.getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(getActivity(), "setNavigationlcon", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setViewPagerChangeLister(viewPagerChangeLister lister){
        this.lister = lister;
    }
}
