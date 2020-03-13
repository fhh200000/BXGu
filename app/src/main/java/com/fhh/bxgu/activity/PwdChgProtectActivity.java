package com.fhh.bxgu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fhh.bxgu.R;
import com.fhh.bxgu.shared.OKHttpHolder;
import com.fhh.bxgu.shared.StaticVariablePlacer;
import com.fhh.bxgu.utility.MD5Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static com.fhh.bxgu.shared.OKHttpHolder.ADDRESS_PREFIX;

public class PwdChgProtectActivity extends AppCompatActivity {
    public static final int RESET_PASSWORD=0;
    public static final int SET_PASSWORD_PROTECT=1;
    private final Handler resultHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(@NotNull Message msg) {
            if(msg.what == 1){
                Toast.makeText(PwdChgProtectActivity.this,R.string.str_operation_success,Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Toast.makeText(PwdChgProtectActivity.this,R.string.str_operation_failed,Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final TextView field1,field2,field3;
        Button confirm;
        CircleImageView imageView;
        super.onCreate(savedInstanceState);
        //在注册界面的基础上更改。
        int theme = getIntent().getIntExtra("theme", R.style.green);
        setTheme(theme);
        setContentView(R.layout.activity_register);
        field1 = findViewById(R.id.register_username);
        field2 = findViewById(R.id.register_password);
        field3 = findViewById(R.id.register_confirm_password);
        imageView = findViewById(R.id.register_profile_image);
        confirm = findViewById(R.id.btn_register);
        if(StaticVariablePlacer.profileImage!=null) {
            imageView.setImageBitmap(StaticVariablePlacer.profileImage);
        }
        confirm.setText(R.string.str_submit);
        field1.setHint(R.string.str_current_password);
        field1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //根据传入的不同操作，动态修改布局。
        if(getIntent().getIntExtra("action",RESET_PASSWORD)==RESET_PASSWORD) {
            field2.setHint(R.string.str_new_password);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断两次输入是否一致
                    if(!field2.getText().toString().equals(field3.getText().toString())) {
                        Toast.makeText(PwdChgProtectActivity.this,R.string.str_password_not_match,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    @NonNull String origPasswordAfterMD5 = Objects.requireNonNull(MD5Util.calculateMD5(field1.getText().toString()));
                    @NonNull String newPasswordAfterMD5 = Objects.requireNonNull(MD5Util.calculateMD5(field2.getText().toString()));
                    final Request changePasswordRequest =  new Request.Builder()
                            .url(ADDRESS_PREFIX+"change_password")
                            .post(new FormBody.Builder()
                                    .add("username",StaticVariablePlacer.username)
                                    .add("origpassword",origPasswordAfterMD5)
                                    .add("newpassword",newPasswordAfterMD5)
                                    .build())
                            .build();
                    OKHttpHolder.clientWithCookie.newCall(changePasswordRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            resultHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if(Objects.requireNonNull(response.body()).string().equals("OK"))
                                resultHandler.sendEmptyMessage(1);
                            else
                                resultHandler.sendEmptyMessage(0);
                        }
                    });
                }
            });
        }
        else {
            field2.setHint(R.string.str_password_confirm_question);
            field2.setInputType(InputType.TYPE_CLASS_TEXT);
            field3.setHint(R.string.str_confirm_question_answer);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //安全起见，密保答案采用MD5存储。
                    @NonNull String questionAnswerAfterMD5 = Objects.requireNonNull(MD5Util.calculateMD5(field3.getText().toString()));
                    @NonNull String passwordAfterMD5 = Objects.requireNonNull(MD5Util.calculateMD5(field1.getText().toString()));
                    final Request changePasswordRequest =  new Request.Builder()
                            .url(ADDRESS_PREFIX+"set_password_protect")
                            .post(new FormBody.Builder()
                                    .add("username",StaticVariablePlacer.username)
                                    .add("password",passwordAfterMD5)
                                    .add("protectquesion",field2.getText().toString())
                                    .add("questionanswer",questionAnswerAfterMD5)
                                    .build())
                            .build();
                    OKHttpHolder.clientWithCookie.newCall(changePasswordRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            resultHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if(Objects.requireNonNull(response.body()).string().equals("OK"))
                                resultHandler.sendEmptyMessage(1);
                            else
                                resultHandler.sendEmptyMessage(0);
                        }
                    });
                }
            });
        }
    }
}
