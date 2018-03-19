package juhe.jiangdajiuye.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.UserCenterBean;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-03-18
 */

public class UserCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserCenterBean> data;
    private int layout ;
    private Context context ;
    private ItemClick itemClick ;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public UserCenterAdapter(Context context , List<UserCenterBean> data , int layout){
        this.data = data;
        this.layout = layout ;
        this.context = context ;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(layout,null,false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.leftTv.setText(data.get(position).getLeftText());
        myHolder.rightTv.setText(data.get(position).getRightText());
        myHolder.imageView.setVisibility(data.get(position).isHasNext() ? View.VISIBLE : View.INVISIBLE);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onItemClick(data.get(position),position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
    private class MyHolder extends RecyclerView.ViewHolder{
        public TextView leftTv,rightTv ;
        public ImageView imageView ;
        public View itemView ;
        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView ;
            leftTv = itemView.findViewById(R.id.item_user_center_left_text);
            rightTv = itemView.findViewById(R.id.item_user_center_right_text);
            imageView = itemView.findViewById(R.id.item_user_center_next);

        }
    }
    public interface ItemClick{
        void onItemClick(UserCenterBean userCenterBean,int position);
    }
}
