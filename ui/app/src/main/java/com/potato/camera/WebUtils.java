package com.potato.camera;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WebUtils {
	private String sessionId = "";
	private String wzxg = "";
	public static final String boundary = "*****";
	public static final String br = "\r\n";
	public static final String twoHyphens = "--";
	public HttpURLConnection conn;

	public WebUtils(String sessionId, String wzxg) {
		this.sessionId = sessionId;
		this.wzxg = wzxg;
	}

	public WebUtils() {

	}

	public String DoPost(String urlStr, Map<String, String> paras) {
		String result = "";
		try {
			URL url = new URL(urlStr);
			try {
				URLConnection urlConn = url.openConnection();
				conn = (HttpURLConnection) urlConn;
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				if (sessionId.trim() != "" && wzxg.trim() != "")
					conn.setRequestProperty("Cookie", sessionId + ";" + wzxg);
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				String data = BuildQuery(paras);
				dos.write(data.getBytes());
				dos.flush();
				dos.close();
				InputStream is = conn.getInputStream();
				result = inputStream2String(is);
				is.close();
				conn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String DoGet(String urlStr, Map<String, String> paras) {
		String result = "";
		if (paras != null && paras.size() > 0) {
			if (urlStr.contains("?")) {
				urlStr = urlStr + "&" + BuildQuery(paras);
			} else {
				urlStr = urlStr + "?" + BuildQuery(paras);
			}
		}
		try {
			URL url = new URL(urlStr);
			try {
				URLConnection urlConn = url.openConnection();
				conn = (HttpURLConnection) urlConn;
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Cookie", sessionId + ";" + wzxg);
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				InputStream is = conn.getInputStream();
				result = inputStream2String(is);
				is.close();
				conn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String inputStream2String(InputStream is) {
		StringBuffer sb = new StringBuffer();
		byte[] buff = new byte[4096];
		try {
			for (int n; (n = is.read(buff)) != -1;) {
				sb.append(new String(buff, 0, n));
			}
		} catch (Exception e) {

		}
		return sb.toString();
	}

	public static byte[] inputStream2Bytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			is.close();
		} catch (IOException e) {

		}
		return baos.toByteArray();
	}

	private String BuildQuery(Map<String, String> paras) {
		boolean first = true;
		String str = "";
		for (String key : paras.keySet()) {
			if (!first) {
				str = str + "&";
			}
			str = str + key;
			str = str + "=";
			str = str + paras.get(key);
			first = false;
		}
		return str;
	}

	public static Bitmap getHttpBitmap(String urlStr) {
		Bitmap bmp = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(6000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			InputStream is = conn.getInputStream();
			bmp = BitmapFactory.decodeStream(is);
			is.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public static void buildImageRawData(String name, DataOutputStream dos,
			String path) {
		try {
			dos.writeBytes("Content-Disposition: form-data; " + "name=\""
					+ name + "\";filename=\"test.jpg\"" + br);
			dos.writeBytes(br);
			FileInputStream fis = new FileInputStream(path);
			byte[] buff = new byte[1024];
			int len = -1;
			while ((len = fis.read(buff)) != -1) {
				dos.write(buff, 0, len);
			}
			fis.close();
		} catch (Exception e) {

		}
	}
}
