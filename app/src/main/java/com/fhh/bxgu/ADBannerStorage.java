package com.fhh.bxgu;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ADBannerStorage {
    private List<ADBanner> banners = new ArrayList<>();
    private boolean loaded;
    public ADBannerStorage() {
        StaticVariablePlacer.adBannerStorage = this;
    }
    public boolean isLoaded() {
        return loaded;
    }
    public void load(JSONArray array) {

    }
}
