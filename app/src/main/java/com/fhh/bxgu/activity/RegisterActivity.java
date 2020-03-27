package com.fhh.bxgu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fhh.bxgu.shared.OKHttpHolder;
import com.fhh.bxgu.R;
import com.fhh.bxgu.shared.StaticVariablePlacer;
import com.fhh.bxgu.utility.MD5Util;

import java.io.IOException;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static com.fhh.bxgu.shared.OKHttpHolder.ADDRESS_PREFIX;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(StaticVariablePlacer.theme );
        setContentView(R.layout.activity_register);
        final TextView username = findViewById(R.id.register_username);
        final TextView password = findViewById(R.id.register_password);
        final TextView passwordConfirm = findViewById(R.id.register_confirm_password);
        Button confirm = findViewById(R.id.btn_register);
        confirm.setOnClickListener(v -> {
            String origPassword = password.getText().toString();
            if(!origPassword.equals(passwordConfirm.getText().toString())) { //两次密码输入不一致
                Toast.makeText(RegisterActivity.this,R.string.str_password_not_match,Toast.LENGTH_LONG).show();
                return;
            }
            String passwordAfterMd5 = MD5Util.calculateMD5(origPassword);
            register(username.getText().toString(),passwordAfterMd5);
        });
    }
    private void register(final String username, String passwordWithMd5) {
        final Request request = new Request.Builder()
                .url(ADDRESS_PREFIX+"register")
                .post(new FormBody.Builder()
                        .add("username",username)
                        .add("password",passwordWithMd5)
                        .build())
                .build();
        new Thread(() -> {
            Response response;
            try {
                response = OKHttpHolder.clientWithCookie.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = Objects.requireNonNull(response.body()).string();
                    if(result.charAt(0)=='F') {//"Fail"
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this,result, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }
}
