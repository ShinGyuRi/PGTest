package com.moyusoft.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ViewUtil {

	
	public static void setListViewHeight(ArrayAdapter adpater, ListView listView) {
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		for (int size = 0; size < listView.getCount(); size++) {
			View listItem = adpater.getView(size, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static View getXmlView(Context context, int layoutRes) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(layoutRes, null);
		inflater = null;
		return view;
	}

	public static void recursiveRecycle(View root) {
		if (root == null)
			return;
		root.setBackgroundDrawable(null);
		if (root instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) root;
			int count = group.getChildCount();
			for (int i = 0; i < count; i++) {
				recursiveRecycle(group.getChildAt(i));
			}

			if (!(root instanceof AdapterView)) {
				group.removeAllViews();
			}

		}

		if (root instanceof ImageView) {
			((ImageView) root).setImageDrawable(null);
		}

		root = null;

		return;
	}

	public static void clearBitmap(Bitmap bm) {
		if (bm == null) {
			return;
		}
		if (!bm.isRecycled()) {
			bm.recycle();
		}
		bm = null;
		System.gc();
	}
	
	
	
	

}
