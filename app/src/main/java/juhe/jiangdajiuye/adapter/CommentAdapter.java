package juhe.jiangdajiuye.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.ui.recyclerView.adapter.FooterAdapter;
import juhe.jiangdajiuye.utils.SkinManager;
import juhe.jiangdajiuye.view.comment.bean.Comment;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-03-15
 */

public class CommentAdapter extends FooterAdapter<Comment> {
    public CommentAdapter(Context mContext, int mLayout) {
        super(mContext, mLayout);
    }

    /**
     * 获取ItemViewHolder
     *
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public ItemViewHolder getItemViewHolder(ViewGroup parent) {
        return new MyItemViewHolder(SkinManager.inflater(mContext,
                mLayout, parent, false));
    }

    /**
     * 绑定Item View
     * 可以在这里设置点击事件之类的
     *
     * @param holder
     * @param position
     * @param comment
     */
    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder holder, final int position, final Comment comment) {
        super.bindItemViewHolder(holder,position,comment);
        if(holder instanceof MyItemViewHolder){
            MyItemViewHolder itemViewHolder = (MyItemViewHolder) holder;
            itemViewHolder.userName.setText(comment.getUser().getName());
            itemViewHolder.content.setText(comment.getContent());
            itemViewHolder.pushTime.setText(comment.getTime());
            itemViewHolder.userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onClick(v,comment,position);
                    }
                }
            });
        }
    }

    class MyItemViewHolder extends ItemViewHolder {
        TextView userName, pushTime, content;

        public MyItemViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) getView(R.id.item_comment_user_name);
            content = (TextView) getView(R.id.item_comment_content);
            pushTime = (TextView) getView(R.id.item_comment_time);
        }
    }
}
