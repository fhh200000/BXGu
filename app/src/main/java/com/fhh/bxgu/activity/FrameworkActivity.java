package com.fhh.bxgu.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fhh.bxgu.component.exercise.chapter.Chapter;
import com.fhh.bxgu.fragment.CourseFragment;
import com.fhh.bxgu.fragment.ExerciseDetailFragment;
import com.fhh.bxgu.fragment.ExerciseFragment;
import com.fhh.bxgu.fragment.MeFragment;
import com.fhh.bxgu.R;
import com.fhh.bxgu.shared.StaticVariablePlacer;
import com.fhh.bxgu.utility.LanguageUtil;
import com.fhh.bxgu.utility.SettingsUtil;

import java.util.Locale;

public class FrameworkActivity extends AppCompatActivity implements MeFragment.Callbacks, LanguageUtil.Callbacks,ExerciseFragment.Callbacks{
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment[] fragments;
    private TextView courseButton;
    private TextView exerciseButton;
    private TextView meButton; //没错TextView变成了按钮
    private TextView[] buttons;
    private int mainColor,mainColorDark, currentTab = 0;
    private BroadcastReceiver languageReceiver;
    //抽象出FrameworkActivity的接口。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticVariablePlacer.meFragmentCallbacks = this;
        StaticVariablePlacer.languageUtil = new LanguageUtil();
        StaticVariablePlacer.languageUtil.register(this);
        Locale locale = getResources().getConfiguration().locale;
        StaticVariablePlacer.languageUtil.language = String.format(locale,"%s_%s",locale.getLanguage(),locale.getCountry());
        onLanguageChanged();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
        languageReceiver = StaticVariablePlacer.languageUtil.getReceiver();
        registerReceiver(languageReceiver, intentFilter);
        StaticVariablePlacer.screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(StaticVariablePlacer.screenSize);
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        StaticVariablePlacer.dpRatio = (float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        setTheme(StaticVariablePlacer.theme );
        setContentView(R.layout.activity_framework);
        //绑定按钮界面元件。
        Fragment courseFragment = new CourseFragment(),exerciseFragment = new ExerciseFragment(),meFragment = new MeFragment();
        StaticVariablePlacer.languageUtil.register((LanguageUtil.Callbacks)exerciseFragment);
        StaticVariablePlacer.languageUtil.register((LanguageUtil.Callbacks)courseFragment);
        courseButton = findViewById(R.id.button_course);
        exerciseButton = findViewById(R.id.button_exercise);
        meButton = findViewById(R.id.button_me);
        fragments = new Fragment[]{courseFragment, exerciseFragment, meFragment};
        buttons = new TextView[]{courseButton, exerciseButton, meButton};
        //获取主要颜色。
        TypedArray array = getTheme().obtainStyledAttributes(StaticVariablePlacer.theme , new int[]{R.attr.colorPrimary,R.attr.colorPrimaryDark});
        mainColor = array.getColor(0, 0xFFFFFFFF);
        mainColorDark = array.getColor(1,0xFF000000);
        array.recycle();
        courseButton.setOnClickListener(v -> {
            switchPage(1);
            switchPageTab(1);
        });
        exerciseButton.setOnClickListener(v -> {
            switchPage(2);
            switchPageTab(2);
        });
        meButton.setOnClickListener(v -> {
            switchPage(3);
            switchPageTab(3);
        });
        //将用户最后一次选择的tab记录下来（旋转屏幕时的切换）
        int tab;
        if (savedInstanceState != null)
            tab = savedInstanceState.getInt("currentTab");
        else
            tab = 1;
        //切换默认页面。
        switchPage(tab);
        currentTab = tab;
        switchPageTab(currentTab);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outBundle) {
        super.onSaveInstanceState(outBundle);
        outBundle.putSerializable("currentTab", currentTab);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        StaticVariablePlacer.languageUtil.unregister();
        unregisterReceiver(languageReceiver);
    }
    //抽象出切换页的函数。
    private void switchPage(int tab) {
        if (currentTab != tab) {
            fm.beginTransaction()
                    .replace(R.id.main_fragment_container, fragments[tab - 1])
                    .commit();
        }
        currentTab = tab;
    }

    private void switchPageTab(int tab) {
        if (tab != 1)
            courseButton.getCompoundDrawables()[1].setTint(0xFF949494);
        if (tab != 2)
            exerciseButton.getCompoundDrawables()[1].setTint(0xFF949494);
        if (tab != 3)
            meButton.getCompoundDrawables()[1].setTint(0xFF949494);
        buttons[tab - 1].getCompoundDrawables()[1].setTint(mainColor);
    }
    @Override
    public int getThemeId() {
        return StaticVariablePlacer.theme ;
    }
    @Override
    public void onThemeChanged(String theme) {
        SettingsUtil.put("theme", theme);
        SettingsUtil.save();
        switch (theme.charAt(0)) {
            case 'g': {
                if (theme.charAt(2) == 'e')
                    StaticVariablePlacer.theme  = (R.style.green);
                else
                    StaticVariablePlacer.theme  = (R.style.gray);
                break;
            }
            case 'b': {
                StaticVariablePlacer.theme  = (R.style.blue);
                break;
            }
            case 'y': {
                StaticVariablePlacer.theme  = (R.style.yellow);
                break;
            }
            case 'p': {
                StaticVariablePlacer.theme  = (R.style.purple);
                break;
            }
            default: {
                StaticVariablePlacer.theme = R.style.green;
            }
        }
        setTheme(StaticVariablePlacer.theme );
        TypedArray array = getTheme().obtainStyledAttributes(StaticVariablePlacer.theme , new int[]{R.attr.colorPrimary,R.attr.colorPrimaryDark});
        mainColor = array.getColor(0, 0xFFFFFFFF);
        mainColorDark = array.getColor(1, 0xFFFFFFFF);
        array.recycle();
        //强制切换页面。
        int tab = currentTab;
        fm.beginTransaction()
                .detach(fragments[tab - 1])
                .attach(fragments[tab - 1])
                .commit();
        switchPageTab(tab);
        //切换状态栏的颜色。
        getWindow().setStatusBarColor(mainColorDark);
    }
    private static long lastClick = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            long timeNow = System.currentTimeMillis();
            if(timeNow-lastClick<2000) {
                finish();
                return true;
            }
            else {
                Toast.makeText(FrameworkActivity.this,R.string.str_confirm_exit,Toast.LENGTH_SHORT).show();
                lastClick = timeNow;
                return false;
            }
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    public int getMainColorDark() {
        return mainColorDark;
    }

    @Override
    public void onLanguageChanged() {
        //Android会自动处理主界面的语言切换。
    }

    @Override
    public void onChapterSelected(Chapter chapter) {
        //接口已经留出，暂时不对平板布局适配。
        int id= chapter.getId();
        String title = chapter.getTitle();
        Intent intent = new Intent(FrameworkActivity.this,FragmentHolderActivity.class);
        intent.putExtra("chapter",id);
        intent.putExtra("title",title);
        intent.putExtra("fragment", ExerciseDetailFragment.class.getName());
        startActivity(intent);
    }
}
