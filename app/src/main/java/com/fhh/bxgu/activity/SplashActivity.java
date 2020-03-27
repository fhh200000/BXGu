package com.fhh.bxgu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;

import com.fhh.bxgu.R;
import com.fhh.bxgu.utility.SDPermUtil;
import com.fhh.bxgu.component.ad.ADBannerStorage;
import com.fhh.bxgu.shared.StaticVariablePlacer;
import com.fhh.bxgu.utility.SettingsUtil;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticVariablePlacer.username = null;
        StaticVariablePlacer.baseDirPath = Objects.requireNonNull(getExternalFilesDir(null)).getAbsolutePath();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SDPermUtil.verifyStoragePermissions(this);
        SettingsUtil.baseDir = getFilesDir().getParent();
        SettingsUtil.init();
        if (SettingsUtil.get("theme") == null) {
            SettingsUtil.put("theme", "blue");
            SettingsUtil.save();
        }
        String mTheme = (String) SettingsUtil.get("theme");
            switch (mTheme.charAt(0)) {
                case 'g': {
                    if(mTheme.charAt(2)=='e')
                        StaticVariablePlacer.theme = (R.style.green);
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
                    StaticVariablePlacer.theme  = R.style.green;
                }
            }

        setTheme(StaticVariablePlacer.theme );
        setContentView(R.layout.activity_splash);
        //从这里开始偷偷下载广告。
        ADBannerStorage bannerStorage = new ADBannerStorage();
        bannerStorage.load();
        //开一个定时器用于跳转页面。
        CountDownTimer countDownTimer = new CountDownTimer(3200,2200) {
            @Override
            public void onTick(long l) {
                //无事发生……
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, FrameworkActivity.class);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
    }
}
