package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import juhe.jiangdajiuye.InterFace.myitemLister;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.entity.MessageItem;

/**
 * Created by wangqiang on 2016/9/28.
 */
public class SDrecyclerAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private myitemLister lister;
    private MyViewHolder viewHolder;
    private Context context ;
    private int view;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private List<MessageItem> data ;
    private String TAG ="SDrecyclerAdapter";
    public SDrecyclerAdapter(Context context, int view, List<MessageItem> data){
        this.context = context;
        this.view = view;
        this.data = data;
    }
    @Override
    public int getItemViewType(int position) {
        if(position+1 == getItemCount())
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("adapter","adapter viewType is "+viewType);
        if (viewType == TYPE_ITEM)
            return new MyViewHolder(LayoutInflater.
                    from(context).inflate(view,parent,false),context);
        else
            return new MyFooterViewHolder(LayoutInflater.
                    from(context).inflate(R.layout.footer, parent, false));

    }

    public void setLister(myitemLister lister){
        this.lister = lister;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        if(position+1 == getItemCount()){
            bindFooterViewHolder(holder,position);
        }else
            bindItemViewHolder(holder,position);
    }

    private void bindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        try{
            ((MyViewHolder) holder).title.setText(data.get(position).getTitle());
            ((MyViewHolder) holder).time.setText(data.get(position).getTime());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void bindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyFooterViewHolder ){
            if(position != 0){
                if( ((MyFooterViewHolder)holder).footerView.getVisibility() == View.INVISIBLE)
                    ((MyFooterViewHolder)holder).footerView.setVisibility(View.VISIBLE);
                ((MyFooterViewHolder)holder).mTv.setText("正在加载");
            }
            else
                ((MyFooterViewHolder)holder).footerView.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Returns the total number of items in the data set hold by the juhe.jiangdajiuye.adapter.
     *
     * @return The total number of items in this juhe.jiangdajiuye.adapter.
     */
    @Override
    public int getItemCount() {
        return data.size()+1;
    }
    public void RefreshDate (List<MessageItem> data){
        this.data = data;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title,time;
        public MyViewHolder(View itemView, Context context) {
            super(itemView);
            if(context!=null){
                getshow();
            }
            itemView.setOnClickListener(this);
        }
        public void getshow(){
            title = (TextView) itemView.findViewById(R.id.shudi_title);
            time = (TextView)itemView.findViewById(R.id.shudi_time);
        }


        @Override
        public void onClick(View v) {
            if(lister != null){
                //获取当前位置
                lister.ItemLister(getAdapterPosition());
            }
        }
    }


    class MyFooterViewHolder extends RecyclerView.ViewHolder{
        private TextView mTv ;
        private View footerView;
        public MyFooterViewHolder(View itemView) {
            super(itemView);
            footerView = itemView.findViewById(R.id.footer_view);
            mTv = (TextView)itemView.findViewById(R.id.footerTitle);
        }
    }
}
