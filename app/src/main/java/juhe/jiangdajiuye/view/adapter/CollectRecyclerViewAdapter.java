package juhe.jiangdajiuye.view.adapter;

import android.content.Context;
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

public class CollectRecyclerViewAdapter extends SlipRecyclerAdapter<MessageItem> {
    private String TAG = "CollectRecyclerViewAdapter";

    public CollectRecyclerViewAdapter(Context context, int view, List<MessageItem> data) {
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
        try {
            viewHolder.title.setText(messageItem.getTitle());
            viewHolder.time.setText(messageItem.getTime());
            if (null != messageItem.getLocate()) {
                viewHolder.place.setVisibility(View.VISIBLE);
                viewHolder.company.setVisibility(View.VISIBLE);
                viewHolder.place.setText(messageItem.getLocate());
                viewHolder.company.setText(messageItem.getFrom());
            } else {
                viewHolder.place.setVisibility(View.GONE);
                viewHolder.company.setVisibility(View.GONE);
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
        TextView title, time, place, company, work;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) getViewById(R.id.title);
            time = (TextView) getViewById(R.id.time);
            place = (TextView) getViewById(R.id.place);
            company = (TextView) getViewById(R.id.company);
//          cardView = (CardView) getViewById(R.id.card_view);
        }
    }
}
