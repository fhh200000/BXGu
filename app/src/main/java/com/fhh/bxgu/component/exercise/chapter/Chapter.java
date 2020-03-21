package com.fhh.bxgu.component.exercise.chapter;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class Chapter {
    private int count;
    private int id;
    private String title;

    public int getCount() {
        return count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @NotNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(),"%d %s %d",id,title,count);
    }
}
