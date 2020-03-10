package com.fhh.bxgu.component.ad;

import android.graphics.Bitmap;

class ADBanner {
    //图片，GUID
    private Bitmap bitmap,bitmapPort;
    private final String GUID;
    ADBanner(String GUID) {
        this.GUID = GUID;
    }
    String getGUID() {
        return this.GUID;
    }
    void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    void setBitmapPort(Bitmap bitmapPort) {
        this.bitmapPort = bitmapPort;
    }
    Bitmap getBitmap() {
        return this.bitmap;
    }
    Bitmap getBitmapPort() {
        return this.bitmapPort;
    }
}
