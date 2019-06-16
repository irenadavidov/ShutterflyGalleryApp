package com.example.shutterflygallery;

import android.graphics.Bitmap;

public interface PhotoResultLisetner {
    void onResult(Bitmap bmp);
    void onFinished(boolean allRcvd);
}
