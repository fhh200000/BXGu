package com.fhh.bxgu;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class CourseFragment extends Fragment {
    private Timer timer=new Timer(true);;
    private ViewPager2 adPager;
    private TimerTask timerTask;
    private int currentADPage=0,totalADPage=0;
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
        View pageWrapper = view.findViewById(R.id.pager_wrapper);
        ViewGroup.LayoutParams adLayout = pageWrapper.getLayoutParams();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            adLayout.width  = StaticVariablePlacer.screenSize.x;
            adLayout.height = adLayout.width / 3;
            adPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        }
        else {
            adLayout.height = StaticVariablePlacer.screenSize.y;
            adLayout.width  = (int) (adLayout.height / 2.5);
            adPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        }
        pageWrapper.setLayoutParams(adLayout);
        adPager.setAdapter(new ADViewAdapter(getContext(),getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE));
        totalADPage = StaticVariablePlacer.adBannerStorage.size();
        timerTask = getTask();
        timer.schedule(timerTask, 0, 5000);
        timerTask.run();
        return view;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        timerTask.cancel();
        timer.purge();
    }
    private TimerTask getTask(){
        return new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
    }
    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(@NotNull Message msg) {
            if(msg.what == 1){
                currentADPage = (currentADPage+1)%totalADPage;
                adPager.setCurrentItem(currentADPage);
            }
            return false;
        }
    });
}
