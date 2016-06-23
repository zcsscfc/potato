package com.potato.camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.potato.R;

public class CustomAlbumAdapter extends SimpleAdapter {
	private SparseBooleanArray boolArr;
	private List<Integer> stateList;
	private List<? extends Map<String, ?>> dataList;
	private Context context;
	private Bitmap bmp;
	private ViewGroup vgNull = null;

	public CustomAlbumAdapter(Context context, List<Map<String, String>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		boolArr = new SparseBooleanArray();
		dataList = data;
		for (int i = 0; i < data.size(); i++) {
			boolArr.put(i, false);
		}
		stateList = new ArrayList<Integer>();
		this.context = context;
	}

	public ArrayList<String> getSelected(ArrayList<String> filepathList) {
		ArrayList<String> lst = new ArrayList<String>();
		int count = stateList.size();
		for (int i = 0; i < count; i++) {
			int pos = stateList.get(i);
			String s = filepathList.get(pos);
			lst.add(s);
		}
		return lst;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setCheckItem(int position, Boolean isChecked) {
		boolArr.put(position, isChecked);
		if (stateList.contains(position))
			stateList.remove((Object) position);
		if (isChecked) {
			stateList.add(position);
		}
	}

	public long[] getCheckItemIds() {
		int count = stateList.size();
		long[] ids = new long[count];
		for (int i = 0; i < count; i++) {
			ids[i] = (long) stateList.get(i);
		}
		return ids;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_item_customalbum, vgNull);
		}
		CheckedTextView chkChoose = (CheckedTextView) convertView
				.findViewById(R.id.itemChkImageInfo);
		chkChoose.setChecked(boolArr.get(position));
		chkChoose.setText((String) dataList.get(position).get(
				CustomAlbumActivity.IMG_NAME));

		TextView labInfo = (TextView) convertView
				.findViewById(R.id.itemTxtImageInfo);
		labInfo.setText((String) dataList.get(position).get(
				CustomAlbumActivity.IMG_INFO));

		ImageView imageview = (ImageView) convertView
				.findViewById(R.id.itemImgImageInfo);

		bmp = Images.Thumbnails.getThumbnail(
				context.getContentResolver(),
				Long.parseLong((String) dataList.get(position).get(
						CustomAlbumActivity.IMG_ID)),
				Images.Thumbnails.MICRO_KIND, null);

		BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
		if (drawable != null) {
			Bitmap bmp2 = drawable.getBitmap();
			if (null != bmp2 && !bmp2.isRecycled()) {
				bmp2.recycle();
				bmp2 = null;
			}
		}

		imageview.setImageBitmap(null);
		imageview.setImageBitmap(bmp);

		return convertView;
	}

	public void destroyBitmap() {
		if (bmp != null)
			bmp.recycle();
	}
}
