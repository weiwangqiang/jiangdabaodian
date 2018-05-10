package juhe.jiangdajiuye.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.MessageBean;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "Searchctivity ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getMessage();
    }

    public void getMessage() {
        BmobQuery<MessageBean> query = new BmobQuery<>();
        query.addWhereEqualTo("title","三只松鼠%%");
        query.findObjects(new FindListener<MessageBean>() {
            @Override
            public void done(List<MessageBean> list, BmobException e) {
                for(MessageBean messageBean :list){
                    Log.i(TAG, "done: "+messageBean.getTitle());
                }
            }
        });
    }
}
