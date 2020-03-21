package com.fhh.bxgu.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.fhh.bxgu.component.ad.ADViewAdapter;
import com.fhh.bxgu.R;
import com.fhh.bxgu.shared.StaticVariablePlacer;
import com.fhh.bxgu.utility.LanguageUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class CourseFragment extends Fragment implements LanguageUtil.Callbacks {
    private final Timer timer=new Timer(true);
    private ViewPager2 adPager;
    private TimerTask timerTask;
    private int currentADPage=0,totalADPage=0;
    private ImageView[] dots;
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
        LinearLayout adIndicator = view.findViewById(R.id.ad_indicator);
        adPager = view.findViewById(R.id.ad_pager);
        final View pageWrapper = view.findViewById(R.id.pager_wrapper);
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
        adPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentADPage = position;
                changeIndicator();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        totalADPage = StaticVariablePlacer.adBannerStorage.size();
        dots = new ImageView[totalADPage];
        ViewGroup.LayoutParams dotLayout = new ViewGroup.LayoutParams((int)(20*StaticVariablePlacer.dpRatio),(int)(20*StaticVariablePlacer.dpRatio));
        for(int i=0;i<totalADPage;i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageResource(R.drawable.ic_dot);
            dots[i].setLayoutParams(dotLayout);
            adIndicator.addView(dots[i]);
        }
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
    private final Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(@NotNull Message msg) {
            if(msg.what == 1){
                currentADPage = (currentADPage+1)%totalADPage;
                adPager.setCurrentItem(currentADPage);
            }
            return false;
        }
    });
    private int prevSelected=0;
    private final ViewGroup.LayoutParams prevIndicatorLayoutParams= new LinearLayout.LayoutParams((int)(20*StaticVariablePlacer.dpRatio), (int)(20*StaticVariablePlacer.dpRatio));
    private final ViewGroup.LayoutParams newIndicatorLayoutParams = new LinearLayout.LayoutParams((int)(30*StaticVariablePlacer.dpRatio), (int)(30*StaticVariablePlacer.dpRatio));
    private void changeIndicator() {
        dots[prevSelected].setImageResource(R.drawable.ic_dot);
        dots[prevSelected].setLayoutParams(prevIndicatorLayoutParams);
        dots[currentADPage].setLayoutParams(newIndicatorLayoutParams);
        dots[currentADPage].setImageResource(R.drawable.ic_selected);
        prevSelected = currentADPage;
    }

    @Override
    public void onLanguageChanged() {
        //还没想好……先写着吧
    }
}
