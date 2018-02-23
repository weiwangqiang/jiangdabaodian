package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.consume.recyclerView.adapter.AbsAdapter;
import juhe.jiangdajiuye.util.ResourceUtils;
import juhe.jiangdajiuye.util.SkinManager;

import static juhe.jiangdajiuye.R.id.footerProgressBar;


/**
 * class description here
 * <p>
 * 首页的fragment 的adapter
 *
 * @author wangqiang
 * @since 2017-08-08
 */

public class IndexFragmentAdapter extends AbsAdapter<MessageItem> {

    private static final String TAG = "IndexFragmentAdapter";
    private mFooterViewHolder footerViewHolder;
    private Context mCtx;
    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(MessageItem item);
    }

    public IndexFragmentAdapter(Context mCtx, @LayoutRes int layout) {
        super(layout);
        this.mCtx = mCtx;
    }

    /**
     * 获取ItemViewHolder
     *
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public ItemViewHolder getItemViewHolder(ViewGroup parent) {
        Log.i(TAG, "getItemViewHolder: ");
        return new mItemViewHolder(SkinManager.inflater(mCtx, mLayout));
    }

    /**
     * 获取footerView的holder
     *
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public FooterViewHolder getFooterViewHolder(ViewGroup parent) {
        Log.i(TAG, "getFooterViewHolder: ");
        if (footerViewHolder == null){
            footerViewHolder = new mFooterViewHolder(SkinManager.inflater(mCtx,
                    R.layout.footer, parent, false));
            stateChange(mStatus);
        }
        return footerViewHolder;
    }

    /**
     * 绑定Item View
     *
     * @param holder
     * @param position
     * @param data
     */
    @Override
    public void bindItemViewHolder(RecyclerView.ViewHolder holder, int position, final MessageItem data) {
        if (!(holder instanceof mItemViewHolder)) return;
        try {
            ((mItemViewHolder) holder).title.setText(data.getTitle());
            ((mItemViewHolder) holder).time.setText(data.getTime());
            if (null != data.getLocate()) {
                ((mItemViewHolder) holder).place.setVisibility(View.VISIBLE);
                ((mItemViewHolder) holder).place.setText(data.getLocate());
            }
            if (data.getTheme() != null) {
                ((mItemViewHolder) holder).company.setText(data.getTheme());
                ((mItemViewHolder) holder).company.setVisibility(View.VISIBLE);
            } else if (data.getFrom() != null) {
                ((mItemViewHolder) holder).company.setText(data.getFrom());
                ((mItemViewHolder) holder).company.setVisibility(View.VISIBLE);
            }
            if (data.getIndustry() != null) {
                ((mItemViewHolder) holder).work.setVisibility(View.VISIBLE);
                ((mItemViewHolder) holder).work.setText(data.getIndustry());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ((mItemViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) ;
                itemClickListener.OnItemClick(data);
            }
        });
    }


    /**
     * 绑定footerView
     *
     * @param holder
     * @param position
     */
    @Override
    public void bindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0)
            ((FooterViewHolder) holder).root.setVisibility(View.VISIBLE);
        else
            ((FooterViewHolder) holder).root.setVisibility(View.INVISIBLE);

    }

    public class mItemViewHolder extends ItemViewHolder {
        TextView title, time, place, company, work;

        private mItemViewHolder(View itemView) {
            super(itemView);
            root = getView(R.id.card_view);
            title = (TextView) getView(R.id.title);
            time = (TextView) getView(R.id.time);
            place = (TextView) getView(R.id.place);
            company = (TextView) getView(R.id.company);
            work = (TextView) getView(R.id.work);
            place.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            work.setVisibility(View.GONE);
        }
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

    @Override
    public void stateChange(int status) {
        Log.i(TAG, "stateChange: ");
        mStatus = status ;
        if (footerViewHolder == null) {
            Log.i(TAG, "stateChange: footer holder is null ");
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
                Log.i(TAG, "stateChange: end and not any more ");
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

}
