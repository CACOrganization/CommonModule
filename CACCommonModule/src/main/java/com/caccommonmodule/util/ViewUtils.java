package com.caccommonmodule.util;

/**
 * Created by laiis.li on 2016/12/20.
 */

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {
    public static <T extends View> T findViewById(Activity activity, int resId) {
        return (T) activity.findViewById(resId);
    }

    public static <T extends View> T findViewByIdWithViewGroup(ViewGroup vg, int resId) {
        return (T) vg.findViewById(resId);
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}