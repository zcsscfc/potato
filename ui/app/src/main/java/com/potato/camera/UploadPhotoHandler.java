package com.potato.camera;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.widget.Toast;

import com.android.potato.PotatoApplication;
import com.android.potato.UserInfoActivity;
import com.android.potato.UserInfoShared;
import com.google.gson.Gson;
import com.potato.model.UploadImageInput;
import com.potato.model.UploadImageResult;

public class UploadPhotoHandler extends Handler {
    public final int msg_upload_fail = 0;
    public final int msg_upload_success = 1;

    private WeakReference<UserInfoActivity> mActivity;
    private UserInfoActivity theActivity;

    public UploadPhotoHandler(UserInfoActivity activity) {
        mActivity = new WeakReference<UserInfoActivity>(activity);
        theActivity = mActivity.get();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case msg_upload_success:
                String jsonStr = (String) msg.obj;
                UploadImageResult uploadImageResult = new Gson().fromJson(jsonStr, UploadImageResult.class);
                String serverFilePath = uploadImageResult.data[0];
                UserInfoShared userInfoShared = new UserInfoShared(PotatoApplication.getInstance());
                UploadImageInput uploadImageInput = new UploadImageInput();
                uploadImageInput.mobile = new String(Base64.encode("18516535230".getBytes(), Base64.DEFAULT));
                uploadImageInput.photo = serverFilePath;
                uploadImageInput.wechat = new String(Base64.encode("liwanjunwechat".getBytes(), Base64.DEFAULT));
                uploadImageInput.nick_name = userInfoShared.getNickName();
                theActivity.UpdateUserInfo(uploadImageInput);
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