package com.android.potato;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity {
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
        tableRowMobile = (TableRow)findViewById(R.id.tableRowMobile);
        tableRowPassWord = (TableRow)findViewById(R.id.tableRowPassWord);
        tableRowPhoto = (TableRow)findViewById(R.id.tableRowPhoto);

        userInfoShared = new UserInfoShared(this);

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
                        .setPositiveButton("拍照", null)
                        .setNegativeButton("相册", null)
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
}
