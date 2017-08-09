package juhe.jiangdajiuye.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import juhe.jiangdajiuye.MainActivity;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.util.UserActionRecordUtils;

/**
 * Created by wangqiang on 2016/7/3.
 */
public class welcome extends BaseActivity {
    private String TAG = "welcome";
    private Animation Loadmation;
    private AlphaAnimation start;
    private ScaleAnimation animation;
    private ImageView welcome,loading;
    private Button button;
    private Boolean isIn = false,isfirst;
    private SharedPreferences sharedPreferences;
    private int[] image = new int[]{R.drawable.welcome,R.drawable.guide1,
            R.drawable.guide1,R.drawable.guide3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        sharedPreferences = getSharedPreferences("welcome", Context.MODE_PRIVATE);
        isfirst = sharedPreferences.getBoolean("first",true);
        init();
        UserActionRecordUtils.setComeTime(System.currentTimeMillis());
//        NetMesManager.setIP(this);
    }




    private void init(){
        welcome  = (ImageView)findViewById(R.id.welcome_image);
        float math = (float) Math.random();
        int ran = (int)(math * image.length);
        welcome.setBackgroundResource(image[ran]);
        loading = (ImageView)findViewById(R.id.welcome_Loading);
        button =  (Button)findViewById(R.id.welcome_button);
        Loadmation = AnimationUtils.loadAnimation(this,R.anim.loading);//点击跳转后等待的动画
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loading.startAnimation(Loadmation);
                ToMainActivity();
            }
        });
         animation =new ScaleAnimation(1.0f, 1.08f, 1.0f, 1.08f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        start = new AlphaAnimation(0.42f, 1.0f);
        animation.setDuration(4000);//设置动画持续时间
        start.setDuration(4000);
//        welcome.setAnimation(start);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animation);
        set.addAnimation(start);
        welcome.startAnimation(set);//图片的动画
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(3000);
                                ToMainActivity();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    //跳转到主界面
    public void ToMainActivity(){
        if(!isIn){
            isIn = true;//避免二次跳转
            Intent intent;
            if(isfirst)
                intent = new Intent(welcome.this,guide.class);
            else
                intent = new Intent(welcome.this,MainActivity.class);
            startActivity(intent);
            welcome.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Loadmation.cancel();
        animation.cancel();
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
