package com.potato.camera;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.android.potato.R;

public class CustomCameraActivity extends Activity {
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private ImageButton btnTakePhotoAction;
	private int rotation = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		surfaceView = (SurfaceView) findViewById(R.id.sviewPhoto);
		btnTakePhotoAction = (ImageButton) findViewById(R.id.btnTakePhotoAction);
		surfaceView.setOnClickListener(listener);
		btnTakePhotoAction.setOnClickListener(listener);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(sholderCallback);
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnTakePhotoAction:
				if (rotation == -1)
					rotation = 90;
				else if ((0 <= rotation && rotation <= 45)
						|| (315 <= rotation && rotation <= 360))
					rotation = 90;
				else if ((45 <= rotation && rotation <= 135))
					rotation = 180;
				else if ((135 <= rotation && rotation <= 225))
					rotation = 270;
				else if ((225 <= rotation && rotation <= 315))
					rotation = 0;
				Camera.Parameters parameters = camera.getParameters();
				parameters.setRotation(rotation);
				camera.setParameters(parameters);
				camera.takePicture(null, null, takePhotoCallback);
				break;
			case R.id.sviewPhoto:
				if (camera != null) {
					camera.cancelAutoFocus();
					camera.autoFocus(pre_focus);
				}
			}
		}
	};

	AutoFocusCallback pre_focus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				camera.cancelAutoFocus();
			} else {

			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_CAMERA
				|| keyCode == KeyEvent.KEYCODE_SEARCH) {
			camera.takePicture(null, null, takePhotoCallback);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	PictureCallback takePhotoCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			camera.stopPreview();

			Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
					new ContentValues());
			try {
				OutputStream os = getContentResolver().openOutputStream(uri);
				os.write(data);
				os.flush();
				os.close();
			} catch (IOException e) {

			}

			String path = getFilePathByContentResolver(
					CustomCameraActivity.this, uri.toString());
			uri = Uri.fromFile(new File(path));

			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			intent.setData(uri);
			CustomCameraActivity.this.sendBroadcast(intent); // �������¼

			ArrayList<String> list = new ArrayList<String>();
			list.add(path);
			intent.putStringArrayListExtra("path", list);
			setResult(RESULT_OK, intent);

			data = null;
			finish();
		}
	};

	private String getFilePathByContentResolver(Context context, String uri) {
		if (null == uri) {
			return null;
		}
		Cursor c = context.getContentResolver().query(Uri.parse(uri), null,
				null, null, null);
		String filePath = null;
		if (null == c) {
			throw new IllegalArgumentException("Query on " + uri
					+ " returns null result.");
		}
		try {
			if ((c.getCount() != 1) || !c.moveToFirst()) {
			} else {
				filePath = c.getString(c
						.getColumnIndexOrThrow(MediaColumns.DATA));
			}
		} finally {
			c.close();
		}
		return filePath;
	}

	SurfaceHolder.Callback sholderCallback = new SurfaceHolder.Callback() {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			camera = Camera.open();
			camera.stopPreview();
			try {
				// camera.setDisplayOrientation(90);
				Camera.Parameters paras = camera.getParameters();

				if (CustomCameraActivity.this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
					paras.set("orientation", "portrait");
				} else {
					paras.set("orientation", "landscape");
				}
				paras.setPictureFormat(ImageFormat.JPEG);
				// paras.setPictureSize(320, 240);
				// List<Size> ls = paras.getSupportedPictureSizes();
				// parameters.setPictureSize(ls.get(1).width, ls.get(1).height);
				paras.setRotation(90);
				paras.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
				camera.setParameters(paras);
				camera.setPreviewDisplay(holder);
			} catch (IOException e) {
				camera.release();
				camera = null;
			}
			camera.startPreview();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			camera.autoFocus(pre_focus);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	};
}