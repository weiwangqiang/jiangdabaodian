package juhe.jiangdajiuye.consume.SlipRecyclerView;

import android.view.View;

/**
 * class description here
 * slipRecyclerView item的点击事件
 * @author wangqiang
 * @since 2018-01-06
 */

public interface SlipItemClickListener<T> {
    void onItemClick(T t,int position);
    void onSubViewClick(View view);
}
