package com.potato.model;

public class UploadImageResult {
    public Meta meta;
    public String[] data;

    public class Meta{
        public String success;
        public String message;
    }
}
