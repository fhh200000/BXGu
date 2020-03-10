package com.fhh.bxgu.utility;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;


public class QRCodeUtil {
    @Nullable
    public static Bitmap createQRCodeBitmap(String content, int width, int height, int color){
        if(TextUtils.isEmpty(content)){ // 字符串内容判空
            return null;
        }

        if(width < 0 || height < 0){ // 宽和高都需要>=0
            return null;
        }

        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();

            if(!TextUtils.isEmpty("UTF-8")) {
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 字符转码格式设置
            }

            if(!TextUtils.isEmpty("H")){
                hints.put(EncodeHintType.ERROR_CORRECTION, "H"); // 容错级别设置
            }

            if(!TextUtils.isEmpty("2")){
                hints.put(EncodeHintType.MARGIN, "2"); // 空白边距设置
            }
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            int[] pixels = new int[width * height];
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    if(bitMatrix.get(x, y)){
                        pixels[y * width + x] = color; // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = Color.WHITE; // 白色色块像素设置
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }
}