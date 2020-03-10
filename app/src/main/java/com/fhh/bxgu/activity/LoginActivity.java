package com.fhh.bxgu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fhh.bxgu.utility.MD5Util;
import com.fhh.bxgu.shared.OKHttpHolder;
import com.fhh.bxgu.R;
import com.fhh.bxgu.shared.StaticVariablePlacer;

import java.io.IOException;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static com.fhh.bxgu.shared.OKHttpHolder.ADDRESS_PREFIX;

public class LoginActivity extends AppCompatActivity {
    private int theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = getIntent().getIntExtra("theme", R.style.green);
        setTheme(theme);
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.btn_login);
        Button registerButton = findViewById(R.id.btn_register);
        final EditText username = findViewById(R.id.login_username);
        final EditText password = findViewById(R.id.login_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username.getText().toString(), MD5Util.calculateMD5(password.getText().toString()));
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("theme",theme);
                startActivityForResult(intent,233);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 233) {
            if(resultCode == RESULT_OK) { //注册成功
                setResult(RESULT_OK);
                finish();
            }
        }
    }
    private void login(final String username, String passwordWithMd5) {
        final Request request = new Request.Builder()
                .url(ADDRESS_PREFIX+"login")
                .post(new FormBody.Builder()
                        .add("username",username)
                        .add("password",passwordWithMd5)
                        .build())
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response;
                try {
                    response = OKHttpHolder.clientWithCookie.newCall(request).execute();
                        if (response.isSuccessful()) {
                            String result = Objects.requireNonNull(response.body()).string();
                            if(result.charAt(0)=='F') {//"Fail"
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this,R.string.str_login_failed, Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            else {
                                StaticVariablePlacer.username = username;
                                setResult(RESULT_OK);
                                finish();
                            }
                        } else {
                            throw new IOException("Unexpected code:" + response);
                        }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start();
    }
}
