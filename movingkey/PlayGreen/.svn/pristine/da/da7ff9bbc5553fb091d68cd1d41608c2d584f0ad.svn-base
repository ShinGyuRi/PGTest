package kr.innisfree.playgreen.common.view;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.moyusoft.util.Definitions;


public class InnisfreeGothicTextView extends TextView {


    public InnisfreeGothicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Definitions.InnisfreeGothic == null) {
            Definitions.InnisfreeGothic = Typeface.createFromAsset(context.getAssets(), "InnisfreeGothicR.ttf");
        }
        if(Definitions.InnisfreeGothic!=null)
            setTypeface(Definitions.InnisfreeGothic);

        //setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);

    }

    public InnisfreeGothicTextView(Context context) {
        super(context);
        if (Definitions.InnisfreeGothic == null) {
            Definitions.InnisfreeGothic = Typeface.createFromAsset(context.getAssets(), "InnisfreeGothicR.ttf");
        }
        if(Definitions.InnisfreeGothic !=null)
            setTypeface(Definitions.InnisfreeGothic);
        //setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);
    }

    public InnisfreeGothicTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (Definitions.InnisfreeGothic == null) {
            Definitions.InnisfreeGothic = Typeface.createFromAsset(context.getAssets(), "InnisfreeGothicR.ttf");
        }
        if(Definitions.InnisfreeGothic !=null)
            setTypeface(Definitions.InnisfreeGothic);
        //setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);
    }


}
