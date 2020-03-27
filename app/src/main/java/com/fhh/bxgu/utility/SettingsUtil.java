package com.fhh.bxgu.utility;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class SettingsUtil {
    private static Map<String, Object> settings;
    public static String baseDir;
    private static File configFile;

    public static void init() {
        settings = new HashMap<>();
        configFile = new File(baseDir + "/config.json");
        try {
            if (!configFile.exists()) {
                if (!configFile.createNewFile()) {
                    Log.e("BXGu", "Cannot create config file!");
                }
                ;
            } else {
                Scanner sc = new Scanner(configFile);
                StringBuilder configJson = new StringBuilder();
                while (sc.hasNextLine())
                    configJson.append(sc.nextLine());
                JSONObject jsonObject = new JSONObject(configJson.toString());
                Iterator<String> keys = jsonObject.keys();
                String key;
                while (keys.hasNext()) {
                    key = keys.next();
                    settings.put(key, jsonObject.opt(key));
                }
            }
        } catch (IOException | JSONException e) {
            Log.e("BXGu", e.toString());
        }
    }

    public static Object get(String key) {
        return settings.get(key);
    }

    public static void put(String key, Object val) {
        settings.put(key, val);
    }

    public static void save() {
        JSONObject json = new JSONObject(settings);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(configFile);
            fos.write(json.toString().getBytes());
            fos.write('\n');
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}