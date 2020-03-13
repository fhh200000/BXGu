package com.fhh.bxgu.component.ad;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.fhh.bxgu.shared.OKHttpHolder;
import com.fhh.bxgu.shared.StaticVariablePlacer;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static com.fhh.bxgu.shared.OKHttpHolder.ADDRESS_PREFIX;

 public class ADBannerStorage {
    private final List<ADBanner> banners = new ArrayList<>();
    private final String externalPath = StaticVariablePlacer.baseDirPath.concat("/ad/");
     public ADBannerStorage() {
        StaticVariablePlacer.adBannerStorage = this;
        File testFile = new File(externalPath);
        if(!testFile.exists()) {
            boolean result = testFile.mkdirs();
            if(!result) {
                Log.e("BXGu","Cannot create folder.");
            }
        }
    }
    public int size() {
        return banners.size();
    }
    ADBanner getBanner(int pos) {
        return banners.get(pos);
    }
    public void load() {
        final Request getADListRequest =  new Request.Builder()
                .url(ADDRESS_PREFIX+"get_ad_list")
                .get()
                .build();
        new Thread() {
            @Override
            public void run() {
                Response response;
                try {
                    response = OKHttpHolder.clientWithCookie.newCall(getADListRequest).execute();
                    if(response.isSuccessful()) {
                        JSONArray resultArray = new JSONArray(Objects.requireNonNull(response.body()).string());
                        for(int i=0;i<resultArray.length();i++) {
                            banners.add(new ADBanner(resultArray.getString(i)));
                            loadImage(i);
                        }
                    }
                }
                catch(IOException | JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    private void loadImage(final int id) {
        final String filename = banners.get(id).getGUID();
        final File bgFile = new File(externalPath+filename+".png");
        try {
            if (bgFile.exists()) {
                FileInputStream fis = new FileInputStream(bgFile);
                banners.get(id).setBitmap(BitmapFactory.decodeStream(fis));
            }
            else {
                //下载图片（重复）
                final Request getImageFileRequest =  new Request.Builder()
                        .url(ADDRESS_PREFIX+"images/ad/"+filename+".png")
                        .get()
                        .build();
                OKHttpHolder.clientWithCookie.newCall(getImageFileRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("BXGu","Cannot download file"+filename);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        try {
                            FileOutputStream fos = new FileOutputStream(bgFile);
                            fos.write(Objects.requireNonNull(response.body()).bytes());
                            fos.close();
                            //重新打开文件。
                            FileInputStream fis = new FileInputStream(bgFile);
                            banners.get(id).setBitmap(BitmapFactory.decodeStream(fis));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        final File bgFilePort = new File(externalPath+filename+"_port.png");
        try {
            if (bgFilePort.exists()) {
                FileInputStream fis = new FileInputStream(bgFilePort);
                banners.get(id).setBitmapPort(BitmapFactory.decodeStream(fis));
            }
            else {
                //下载图片（重复）
                final Request getImageFileRequest =  new Request.Builder()
                        .url(ADDRESS_PREFIX+"images/ad/"+filename+"_port.png")
                        .get()
                        .build();
                OKHttpHolder.clientWithCookie.newCall(getImageFileRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("BXGu","Cannot download file"+filename);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        try {
                            FileOutputStream fos = new FileOutputStream(bgFilePort);
                            fos.write(Objects.requireNonNull(response.body()).bytes());
                            fos.close();
                            //重新打开文件。
                            FileInputStream fis = new FileInputStream(bgFilePort);
                            banners.get(id).setBitmapPort(BitmapFactory.decodeStream(fis));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
