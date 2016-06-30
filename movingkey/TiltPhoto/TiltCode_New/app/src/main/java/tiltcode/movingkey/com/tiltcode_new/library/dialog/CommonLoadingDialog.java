package tiltcode.movingkey.com.tiltcode_new.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import tiltcode.movingkey.com.tiltcode_new.R;

/**
 * Created by Gyul on 2016-06-16.
 */
public class CommonLoadingDialog extends Dialog {

    private boolean isTransBG;
    public CommonLoadingDialog(Context context) {
        super(context, R.style.Theme_AppCompat_Dialog);
    }

    public CommonLoadingDialog(Context context, boolean isTransBG) {
        super(context, R.style.Theme_AppCompat_Dialog);
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
