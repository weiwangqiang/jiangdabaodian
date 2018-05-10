package juhe.jiangdajiuye.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.utils.SharePreUtils;
import juhe.jiangdajiuye.adapter.ViewPagerAdapter;

/**
 * Created by wangqiang on 2016/11/8.
 *
 *  引导界面
 *
 *  暂时不用
 */

public class AppGuide extends BaseActivity {
    private ViewPager viewpager;
    private Button begin;
    private ImageView view1,view2,view3;
    private Animation in,out;
    private ArrayList<View> list = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private RadioGroup radioGroup ;
    @Override
    public void onCreate(Bundle SaveInstanceState){
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.guide);
        getView();
        findId();
        initViewpager();
        initRadioGroup();
    }

    private void initRadioGroup() {
        radioGroup = findViewById(R.id.guide_radioGroup);
        for(int i = 0 ;i< list.size();i++){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i+10);
            radioButton.setClickable(false);
            radioGroup.addView(radioButton);
        }
        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
    }

    public void getView(){
        in = AnimationUtils.loadAnimation(AppGuide.this,R.anim.in_from_bottom_to_top);
        out = AnimationUtils.loadAnimation(AppGuide.this,R.anim.out_from_top_to_bottom);
        view1 = (ImageView) getLayoutInflater().inflate(R.layout.guideimage,null);
        view2 = (ImageView) getLayoutInflater().inflate(R.layout.guideimage,null);
        view3 = (ImageView) getLayoutInflater().inflate(R.layout.guideimage,null);
//        view1.setImageResource(R.drawable.guide11);
//        view2.setImageResource(R.drawable.guide22);
//        view3.setImageResource(R.drawable.guide33);
    }
    public void findId(){
        viewpager = (ViewPager)findViewById(R.id.guideViewpager);
        begin = (Button)findViewById(R.id.guideButton);
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
                    radioGroup.setVisibility(View.GONE);
                    begin.startAnimation(in);
                    begin.setVisibility(View.VISIBLE);
                    begin.setClickable(true);
                }
                else if(begin.getVisibility()==View.VISIBLE)
                {
                    begin.startAnimation(out);
                    begin.setVisibility(View.GONE);
                    begin.setClickable(false);
                    radioGroup.setVisibility(View.VISIBLE);
                }
                ((RadioButton)radioGroup.getChildAt(position)).setChecked(true);
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
                SharePreUtils.setBoolean(SharePreUtils.isFirst,false);
                Intent intent = new Intent(AppGuide.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
