package kr.innisfree.playgreen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import kr.innisfree.playgreen.R;


public class CommonLoadingDialog extends Dialog {

	private boolean isTransBG;
	public CommonLoadingDialog(Context context) {
		super(context, android.support.v7.appcompat.R.style.Theme_AppCompat_Dialog);
	}

	public CommonLoadingDialog(Context context, boolean isTransBG) {
		super(context, android.support.v7.appcompat.R.style.Theme_AppCompat_Dialog);
		this.isTransBG = isTransBG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		if(window!=null){
			window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			if(isTransBG)	   window.setDimAmount(0);
		}
		setCancelable(false);
		setContentView(R.layout.dlg_progress);
	}
}
