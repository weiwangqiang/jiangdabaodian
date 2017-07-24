package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import juhe.jiangdajiuye.R;

/**
 * Created by wangqiang on 2016/9/28.
 */
public class recyclerView extends  RecyclerView.Adapter<recyclerView.MyViewHolder> {
    private MyViewHolder viewHolder;
    private Context context ;
    private int view;
    private ArrayList<Map<String,String>> data ;
    public recyclerView(Context context, int view, ArrayList<Map<String,String>> data){
        this.context = context;
        this.view = view;
        this.data = data;
    }

    @Override
    public recyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewHolder = new MyViewHolder(LayoutInflater.
                from(context).inflate(view,parent,false));
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(recyclerView.MyViewHolder holder, int position) {
        holder.title.setText(data.get(position).get("title").toString());
        holder.time.setText(data.get(position).get("time").toString());
    }

    /**
     * Returns the total number of items in the data set hold by the juhe.jiangdajiuye.adapter.
     *
     * @return The total number of items in this juhe.jiangdajiuye.adapter.
     */
    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView)itemView.findViewById(R.id.time);
        }
    }
}
