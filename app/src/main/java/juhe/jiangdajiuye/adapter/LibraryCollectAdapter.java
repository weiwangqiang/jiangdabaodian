package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.InterFace.MyItemLister;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.consume.ItemScrollView;
import juhe.jiangdajiuye.sql.repository.LibraryRepository;

/**
 * 图书馆 adapter
 * Created by wangqiang on 2016/9/28.
 */
public class LibraryCollectAdapter extends RecyclerView.Adapter<LibraryCollectAdapter.MyViewHolder> {
    private String TAG = "LibraryCollectAdapter";
    private MyItemLister lister;
    private MyViewHolder viewHolder;
    private Context context;
    private int view;
    //id常量
    private final int ID_ITEMVIEW = 0x10;
    private final int ID_DELETE = 0x20;
    private final int ID_COLLECT = 0x30;

    public int getOpenPosition() {
        return openPosition;
    }

    private int openPosition = -1;//记录需要关闭的位置,默认不需要关闭
    private ArrayList<BookBean> data;

    public boolean isHasChildOpen() {
        return hasChildOpen;
    }

    public boolean hasChildOpen = false;

    public LibraryCollectAdapter(Context context, int view, ArrayList<BookBean> data) {
        this.context = context;
        this.view = view;
        this.data = data;
    }

    @Override
    public LibraryCollectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).inflate(view, parent, false);
        ItemScrollView itemScrollView = new ItemScrollView(context);
        itemScrollView.addItemView(itemView);
        itemScrollView.addDeleteSubItem();
        viewHolder = new MyViewHolder(itemScrollView);
        return viewHolder;
    }

    public void setLister(MyItemLister lister) {
        this.lister = lister;
    }

    @Override
    public void onBindViewHolder(LibraryCollectAdapter.MyViewHolder holder, final int position) {
        holder.itemScrollView.close();
        try {

            holder.getMySubItemHolder().book.setText((position + 1) + "." + data.get(position).getBook());
            holder.getMySubItemHolder().editor.setText(data.get(position).getEditor());
            holder.getMySubItemHolder().available.setText(data.get(position).getAvailable());
            holder.getMySubItemHolder().number.setText(data.get(position).getNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemScrollView.setOnSubItemClickListener(new ItemScrollView.OnSubItemClickListener() {

            /**
             * 点击删除
             *
             * @param view
             */
            @Override
            public void onItemClick(View view) {
                switch ((int) view.getTag()) {
                    case ID_DELETE:
                        deleteData(position);
                        break;
                    case ID_COLLECT:
                        break;
                    case ID_ITEMVIEW:
                        if (lister != null) {
                            lister.ItemLister(position);
                        }
                        break;
                    default:
                        break;
                }

            }

            /**
             * 滑动时候
             * @param isClose 是否处于关闭状态
             */
            @Override
            public void onStateChange(boolean isClose) {
                if (!isClose) {
                    openPosition = position;
                    hasChildOpen = true;
                } else {
                    openPosition = -1;
                    hasChildOpen = false;
                }
            }
        });
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Partial bind vs full bind:
     * <p>
     * The payloads parameter is a merge list from {@link #notifyItemChanged(int, Object)} or
     * {@link #notifyItemRangeChanged(int, int, Object)}.  If the payloads list is not empty,
     * the ViewHolder is currently bound to old data and Adapter may run an efficient partial
     * update using the payload info.  If the payload is empty,  Adapter must run a full bind.
     * Adapter should not assume that the payload passed in notify methods will be received by
     * onBindViewHolder().  For example when the view is not attached to the screen, the
     * payload in notifyItemChange() will be simply dropped.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param payloads A non-null list of merged payloads. Can be empty list if requires full
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        Log.i(TAG, "onBindViewHolder: ===========================");
        if(!payloads.isEmpty())
        holder.getItemScrollView().close();
        else
        super.onBindViewHolder(holder, position, payloads);
    }

    private void deleteData(int position) {
        LibraryRepository.getInstance().delete(data.get(position).getUrl());
        data.remove(position);
       notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void RefreshDate(ArrayList<BookBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemScrollView getItemScrollView() {
            return itemScrollView;
        }

        ItemScrollView itemScrollView;
        View view;

        public MySubItemHolder getMySubItemHolder() {
            return mySubItemHolder;
        }

        MySubItemHolder mySubItemHolder;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof ItemScrollView) {
                this.itemScrollView = (ItemScrollView) itemView;
                mySubItemHolder = new MySubItemHolder(itemScrollView.getItemView());
            } else {
                this.view = itemView;
                mySubItemHolder = new MySubItemHolder(view);
            }
        }

        class MySubItemHolder {
            TextView book, editor, available, number;

            public MySubItemHolder(View itemView) {
                book = (TextView) itemView.findViewById(R.id.search_book);
                editor = (TextView) itemView.findViewById(R.id.search_editor);
                available = (TextView) itemView.findViewById(R.id.search_available);
                number = (TextView) itemView.findViewById(R.id.search_number);
            }
        }
    }

    //关闭所有的侧滑栏
    public void closeSubItem() {
        Log.i(TAG, "closeSubItem: " + openPosition);
        if (openPosition == -1 || openPosition>=getItemCount())
            return;
        notifyItemChanged(openPosition,1);
        hasChildOpen = false;
        openPosition = -1;
    }
}
