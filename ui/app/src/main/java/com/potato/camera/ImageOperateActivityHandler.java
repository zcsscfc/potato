package com.potato.camera;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.potato.UserInfoActivity;

public class ImageOperateActivityHandler extends Handler {
    public final int msg_upload_fail = 0;
    public final int msg_upload_success = 1;

    private WeakReference<UserInfoActivity> mActivity;
    private UserInfoActivity theActivity;

    public ImageOperateActivityHandler(UserInfoActivity activity) {
        mActivity = new WeakReference<UserInfoActivity>(activity);
        theActivity = mActivity.get();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case msg_upload_success:
                Toast.makeText(theActivity.getApplicationContext(),
                        "操作成功:" + msg.obj, Toast.LENGTH_LONG).show();
                break;
            case msg_upload_fail:
                Toast.makeText(theActivity.getApplicationContext(),
                        "操作失败:" + msg.obj, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}