package com.potato.api.model;

import java.nio.channels.Pipe;

/**
 * Created by zhangcs on 2016/6/1.
 */
public class UploadResult {
    private boolean isSuccess;
    private String fileNmae;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getFileNmae() {
        return fileNmae;
    }

    public void setFileNmae(String fileNmae) {
        this.fileNmae = fileNmae;
    }
}
