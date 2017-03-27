package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/10/1.
 */
public class listViewAdapter extends SimpleAdapter {
    private Context context;
    private int resource;
    private String[] from;
    private int[] to;
    private List<? extends Map<String, ?>> data;

    public listViewAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.resource = resource;
        this.from = from;
        this.to = to ;
        this.data =data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        return null;
    }
}
