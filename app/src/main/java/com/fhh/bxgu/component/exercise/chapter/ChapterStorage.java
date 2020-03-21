package com.fhh.bxgu.component.exercise.chapter;

import android.util.Log;

import com.fhh.bxgu.shared.OKHttpHolder;
import com.fhh.bxgu.shared.StaticVariablePlacer;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static com.fhh.bxgu.shared.OKHttpHolder.ADDRESS_PREFIX;

public class ChapterStorage {
    static List<Chapter> chapters = new ArrayList<>();

    static void load(ChapterStorage.Callbacks callback) {
        ChapterStorage.callback = callback;
        if(chapters.size()!=0)
            return;
        reload();
    }
    public interface Callbacks {
        void onReloadFinished();
    }
    private static ChapterStorage.Callbacks callback;
    public static void reload() {
        chapters.clear();
        //去服务器拿数据。
        final Request getImageFileRequest =  new Request.Builder()
                .url(ADDRESS_PREFIX+"get_question_chapter?lang="+ StaticVariablePlacer.languageUtil.language)
                .get()
                .build();
        OKHttpHolder.clientWithCookie.newCall(getImageFileRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("ChapterStorage",e.toString());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    JSONArray array = new JSONArray(new String(Objects.requireNonNull(response.body()).bytes()));
                    for(int i=0;i<array.length();i++) {
                        JSONObject object = array.getJSONObject(i);
                        Chapter chapter = new Chapter();
                        chapter.setCount(object.getInt("count"));
                        chapter.setTitle(object.getString("name"));
                        chapter.setId(object.getInt("id"));
                        Log.e("cccccccc",chapter.toString());
                        chapters.add(chapter);
                        callback.onReloadFinished();
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
