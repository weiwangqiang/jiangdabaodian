package juhe.jiangdajiuye.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import juhe.jiangdajiuye.MainActivity;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.ViewPagerAdapter;
import juhe.jiangdajiuye.core.BaseActivity;

/**
 * Created by wangqiang on 2016/11/8.
 * 引导界面
 */

public class Guide extends BaseActivity {
    private ViewPager viewpager;
    private Button begin;
    private View view1,view2,view3;
    private ImageView image1,image2,image3;
    private Animation in,out;
    private int oldPosition = 0;
    private ArrayList<View> list = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle SaveInstanceState){
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.guide);
        sharedPreferences = getSharedPreferences("IndexView", Context.MODE_PRIVATE);
        getView();
        findId();
        initViewpager();
    }
    public void getView(){
        in = AnimationUtils.loadAnimation(Guide.this,R.anim.in_from_bottom_to_top);
        out = AnimationUtils.loadAnimation(Guide.this,R.anim.out_from_top_to_bottom);
        view1 = getLayoutInflater().inflate(R.layout.guideimage,null);
        view2 = getLayoutInflater().inflate(R.layout.guideimage,null);
        view3 = getLayoutInflater().inflate(R.layout.guideimage,null);
    }
    public void findId(){
        viewpager = (ViewPager)findViewById(R.id.guideViewpager);
        begin = (Button)findViewById(R.id.guideButton);
        image1 = (ImageView)view1.findViewById(R.id.guideImage);
        image2 = (ImageView)view2.findViewById(R.id.guideImage);
        image3 = (ImageView)view3.findViewById(R.id.guideImage);
        image1.setImageResource(R.drawable.guide11);
        image2.setImageResource(R.drawable.guide22);
        image3.setImageResource(R.drawable.guide33);
    }
    public void initViewpager(){
        begin.setOnClickListener(this);
        list.add(view1);
        list.add(view2);
        list.add(view3);
        adapter = new ViewPagerAdapter(list);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==2&&(begin.getVisibility()==View.GONE)){
                    begin.startAnimation(in);
                    begin.setVisibility(View.VISIBLE);
                    begin.setClickable(true);
                }
                else if(begin.getVisibility()==View.VISIBLE)
                {
                    begin.startAnimation(out);
                    begin.setVisibility(View.GONE);
                    begin.setClickable(false);
                }
                oldPosition = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.guideButton:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("first",false);
                editor.commit();
                Intent intent = new Intent(Guide.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
