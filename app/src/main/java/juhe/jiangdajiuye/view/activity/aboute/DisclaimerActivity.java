package juhe.jiangdajiuye.view.activity.aboute;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.utils.ResourceUtils;

public class Disclaimer extends BaseActivity {
    private TextView tvOpen ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        supportToolbar(R.id.disclaimer_toolbar, ResourceUtils.getString(R.string.title_disclaimer));
        initView();
    }

    private void initView() {
        tvOpen = findViewById(R.id.disclaimer_open_content);
        StringBuilder sb = new StringBuilder();
        sb.append("org.jsoup:jsoup\n");
        sb.append("com.github.bumptech.glide:glide");
        tvOpen.setText(sb.toString());
//        tvDisclaimer.setText(ResourceUtils.getString(R.string.textview_disclaimer_content));
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
