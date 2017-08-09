package juhe.jiangdajiuye.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.consume.recyclerView.adapter.AbsAdapter;
import juhe.jiangdajiuye.entity.MessageItem;
import juhe.jiangdajiuye.util.SkinManager;


/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-08
 */

public class SDImpAdapter extends AbsAdapter<MessageItem> {
    public static final int  STATUS_DEFAULT = 0 ;
    public static final int STATUS_REFRESHING  = 1;
    public static final int STATUS_END  = 2;

    public mItemViewHodler itemViewHodler ;
    public mFooterViewHolder footerViewHolder ;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OnItemClickListener itemClickListener ;
    public interface  OnItemClickListener{
        void OnItemClick(MessageItem item);
    }

    public SDImpAdapter(@LayoutRes int layout) {
        super(layout);
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
        return new mItemViewHodler(SkinManager.inflater(mLayout));
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
        if(footerViewHolder == null)
            footerViewHolder = new mFooterViewHolder(SkinManager.inflater(R.layout.footer));
        return footerViewHolder ;
    }

    /**
     * 绑定Item View
     *
     * @param holder
     * @param position
     * @param data
     */
    @Override
    public void bindItemViewHolder(RecyclerView.ViewHolder holder, int position, MessageItem data) {
        if(!(holder instanceof mItemViewHodler)) return;
        try{
            ((mItemViewHodler)holder).title.setText(data.getTitle());
            ((mItemViewHodler)holder).time.setText(data.getTime());
            if(null != data.getLocate()){
                ((mItemViewHodler)holder).place.setVisibility(View.VISIBLE);
                ((mItemViewHodler)holder).place.setText(data.getLocate());
            }
            if(data.getFrom() != null){
                ((mItemViewHodler)holder).company.setText(data.getFrom());
                ((mItemViewHodler)holder).company.setVisibility(View.VISIBLE);
            }
            if(data.getIndustry()!= null){
                ((mItemViewHodler)holder).work.setVisibility(View.VISIBLE);
                ((mItemViewHodler)holder).work.setText(data.getIndustry());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
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
            ((FooterViewHolder)holder).root.setVisibility(View.VISIBLE);
        else
            ((FooterViewHolder)holder).root.setVisibility(View.INVISIBLE);

    }

    public class mItemViewHodler extends ItemViewHolder{
        TextView title,time,place,company,work;

        private mItemViewHodler(View itemView) {
            super(itemView);
            title = (TextView) getView(R.id.title);
            time = (TextView) getView(R.id.time);
            place = (TextView)getView(R.id.place);
            company = (TextView)getView(R.id.company);
            work = (TextView)getView(R.id.work);
            place.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            work.setVisibility(View.GONE);
        }
    }
    public class mFooterViewHolder extends FooterViewHolder{
        public TextView tv;
        public mFooterViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) getView(R.id.footerTitle);
        }
    }
    public void setStatus(int status){
        if(footerViewHolder == null) return;
        switch (status){
            case STATUS_DEFAULT:
                footerViewHolder.tv.setText("上卡加载更多");
                break;
            case STATUS_REFRESHING:
                footerViewHolder.tv.setText("正在加载");
                break;
            case STATUS_END:
                footerViewHolder.tv.setText("没有更多了");
                break;
        }
    }
}
