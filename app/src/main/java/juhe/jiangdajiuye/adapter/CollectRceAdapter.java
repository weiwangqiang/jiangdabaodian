package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import juhe.jiangdajiuye.InterFace.myitemLister;
import juhe.jiangdajiuye.R;

/**收藏栏的adapter
 * Created by wangqiang on 2016/9/28.
 */
public class CollectRceAdapter extends  RecyclerView.Adapter<CollectRceAdapter.MyViewHolder> {
    private String TAG = "CollectRceAdapter";
    private myitemLister lister;
    private MyViewHolder viewHolder;
    private Context context ;
    private int view;
    private ArrayList<HashMap<String,String>> data ;
    public CollectRceAdapter(Context context, int view, ArrayList<HashMap<String,String>> data){
        this.context = context;
        this.view = view;
        this.data = data;
    }
    @Override
    public CollectRceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewHolder = new MyViewHolder(LayoutInflater.
                from(context).inflate(view,parent,false),context);
        return viewHolder;
    }
    public void setLister(myitemLister lister){
        this.lister = lister;
    }
    @Override
    public void onBindViewHolder(CollectRceAdapter.MyViewHolder holder, final int position) {
        try{
            holder.title.setText(data.get(position).get("title").toString());
            holder.time.setText(data.get(position).get("time").toString());
            if(data.get(position).containsKey("location")){
                holder.place.setText(data.get(position).get("location").toString());
                holder.company.setText(data.get(position).get("company").toString());
            }
            else
            {
                holder.place.setVisibility(View.GONE);
                holder.company.setVisibility(View.GONE);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the total number of items in the data set hold by the juhe.jiangdajiuye.adapter.
     *
     * @return The total number of items in this juhe.jiangdajiuye.adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void RefreshDate (ArrayList<HashMap<String,String>> data){
        this.data = data;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title,time,place,company,work;
        private CardView cardView;
        public MyViewHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            if(context!=null){
                getshow();
            }

        }
        public void getshow(){
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView)itemView.findViewById(R.id.time);
            place = (TextView)itemView.findViewById(R.id.place);
            company = (TextView)itemView.findViewById(R.id.company);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

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
}
