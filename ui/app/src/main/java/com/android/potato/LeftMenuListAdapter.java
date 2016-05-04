package com.android.potato;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by s1112001 on 2016/5/3.
 */
public class LeftMenuListAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public LeftMenuListAdapter(Context context,List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应leftmenu_item.xml中的控件
     */
    public final class leftMenu{
        public ImageView iv_icon;
        public TextView tv_name;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        leftMenu LeftMenu=null;
        if(convertView==null){
            LeftMenu=new leftMenu();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.leftmenu_item, null);
            LeftMenu.iv_icon=(ImageView)convertView.findViewById(R.id.imageViewThumb);
            LeftMenu.tv_name=(TextView)convertView.findViewById(R.id.textViewTitle);
            convertView.setTag(LeftMenu);
        }else{
            LeftMenu=(leftMenu)convertView.getTag();
        }
        //绑定数据
        LeftMenu.iv_icon.setBackgroundResource((Integer)data.get(position).get("iv_icon"));
        LeftMenu.tv_name.setText((String)data.get(position).get("tv_name"));
        return convertView;
    }

}
