package juhe.jiangdajiuye.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

import java.io.File;

import juhe.jiangdajiuye.MainActivity;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.util.UserActionRecordUtils;
import juhe.jiangdajiuye.view.constant.FileConstant;

/**
 * Created by wangqiang on 2016/7/3.
 * 启动界面
 */
public class IndexView extends BaseActivity {
    private String TAG = "IndexView";
    private Animation LoadAnimation;
    private AlphaAnimation start;
    private ScaleAnimation animation;
    private ImageView welcome,loading;
    private Button button;
    private Boolean isIn = false,isFirst;
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
        sharedPreferences = getSharedPreferences("IndexView", Context.MODE_PRIVATE);
        isFirst = sharedPreferences.getBoolean("first",true);
        init();
        UserActionRecordUtils.setComeTime(System.currentTimeMillis());
    }

    private void init(){
        findId();
        setBootAdvert();

        LoadAnimation = AnimationUtils.loadAnimation(this,R.anim.loading);//点击跳转后等待的动画
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loading.startAnimation(LoadAnimation);
                ToMainActivity();
            }
        });
         animation =new ScaleAnimation(1.0f, 1.08f, 1.0f, 1.08f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        start = new AlphaAnimation(0.42f, 1.0f);
        animation.setDuration(4000);//设置动画持续时间
        start.setDuration(4000);
//        IndexView.setAnimation(start);
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

    private void findId() {
        welcome  = (ImageView)findViewById(R.id.welcome_image);
        loading = (ImageView)findViewById(R.id.welcome_Loading);
        button =  (Button)findViewById(R.id.welcome_button);
    }

    private void setBootAdvert() {
        float math = (float) Math.random();
        int ran = (int)(math * image.length);
        File file = new File(FileConstant.BootAdvertSaveRootFile+FileConstant.BootAdvertSavePictureName);
        if(file.exists())
            welcome.setImageDrawable(Drawable.createFromPath(FileConstant.BootAdvertSaveRootFile
                    +FileConstant.BootAdvertSavePictureName));
        if(welcome.getDrawable() == null)
            welcome.setBackgroundResource(image[ran]);
    }

    //跳转到主界面
    public void ToMainActivity(){
        if(!isIn){
            isIn = true;//避免二次跳转
            Intent intent;
            if(isFirst)
                intent = new Intent(IndexView.this,Guide.class);
            else
                intent = new Intent(IndexView.this,MainActivity.class);
            startActivity(intent);
            IndexView.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadAnimation.cancel();
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
