package com.fhh.bxgu.utility;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;

import com.fhh.bxgu.R;
import com.fhh.bxgu.shared.StaticVariablePlacer;

public class BottomDialogUtil {

    public static BottomDialogUtilBuilder builder(Context context) {
        return new BottomDialogUtilBuilder(context);
    }
    public static class BottomDialogUtilBuilder {
        private LinearLayout layout;
        private Context context;
        private Dialog dialog;
        BottomDialogUtilBuilder(Context context) {
            this.context = context;
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
        }
        public void show() {
            //由于按键遮挡，补偿部分padding.
            layout.setPadding(0,0,0,(int)(6*StaticVariablePlacer.dpRatio));
            layout.setBackgroundColor(0xFFFFFFFF);
            dialog = new Dialog(context, R.style.DialogTheme);
            dialog.setContentView(layout);
            Window window = dialog.getWindow();
            assert window != null;
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(R.style.dialog_anim_style);
            dialog.show();
        }
        public BottomDialogUtilBuilder addItem(View view) {
            layout.addView(view);
            return this;
        }
        public BottomDialogUtilBuilder addItem(@LayoutRes int res) {
            layout.addView(View.inflate(context,res,null));
            return this;
        }
        public BottomDialogUtilBuilder addItem(View view,View.OnClickListener listener) {
            layout.addView(view);
            view.setOnClickListener(listener);
            return this;
        }
        public BottomDialogUtilBuilder addItem(@LayoutRes int res,View.OnClickListener listener) {
            View view = View.inflate(context,res,null);
            return addItem(view,listener);
        }
        public void quit() {
            dialog.cancel();
        }
    }
}
