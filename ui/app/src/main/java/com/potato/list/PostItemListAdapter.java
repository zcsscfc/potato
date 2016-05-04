package com.potato.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.android.potato.R;

public class PostItemListAdapter extends BaseAdapter {
    private Context context;
    private List<PostItem> postItemList;
    private LayoutInflater layoutInflater;

    public PostItemListAdapter(Context context, List<PostItem> postItemList) {
        this.context = context;
        this.postItemList = postItemList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return postItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return postItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.post_item, null);
            holder = new ViewHolder();
            holder.imageViewThumb = (ImageView) convertView.findViewById(R.id.imageViewThumb);
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.textViewOrigin = (TextView) convertView.findViewById(R.id.textViewOrigin);
            holder.textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PostItem postItem = postItemList.get(position);
        holder.textViewTitle.setText(postItem.getTitle());
        holder.textViewTime.setText(postItem.getTime());
        holder.textViewOrigin.setText(postItem.getOrigin());
        return convertView;
    }

    public class ViewHolder {
        TextView textViewTitle;
        TextView textViewOrigin;
        TextView textViewTime;
        ImageView imageViewThumb;
    }
}
