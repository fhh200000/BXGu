package com.fhh.bxgu;

import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FrameworkActivity extends AppCompatActivity implements MeFragment.Callbacks {
    final FragmentManager fm = getSupportFragmentManager();
    Fragment courseFragment, exerciseFragment, meFragment;
    Fragment[] fragments;
    TextView courseButton, exerciseButton, meButton; //没错TextView变成了按钮
    TextView[] buttons;
    private int theme, mainColor,mainColorDark, currentTab = 0;
    //抽象出FrameworkActivity的接口。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticVariablePlacer.meFragmentCallbacks = this;
        StaticVariablePlacer.screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(StaticVariablePlacer.screenSize);
        theme = getIntent().getIntExtra("theme", R.style.green);
        setTheme(theme);
        setContentView(R.layout.activity_framework);
        //绑定按钮界面元件。
        courseFragment = new CourseFragment();
        exerciseFragment = new ExerciseFragment();
        meFragment = new MeFragment();
        courseButton = findViewById(R.id.button_course);
        exerciseButton = findViewById(R.id.button_exercise);
        meButton = findViewById(R.id.button_me);
        fragments = new Fragment[]{courseFragment, exerciseFragment, meFragment};
        buttons = new TextView[]{courseButton, exerciseButton, meButton};
        //获取主要颜色。
        TypedArray array = getTheme().obtainStyledAttributes(theme, new int[]{R.attr.colorPrimary,R.attr.colorPrimaryDark});
        mainColor = array.getColor(0, 0xFFFFFFFF);
        mainColorDark = array.getColor(1,0xFF000000);
        array.recycle();
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(1);
                switchPageTab(1);
            }
        });
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(2);
                switchPageTab(2);
            }
        });
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(3);
                switchPageTab(3);
            }
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

    //抽象出切换页的函数。
    void switchPage(int tab) {
        if (currentTab != tab) {
            fm.beginTransaction()
                    .replace(R.id.main_fragment_container, fragments[tab - 1])
                    .commit();
        }
        currentTab = tab;
    }

    void switchPageTab(int tab) {
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
        return theme;
    }
    @Override
    public void onThemeChanged(String theme) {
        try {
            File mThemeFile = new File(getFilesDir().getAbsolutePath() + "theme.conf");
            //此处文件必然存在！
            FileOutputStream fos = new FileOutputStream(mThemeFile);
            fos.write(theme.getBytes());
            fos.flush();
            fos.close();

        } catch (IOException ignored) {
            //此处不可能产生错误！
        }
        switch (theme.charAt(0)) {
            case 'g': {
                if (theme.charAt(2) == 'e')
                    this.theme = (R.style.green);
                else
                    this.theme = (R.style.gray);
                break;
            }
            case 'b': {
                this.theme = (R.style.blue);
                break;
            }
            case 'y': {
                this.theme = (R.style.yellow);
                break;
            }
            case 'p': {
                this.theme = (R.style.purple);
                break;
            }
            default: {
                this.theme = R.style.green;
            }
        }
        setTheme(this.theme);
        TypedArray array = getTheme().obtainStyledAttributes(this.theme, new int[]{R.attr.colorPrimary,R.attr.colorPrimaryDark});
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
        //更新intent中的数据。
        getIntent().putExtra("theme",this.theme);
    }
    @Override
    public int getMainColorDark() {
        return mainColorDark;
    }
}
