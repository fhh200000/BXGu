package com.fhh.bxgu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends Fragment {
    private TextView changePassword,logout,login;
    private TextView profileName;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ImageView qrButton= view.findViewById(R.id.btn_qr);
        TextView updateTheme = view.findViewById(R.id.text_theme);
        login = view.findViewById(R.id.text_login);
        logout = view.findViewById(R.id.text_logout);
        changePassword = view.findViewById(R.id.text_change_password);
        profileName = view.findViewById(R.id.profile_name);
        if(StaticVariablePlacer.username==null) {
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
            changePassword.setVisibility(View.GONE);
        }
        else {
            login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.VISIBLE);
            profileName.setText(StaticVariablePlacer.username);

        }
        final int[] radioButtons = {R.id.radio_green, R.id.radio_blue, R.id.radio_gray, R.id.radio_yellow, R.id.radio_purple};
        final String[] colors = {"green", "blue", "gray", "yellow", "purple"};
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
                final View themeChooser = getLayoutInflater().inflate(R.layout.dialog_theme_chooser, null);
                final RadioGroup rg = themeChooser.findViewById(R.id.rg_theme);
                new AlertDialog.Builder(getContext())
                        .setView(themeChooser)
                        .setTitle("请选择主题")
                        .setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int selected = rg.getCheckedRadioButtonId();
                                for (int i = 0; i < radioButtons.length; i++) {
                                    if (selected == radioButtons[i]) {
                                        StaticVariablePlacer.meFragmentCallbacks.onThemeChanged(colors[i]);
                                    }
                                }
                            }
                        })
                        .setNegativeButton(R.string.str_cancel, null)
                        .show();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
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
                changePassword.setVisibility(View.GONE);
                profileName.setText(R.string.str_name);
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
                changePassword.setVisibility(View.VISIBLE);
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
