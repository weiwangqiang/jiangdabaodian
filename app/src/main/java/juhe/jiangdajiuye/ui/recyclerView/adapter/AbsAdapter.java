package juhe.jiangdajiuye.ui.recyclerView.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class description here
 * <p>
 * adapter的抽象类
 *
 * @author wangqiang
 * @project fourAssembly
 * @since 2017-08-08
 */
public abstract class AbsAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "AbsAdapter";
    protected int mLayout;
    protected Context mContext;
    protected final int TYPE_ITEM = 1;
    protected final int TYPE_FOOTER = 2;
    protected int mStatus = 0;
    public static final int STATUS_DEFAULT = 0;//不在刷新或者end状态
    public static final int STATUS_PULL_DOWN_TO_REFRESH = 0x10; //下拉刷新状态
    public static final int STATUS_PULL_UP_TO_REFRESH = 0x11;//上拉刷新状态
    public static final int STATUS_END = 0x12;//没有更多状态
    public static final int STATUS_ERROR = 0x13;//出错状态
    private List<T> data = new ArrayList<>();
    protected OnItemClickListener listener;
    private boolean needFooter = true;//是否需要footer view

    public void setNeedFooter(boolean needFooter) {
        this.needFooter = needFooter;
    }

    public interface OnItemClickListener<T> {
        void onClick(View view, T t, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public int getDataSize() {
        return data.size();
    }

    public AbsAdapter(Context mContext, @LayoutRes int mLayout) {
        this.mLayout = mLayout;
        this.mContext = mContext;
    }


    public void upDate(List<T> list) {
        data.clear();
        appendData(list);
    }

    public void appendData(List<T> list) {
        appendData(data.size(), list);
    }

    public void appendData(int position, List<T> list) {
        if (list != null) {
            data.addAll(position, list);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return getItemViewHolder(LayoutInflater.from(mContext).
                        inflate(mLayout, parent, false));
            case TYPE_FOOTER:
                return getFooterViewHolder(parent);
            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position + 1 == getItemCount() && needFooter) {
            bindFooterViewHolder(holder, position);
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onClick(v, data.get(position), position);
                    }
                }
            });
            bindItemViewHolder(holder, position, data.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && needFooter) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : needFooter ? data.size() + 1 :data.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public View root;
        public Map<Integer, View> views;

        public BaseViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            views = new HashMap<>();
        }

        protected <T extends View> T getView(int id) {
            View v = views.get(id);
            if (v == null)
                v = root.findViewById(id);
            return (T) v;
        }
    }

    public abstract void stateChange(int status);


    /**
     * 获取ItemViewHolder
     *
     * @return
     */

    public abstract @NonNull
    BaseViewHolder getItemViewHolder(View parent);

    /**
     * 获取footerView的holder
     *
     * @param parent
     * @return
     */
    public abstract @NonNull
    BaseViewHolder getFooterViewHolder(ViewGroup parent);

    /**
     * 绑定Item View
     * 可以在这里设置点击事件之类的
     *
     * @param holder
     * @param position
     */
    protected abstract void bindItemViewHolder(RecyclerView.ViewHolder holder, int position, T t);

    /**
     * 绑定footerView
     *
     * @param holder
     * @param position
     */
    protected abstract void bindFooterViewHolder(RecyclerView.ViewHolder holder, int position);
}
