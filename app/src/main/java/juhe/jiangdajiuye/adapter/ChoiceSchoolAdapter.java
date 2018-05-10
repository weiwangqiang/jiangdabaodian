package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-03-18
 */

public class ChoiceSchoolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> data = new ArrayList<>();
    private int layout ;
    private Context context ;
    private ItemClick itemClick ;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public ChoiceSchoolAdapter(Context context, int layout){
        this.layout = layout ;
        this.context = context ;
    }
    public void append(List<String> data){
        this.data.addAll(data) ;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(layout,null,false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.school.setText(data.get(position));
        myHolder.school.setOnClickListener(new View.OnClickListener() {
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
        public TextView school ;
        public MyHolder(View itemView) {
            super(itemView);
            school = itemView.findViewById(R.id.item_choice_school_name);

        }
    }
    public interface ItemClick{
        void onItemClick(String school,int position);
    }
}
