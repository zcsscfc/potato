package com.potato.api.framework;

import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangcs on 2016/4/14.
 */
public class Response {
    private Object data;
    private Meta meta;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Response success() {
        Response resp = new Response();
        resp.setData(null);
        Meta meta = new Meta(true, "");
        resp.setMeta(meta);
        return resp;
    }

    public Response success(Object data) {
        Response resp = new Response();
        resp.setData(data);
        Meta meta = new Meta(true, "");
        resp.setMeta(meta);
        return resp;
    }

    public Response failure() {
        Response resp = new Response();
        resp.setData(null);
        Meta meta = new Meta(false, "");
        resp.setMeta(meta);
        return resp;
    }

    public Response failure(String message) {
        Response resp = new Response();
        resp.setData(null);
        Meta meta = new Meta(false, message);
        resp.setMeta(meta);
        return resp;
    }
}
