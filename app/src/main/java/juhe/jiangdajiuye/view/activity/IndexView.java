package juhe.jiangdajiuye.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import juhe.jiangdajiuye.utils.SharePreUtils;
import juhe.jiangdajiuye.utils.netUtils.NetStateUtils;
import juhe.jiangdajiuye.user.UserActionRecordUtils;
import juhe.jiangdajiuye.view.constant.FileConstant;

/**
 * Created by wangqiang on 2016/7/3.
 * 启动界面
 */
public class IndexView extends BaseActivity {
    private String TAG = "IndexView";
    private Animation LoadAnimation;
    private AlphaAnimation alphaAnima;
    private ScaleAnimation scaleAnimat;
    private ImageView welcome, loading;
    private Button button;
    private Boolean isIn = false, isFirst;
//    private int[] image = new int[]{R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3};
    private int[] image = new int[]{R.drawable.index_bg, R.drawable.index_bg};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        isFirst = SharePreUtils.getBoolean(SharePreUtils.isFirst, true);
        init();
        UserActionRecordUtils.setComeTime(System.currentTimeMillis());
    }

    private void init() {
        findId();
        setBootAdvert();

        LoadAnimation = AnimationUtils.loadAnimation(this, R.anim.loading);//点击跳转后等待的动画
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loading.startAnimation(LoadAnimation);
                ToMainActivity();
            }
        });
        scaleAnimat = new ScaleAnimation(1.0f, 1.08f, 1.0f, 1.08f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        alphaAnima = new AlphaAnimation(0.42f, 1.0f);
        scaleAnimat.setDuration(4000);//设置动画持续时间
        alphaAnima.setDuration(4000);
//        IndexView.setAnimation(alphaAnima);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(scaleAnimat);
        set.addAnimation(alphaAnima);
        welcome.startAnimation(set);//图片的动画
        scaleAnimat.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.i(TAG, "run: ");
                                if(NetStateUtils.getNetWorkAvailable()){
                                    Log.i(TAG, "run:  get use ip =======================");
                                    UserActionRecordUtils.setIP();
                                }
                                Thread.sleep(2000);
                                ToMainActivity();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
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
        welcome = (ImageView) findViewById(R.id.welcome_image);
        loading = (ImageView) findViewById(R.id.welcome_Loading);
        button = (Button) findViewById(R.id.welcome_button);
    }

    private void setBootAdvert() {
        float math = (float) Math.random();
        int ran = (int) (math * image.length);
        File file = new File(FileConstant.BootAdvertSaveRootFile + FileConstant.BootAdvertSavePictureName);
        if (file.exists()){
            welcome.setImageDrawable(Drawable.createFromPath(FileConstant.BootAdvertSaveRootFile
                    + FileConstant.BootAdvertSavePictureName));
        }
        if (welcome.getDrawable() == null){
            welcome.setBackgroundResource(image[ran]);
        }
    }

    //跳转到主界面
    public void ToMainActivity() {
        if (!isIn) {
            isIn = true;//避免二次跳转
            SharePreUtils.setInt(SharePreUtils.visitFrequency,
                    SharePreUtils.getInt(SharePreUtils.visitFrequency,0)+1);
            Intent intent;
//            if(isFirst)
//                intent = new Intent(IndexView.this,Guide.class);
//            else
            intent = new Intent(IndexView.this, MainActivity.class);
            startActivity(intent);
            IndexView.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != LoadAnimation){
            LoadAnimation.cancel();
        }
        if(null != scaleAnimat){
            scaleAnimat.cancel();
        }
        if(null != alphaAnima){
            alphaAnima.cancel();
        }

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
