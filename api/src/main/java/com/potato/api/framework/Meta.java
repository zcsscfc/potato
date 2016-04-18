package com.potato.api.framework;

/**
 * Created by zhangcs on 2016/4/14.
 */
public class Meta {
    private boolean isSuccess;
    private String message;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Meta(boolean isSuccess,String message){
        this.isSuccess=isSuccess;
        this.message=message;
    }
}
