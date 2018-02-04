package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.consume.SlipRecyclerView.SlipItemClickListener;
import juhe.jiangdajiuye.consume.SlipRecyclerView.SlipItemView;
import juhe.jiangdajiuye.sql.repository.CollectRepository;

/**
 * SlipRecyclerAdapter
 */
public class CollectRecyclerAdapter extends RecyclerView.Adapter<CollectRecyclerAdapter.SlipViewHolder> {
    private String TAG = "SlipRecyclerAdapter";
    private int openPosition = -1;//记录打开的位置
    private int view;
    public boolean hasChildOpen = false;//记录是否有 item 打开
    protected List<MessageItem> data;
    private SlipItemClickListener<MessageItem> lister;
    private Context context;
    //item id常量
    private final int ID_ITEM = 0x10;
    private final int ID_DELETE = 0x20;
    private final int ID_COLLECT = 0x30;

    //获取处于打开状态的item位置
    public int getOpenPosition() {
        return openPosition;
    }

    public boolean isHasChildOpen() {
        return hasChildOpen;
    }

    public CollectRecyclerAdapter() {
        super();
    }

    public CollectRecyclerAdapter(Context context, @LayoutRes int view, List<MessageItem> data) {
        this.context = context;
        this.view = view;
        this.data = data;
    }

    @Override
    public CollectRecyclerAdapter.SlipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).inflate(view, parent, false);
        SlipItemView slipItemView = new SlipItemView(context);
        slipItemView.addItemView(itemView);
        slipItemView.addDeleteSubView();
        return new SlipViewHolder(slipItemView);
    }

    public void setOnItemLister(SlipItemClickListener<MessageItem> lister) {
        this.lister = lister;
    }

    @Override
    public void onBindViewHolder(CollectRecyclerAdapter.SlipViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: ---------------------");
        if (!holder.getSlipItemView().isClose())
            holder.getSlipItemView().close();

        try{
            holder.title.setText(data.get(position).getTitle());
            holder.time.setText(data.get(position).getTime());
            if(null != data.get(position).getLocate() ){
                holder.place.setVisibility(View.VISIBLE);
                holder.company.setVisibility(View.VISIBLE);
                holder.place.setText(data.get(position).getLocate());
                holder.company.setText(data.get(position).getFrom());
            }
            else
            {
                holder.place.setVisibility(View.GONE);
                holder.company.setVisibility(View.GONE);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        holder.getSlipItemView().setOnSlipItemViewClickListener(new SlipItemView.OnSlipItemViewClickListener() {

            /**
             * 点击删除
             *
             * @param view
             */
            @Override
            public void onItemClick(View view) {
                switch ((int) view.getTag()) {
                    case ID_DELETE:
                        CollectRepository.getInstance().delete(data.get(position));
                        data.remove(position);
                        notifyDataSetChanged();
                        break;
                    case ID_COLLECT:
                        break;
                    case ID_ITEM:
                        if (lister != null) {
                            lister.onItemClick(data.get(position), position);
                        }
                        break;
                    default:
                        lister.onSubViewClick(view);
                        break;
                }
            }

            /**
             * 滑动时候
             * @param isClose 是否处于关闭状态
             */
            @Override
            public void onStateChange(boolean isClose) {
                if (!isClose) {
                    openPosition = position;
                    hasChildOpen = true;
                } else {
                    openPosition = -1;
                    hasChildOpen = false;
                }
            }
        });
    }

        @Override
    public void onBindViewHolder(SlipViewHolder holder, int position, List<Object> payloads) {
        Log.i(TAG, "onBindViewHolder: ===========================");
        if (!payloads.isEmpty())
            holder.getSlipItemView().close();
        else
            super.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void RefreshDate(List<MessageItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    //关闭所有的侧滑栏
    public void closeSlipItem() {
        if (openPosition == -1 || openPosition >= getItemCount())
            return;
        notifyItemChanged(openPosition, 1);
        hasChildOpen = false;
        openPosition = -1;
    }

    public class SlipViewHolder extends RecyclerView.ViewHolder {
        protected SlipItemView slipItemView = null;
        protected View itemView;//保存item的主view
        TextView title,time,place,company,work;
        protected Map<Integer, View> viewContainer = new HashMap<>();//保存view

        public SlipItemView getSlipItemView() {
            return slipItemView;
        }

        public SlipViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof SlipItemView) {
                this.slipItemView = (SlipItemView) itemView;
                this.itemView = slipItemView.getItemView();
            } else {
                this.itemView = itemView;
            }

            title = (TextView) getViewById(R.id.title);
            time = (TextView)getViewById(R.id.time);
            place = (TextView)getViewById(R.id.place);
            company = (TextView)getViewById(R.id.company);
        }

        public View getViewById(int viewId) {
            View view = viewContainer.get(viewId);
            if (null == view) {
                view = itemView.findViewById(viewId);
                viewContainer.put(viewId, view);
            }
            return view;
        }
    }
}
