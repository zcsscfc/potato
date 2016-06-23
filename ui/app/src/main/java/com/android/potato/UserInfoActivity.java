package com.android.potato;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.potato.camera.CustomAlbumActivity;
import com.potato.camera.CustomCameraActivity;
import com.potato.camera.ImageOperateActivityHandler;
import com.potato.camera.ImageUtils;
import com.potato.camera.UploadImages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class UserInfoActivity extends Activity {
    private static final int REQ_CODE_IMAGE_CHOOSE = 1;
    private static final int REQ_CODE_IMAGE_TAKE = 2;
    private Button btnLogout = null;
    private ImageButton imgBtnGoBack = null;
    private TextView textViewNickName = null;
    private UserInfoShared userInfoShared = null;
    private TableRow tableRowNickName = null;
    private TextView textViewLogName = null;
    private TableRow tableRowWeChat = null;
    private TableRow tableRowMobile = null;
    private TableRow tableRowPassWord = null;
    private TableRow tableRowPhoto = null;
    private ImageView imageViewUserPhoto = null;
    private ImageOperateActivityHandler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        imgBtnGoBack = (ImageButton) findViewById(R.id.imgBtnGoBack);
        textViewNickName = (TextView) findViewById(R.id.textViewNickName);
        tableRowNickName = (TableRow) findViewById(R.id.tableRowNickName);
        textViewLogName = (TextView) findViewById(R.id.textViewLogName);
        tableRowWeChat = (TableRow) findViewById(R.id.tableRowWeChat);
        tableRowMobile = (TableRow) findViewById(R.id.tableRowMobile);
        tableRowPassWord = (TableRow) findViewById(R.id.tableRowPassWord);
        tableRowPhoto = (TableRow) findViewById(R.id.tableRowPhoto);
        imageViewUserPhoto = (ImageView) findViewById(R.id.imageViewUserPhoto);

        userInfoShared = new UserInfoShared(this);
        handler = new ImageOperateActivityHandler(this);

        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintResource(R.color.color_11);

        textViewLogName.setText(userInfoShared.getUserName());
        textViewNickName.setText(userInfoShared.getNickName());

        tableRowNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("多选框")
                        .setMultiChoiceItems(new String[]{"选项1", "选项2", "选项3", "选项4"}, null, null)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        tableRowWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("多选框")
                        .setMultiChoiceItems(new String[]{"选项1", "选项2", "选项3", "选项4"}, null, null)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        tableRowMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("多选框")
                        .setMultiChoiceItems(new String[]{"选项1", "选项2", "选项3", "选项4"}, null, null)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        tableRowPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("多选框")
                        .setMultiChoiceItems(new String[]{"选项1", "选项2", "选项3", "选项4"}, null, null)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        tableRowPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("编辑头像")
                        .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(UserInfoActivity.this, CustomCameraActivity.class);
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
            }
        });

        imgBtnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoShared.edit();
                userInfoShared.setUserName("");
                userInfoShared.setUserId("");
                userInfoShared.setToken("");
                userInfoShared.commit();

                Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(UserInfoActivity.this, "注销成功！", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String savePath = "user/" + userInfoShared.getUserId() + "/photo";
        if (requestCode == REQ_CODE_IMAGE_TAKE && resultCode == RESULT_OK) {
            ArrayList<String> list = data.getStringArrayListExtra("pathlist");
            int len = list.size();
            for (int i = 0; i < len; i++) {
                String dpath = insertBD_IMAGES(list.get(i), savePath, 100, 100);
                displayImage(dpath);
                list.clear();
                list.add(dpath);
            }
            new UploadImages(list, handler, savePath).start();
        } else if (requestCode == REQ_CODE_IMAGE_CHOOSE && resultCode == RESULT_OK) {
            ArrayList<String> list = data.getStringArrayListExtra("pathlist");
            int len = list.size();
            for (int i = 0; i < len; i++) {
                String dpath = insertBD_IMAGES(list.get(i), savePath, 100, 100);
                displayImage(dpath);
                list.clear();
                list.add(dpath);
            }
            new UploadImages(list, handler, savePath).start();
        }
    }

    private void displayImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(fis, null, options);

        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BitmapDrawable drawable = (BitmapDrawable) imageViewUserPhoto.getDrawable();
        if (drawable != null) {
            Bitmap bmp2 = drawable.getBitmap();
            if (null != bmp2 && !bmp2.isRecycled()) {
                bmp2.recycle();
                bmp2 = null;
            }
        }

        imageViewUserPhoto.setImageBitmap(null);
        imageViewUserPhoto.setImageBitmap(bmp);
    }

    private String insertBD_IMAGES(String spath, String tag, int w, int h) {
        String save_dir = app_base_dir + tag;
        File file = new File(save_dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        int index = spath.lastIndexOf("/");
        String name = spath.substring(index + 1);
        String uid = UUID.randomUUID().toString();
        String dpath = save_dir + "/" + uid + name;
        ImageUtils.resize(spath, w, h, dpath);
        return dpath;
    }

    private static final String app_base_dir = Environment.getExternalStorageDirectory() + "/potato/";
}
