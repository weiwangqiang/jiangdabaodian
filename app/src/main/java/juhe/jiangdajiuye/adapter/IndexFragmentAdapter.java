package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.consume.recyclerView.adapter.AbsAdapter;
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.util.SkinManager;

import static juhe.jiangdajiuye.R.id.footerProgressBar;


/**
 * class description here
 *
 *   首页的fragment 的adapter
 *
 * @author wangqiang
 * @since 2017-08-08
 */

public class IndexFragmentAdapter extends AbsAdapter<MessageItem> {

    public mItemViewHodler itemViewHodler ;
    public mFooterViewHolder footerViewHolder ;
    private Context mCtx ;
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OnItemClickListener itemClickListener ;
    public interface  OnItemClickListener{
        void OnItemClick(MessageItem item);
    }

    public IndexFragmentAdapter(Context mCtx ,@LayoutRes int layout) {
        super(layout);
        this.mCtx = mCtx ;
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
        return new mItemViewHodler(SkinManager.inflater(mCtx,mLayout));
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
            footerViewHolder = new mFooterViewHolder(SkinManager.inflater(mCtx,
                    R.layout.footer,parent,false));
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
    public void bindItemViewHolder(RecyclerView.ViewHolder holder, int position, final MessageItem data) {
        if(!(holder instanceof mItemViewHodler)) return;
        try{
            ((mItemViewHodler)holder).title.setText(data.getTitle());
            ((mItemViewHodler)holder).time.setText(data.getTime());
            if(null != data.getLocate()){
                ((mItemViewHodler)holder).place.setVisibility(View.VISIBLE);
                ((mItemViewHodler)holder).place.setText(data.getLocate());
            }
            if(data.getTheme() != null){
                ((mItemViewHodler)holder).company.setText(data.getTheme());
                ((mItemViewHodler)holder).company.setVisibility(View.VISIBLE);
            }
            else if(data.getFrom() != null){
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
        ((mItemViewHodler)holder).root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!= itemClickListener );
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
            ((FooterViewHolder)holder).root.setVisibility(View.VISIBLE);
        else
            ((FooterViewHolder)holder).root.setVisibility(View.INVISIBLE);

    }

    public class mItemViewHodler extends ItemViewHolder{
        TextView title,time,place,company,work;
        private mItemViewHodler(View itemView) {
            super(itemView);
            root = getView(R.id.card_view);
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
        public View progressBar ;
        public mFooterViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) getView(R.id.footerTitle);
            progressBar = getView(footerProgressBar);
        }
    }
    @Override
    public void stateChange(int state) {
        if(footerViewHolder == null) return;
        switch (state){
            case STATUS_DEFAULT:
                footerViewHolder.tv.setText("上拉加载更多");
                footerViewHolder.progressBar.setVisibility(View.GONE);
                break;
            case STATUS_REFRESHING:
                footerViewHolder.tv.setText("正在加载");
                footerViewHolder.progressBar.setVisibility(View.VISIBLE);

                break;
            case STATUS_END:
                footerViewHolder.tv.setText("没有更多了");
                footerViewHolder.progressBar.setVisibility(View.GONE);
                break;
            case STATUS_ERROR:
                footerViewHolder.tv.setText("网络出错，请连接后再试");
                footerViewHolder.progressBar.setVisibility(View.GONE);
                break;
        }
    }

}
