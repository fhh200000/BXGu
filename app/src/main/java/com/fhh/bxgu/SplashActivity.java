package com.fhh.bxgu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
public class SplashActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void onCreate(Bundle savedInstanceState) {
        StaticVariablePlacer.username = null;
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SDPermUtil.verifyStoragePermissions(this);
        final int theme;
        String mTheme="";
        try {
            File mThemeFile = new File(getFilesDir().getAbsolutePath() + "theme.conf");
            if (!mThemeFile.exists()) {

                mThemeFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(mThemeFile);
                fos.write("green".getBytes());
                fos.flush();
                fos.close();
                mTheme = "green";
            } else {
                Scanner sc = new Scanner(new FileInputStream(mThemeFile));
                mTheme = sc.nextLine();
                sc.close();
            }
        }
        catch (IOException ignored){
            //此处不可能产生错误！
        }
            switch (mTheme.charAt(0)) {
                case 'g': {
                    if(mTheme.charAt(2)=='e')
                        theme = (R.style.green);
                    else
                        theme = (R.style.gray);
                    break;
                }
                case 'b': {
                    theme = (R.style.blue);
                    break;
                }
                case 'y': {
                    theme = (R.style.yellow);
                    break;
                }
                case 'p': {
                    theme = (R.style.purple);
                    break;
                }
                default: {
                    theme = R.style.green;
                }
            }

        setTheme(theme);
        setContentView(R.layout.activity_splash);
        //新开一个定时器用于跳转页面。
        CountDownTimer countDownTimer = new CountDownTimer(3200,2200) {
            @Override
            public void onTick(long l) {
                //无事发生……
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this,FrameworkActivity.class);
                intent.putExtra("theme",theme);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
    }
}
