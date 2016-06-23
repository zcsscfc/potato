package com.potato.camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class ImageUtils {

	/**
	 * 等比例缩放图片，当原始宽，高都小于预计缩放后宽，高时，不缩放；当宽大于预计时，按宽缩放；高大于预计时，按高缩放； 宽高都大于时，按比例大的缩放
	 *
	 * @param spath
	 *            原始图片路径
	 * @param newWidth
	 *            预计缩放后宽度
	 * @param newHeight
	 *            预计缩放后高度
	 * @param dpath
	 *            缩放后图片存放路径
	 * */
	public static void resize(String spath, int newWidth, int newHeight,
							  String dpath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(spath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BitmapFactory.decodeFile(spath, options);

		float rateW = (float) (options.outWidth / (float) (newWidth));
		float rateH = (float) (options.outHeight / (float) (newHeight));

		float rate = Math.max(rateW, rateH);

		if (rate <= 1) {
			rate = 1;
		}

		rate = (int) Math.ceil(rate); // first rate

		options.inJustDecodeBounds = false;
		options.inSampleSize = (int) rate;

		Bitmap bmp = BitmapFactory.decodeStream(fis, null, options);

		try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		rateW = (float)options.outWidth / (float)newWidth;
		rateH = (float)options.outHeight / (float)newHeight;

		rate = Math.max(rateW, rateH); // second rate

		if (rate <= 1) {
			rate = 1;
		}

		Matrix matrix = new Matrix();
		matrix.postScale((float) 1 / rate, (float) 1 / rate); // second rate
		matrix.postRotate(ImageUtils.readPictureDegree(spath));

		Bitmap bmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);
		File dfile = new File(dpath);
		try {
			FileOutputStream fos = new FileOutputStream(dfile);
			bmp2.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();

			bmp.recycle();
			bmp2.recycle();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] bitmap2Bytes(Bitmap bmp) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
		return os.toByteArray();
	}

	public static Bitmap rotateBitmap(Bitmap bmp, int angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap bmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);
		bmp.recycle();
		return bmp2;
	}

	/*
	 * 读取图片的旋转角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static byte[] inputStream2Bytes(InputStream is) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		os.close();
		is.close();
		return os.toByteArray();
	}
}
