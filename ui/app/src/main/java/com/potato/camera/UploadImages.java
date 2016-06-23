package com.potato.camera;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Message;

public class UploadImages extends Thread {
    private ImageOperateActivityHandler handler;
    private ArrayList<String> list;

    private String urlStr = "http://ec2-52-193-201-108.ap-northeast-1.compute.amazonaws.com/file/upload";
    private String server_save_path = null;

    public static final String boundary = "*****";
    public static final String br = "\r\n";
    public static final String twoHyphens = "--";

    public UploadImages(ArrayList<String> list,
                        ImageOperateActivityHandler handler, String server_save_path) {
        this.list = list;
        this.handler = handler;
        this.server_save_path = server_save_path;
    }

    @Override
    public void run() {
        DataOutputStream dos = null;
        InputStream is = null;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();
            conn = (HttpURLConnection) urlConn;

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary); // 类型和分割标志

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + br);

            dos.writeBytes("Content-Disposition: form-data; " + "name=\"path\""
                    + br);
            dos.writeBytes(br);
            dos.writeBytes(server_save_path + br);
            dos.writeBytes(twoHyphens + boundary + br);

            int len = list.size();
            for (int i = 0; i < len; i++) {
                String path = list.get(i);
                WebUtils.buildImageRawData("fileUpload" + i, dos, path);
                dos.writeBytes(br);
                if (i < len - 1)
                    dos.writeBytes(twoHyphens + boundary + br); // 最后一个图片，如果是http最末尾，则
            }

            dos.writeBytes(twoHyphens + boundary + twoHyphens + br); // http 最末尾
            dos.flush();

            is = conn.getInputStream();
            String result = WebUtils.inputStream2String(is);

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
                if (dos != null)
                    dos.close();
                if (is != null)
                    is.close();
                if (conn != null)
                    conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
