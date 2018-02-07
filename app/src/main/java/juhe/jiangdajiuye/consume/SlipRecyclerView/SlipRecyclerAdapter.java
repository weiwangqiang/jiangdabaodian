package juhe.jiangdajiuye.consume.SlipRecyclerView;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SlipRecyclerAdapter
 */
public abstract class SlipRecyclerAdapter<T> extends RecyclerView.Adapter<SlipRecyclerAdapter.SlipViewHolder> {
    private String TAG = "SlipRecyclerAdapter";
    private int openPosition = -1;//记录打开的位置
    private int view;
    public boolean hasChildOpen = false;//记录是否有 item 打开
    protected List<T> data;
    private SlipItemClickListener<T> lister;
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

    public SlipRecyclerAdapter() {
        super();
    }

    public SlipRecyclerAdapter(Context context, @LayoutRes int view, List<T> data) {
        this.context = context;
        this.view = view;
        this.data = data;
    }

    @Override
    public SlipRecyclerAdapter.SlipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).inflate(view, parent, false);
        SlipItemView slipItemView = new SlipItemView(context);
        slipItemView.addItemView(itemView);
        return onCreateSlipViewHolder(slipItemView, viewType);
    }

    public void setOnItemLister(SlipItemClickListener<T> lister) {
        this.lister = lister;
    }

    @Override
    public void onBindViewHolder(final SlipRecyclerAdapter.SlipViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: ---------------------");
        if (!holder.getSlipItemView().isClose())
            holder.getSlipItemView().close();
        onBindSlipViewHolder(holder, data.get(position));
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
                        deleteData(data.get(position));
                        data.remove(position);
                        notifyDataSetChanged();
                        changeToCloseState();
                        holder.getSlipItemView().close();
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
                changeSlipItemState(isClose, position);
            }
        });
    }
    private void changeToCloseState(){
        changeSlipItemState(true,-1);
    }
    //改变子view的打开状态
    private void changeSlipItemState(boolean isClose, int position) {
        openPosition = position;
        if (!isClose) {
            hasChildOpen = true;
        } else {
            hasChildOpen = false;
        }
    }

    //    @Override
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

    public void RefreshDate(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    //关闭所有的侧滑栏
    public void closeSlipItem() {
        if (openPosition == -1 || openPosition >= getItemCount()){
            changeToCloseState();
            return;
        }
        notifyItemChanged(openPosition, 1);
        changeToCloseState();
    }
    //初始化的时候应当传入SlipItemView 对象，否则需要判断getSlipItemView() 方法获得的View 是否为null
    public abstract class SlipViewHolder extends RecyclerView.ViewHolder {
        protected SlipItemView slipItemView = null;
        protected View itemView;//保存item的主view
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

    protected abstract SlipViewHolder onCreateSlipViewHolder(SlipItemView slipItemView, int viewType);

    protected abstract void onBindSlipViewHolder(SlipViewHolder holder, final T t);
    //删除数据源，如数据库对应的对象
    protected abstract void deleteData(T t);
}
