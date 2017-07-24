package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import juhe.jiangdajiuye.InterFace.myitemLister;
import juhe.jiangdajiuye.R;

/**图书馆
 * Created by wangqiang on 2016/9/28.
 */
public class ly_recyclerAdapter extends  RecyclerView.Adapter<ly_recyclerAdapter.MyViewHolder> {
    private myitemLister lister;
    private MyViewHolder viewHolder;
    private Context context ;
    private int view;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private ArrayList<Map<String,String>> data ;
    public ly_recyclerAdapter(Context context, int view, ArrayList<Map<String,String>> data){
        this.context = context;
        this.view = view;
        this.data = data;
    }

    @Override
    public ly_recyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            viewHolder = new MyViewHolder(LayoutInflater.
                    from(context).inflate(view,parent,false));
            return viewHolder;
        }
        // type == TYPE_FOOTER 返回footerView
        else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));
            return new MyViewHolder(view);
        }
        return viewHolder;
    }
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
         if (position + 1 == getItemCount())
        {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }



    public void setLister(myitemLister lister){
        this.lister = lister;
    }
    @Override
    public void onBindViewHolder(ly_recyclerAdapter.MyViewHolder holder,final int position) {
        try
        {
            holder.book.setText(data.get(position).get("book").toString());
            holder.editor.setText(data.get(position).get("editor").toString());
            holder.available.setText(data.get(position).get("available").toString());
            holder.number.setText(data.get(position).get("number").toString());
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
        return data.size()+1;
    }
    public void RefreshDate (ArrayList<Map<String,String>> data){
        this.data = data;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView book,editor,available,number;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            book = (TextView) itemView.findViewById(R.id.search_book);
            editor = (TextView)itemView.findViewById(R.id.search_editor);
            available = (TextView)itemView.findViewById(R.id.search_available);
            number = (TextView)itemView.findViewById(R.id.search_number);
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
