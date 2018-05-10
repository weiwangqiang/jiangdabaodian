package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.ui.SlipRecyclerView.SlipItemView;
import juhe.jiangdajiuye.ui.SlipRecyclerView.SlipRecyclerAdapter;
import juhe.jiangdajiuye.db.repository.LibraryDepository;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-01-06
 */

public class LibraryColAdapter extends SlipRecyclerAdapter<BookBean> {
    public LibraryColAdapter(Context context, int view, ArrayList<BookBean> data) {
        super(context, view, data);
    }

    @Override
    public void onBindViewHolder(SlipViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public SlipViewHolder onCreateSlipViewHolder(SlipItemView slipItemView, int viewType) {
        slipItemView.addDeleteSubView();
        return new ViewHolder(slipItemView);
    }

    @Override
    public void onBindSlipViewHolder(SlipViewHolder holder,final int position, BookBean  bookBean) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.book.setText((position + 1) + "."+bookBean.getBook());
            viewHolder.editor.setText(bookBean.getEditor());
            viewHolder.available.setText(bookBean.getAvailable());
            viewHolder.number.setText(bookBean.getNumber());
        }
    }

    @Override
    protected void deleteData(BookBean bookBean) {
        LibraryDepository.getInstance().delete(bookBean);
    }

    private class ViewHolder extends SlipViewHolder{
        TextView book,editor,available,number;

        public ViewHolder(View itemView) {
            super(itemView);
            book = (TextView) getViewById(R.id.search_book);
            editor = (TextView) getViewById(R.id.search_editor);
            available = (TextView) getViewById(R.id.search_available);
            number = (TextView) getViewById(R.id.search_number);
        }
    }
}
