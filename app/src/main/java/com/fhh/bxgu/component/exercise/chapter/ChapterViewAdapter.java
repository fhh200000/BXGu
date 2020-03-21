package com.fhh.bxgu.component.exercise.chapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fhh.bxgu.R;
import com.fhh.bxgu.fragment.ExerciseFragment;

public class ChapterViewAdapter extends RecyclerView.Adapter<ChapterViewHolder> {
    private Context context;
    public ChapterViewAdapter(Context context, ChapterStorage.Callbacks fragment) {
        ChapterStorage.load(fragment);
        this.context = context;
    }
    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new ChapterViewHolder(layoutInflater.inflate(R.layout.single_exercise_chapter_bar,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = ChapterStorage.chapters.get(position);
        holder.bind(chapter.getTitle(),context.getString(R.string.str_exec_count,chapter.getCount()));
    }

    @Override
    public int getItemCount() {
        return ChapterStorage.chapters.size();
    }
}
