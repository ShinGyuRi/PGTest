package kr.innisfree.playgreen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ParentAct;
import com.moyusoft.util.Definitions.INTENT_ACTION;


/**
 * @author H1407050
 * 
 */
public class ActivityActionReceiver extends BroadcastReceiver {
	//EventBus bus;
	@Override
	public void onReceive(Context context, Intent intent) {
		if(context instanceof ParentAct){
			((ParentAct)context).onReceive(context, intent);
		}
        
	}
		
	public void registerReciever(Context mContext) {
	    IntentFilter localIntentFilter = new IntentFilter();
	    localIntentFilter.addCategory("android.intent.category.DEFAULT");
	    localIntentFilter.addAction(INTENT_ACTION.MEMBER_CODE_IS_NULL);
	    
	    mContext.registerReceiver(this, localIntentFilter);
	}

	
	public void unregisterReceiver(Context mContext) {
		mContext.unregisterReceiver(this);
	}

}

