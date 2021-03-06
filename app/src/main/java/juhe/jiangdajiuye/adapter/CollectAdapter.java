package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.ui.SlipRecyclerView.SlipItemView;
import juhe.jiangdajiuye.ui.SlipRecyclerView.SlipRecyclerAdapter;
import juhe.jiangdajiuye.db.repository.CollectDepository;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-01-06
 */

public class CollectAdapter extends SlipRecyclerAdapter<MessageBean> {
    private String TAG = "CollectAdapter";
    private String margin = " ";
    public CollectAdapter(Context context, int view, List<MessageBean> data) {
        super(context, view, data);
    }

    @Override
    public void onBindViewHolder(SlipViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected SlipViewHolder onCreateSlipViewHolder(SlipItemView slipItemView, int viewType) {
        slipItemView.addDeleteSubView();
        return new ViewHolder(slipItemView);
    }

    @Override
    protected void onBindSlipViewHolder(SlipViewHolder holder, final int position, MessageBean messageBean) {
        if (!(holder instanceof ViewHolder))
            return;
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.theme.setVisibility(View.GONE);
        viewHolder.city.setVisibility(View.GONE);
        viewHolder.position.setVisibility(View.GONE);
        viewHolder.company.setVisibility(View.GONE);
        try {
            viewHolder.title.setText(messageBean.getTitle());
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
    }

    @Override
    protected void deleteData(MessageBean messageBean) {
        CollectDepository.getInstance().delete(messageBean);
    }

    private class ViewHolder extends SlipViewHolder {
        TextView title, time, position, company , city,theme;

        public ViewHolder(View itemView) {
            super(itemView);


            title = (TextView) getViewById(R.id.recycler_message_title);
            time = (TextView) getViewById(R.id.recycler_message_time);
            position = (TextView) getViewById(R.id.recycler_message_position);
            city = (TextView) getViewById(R.id.recycler_message_city);
            theme = (TextView) getViewById(R.id.recycler_message_theme);
            company = (TextView) getViewById(R.id.recycler_message_company);
            setTextViewColor(time,R.color.grey_600);
            setTextViewColor(position,R.color.grey_600);
            setTextViewColor(theme,R.color.grey_600);
            setTextViewColor(city,R.color.grey_600);
            setTextViewColor(company,R.color.grey_600);
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
}
