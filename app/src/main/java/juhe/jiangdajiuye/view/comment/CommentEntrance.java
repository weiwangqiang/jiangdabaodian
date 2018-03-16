package juhe.jiangdajiuye.view.comment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.bmobUser.UserBean;
import juhe.jiangdajiuye.consume.recyclerView.MyRecyclerView;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.view.adapter.CommentAdapter;
import juhe.jiangdajiuye.view.comment.bean.Comment;

public class CommentEntrance extends BaseActivity implements MyRecyclerView.OnLoadMoreListener {
    private RecyclerView.LayoutManager manager ;
    private CommentAdapter  adapter ;
    private MyRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_entrance);
        supportToolbar(R.id.comment_toolbar, ResourceUtils.getString(R.string.title_comment));
        initRecycler();
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.comment_recyclerView);
        adapter = new CommentAdapter(this,R.layout.item_recycler_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        List<Comment> list = new ArrayList<>();
        Comment comment = new Comment() ;
        comment.setContent("adqadqdq");
        UserBean userBean = new UserBean();
        userBean.setName("无二清除v");
        comment.setUser(userBean);
        for(int i = 0;i<10;i++){
            list.add(comment);
        }
        adapter.appendDate(list);
        recyclerView.setOnLoadMoreListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * 下拉加载更多
     */
    @Override
    public void onLoadMore() {

    }
}
