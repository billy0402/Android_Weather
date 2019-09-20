package edu.ntub.weather.helper;

import android.content.Context;
import android.content.res.Resources;

import java.io.InputStream;

public class ResourceHelper {
    private ResourceHelper() {

    }

    public static InputStream getRawFileInputStream(Context context, String fileName) {
        Resources resources = context.getResources();
        int rID = resources.getIdentifier(context.getPackageName() + ":raw/" + fileName, null, null);
        return resources.openRawResource(rID);
    }
}
