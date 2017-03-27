package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the juhe.jiangdajiuye.adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent    The ViewGroup into which the new View will be added after it is bound to
     *                  an juhe.jiangdajiuye.adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public CollectRceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewHolder = new MyViewHolder(LayoutInflater.
                from(context).inflate(view,parent,false),context);
        return viewHolder;
    }
    public void setLister(myitemLister lister){
        this.lister = lister;
    }
    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated juhe.jiangdajiuye.adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the juhe.jiangdajiuye.adapter's data set.
     */
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister != null){
                    lister.ItemLister(position);
                }
            }
        });

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

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,time,place,company,work;
        private CardView cardView;
        public MyViewHolder(View itemView, Context context) {
            super(itemView);
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
    }
}
