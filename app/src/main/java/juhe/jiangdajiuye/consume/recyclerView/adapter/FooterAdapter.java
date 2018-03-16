package juhe.jiangdajiuye.consume.recyclerView.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.SkinManager;

import static juhe.jiangdajiuye.R.id.footerProgressBar;

/**
 * class description here
 * 带有rooter view
 *
 * @author wangqiang
 * @since 2018-03-15
 */

public abstract class FooterAdapter<T> extends AbsAdapter<T> {
    private mFooterViewHolder footerViewHolder;
    private OnItemClickListener listener ;
    public FooterAdapter(Context mContext, int mLayout) {
        super(mContext, mLayout);
    }


    public interface OnItemClickListener<T> {
        void onClick(T t, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener ;
    }

    @Override
    public void bindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0)
            ((FooterViewHolder) holder).root.setVisibility(View.VISIBLE);
        else
            ((FooterViewHolder) holder).root.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder holder, final int position, final T t) {
        if (holder instanceof AbsAdapter.ItemViewHolder) {
            ((AbsAdapter.ItemViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener){
                        listener.onClick(t,position);
                    }
                }
            });
        }
    }

    @Override
    public void stateChange(int status) {
        mStatus = status;
        if (footerViewHolder == null) {
            return;
        }
        switch (status) {
            case STATUS_DEFAULT:
                footerViewHolder.tv.setText(ResourceUtils.getString(R.string.recycler_statues_pull_to_load_more));
                footerViewHolder.progressBar.setVisibility(View.GONE);
                break;
            case STATUS_REFRESHING:
                footerViewHolder.tv.setText(ResourceUtils.getString(R.string.recycler_statues_loading));
                footerViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
            case STATUS_END:
                footerViewHolder.tv.setText(ResourceUtils.getString(R.string.recycler_statues_not_any_more));
                footerViewHolder.progressBar.setVisibility(View.GONE);
                break;
            case STATUS_ERROR:
                footerViewHolder.tv.setText(ResourceUtils.getString(R.string.recycler_statues_network_error));
                footerViewHolder.progressBar.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public FooterViewHolder getFooterViewHolder(ViewGroup parent) {
        if (footerViewHolder == null) {
            footerViewHolder = new mFooterViewHolder(SkinManager.inflater(mContext,
                    R.layout.footer, parent, false));
            stateChange(mStatus);
        }
        return footerViewHolder;
    }

    public class mFooterViewHolder extends FooterViewHolder {
        public TextView tv;
        public View progressBar;

        public mFooterViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) getView(R.id.footerTitle);
            progressBar = getView(footerProgressBar);
        }
    }
}
