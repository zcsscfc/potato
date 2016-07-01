package com.potato.camera;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.android.potato.R;

public class CustomAlbumActivity extends Activity {
	private ListView listviewPhotos;
	public static final String IMG_ID = "IMG_ID";
	public static final String IMG_NAME = "IMG_NAME";
	public static final String IMG_INFO = "IMG_INFO";
	private ArrayList<String> pathList = new ArrayList<String>();
	private CustomAlbumAdapter adapter;
	private Button btnConfirm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customalbum);

		listviewPhotos = (ListView) this.findViewById(R.id.listviewPhotos);
		listviewPhotos.setItemsCanFocus(false);

		listviewPhotos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckedTextView checkedTextView = (CheckedTextView) view
						.findViewById(R.id.itemChkImageInfo);
				checkedTextView.toggle();
				adapter.setCheckItem(position, checkedTextView.isChecked());
			}
		});

		try {
			String[] from = { IMG_ID, IMG_NAME, IMG_INFO };

			int[] to = { R.id.itemImgImageInfo, R.id.itemChkImageInfo,
					R.id.itemTxtImageInfo };

			adapter = new CustomAlbumAdapter(CustomAlbumActivity.this,
					GetImageList(), R.layout.list_item_customalbum, from, to);

			listviewPhotos.setAdapter(adapter);

		} catch (Exception ex) {
			return;
		}

		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(clickListener);
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.btnConfirm:
				Intent intent = new Intent();
				ArrayList<String> list = adapter.getSelected(pathList);
				intent.putStringArrayListExtra("path", list);
				setResult(RESULT_OK, intent);
				finish();
				break;
			}
		}
	};

	private ArrayList<Map<String, String>> GetImageList() {
		HashMap<String, String> map;
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

		String[] projection = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE };

		String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
		String[] selectionArg = { "image/jpeg" };

		Cursor cursor = getContentResolver().query(uri, projection, selection,
				selectionArg, MediaStore.Images.Media.DISPLAY_NAME);

		list.clear();
		if (cursor != null) {
			cursor.moveToFirst();
			while (cursor.getPosition() != cursor.getCount()) {

				map = new HashMap<String, String>();

				map.put(IMG_ID, cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media._ID)));
				map.put(IMG_NAME, cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
				map.put(IMG_INFO,
						" "
								+ formatFileSize(cursor.getLong(cursor
										.getColumnIndex(MediaStore.Images.Media.SIZE))));

				list.add(map);
				pathList.add(cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.DATA)));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return list;
	}

	private String formatFileSize(long size) {
		DecimalFormat f = new DecimalFormat("#.00");
		String str = "";
		if (size < 1024) {
			str = f.format((double) size) + "B";
		} else if (size < 1048576) {
			str = f.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			str = f.format((double) size / 1048576) + "M";
		} else {
			str = f.format((double) size / 1073741824) + "G";
		}
		return str;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};

	@Override
	public void finish() {
		adapter.destroyBitmap();
		super.finish();
	}
}