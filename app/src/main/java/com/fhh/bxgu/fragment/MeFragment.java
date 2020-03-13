package com.fhh.bxgu.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.fhh.bxgu.R;
import com.fhh.bxgu.activity.LoginActivity;
import com.fhh.bxgu.activity.PwdChgProtectActivity;
import com.fhh.bxgu.shared.StaticVariablePlacer;
import com.fhh.bxgu.utility.BottomDialogUtil;
import com.fhh.bxgu.utility.QRCodeUtil;

import org.jetbrains.annotations.NotNull;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends Fragment {
    private TextView userSettings,logout,login;
    private TextView profileName;
    //空间换时间，提前加载所有信息。
    private final String[] colorNames = {"green", "blue", "gray", "yellow", "purple"};
    private final @StringRes int[] colorHints = {
            R.string.color_green,
            R.string.color_blue,
            R.string.color_gray,
            R.string.color_yellow,
            R.string.color_purple
    };
    private final @ColorRes int[] colorPrimaries = {
            R.color.color_green,
            R.color.color_blue,
            R.color.color_gray,
            R.color.color_yellow,
            R.color.color_purple
    };
    private final @ColorRes int[] colorDarkPrimaries = {
            R.color.color_green_dark,
            R.color.color_blue_dark,
            R.color.color_gray_dark,
            R.color.color_yellow_dark,
            R.color.color_purple_dark
    };
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @SuppressWarnings("InflateParams")
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        final ImageView qrButton= view.findViewById(R.id.btn_qr);
        TextView updateTheme = view.findViewById(R.id.text_theme);
        login = view.findViewById(R.id.text_login);
        logout = view.findViewById(R.id.text_logout);
        userSettings = view.findViewById(R.id.text_user_settings);
        profileName = view.findViewById(R.id.profile_name);
        CircleImageView imageView = view.findViewById(R.id.profile_image);
        if(StaticVariablePlacer.username==null) {
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
            userSettings.setVisibility(View.GONE);
        }
        else {
            login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
            userSettings.setVisibility(View.VISIBLE);
            profileName.setText(StaticVariablePlacer.username);

        }

        if(StaticVariablePlacer.profileImage!=null) {
            imageView.setImageBitmap(StaticVariablePlacer.profileImage);
        }
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生成二维码。
                String data = "@Unknown";//只需要传输用户id即可。
                ImageView generatedQR = new ImageView(getContext());
                generatedQR.setImageBitmap(QRCodeUtil.createQRCodeBitmap(data, 512, 512,StaticVariablePlacer.meFragmentCallbacks.getMainColorDark()));
                new AlertDialog.Builder(getContext())
                        .setView(generatedQR)
                        .setTitle(R.string.qr_title)
                        .setPositiveButton(R.string.str_ok, null)
                        .show();
            }
        });
        updateTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = getResources();
                final BottomDialogUtil.BottomDialogUtilBuilder builder = BottomDialogUtil.builder(getContext());
                for(int i=0;i<5;i++) {
                    View basicView = View.inflate(getContext(),R.layout.single_theme_bar,null);
                    ImageView hintImage = basicView.findViewById(R.id.theme_preview);
                    TextView hintString = basicView.findViewById(R.id.theme_name);
                    hintImage.setBackgroundColor(res.getColor(colorPrimaries[i]));
                    hintImage.getDrawable().setTint(res.getColor(colorDarkPrimaries[i]));
                    hintString.setText(colorHints[i]);
                    final int current = i;
                    builder.addItem(basicView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StaticVariablePlacer.meFragmentCallbacks.onThemeChanged(colorNames[current]);
                            builder.quit();
                        }
                    });
                }
                builder.show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("theme",StaticVariablePlacer.meFragmentCallbacks.getThemeId());
                startActivityForResult(intent,666);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariablePlacer.username = null;
                login.setVisibility(View.VISIBLE);
                logout.setVisibility(View.GONE);
                userSettings.setVisibility(View.GONE);
                profileName.setText(R.string.str_name);
            }
        });
        userSettings.setOnClickListener(new View.OnClickListener() {
             BottomDialogUtil.BottomDialogUtilBuilder builder;
            @Override
            public void onClick(View v) {
                Context context = getContext();
                TextView changePassword = new TextView(context);
                changePassword.setHeight((int)(60*StaticVariablePlacer.dpRatio));
                changePassword.setGravity(Gravity.CENTER);
                changePassword.setText(R.string.btn_change_password);
                changePassword.setTextSize(16);
                changePassword.setTextColor(0xFF000000);
                changePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PwdChgProtectActivity.class);
                        intent.putExtra("theme",StaticVariablePlacer.meFragmentCallbacks.getThemeId());
                        intent.putExtra("action",PwdChgProtectActivity.RESET_PASSWORD);
                        startActivityForResult(intent,888);
                        builder.quit();

                    }
                });
                TextView setPasswordChallenge = new TextView(context);
                setPasswordChallenge.setHeight((int)(60*StaticVariablePlacer.dpRatio));
                setPasswordChallenge.setText(R.string.btn_set_password_protect);
                setPasswordChallenge.setGravity(Gravity.CENTER);
                setPasswordChallenge.setTextColor(0xFF000000);
                setPasswordChallenge.setTextSize(16);
                setPasswordChallenge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PwdChgProtectActivity.class);
                        intent.putExtra("theme",StaticVariablePlacer.meFragmentCallbacks.getThemeId());
                        intent.putExtra("action",PwdChgProtectActivity.SET_PASSWORD_PROTECT);
                        startActivityForResult(intent,888);
                        builder.quit();
                    }
                });
                builder = BottomDialogUtil.builder(context);
                builder .addItem(changePassword)
                        .addItem(setPasswordChallenge)
                        .show();
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 666) {
            if(resultCode == RESULT_OK) { //登录成功
                login.setVisibility(View.GONE);
                logout.setVisibility(View.VISIBLE);
                userSettings.setVisibility(View.VISIBLE);
                profileName.setText(StaticVariablePlacer.username);
            }
        }
    }
    //MeFragment中的回调接口。
    public interface Callbacks {
        void onThemeChanged(String theme);
        int getThemeId();
        int getMainColorDark();
    }
}
