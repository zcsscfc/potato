package com.potato.list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.android.potato.AppConfig;
import com.android.potato.R;
import com.potato.image.EasyImageLoader;

public class PostItemListAdapter extends BaseAdapter {
    private List<PostItem> postItemList;
    private LayoutInflater layoutInflater;
    private Context context;


    public PostItemListAdapter(Context context, List<PostItem> postItemList) {
        this.postItemList = postItemList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
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
        holder.textViewOrigin.setText(postItem.getOriginName());
        String thumb = postItem.getThumb();
        if (thumb != null && thumb != "") {
            holder.imageViewThumb.setVisibility(View.VISIBLE);
            String imageUrl = AppConfig.SERVER_URL2 + thumb;
            //根据图片url给imageView加载图片，自动本地缓存、内存缓存
            try {
                EasyImageLoader.getInstance(context).bindBitmap(imageUrl, holder.imageViewThumb);
            } catch (Exception ex) {

            }

//重载方法加载图片并根据需求宽高压缩图片
            //EasyImageLoader.getInstance(context).bindBitmap(imageUrl, imageView,reqWidth,reqHeight);

//根据url自动从内存缓存、本地缓存、网络获取bitmap，并回调
//        EasyImageLoader.getInstance(context).getBitmap(imageUrl, new EasyImageLoader.BitmapCallback() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//                //保存bitmap到本地
//                saveBitmap(bitmap);
//            }
//        });
        }else {
            holder.imageViewThumb.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView textViewTitle;
        TextView textViewOrigin;
        TextView textViewTime;
        ImageView imageViewThumb;
    }
}
