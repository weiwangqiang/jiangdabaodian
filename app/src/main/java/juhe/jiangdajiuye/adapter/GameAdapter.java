package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;


/**
 * Created by wangqiang on 2016/6/12.
 * 主界面list 的adapter
 */
public class GameAdapter extends SimpleAdapter
{
    private String TAG  = "GameAdapter";
    String filePath = Environment.getExternalStorageDirectory()+"/baodian/pictures";
    Context context;
    List<? extends Map<String ,?>> data ;
    int resource;
    String[] from;
    int[] to;
    ViewHolder holder;


    public GameAdapter(Context context, List<? extends Map<String ,?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.data  = data;
        this.resource = resource;
        this.from  =from;
        this.to = to;
    }
    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        if(null ==convertView){
            convertView = LayoutInflater.from(context).inflate(resource, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(to[0]);
            holder.title    = (TextView) convertView.findViewById(to[1]);
            holder.message   = (TextView) convertView.findViewById(to[2]);
            holder.visit  = (TextView) convertView.findViewById(to[3]);
            convertView.setTag(holder);
        }

        else
            holder = (ViewHolder) convertView.getTag();
        String url = this.data.get(position).get(from[0]).toString();
        Glide.with(context).load(url).into(holder.imageView);
        holder.title.setText(this.data.get(position).get(from[1]).toString());
        holder.message.setText(this.data.get(position).get(from[2]).toString());
        holder.visit.setText(this.data.get(position).get(from[3]).toString());
        return convertView;

    }
    private class ViewHolder{
        ImageView imageView;
        TextView title;
        TextView message;
        TextView visit;
    }
}
