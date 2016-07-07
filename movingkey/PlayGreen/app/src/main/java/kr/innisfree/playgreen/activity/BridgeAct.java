package kr.innisfree.playgreen.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ParentAct;
import com.moyusoft.util.Definitions.GOTO;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.Definitions.LINK;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.fragment.CommentListFrag;
import kr.innisfree.playgreen.fragment.LikeListFrag;
import kr.innisfree.playgreen.fragment.ReportFrag;
import kr.innisfree.playgreen.fragment.home.BestPickFrag;
import kr.innisfree.playgreen.fragment.home.PG21Frag;
import kr.innisfree.playgreen.fragment.home.PGNowFrag;
import kr.innisfree.playgreen.fragment.setting.PolicyFrag;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class BridgeAct extends ParentAct {

	public int type;
	public String membId, category, timelineId;
	public int intCategory;
	public Fragment gotoFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_bridge);
		setLoading(this);

		type = getIntent().getIntExtra(INTENT_KEY.DATA, -1);
		membId = getIntent().getStringExtra(INTENT_KEY.MEMB_ID);
		timelineId = getIntent().getStringExtra(INTENT_KEY.TIMELINE_ID);
		category = getIntent().getStringExtra(INTENT_KEY.CATEGORY);
		switch (type) {
			case GOTO.PG21:
				gotoFrag = PG21Frag.newInstance(membId);
				break;
			case GOTO.PGNOW:
				int position = -1;
				switch (category){
					case LINK.PGNOW_CLASS:
						position = 1;
						break;
					case LINK.PGNOW_FESTIVAL:
						position = 2;
						break;
					case LINK.PGNOW_HANGKI:
						position = 3;
						break;
					case LINK.PGNOW_NEWS:
						position = 5;
						break;
				}
				gotoFrag = PGNowFrag.newInstance(position);
				break;
			case GOTO.BESTPICK:
				gotoFrag = BestPickFrag.newInstance();
				break;
			case GOTO.REPORT:
				gotoFrag = ReportFrag.newInstance(category, timelineId);
				break;
			case GOTO.LIKE_USER_LIST:
				gotoFrag = LikeListFrag.newInstance(category,timelineId);
				break;
			case GOTO.COMMENT_LIST:
				gotoFrag = CommentListFrag.newInstance(category, timelineId);
				break;
			case GOTO.POLICY:
				gotoFrag =  PolicyFrag.newInstance(Integer.parseInt(category));
				break;
		}

		if (gotoFrag != null)
			switchContent(gotoFrag, R.id.container, false, false);

	}
}
