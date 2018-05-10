package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.ui.recyclerView.adapter.AbsAdapter;
import juhe.jiangdajiuye.base.BaseApplication;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.SkinManager;

import static juhe.jiangdajiuye.R.id.footerProgressBar;


/**
 * class description here
 *
 * 图书馆搜索列表
 *
 * @author wangqiang
 * @since 2017-08-08
 */

public class SearchLibraryAdapter extends AbsAdapter<BookBean> {

    private static final String TAG = "SearchLibraryAdapter";
    public mItemViewHolder itemViewHodler ;
    public mFooterViewHolder footerViewHolder ;
    private Context mCtx ;
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OnItemClickListener itemClickListener ;
    public interface  OnItemClickListener{
        void OnItemClick(BookBean item);
    }

    public SearchLibraryAdapter(Context mCtx , @LayoutRes int layout) {
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
        return new mItemViewHolder(SkinManager.inflater(mCtx,mLayout,parent,false));
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
            footerViewHolder = new mFooterViewHolder(SkinManager.inflater(BaseApplication.getContext(),
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
    public void bindItemViewHolder(RecyclerView.ViewHolder holder, int position, final BookBean data) {
        if(!(holder instanceof mItemViewHolder)) return;
        try
        {
            ((mItemViewHolder) holder).book.setText((position+1)+"."+data.getBook());
            ((mItemViewHolder) holder).editor.setText(data.getEditor());
            ((mItemViewHolder) holder).available.setText(data.getAvailable());
            ((mItemViewHolder) holder).number.setText(data.getNumber());
        }catch(Exception e){
            e.printStackTrace();
        }
        ((mItemViewHolder)holder).root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!= itemClickListener )
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

    public class mItemViewHolder extends ItemViewHolder{
        TextView book,editor,available,number;
        public mItemViewHolder(View itemView) {
            super(itemView);
            book = (TextView) itemView.findViewById(R.id.search_book);
            editor = (TextView)itemView.findViewById(R.id.search_editor);
            available = (TextView)itemView.findViewById(R.id.search_available);
            number = (TextView)itemView.findViewById(R.id.search_number);
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
    public void stateChange(int status) {
        mStatus = status;
        if (footerViewHolder == null) {
            return;
        }
        switch (status) {
            case STATUS_PULL_TO_REFRESH:
            case STATUS_DEFAULT:
//                footerViewHolder.tv.setText(ResourceUtils.getString(R.string.recycler_statues_pull_to_load_more));
//                footerViewHolder.progressBar.setVisibility(View.GONE);
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
}
