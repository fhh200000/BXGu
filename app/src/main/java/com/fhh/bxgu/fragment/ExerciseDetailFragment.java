package com.fhh.bxgu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fhh.bxgu.R;
import com.fhh.bxgu.component.exercise.question.Question;
import com.fhh.bxgu.component.exercise.question.QuestionStorage;
import com.fhh.bxgu.component.exercise.question.QuestionViewAdapter;
import com.fhh.bxgu.utility.LanguageUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExerciseDetailFragment extends Fragment implements QuestionStorage.Callbacks, LanguageUtil.Callbacks {
    private RecyclerView execDetailView;
    private List<Question> questions;
    private QuestionStorage questionStorage = new QuestionStorage();
    private int chapter;
    private String title;
    public ExerciseDetailFragment(int chapter,String title) {
        this.chapter = chapter;
        this.title = title;
    }
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_recycler, container, false);
        execDetailView = view.findViewById(R.id.basic_recycler_view);
        execDetailView.setLayoutManager(new LinearLayoutManager(getActivity()));
        questionStorage.reload(chapter,this);
        TextView title = view.findViewById(R.id.basic_recycler_title);
        title.setText(this.title);
        return view;
    }

    @Override
    public void onLanguageChanged() {
        questionStorage.reload(chapter,this);
    }
    private final Handler handler = new Handler(msg -> {
        if(msg.what == 1){
            updateUI();
        }
        return false;
    });
    private void updateUI() {
        QuestionViewAdapter questionViewAdapter = new QuestionViewAdapter(getActivity(),questions);
        execDetailView.setAdapter(questionViewAdapter);
    }

    @Override
    public void onReloadFinished(List<Question> questions) {
        this.questions = questions;
        handler.sendEmptyMessage(1);
    }
}
