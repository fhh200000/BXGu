package com.fhh.bxgu.shared;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.fhh.bxgu.component.ad.ADBannerStorage;
import com.fhh.bxgu.fragment.MeFragment;
import com.fhh.bxgu.utility.LanguageUtil;

public class StaticVariablePlacer {
    //此处存放一些静态变量。
    public static MeFragment.Callbacks meFragmentCallbacks;
    public static ADBannerStorage adBannerStorage;
    public static Point screenSize;
    public static float dpRatio;
    public static String username;
    public static String nickname;
    public static Bitmap profileImage;
    public static String baseDirPath;
    public static LanguageUtil languageUtil;
}
