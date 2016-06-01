package com.potato.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class TaskResult {
    public ImageView imageView;
    public String uri;
    public Bitmap bitmap;

    public TaskResult(ImageView imageView, String uri, Bitmap bitmap) {
        this.imageView = imageView;
        this.uri = uri;
        this.bitmap = bitmap;
    }
}
