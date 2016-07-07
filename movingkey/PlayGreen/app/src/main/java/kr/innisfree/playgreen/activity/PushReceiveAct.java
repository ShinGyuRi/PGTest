package kr.innisfree.playgreen.activity;

import android.content.Intent;
import android.os.Bundle;

import com.ParentAct;
import com.moyusoft.util.Definitions.GOTO;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.Definitions.LINK;
import com.moyusoft.util.Util;

import kr.innisfree.playgreen.activity.search.ProfileDetailAct;
import kr.innisfree.playgreen.activity.setting.NoticeAct;

/**
 * Created by jooyoung on 2016-05-02.
 */
public class PushReceiveAct extends ParentAct {

	private String infoLink, membId, timelineId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pushCheck();
	}

	public void pushCheck(){
		Intent intent = null;
		infoLink = getIntent().getStringExtra(INTENT_KEY.INFO_LINK);
		membId = getIntent().getStringExtra(INTENT_KEY.MEMB_ID);
		timelineId = getIntent().getStringExtra(INTENT_KEY.TIMELINE_ID);

		if(activityList == null ||  activityList.size() <= 0){
			intent  = new Intent(this, MainAct.class);
			intent.putExtra(INTENT_KEY.IS_FROM_PUSH, true);
			intent.putExtra(INTENT_KEY.INFO_LINK, infoLink);
			intent.putExtra(INTENT_KEY.MEMB_ID, membId);
			intent.putExtra(INTENT_KEY.TIMELINE_ID, timelineId);
			Util.moveActivity(this, intent, -1,-1, true,false);
			return;
		}

		//TEST
		//infoLink = LINK.PGNOW_HANGKI;
		switch (infoLink){
			case LINK.PROFILE_DETAIL:
				intent = new Intent(this, ProfileDetailAct.class);
				intent.putExtra(INTENT_KEY.MEMB_ID, membId);
				break;
			case LINK.TIMELINE_DETAIL:
				intent = new Intent(this, TimelineDetailAct.class);
				intent.putExtra(INTENT_KEY.TIMELINE_ID, timelineId);
				break;
			case LINK.PG21:
				intent = new Intent(this, BridgeAct.class);
				intent.putExtra(INTENT_KEY.DATA, GOTO.PG21);
				intent.putExtra(INTENT_KEY.MEMB_ID, membId);
				break;
			case LINK.BESTPICK:
				intent = new Intent(this, BridgeAct.class);
				intent.putExtra(INTENT_KEY.DATA, GOTO.BESTPICK);
				break;
			case LINK.NOTICE:
				intent = new Intent(this, NoticeAct.class);
				break;
			case LINK.PGNOW_CLASS:
			case LINK.PGNOW_NEWS:
			case LINK.PGNOW_FESTIVAL:
			case LINK.PGNOW_HANGKI:
				intent = new Intent(this, BridgeAct.class);
				intent.putExtra(INTENT_KEY.DATA, GOTO.PGNOW);
				intent.putExtra(INTENT_KEY.CATEGORY, infoLink);
				break;

		}

		Util.moveActivity(PushReceiveAct.this, intent , -1, -1, true, false);

//		int voteNumber = getIntent().getIntExtra("vote_number", -1);
//		int replyNumber =getIntent().getIntExtra("reply_number", -1);
//		//boolean isRunning = getIntent().getBooleanExtra("isRunning", false);
//		if(voteNumber>0){
//			Intent i = null;
//			if(ParentAct.activityList!=null && ParentAct.activityList.size()>0){
//				JYLog.D(ParentAct.activityList.size() + "", new Throwable());
//				i = new Intent(this, VoteDetailAct.class);
//			}else{
//				i = new Intent(this, MainAct.class);
//			}
//			i.putExtra("vote_number", voteNumber);
//			i.putExtra("reply_number", replyNumber);
//			Util.moveActivity(PushReceiveAct.this, i, -1, -1, true, false);
//		}
	}
}
