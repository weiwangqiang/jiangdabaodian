package juhe.jiangdajiuye.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.ui.recyclerView.LoadMoreRecyclerView;
import juhe.jiangdajiuye.ui.recyclerView.adapter.FooterAdapter;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.view.activity.userCenter.UserEntranceActivity;
import juhe.jiangdajiuye.view.activity.userCenter.engine.UserManager;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.adapter.CommentAdapter;
import juhe.jiangdajiuye.bean.comment.Post;
import juhe.jiangdajiuye.view.dialog.ProgressDialog;
import juhe.jiangdajiuye.view.activity.userCenter.UserMessageActivity;

public class CommentActivity extends BaseActivity implements LoadMoreRecyclerView.OnLoadMoreListener {
    private static final String TAG = "CommentActivity";
    private RecyclerView.LayoutManager manager;
    private CommentAdapter adapter;
    private LoadMoreRecyclerView recyclerView;
    private EditText input;
    private ImageButton send;
    private MessageBean messageBean;
    private ProgressDialog mProgress;
    private ViewStub viewSub;
    private View mErrorView;
    private Post post;
    private int pageNum  = 5 ;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示
    public static void StartActivity(Context context, MessageBean item) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(MessageBean.keyVal.url, item.getUrl());
        intent.putExtra(MessageBean.keyVal.title, item.getTitle());
        intent.putExtra(MessageBean.keyVal.time, item.getTime());
        intent.putExtra(MessageBean.keyVal.from, item.getFrom());
        intent.putExtra(MessageBean.keyVal.locate, item.getLocate());
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_entrance);
        supportToolbar(R.id.comment_toolbar, ResourceUtils.getString(R.string.title_comment));
        initRecycler();
        initView();
        initParams();
        initData();
    }

    private void initData() {
        send.post(new Runnable() {
            @Override
            public void run() {
                mProgress.show();
            }
        });
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("url", messageBean.getUrl());
        query.setLimit(1);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    post = list.get(0) ;
                    findComment(post);
                }else{
                    mProgress.cancel();
                    showError(true);
                }
            }
        });
    }

    private void findComment(Post post) {
        BmobQuery<juhe.jiangdajiuye.bean.comment.Comment> query = new BmobQuery<>();
//用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        query.addWhereEqualTo("post", new BmobPointer(post));
//希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("user,post.title");
        query.setLimit(pageNum);
        query.setSkip(adapter.getDataSize());
        query.findObjects(new FindListener<juhe.jiangdajiuye.bean.comment.Comment>() {

            @Override
            public void done(List<juhe.jiangdajiuye.bean.comment.Comment> objects, BmobException e) {
                if (e == null && objects.size() != 0) {
                    adapter.appendData(objects);
                    if(objects.size()<pageNum){
                        recyclerView.setCanLoadMoreRefresh(false);
                    }else{
                        recyclerView.setDefaultStatus();
                    }
                }else{
                    recyclerView.setCanLoadMoreRefresh(false);
                }
                mProgress.cancel();
                showError(adapter.getDataSize()==0);
            }
        });
    }

    private void showError(boolean error) {
        recyclerView.setVisibility(error ? View.GONE : View.VISIBLE);
        mErrorView = (null == mErrorView) ? viewSub.inflate() : mErrorView;
        mErrorView.setVisibility(error ? View.VISIBLE : View.GONE);
    }

    private void initView() {
        input = findViewById(R.id.comment_input);
        send = findViewById(R.id.comment_send);
        viewSub = findViewById(R.id.comment_viewsub);
        mProgress = new ProgressDialog(this);
        setTextViewColor(send,R.color.baseColor);

    }

    public static void setTextViewColor(ImageButton view, int colorResId) {
        //mutate()
        Drawable modeDrawable = view.getDrawable();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList =     ColorStateList.valueOf(view.getResources().getColor(colorResId));
        DrawableCompat.setTintList(temp, colorStateList);
        view.setImageDrawable(temp);
    }

    /**
     * 获取参数信息
     */
    public void initParams() {
        Intent intent = getIntent();
        messageBean = new MessageBean();
        messageBean.setUrl(intent.getStringExtra(MessageBean.keyVal.url));
        messageBean.setTitle(intent.getStringExtra(MessageBean.keyVal.title));
        messageBean.setTime(intent.getStringExtra(MessageBean.keyVal.time));
        messageBean.setFrom(intent.getStringExtra(MessageBean.keyVal.from));
        messageBean.setLocate(intent.getStringExtra(MessageBean.keyVal.locate));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.comment_recyclerView);
        adapter = new CommentAdapter(this, R.layout.item_recycler_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(new FooterAdapter.OnItemClickListener<juhe.jiangdajiuye.bean.comment.Comment>() {
            @Override
            public void onClick(View view, juhe.jiangdajiuye.bean.comment.Comment comment, int position) {
                    switch (view.getId()){
                        case R.id.item_comment_user_name:
                            Intent intent = new Intent(CommentActivity.this, UserMessageActivity.class);
                            intent.putExtra("userId",comment.getUser().getObjectId());
                            startActivitySlideInRight(intent);
                            break;
                            default:
                                break;
                    }
            }
        });
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
        if(!recyclerView.isRefreshing()){
            recyclerView.setPullUpToRefresh();
            findComment(post);
        }
    }

    public void send(View view) {
        if (!UserManager.getInStance().isLogin()) {
             startActivitySlideInRight(this,UserEntranceActivity.class);
            return;
        }
        if(TextUtils.isEmpty(input.getText().toString())){
            return;
        }
        mProgress.show();
        if (null == post) {
            createPost();
        } else {
            pushComment();
        }

    }
    private juhe.jiangdajiuye.bean.comment.Comment comment = new juhe.jiangdajiuye.bean.comment.Comment();
    private void pushComment() {
        comment.setUser(UserManager.getInStance().getUserBean());
        comment.setContent(input.getText().toString());
        comment.setPost(post);
        comment.setTime(dateFormat.format(System.currentTimeMillis()));
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    input.setText("");
                    findComment(post);
                }else {
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
                }
                mProgress.cancel();
            }
        });
    }

    private void createPost() {
        post = new Post();
        post.setTitle(messageBean.getTitle());
        post.setUrl(messageBean.getUrl());
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    findPost();
                } else {
                    mProgress.cancel();
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
                }
            }
        });
    }
    //找到创建的post ID
    public void findPost() {
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("url", messageBean.getUrl());
        query.setLimit(1);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    post = list.get(0);
                    pushComment();
                } else {
                    mProgress.cancel();
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
                }
            }
        });
    }
}
