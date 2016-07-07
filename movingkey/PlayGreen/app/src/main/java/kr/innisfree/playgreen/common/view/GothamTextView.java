package kr.innisfree.playgreen.common.view;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.moyusoft.util.Definitions;


public class GothamTextView extends TextView {


    public GothamTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Definitions.Gotham == null) {
            Definitions.Gotham = Typeface.createFromAsset(context.getAssets(), "GothamMedium.otf");
        }
        if(Definitions.Gotham !=null)
            setTypeface(Definitions.Gotham);
        //setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);

    }

    public GothamTextView(Context context) {
        super(context);
        if (Definitions.Gotham == null) {
            Definitions.Gotham = Typeface.createFromAsset(context.getAssets(), "GothamMedium.otf");
        }
        if(Definitions.Gotham !=null)
            setTypeface(Definitions.Gotham);
        //setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);
    }

    public GothamTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (Definitions.Gotham == null) {
            Definitions.Gotham = Typeface.createFromAsset(context.getAssets(), "GothamMedium.otf");
        }
        if(Definitions.Gotham !=null)
            setTypeface(Definitions.Gotham);
        //setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);
    }


}
