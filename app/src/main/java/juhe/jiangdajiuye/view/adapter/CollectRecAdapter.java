package juhe.jiangdajiuye.view.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.consume.SlipRecyclerView.SlipItemView;
import juhe.jiangdajiuye.consume.SlipRecyclerView.SlipRecyclerAdapter;
import juhe.jiangdajiuye.sql.repository.CollectRepository;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-01-06
 */

public class CollectRecAdapter extends SlipRecyclerAdapter<MessageItem> {
    private String TAG = "CollectRecAdapter";
    private String margin = " ";
    public CollectRecAdapter(Context context, int view, List<MessageItem> data) {
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
    protected void onBindSlipViewHolder(SlipViewHolder holder, final int position, MessageItem messageItem) {
        if (!(holder instanceof ViewHolder))
            return;
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.theme.setVisibility(View.GONE);
        viewHolder.city.setVisibility(View.GONE);
        viewHolder.position.setVisibility(View.GONE);
        viewHolder.company.setVisibility(View.GONE);
        try {
            viewHolder.title.setText(messageItem.getTitle());
            viewHolder.time.setText(messageItem.getTime());
            if(!TextUtils.isEmpty(messageItem.getCompany())){
                viewHolder.company.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(messageItem.getIndustry())){
                    viewHolder.company.setText(messageItem.getCompany()+"【"+messageItem.getIndustry()+"】");
                }else{
                    viewHolder.company.setText(messageItem.getCompany());
                }
            }
            if (!TextUtils.isEmpty(messageItem.getFrom()) || !TextUtils.isEmpty(messageItem.getLocate())) {
                viewHolder.position.setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder();
                if(!TextUtils.isEmpty(messageItem.getFrom())){
                    sb.append(messageItem.getFrom());
                    sb.append("  ") ;
                }
                if(!TextUtils.isEmpty(messageItem.getLocate())){
                    sb.append(messageItem.getLocate());
                }
                viewHolder.position.setText(sb.toString());
            }
            if(!TextUtils.isEmpty(messageItem.getCity())){
                viewHolder.city.setVisibility(View.VISIBLE);
                viewHolder.city.setText(messageItem.getCity());
            }
            if(!TextUtils.isEmpty(messageItem.getTheme())){
                viewHolder.theme.setVisibility(View.VISIBLE);
                viewHolder.theme.setText(messageItem.getTheme());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void deleteData(MessageItem messageItem) {
        CollectRepository.getInstance().delete(messageItem);
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
