package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import juhe.jiangdajiuye.bean.CompanyUrl;
import juhe.jiangdajiuye.ui.recyclerView.adapter.FooterAdapter;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-05-10
 */
public class CompanyUrlAdapter extends FooterAdapter<CompanyUrl> {

    public CompanyUrlAdapter(Context mContext, int mLayout) {
        super(mContext, mLayout);
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
        return new MyViewHolder(parent);
    }

    /**
     * 绑定Item View
     * 可以在这里设置点击事件之类的
     *
     * @param holder
     * @param position
     * @param companyUrl
     */
    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder holder,
                                      int position, CompanyUrl companyUrl) {
        MyViewHolder myViewHolder  = (MyViewHolder) holder;
        myViewHolder.companyName.setText(companyUrl.getCompanyName());
        myViewHolder.companyType.setText(companyUrl.getCompanyType());
    }
    private class  MyViewHolder extends BaseViewHolder{
        private TextView companyName ,companyType ;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
