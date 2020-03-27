package com.fhh.bxgu.component.exercise.question;

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

public class QuestionStorage {
    public interface Callbacks {
        void onReloadFinished(List<Question> questions);
    }
    private List<Question> questions = new ArrayList<>();
    public void reload(int chapter,Callbacks callbacks) {
        questions.clear();
        //去服务器拿数据。
        final Request getImageFileRequest = new Request.Builder()
                .url(ADDRESS_PREFIX + "load_question?lang=" + StaticVariablePlacer.languageUtil.language+"&chapter="+chapter)
                .get()
                .build();
        OKHttpHolder.clientWithCookie.newCall(getImageFileRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("ChapterStorage", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    JSONArray array = new JSONArray(new String(Objects.requireNonNull(response.body()).bytes()));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Question question = new Question();
                        question.setTitle(object.getString("desc"));
                        question.setA(object.getString("a"));
                        question.setB(object.getString("b"));
                        question.setC(object.getString("c"));
                        question.setD(object.getString("d"));
                        question.setAnswer(object.getInt("answer") - 1);
                        questions.add(question);
                    }
                    callbacks.onReloadFinished(questions);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
