package com.android.potato;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.potato.camera.CustomAlbumActivity;
import com.potato.camera.UploadPhotoHandler;
import com.potato.camera.ImageUtils;
import com.potato.camera.UploadPhotoThread;
import com.potato.model.UploadImageInput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity extends Activity {
    private static final int REQ_CODE_IMAGE_CHOOSE = 1;
    private static final int REQ_CODE_IMAGE_TAKE = 2;
    private UserInfoShared userInfoShared = null;
    private ImageView imageViewPhoto = null;
    private UploadPhotoHandler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Button buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        ImageButton imageButtonGoBack = (ImageButton) findViewById(R.id.imageButtonGoBack);
        TextView textViewNickName = (TextView) findViewById(R.id.textViewNickName);
        TableRow tableRowNickName = (TableRow) findViewById(R.id.tableRowNickName);
        TextView textViewLogName = (TextView) findViewById(R.id.textViewLogName);
        TableRow tableRowWeChat = (TableRow) findViewById(R.id.tableRowWeChat);
        TableRow tableRowMobile = (TableRow) findViewById(R.id.tableRowMobile);
        TableRow tableRowPassWord = (TableRow) findViewById(R.id.tableRowPassWord);
        TableRow tableRowPhoto = (TableRow) findViewById(R.id.tableRowPhoto);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

        userInfoShared = new UserInfoShared(this);
        handler = new UploadPhotoHandler(this);

        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintResource(R.color.color_11);

        textViewLogName.setText(userInfoShared.getUserName());
        textViewNickName.setText(userInfoShared.getNickName());
        SetImageViewPhoto(userInfoShared.getPhotoDiskPath());

        tableRowNickName.setOnClickListener(onClickListener);
        tableRowWeChat.setOnClickListener(onClickListener);
        tableRowMobile.setOnClickListener(onClickListener);
        tableRowPassWord.setOnClickListener(onClickListener);
        tableRowPhoto.setOnClickListener(onClickListener);
        imageButtonGoBack.setOnClickListener(onClickListener);
        buttonLogOut.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonLogOut:
                    userInfoShared.edit();
                    userInfoShared.clear(); // 清除所有讯息
                    userInfoShared.commit();
                    Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(UserInfoActivity.this, "注销成功！", Toast.LENGTH_LONG).show();
                    break;
                case R.id.imageButtonGoBack:
                    finish();
                    break;
                case R.id.tableRowPhoto:
                    new AlertDialog.Builder(UserInfoActivity.this)
                            .setTitle("编辑头像")
                            .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    String savePath = "user/" + userInfoShared.getUserId() + "/photo";
                                    String photoPath = APP_BASE_DISK_PATH + savePath + "/temp.jpg";
                                    File photoFile = new File(photoPath);
                                    Uri photoUri = Uri.fromFile(photoFile);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                    startActivityForResult(intent, REQ_CODE_IMAGE_TAKE);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Intent intent = new Intent();
                                        intent.setClass(UserInfoActivity.this, CustomAlbumActivity.class);
                                        startActivityForResult(intent, REQ_CODE_IMAGE_CHOOSE);
                                        dialog.dismiss();
                                    } catch (Exception ex) {
                                        Log.e("lance_test", ex.toString());
                                    }
                                }
                            })
                            .show();
                    break;
                case R.id.tableRowPassWord:
                    new AlertDialog.Builder(UserInfoActivity.this)
                            .setTitle("多选框")
                            .setMultiChoiceItems(new String[]{"选项1", "选项2", "选项3", "选项4"}, null, null)
                            .setPositiveButton("确定", null)
                            .setNegativeButton("取消", null)
                            .show();
                    break;
                case R.id.tableRowMobile:
                    new AlertDialog.Builder(UserInfoActivity.this)
                            .setTitle("多选框")
                            .setMultiChoiceItems(new String[]{"选项1", "选项2", "选项3", "选项4"}, null, null)
                            .setPositiveButton("确定", null)
                            .setNegativeButton("取消", null)
                            .show();
                    break;
                case R.id.tableRowWeChat:
                    new AlertDialog.Builder(UserInfoActivity.this)
                            .setTitle("多选框")
                            .setMultiChoiceItems(new String[]{"选项1", "选项2", "选项3", "选项4"}, null, null)
                            .setPositiveButton("确定", null)
                            .setNegativeButton("取消", null)
                            .show();
                    break;
                case R.id.tableRowNickName:
                    new AlertDialog.Builder(UserInfoActivity.this)
                            .setTitle("多选框")
                            .setMultiChoiceItems(new String[]{"选项1", "选项2", "选项3", "选项4"}, null, null)
                            .setPositiveButton("确定", null)
                            .setNegativeButton("取消", null)
                            .show();
                    break;
            }
        }
    };

    public void UpdateUserInfo(final UploadImageInput uploadImageInput) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String json = new Gson().toJson(uploadImageInput, UploadImageInput.class);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .header("X-Token", userInfoShared.getToken())
                            .url(AppConfig.SERVER_URL + "user/edit")
                            .post(body).build();
                    Response response = okHttpClient.newCall(request).execute();
                    String rspStr = response.body().string();
                } catch (Exception ex) {
                    Log.e("lance_test", ex.toString());
                }
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String savePath = "user/" + userInfoShared.getUserId() + "/photo";
        if (requestCode == REQ_CODE_IMAGE_TAKE && resultCode == RESULT_OK) {
            try {
                String path = APP_BASE_DISK_PATH + savePath + "/temp.jpg";
                ArrayList<String> list = new ArrayList<String>();
                String photoDiskPath = SavePhotoMobileDisk(path, savePath, 100, 100);
                SetImageViewPhoto(photoDiskPath);
                list.add(photoDiskPath);
                userInfoShared.edit();
                userInfoShared.setPhotoDiskPath(photoDiskPath);
                userInfoShared.commit();
                new UploadPhotoThread(list, handler, savePath).start();
            } catch (Exception ex) {
                Log.e("E000000005", ex.toString());
            }
        } else if (requestCode == REQ_CODE_IMAGE_CHOOSE && resultCode == RESULT_OK) {
            ArrayList<String> photoPathList = data.getStringArrayListExtra("path");
            int len = photoPathList.size();
            if (len > 0) {
                String photoDiskPath = SavePhotoMobileDisk(photoPathList.get(0), savePath, 100, 100);
                SetImageViewPhoto(photoDiskPath);
                photoPathList.clear();
                photoPathList.add(photoDiskPath);
                userInfoShared.edit();
                userInfoShared.setPhotoDiskPath(photoDiskPath);
                userInfoShared.commit();
            }
            new UploadPhotoThread(photoPathList, handler, savePath).start();
        }
    }

    private void SetImageViewPhoto(String photoDiskPath) {
        if (photoDiskPath == null || photoDiskPath == "") {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(photoDiskPath);
        } catch (FileNotFoundException e) {
            Log.e("E000000001", e.toString());
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);
        try {
            fileInputStream.close();
        } catch (IOException e) {
            Log.e("E000000002", e.toString());
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageViewPhoto.getDrawable();
        if (bitmapDrawable != null) {
            Bitmap bitmap2 = bitmapDrawable.getBitmap();
            if (null != bitmap2 && !bitmap2.isRecycled()) {
                bitmap2.recycle();
                bitmap2 = null;
            }
        }
        imageViewPhoto.setImageBitmap(null);
        imageViewPhoto.setImageBitmap(bitmap);
    }

    private String SavePhotoMobileDisk(String photoPath, String savePath, int width, int height) {
        String saveDirectory = APP_BASE_DISK_PATH + savePath;
        File file = new File(saveDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
        int index = photoPath.lastIndexOf("/");
        String fileName = photoPath.substring(index + 1);
        String sid = UUID.randomUUID().toString();
        String diskPath = saveDirectory + "/" + sid + fileName;
        ImageUtils.ReSize(photoPath, width, height, diskPath);
        return diskPath;
    }

    private static final String APP_BASE_DISK_PATH = Environment.getExternalStorageDirectory() + "/potato/";
}
