package com.fhh.bxgu.component.exercise.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fhh.bxgu.R;

import java.util.List;

public class QuestionViewAdapter extends RecyclerView.Adapter<QuestionViewHolder> {
    private List<Question> questions;
    private LayoutInflater layoutInflater;
    public QuestionViewAdapter(Context context,List<Question> questions) {
        this.questions = questions;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.single_exercise_question_bar,parent,false);
        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.bind(questions.get(position),position);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
