package com.fhh.bxgu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.fhh.bxgu.OKHttpHolder.ADDRESS_PREFIX;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme;
        theme = getIntent().getIntExtra("theme",R.style.green);
        setTheme(theme);
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.btn_login);
        final EditText username = findViewById(R.id.login_username);
        final EditText password = findViewById(R.id.login_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username.getText().toString(),MD5Util.calculateMD5(password.getText().toString()));
            }
        });
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
                            if(Objects.requireNonNull(response.body()).string().charAt(0)=='F') {//"Fail"
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this, Objects.requireNonNull(response.body()).string(), Toast.LENGTH_SHORT).show();
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
