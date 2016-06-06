package com.potato.api.model;

/**
 * Created by zhangcs on 2016/6/3.
 */
public class ServiceResult {

    private boolean isSuccess;
    private String message;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ServiceResult(){
        isSuccess=true;
        message="";
    }

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
}
