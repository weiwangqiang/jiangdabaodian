package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
public class recyclerAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private myitemLister lister;
    private RecyclerView.ViewHolder viewHolder;
    private Context context ;
    private int view;
    private String TAG = "recyclerAdapter";
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private List<MessageItem> data ;
    public recyclerAdapter(Context context, int view, List<MessageItem> data){
        this.context = context;
        this.view = view;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM)
            return new MyViewHolder(LayoutInflater.
                    from(context).inflate(view, parent, false));
        else
            return new MyFooterViewHolder(LayoutInflater.
                    from(context).inflate(R.layout.footer, parent, false));
    }


    public void setLister(myitemLister lister){
        this.lister = lister;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(position+1 == getItemCount()){
            bindFooterViewHolder(holder,position);
        }else
            bindItemViewHolder(holder,position);

    }

    private void bindFooterViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(!(holder instanceof MyFooterViewHolder)) return;

        if(position != 0){
            if( ((MyFooterViewHolder)holder).footerView.getVisibility() == View.INVISIBLE)
                ((MyFooterViewHolder)holder).footerView.setVisibility(View.VISIBLE);
            ((MyFooterViewHolder)holder).mTv.setText("正在加载");
        }
        else
            ((MyFooterViewHolder)holder).footerView.setVisibility(View.INVISIBLE);

    }

    private void bindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(!(holder instanceof MyViewHolder)) return;
        try{
            ((MyViewHolder)holder).title.setText(data.get(position).getTitle());
            ((MyViewHolder)holder).time.setText(data.get(position).getTime());
            if(null != data.get(position).getLocate()){
                ((MyViewHolder)holder).place.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).place.setText(data.get(position).getLocate());
            }
            if(data.get(position).getFrom() != null){
                ((MyViewHolder)holder).company.setText(data.get(position).getFrom());
                ((MyViewHolder)holder).company.setVisibility(View.VISIBLE);
            }
            if(data.get(position).getIndustry()!= null){
                ((MyViewHolder)holder).work.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).work.setText(data.get(position).getIndustry());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1 == getItemCount())
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }
    public void RefreshDate (List<MessageItem> data){
        this.data = data;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title,time,place,company,work;
        CardView cardView ;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView)itemView.findViewById(R.id.time);
            place = (TextView)itemView.findViewById(R.id.place);
            company = (TextView)itemView.findViewById(R.id.company);
            work = (TextView)itemView.findViewById(R.id.work);
            place.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            work.setVisibility(View.GONE);
            cardView.setOnClickListener(this);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
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
