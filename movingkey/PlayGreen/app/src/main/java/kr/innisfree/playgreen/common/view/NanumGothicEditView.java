package kr.innisfree.playgreen.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.moyusoft.util.Definitions;


public class NanumGothicEditView extends EditText {

	public NanumGothicEditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (Definitions.NanumGothic != null)
			setTypeface(Definitions.NanumGothic);
		//setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);
	}

	public NanumGothicEditView(Context context) {
		super(context);
		if (Definitions.NanumGothic != null)
			setTypeface(Definitions.NanumGothic);

		//setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);
	}

	public NanumGothicEditView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (Definitions.NanumGothic != null)
			setTypeface(Definitions.NanumGothic);

		//setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);
	}


}
