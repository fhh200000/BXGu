package com.fhh.bxgu.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Locale;

public class LanguageUtil {
    public String language;
    public interface Callbacks{
        void onLanguageChanged();
    }
    private ArrayList<LanguageUtil.Callbacks> registeredItems = new ArrayList<>();
    public void register(LanguageUtil.Callbacks component) {
        registeredItems.add(component);
    }
    public void unregister() {
        registeredItems.clear();
    }
    public BroadcastReceiver getReceiver() {
        return new LocaleChangeReceiver();
    }
    class LocaleChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Locale locale = Locale.getDefault();
            language = String.format(locale,"%s_%s",locale.getLanguage(),locale.getCountry());
            for(LanguageUtil.Callbacks i:registeredItems) {
                i.onLanguageChanged();
            }
        }
    }
}
