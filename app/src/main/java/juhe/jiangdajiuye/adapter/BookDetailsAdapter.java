package juhe.jiangdajiuye.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;

/**
 * 图书馆详情列表
 */
public class BookDetailsAdapter implements ExpandableListAdapter {
    Context context;
    String header;
    List<List<String>> list;
    List<String> itme_text1 = new ArrayList<String>();

    public BookDetailsAdapter(Context context, String header,
                              List<List<String>> list) {
        this.context = context;
        this.header = header;
        this.list = list;

        itme_text1.add("索书号");
        itme_text1.add("条码号");
        itme_text1.add("年卷期");
        itme_text1.add("校区—馆藏地");
        itme_text1.add("书刊状态̬");
    }

    @Override
    public boolean areAllItemsEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        Log.e("", "------getChild��������---" + list.get(groupPosition).get(childPosition));
        return list.get(groupPosition).get(childPosition);
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    /**
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(context).inflate(
                R.layout.library_search_list_itme, null);
        TextView text1 = (TextView) v.findViewById(R.id.search_list_itme_text1);
        TextView text2 = (TextView) v.findViewById(R.id.search_list_itme_text2);
        text1.setText(itme_text1.get(childPosition));
        text2.setText(list.get(groupPosition).get(childPosition));
        return v;
    }

    /**
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */

    @Override
    public int getChildrenCount(int position) {
        // TODO Auto-generated method stub
        return list.get(position).size();
    }

    @Override
    public long getCombinedChildId(long arg0, long arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getCombinedGroupId(long arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        Log.e("", "------getGroup��������---" + header);
        return list.get(groupPosition).get(3);
    }

    /**
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    /**
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    /**
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        convertView = LayoutInflater.from(context).inflate(
                R.layout.library_search_list_group, null);
        TextView text = (TextView) convertView.findViewById
                (R.id.library_search_list_header_text);
        ImageView image = (ImageView) convertView.findViewById
                (R.id.library_search_list_header_image);

        if (!isExpanded) {
            image.setBackgroundResource(R.drawable.list_extend_off);
        } else {
            image.setBackgroundResource(R.drawable.list_extend_on);
        }
        Log.e("", "------getGroupview��������---" + header);
        text.setText(list.get(groupPosition).get(3));
        return convertView;
    }

    /**
     * @return
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onGroupCollapsed(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGroupExpanded(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerDataSetObserver(DataSetObserver arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver arg0) {
        // TODO Auto-generated method stub

    }

}
