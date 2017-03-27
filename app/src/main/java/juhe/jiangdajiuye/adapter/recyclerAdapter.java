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

/**
 * Created by wangqiang on 2016/9/28.
 */
public class recyclerAdapter extends  RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private myitemLister lister;
    private MyViewHolder viewHolder;
    private Context context ;
    private int view;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private String TAG = "recyclerAdapter";
    private ArrayList<HashMap<String,String>> data ;
    public recyclerAdapter(Context context, int view, ArrayList<HashMap<String,String>> data){
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
    public recyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("adapter","adapter viewType is "+viewType);

        viewHolder = new MyViewHolder(LayoutInflater.
                from(context).inflate(view,parent,false));

//        if (viewType == TYPE_ITEM) {
//            return viewHolder;
//        }
//        // type == TYPE_FOOTER 返回footerView
//        else if (viewType == TYPE_FOOTER) {
//            Log.e("adapter","adapter viewType is "+viewType+" has get View footer");
//            View view = LayoutInflater.from(parent.getContext()).inflate(
//                    R.layout.footer, null);
//            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
//                    RecyclerView.LayoutParams.WRAP_CONTENT));
//            return new MyViewHolder(view,null);
//        }
        return viewHolder;
    }
//    @Override
//    public int getItemViewType(int position) {
//        // 最后一个item设置为footerView
//        Log.e("adapter","return position is "+position);
//        return TYPE_ITEM;
////        if (position + 1 == getItemCount())
////        {
////            return TYPE_FOOTER;
////        } else if(position!=0){
////            return TYPE_ITEM;
////        }
////        return -1;
//    }



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
    public void onBindViewHolder(recyclerAdapter.MyViewHolder holder, final int position) {
        try{
            holder.title.setText(data.get(position).get("title").toString());
            holder.time.setText(data.get(position).get("time").toString());
            if(data.get(position).containsKey("place")){
                Log.e(TAG,"view set visible");
                holder.place.setVisibility(View.VISIBLE);
                holder.company.setVisibility(View.VISIBLE);
                holder.place.setText(data.get(position).get("place").toString());
                holder.company.setText(data.get(position).get("company").toString());
            }
            if(data.get(position).containsKey("work")){
                Log.e(TAG,"view set gone");
                holder.work.setVisibility(View.VISIBLE);
                holder.work.setText(data.get(position).get("work").toString());
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,time,place,company,work;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView)itemView.findViewById(R.id.time);
            place = (TextView)itemView.findViewById(R.id.place);
            company = (TextView)itemView.findViewById(R.id.company);
            work = (TextView)itemView.findViewById(R.id.work);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            place.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            work.setVisibility(View.GONE);
        }

    }
}
