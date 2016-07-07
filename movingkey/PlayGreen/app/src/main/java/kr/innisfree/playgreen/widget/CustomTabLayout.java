package kr.innisfree.playgreen.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyusoft.util.Definitions;

/**
 * Created by wonderland on 16. 4. 27..
 */
public class CustomTabLayout extends TabLayout{

    public CustomTabLayout(Context context) {
        super(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTabsFromPagerAdapter(@NonNull PagerAdapter adapter) {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "NanumGothic.ttf");

        this.removeAllTabs();

        ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

        for (int i = 0, count = adapter.getCount(); i < count; i++) {
//            Tab tab = this.newTab();
//            this.addTab(tab.setText(adapter.getPageTitle(i)));
//            AppCompatTextView view = (AppCompatTextView) ((ViewGroup)slidingTabStrip.getChildAt(i)).getChildAt(1);
//            view.setTypeface(typeface, Typeface.NORMAL);
        }
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab);

        if(Definitions.InnisfreeGothicBold ==null) return;

        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                ((TextView) tabViewChild).setTypeface(Definitions.InnisfreeGothicBold, Typeface.NORMAL);
            }
        }
    }
}
