package kr.innisfree.playgreen.common.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyusoft.util.Definitions;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class InnisfreeTabLayout extends TabLayout {

	public InnisfreeTabLayout(Context context) {
		super(context);
		init();
	}

	public InnisfreeTabLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InnisfreeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init(){
		isGothamFont = false;
	}

	private boolean isGothamFont;
	public void setGothamFont(boolean bool){
		isGothamFont = bool;
	}


	@Override
	public void addTab(Tab tab) {
		super.addTab(tab);

		if(isGothamFont){
			if (Definitions.Gotham == null)		return;
		}else{
			if(Definitions.InnisfreeGothicBold ==null) return;
		}

		ViewGroup mainView = (ViewGroup) getChildAt(0);
		ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
		int tabChildCount = tabView.getChildCount();
		for (int i = 0; i < tabChildCount; i++) {
			View tabViewChild = tabView.getChildAt(i);
			if (tabViewChild instanceof TextView) {
				if(isGothamFont)
					((TextView) tabViewChild).setTypeface(Definitions.Gotham, Typeface.NORMAL);
				else
					((TextView) tabViewChild).setTypeface(Definitions.InnisfreeGothicBold, Typeface.NORMAL);
			}
		}
	}

}
