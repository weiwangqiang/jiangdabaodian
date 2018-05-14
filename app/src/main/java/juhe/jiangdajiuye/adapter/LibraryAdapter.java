package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.ui.recyclerView.adapter.FooterAdapter;


/**
 * class description here
 * <p>
 * 图书馆搜索列表
 *
 * @author wangqiang
 * @since 2017-08-08
 */

public class LibraryAdapter extends FooterAdapter<BookBean> {

    private static final String TAG = "LibraryAdapter";
    public mItemViewHolder itemViewHodler;
    public mFooterViewHolder footerViewHolder;

    public LibraryAdapter(Context mCtx, @LayoutRes int layout) {
        super(mCtx, layout);
    }

    /**
     * 获取ItemViewHolder
     *
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public BaseViewHolder getItemViewHolder(View parent) {
        return new mItemViewHolder(parent);
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
        ((mItemViewHolder) holder).book.setText((position + 1) + "." + data.getBook());
        ((mItemViewHolder) holder).editor.setText(data.getEditor());
        ((mItemViewHolder) holder).available.setText(data.getAvailable());
        ((mItemViewHolder) holder).number.setText(data.getNumber());
    }

    public class mItemViewHolder extends BaseViewHolder {
        TextView book, editor, available, number;

        public mItemViewHolder(View itemView) {
            super(itemView);
            book = (TextView) itemView.findViewById(R.id.search_book);
            editor = (TextView) itemView.findViewById(R.id.search_editor);
            available = (TextView) itemView.findViewById(R.id.search_available);
            number = (TextView) itemView.findViewById(R.id.search_number);
        }
    }
}
