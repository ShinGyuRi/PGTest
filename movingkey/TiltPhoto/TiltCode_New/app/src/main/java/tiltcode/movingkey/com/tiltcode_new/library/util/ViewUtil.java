package tiltcode.movingkey.com.tiltcode_new.library.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Gyul on 2016-06-16.
 */
public class ViewUtil {

    public static View getXmlView(Context context, int layoutRes) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutRes, null);
        inflater = null;
        return view;
    }

}
