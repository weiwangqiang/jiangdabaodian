package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.SkinManager;

import static juhe.jiangdajiuye.R.id.footerProgressBar;


/**
 * class description here
 * <p>
 * 首页的fragment 的adapter
 *
 * @author wangqiang
 * @since 2017-08-08
 */

public class IndexAdapter extends AbsAdapter<MessageBean> {
    private static final String TAG = "IndexAdapter";
    private mFooterViewHolder footerViewHolder;
    private Context mCtx;
    private OnItemClickListener itemClickListener;
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(MessageBean item,int position);
    }

    public IndexAdapter(Context mCtx, @LayoutRes int layout) {
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
        if (footerViewHolder == null) {
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
     * @param messageBean
     */
    @Override
    public void bindItemViewHolder(RecyclerView.ViewHolder holder, final int position, final MessageBean messageBean) {
        if (!(holder instanceof mItemViewHolder)) {
            return;
        }
        mItemViewHolder viewHolder = (mItemViewHolder) holder;
        viewHolder.theme.setVisibility(View.GONE);
        viewHolder.city.setVisibility(View.GONE);
        viewHolder.position.setVisibility(View.GONE);
        viewHolder.company.setVisibility(View.GONE);
        try {
            viewHolder.title.setText(messageBean.getTitle());
            viewHolder.title.setTextColor(mCtx.getResources()
                    .getColor(messageBean.getHasBrowse() ?
                            R.color.grey_600:R.color.grey_900));
            viewHolder.time.setText(messageBean.getTime());
            if(!TextUtils.isEmpty(messageBean.getCompany())){
                viewHolder.company.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(messageBean.getIndustry())){
                    viewHolder.company.setText(messageBean.getCompany()+"【"+ messageBean.getIndustry()+"】");
                }else{
                    viewHolder.company.setText(messageBean.getCompany());
                }
            }
            if (!TextUtils.isEmpty(messageBean.getFrom()) || !TextUtils.isEmpty(messageBean.getLocate())) {
                viewHolder.position.setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder();
                if(!TextUtils.isEmpty(messageBean.getFrom())){
                    sb.append(messageBean.getFrom());
                    sb.append("  ") ;
                }
                if(!TextUtils.isEmpty(messageBean.getLocate())){
                    sb.append(messageBean.getLocate());
                }
                viewHolder.position.setText(sb.toString());
            }
            if(!TextUtils.isEmpty(messageBean.getCity())){
                viewHolder.city.setVisibility(View.VISIBLE);
                viewHolder.city.setText(messageBean.getCity());
            }
            if(!TextUtils.isEmpty(messageBean.getTheme())){
                viewHolder.theme.setVisibility(View.VISIBLE);
                viewHolder.theme.setText(messageBean.getTheme());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((mItemViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.OnItemClick(messageBean,position);
                }
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
        TextView title, time, position, city,theme,company;

        private mItemViewHolder(View itemView) {
            super(itemView);
            root = getView(R.id.card_view);
            title = (TextView) getView(R.id.recycler_message_title);
            time = (TextView) getView(R.id.recycler_message_time);
            position = (TextView) getView(R.id.recycler_message_position);
            city = (TextView) getView(R.id.recycler_message_city);
            theme = (TextView) getView(R.id.recycler_message_theme);
            company = (TextView) getView(R.id.recycler_message_company);
            setTextViewColor(time,R.color.grey_600);
            setTextViewColor(position,R.color.grey_600);
            setTextViewColor(city,R.color.grey_600);
            setTextViewColor(company,R.color.grey_600);
            setTextViewColor(theme,R.color.grey_600);
        }
    }
    // 修改textView left drawable的颜色
    public static void setTextViewColor(TextView view, int colorResId) {
        //mutate()
        Drawable modeDrawable = view.getCompoundDrawables()[0].mutate();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList =     ColorStateList.valueOf(view.getResources().getColor(colorResId));
        DrawableCompat.setTintList(temp, colorStateList);
        view.setCompoundDrawables(temp,null,null,null);
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
        mStatus = status;
        if (footerViewHolder == null) {
            return;
        }
        switch (status) {
            case STATUS_PULL_DOWN_TO_REFRESH:
            case STATUS_DEFAULT:
//                footerViewHolder.tv.setText(ResourceUtils.getString(R.string.recycler_statues_pull_to_load_more));
//                footerViewHolder.progressBar.setVisibility(View.GONE);
//                break;
            case STATUS_PULL_UP_TO_REFRESH:
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

}
