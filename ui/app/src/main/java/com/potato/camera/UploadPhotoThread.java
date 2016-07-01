package com.potato.camera;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.os.Message;

import com.android.potato.AppConfig;

public class UploadPhotoThread extends Thread {
    private UploadPhotoHandler handler = null;
    private ArrayList<String> list = null;

    private String api = AppConfig.SERVER_URL + "file/upload";
    private String savePath = null;

    public static final String boundary = "*****";
    public static final String br = "\r\n";
    public static final String twoHyphens = "--";

    public UploadPhotoThread(ArrayList<String> list, UploadPhotoHandler handler, String savePath) {
        this.list = list;
        this.handler = handler;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        DataOutputStream dataOutputStream = null;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(api);
            URLConnection urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary); // 类型和分割标志

            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

            dataOutputStream.writeBytes(twoHyphens + boundary + br);

            dataOutputStream.writeBytes("Content-Disposition: form-data; " + "name=\"path\""
                    + br);
            dataOutputStream.writeBytes(br);
            dataOutputStream.writeBytes(savePath + br);
            dataOutputStream.writeBytes(twoHyphens + boundary + br);

            int len = list.size();
            for (int i = 0; i < len; i++) {
                String path = list.get(i);
                WebUtils.buildImageRawData("fileUpload" + i, dataOutputStream, path);
                dataOutputStream.writeBytes(br);
                if (i < len - 1)
                    dataOutputStream.writeBytes(twoHyphens + boundary + br); // 最后一个图片，如果是http最末尾，则
            }

            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + br); // http 最末尾
            dataOutputStream.flush();

            inputStream = httpURLConnection.getInputStream();
            String result = WebUtils.inputStream2String(inputStream);

            Message msg = Message.obtain();
            msg.what = handler.msg_upload_success;
            msg.obj = result;
            handler.sendMessage(msg);

        } catch (Exception e) {
            Message msg = Message.obtain();
            msg.what = handler.msg_upload_fail;
            msg.obj = e.toString();
            handler.sendMessage(msg);
        } finally {
            try {
                if (dataOutputStream != null)
                    dataOutputStream.close();
                if (inputStream != null)
                    inputStream.close();
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
