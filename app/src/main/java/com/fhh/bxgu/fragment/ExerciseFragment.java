package com.fhh.bxgu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fhh.bxgu.R;
import com.fhh.bxgu.component.exercise.chapter.ChapterStorage;
import com.fhh.bxgu.component.exercise.chapter.ChapterViewAdapter;
import com.fhh.bxgu.shared.StaticVariablePlacer;
import com.fhh.bxgu.utility.LanguageUtil;

import org.jetbrains.annotations.NotNull;

public class ExerciseFragment extends Fragment implements LanguageUtil.Callbacks,ChapterStorage.Callbacks{
    private RecyclerView execChapterView;
    private ChapterViewAdapter execChapterViewAdapter;
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        execChapterView = view.findViewById(R.id.exec_chapter_view);
        execChapterView.setLayoutManager(new LinearLayoutManager(getActivity()));
        onReloadFinished();
        return view;
    }

    @Override
    public void onLanguageChanged() {
        ChapterStorage.reload();
    }
    private final Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(@NotNull Message msg) {
            if(msg.what == 1){
                updateUI();
            }
            return false;
        }
    });
    @Override
    public void onReloadFinished() {
        handler.sendEmptyMessage(1);
    }
    private void updateUI() {
        execChapterViewAdapter = new ChapterViewAdapter(getActivity(),this);
        execChapterView.setAdapter(execChapterViewAdapter);
    }
}
