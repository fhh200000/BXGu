package com.fhh.bxgu;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

public class CourseFragment extends Fragment {
    private ViewPager2 adPager;
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        adPager = view.findViewById(R.id.ad_pager);
        ViewGroup.LayoutParams adLayout = adPager.getLayoutParams();
        Log.e("GGGGG",String.format("The screen size is %d * %d.",StaticVariablePlacer.screenSize.x,StaticVariablePlacer.screenSize.y));
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            adLayout.width  = StaticVariablePlacer.screenSize.x;
            adLayout.height = adLayout.width / 3;
        }
        else {
            adLayout.height = StaticVariablePlacer.screenSize.y;
            adLayout.width  = adLayout.height / 3;
        }
        Log.e("FFFFFF",String.format("The size will be:%d*%d.",adLayout.width,adLayout.height));
        adPager.setLayoutParams(adLayout);
        return view;
    }
}
