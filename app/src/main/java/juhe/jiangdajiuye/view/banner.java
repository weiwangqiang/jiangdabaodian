package juhe.jiangdajiuye.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.tool.GlideImageLoader;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jiangdajiuye
 * @since 2017-07-07
 */
@ContentView(R.layout.mybanner)
public class banner extends BaseActivity implements OnBannerListener {
    private static final String TAG = "banner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        findId();
    }
    private Banner banner ;
    public void findId() {
        List<String> list =  new ArrayList<>();
        banner = (Banner) findViewById(R.id.banner);
//        banner.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        String s = "http://imgsrc.baidu.com/forum/w%3D580/sign=dc0131e5d42a60595210e1121834342d/ea8ce21190ef76c62ae126079e16fdfaaf5167b8.jpg";
        String s1 = "http://imgsrc.baidu.com/forum/w%3D580/sign=c198e6e818d5ad6eaaf964e2b1ca39a3/d5e54fed2e738bd4a09f6f1da28b87d6277ff935.jpg";
        list.add(s);
        list.add(s1);
        list.add(s);
        list.add(s1);
        //简单使用
        banner.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .setDelayTime(5000)
                .start();
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    /**
     * Called when an activity you launched exits
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(banner.this,position+"" ,0).show();
    }
}