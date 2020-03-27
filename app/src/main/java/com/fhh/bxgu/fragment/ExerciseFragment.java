package com.fhh.bxgu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fhh.bxgu.R;
import com.fhh.bxgu.component.exercise.chapter.Chapter;
import com.fhh.bxgu.component.exercise.chapter.ChapterStorage;
import com.fhh.bxgu.component.exercise.chapter.ChapterViewAdapter;
import com.fhh.bxgu.utility.LanguageUtil;

import org.jetbrains.annotations.NotNull;

public class ExerciseFragment extends Fragment implements LanguageUtil.Callbacks,ChapterStorage.Callbacks{
    private RecyclerView execChapterView;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_recycler, container, false);
        execChapterView = view.findViewById(R.id.basic_recycler_view);
        execChapterView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TextView header = view.findViewById(R.id.basic_recycler_title);
        header.setText(R.string.str_bxgu_exercise);
        updateUI();
        return view;
    }

    @Override
    public void onLanguageChanged() {
        ChapterStorage.reload();
    }
    private final Handler handler = new Handler(msg -> {
        if(msg.what == 1){
            updateUI();
        }
        return false;
    });
    @Override
    public void onReloadFinished() {
        handler.sendEmptyMessage(1);
    }
    private void updateUI() {
        ChapterViewAdapter execChapterViewAdapter = new ChapterViewAdapter(getActivity(), this);
        execChapterView.setAdapter(execChapterViewAdapter);
    }
    public interface Callbacks {
        void onChapterSelected(Chapter chapter);
    }
}
