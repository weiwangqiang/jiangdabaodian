package juhe.jiangdajiuye.consume.recyclerView.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class description here
 *
 *  adapter的抽象类
 *
 * @author wangqiang
 * @project fourAssembly
 * @since 2017-08-08
 */
public abstract class AbsAdapter<T extends Object>  extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "AbsAdapter";
    protected  int mLayout ;
    protected  final int TYPE_ITEM = 1;
    protected  final int TYPE_FOOTER = 2;

    public static final int STATUS_DEFAULT = 0;//不在刷新或者end状态
    public static final int STATUS_REFRESHING = 0x10 ;
    public static final int STATUS_PULLTOREFRESH = 0x11;
    public static final int STATUS_END = 0x12;
    public static final int STATUS_ERROR = 0x13;
    public int getDataSize() {
        return data == null ? 0 : data.size() ;
    }

    private List<T> data;
    public AbsAdapter(@LayoutRes int layout) {
        mLayout = layout ;
    }

    public void clearAll(){
        if(data == null) return;
        data.clear();
    }
    public void upDate(List<T> list){
        data = new ArrayList<>(list);
        notifyDataSetChanged();
    }
    public void appendDate(List<T> list){
        appendDate(list,list.size()) ;

    }
    public void appendDate(List<T> list,int position){
        if(data == null){
            data = new ArrayList<>(list) ;
        }else{
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_ITEM:
                return  getItemViewHolder(parent);
            case TYPE_FOOTER:
                return  getFooterViewHolder(parent);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position+1 == getItemCount()){
            bindFooterViewHolder(holder,position);
        }else
            bindItemViewHolder(holder,position,data.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1 == getItemCount())
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        if(data == null) {
            return 0 ;
        }
        else{
            return data.size() == 0 ? 0 : data.size() + 1;
        }
    }

    public class ItemViewHolder extends  RecyclerView.ViewHolder{
        public View root ;
        public Map<Integer,View> views ;
        public ItemViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            views = new HashMap<>();
        }
        protected View getView(int id){
            View v = views.get(id);
            if(v == null)
                v = root.findViewById(id);
            return v ;
        }
    }

    public class FooterViewHolder extends  RecyclerView.ViewHolder{
        public View root;
       public  Map<Integer,View> views ;
        public FooterViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            views = new HashMap<>();
        }
        protected View getView(int id){
            View v = views.get(id);
            if(v == null)
                v = root.findViewById(id);
            return v ;
        }
    }
    public abstract void stateChange(int state);


    /**
     * 获取ItemViewHolder
     * @return
     */

    public abstract @NonNull
    ItemViewHolder getItemViewHolder(ViewGroup parent);

    /**
     *  获取footerView的holder
     * @param parent
     * @return
     */
    public abstract @NonNull
    FooterViewHolder getFooterViewHolder(ViewGroup parent);
    /**
     * 绑定Item View
     * 可以在这里设置点击事件之类的
     * @param holder
     * @param position
     */
    protected abstract void bindItemViewHolder(RecyclerView.ViewHolder holder, int position , T t);

    /**
     * 绑定footerView
     * @param holder
     * @param position
     */
    protected abstract void bindFooterViewHolder(RecyclerView.ViewHolder holder, int position);
}
