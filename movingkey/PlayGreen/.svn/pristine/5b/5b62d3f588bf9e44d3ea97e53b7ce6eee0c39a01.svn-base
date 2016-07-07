package com.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.BaseApplication;
import com.android.volley.VolleyError;
import com.google.android.gms.gcm.GcmListenerService;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.PrefUtil;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkListener;
import com.volley.network.NetworkRequestFunction;
import com.volley.network.NetworkRequestUtil;
import com.volley.network.dto.NetworkJson;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.PushReceiveAct;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.data.PlayGreenEvent.EVENT_TYPE;

public class CustomGcmListenerService extends GcmListenerService {

	private String message, infoLink, membId, timelineId;

	@Override
	public void onMessageReceived(String from, Bundle data) {
		if (PlaygreenManager.getAuthToken() == null) return;

		message = data.getString("message");
		infoLink = data.getString("INFO_LINK");
		membId = data.getString("MEMB_ID");
		timelineId = data.getString("TIMELINE_ID");

		try {
			message = URLDecoder.decode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		JYLog.D("From: " + from, new Throwable());
		JYLog.D("message: " + message, new Throwable());
		JYLog.D("INFO_LINK:" + infoLink, new Throwable());
		JYLog.D("MEMB_ID:" + membId, new Throwable());
		JYLog.D("TIMELINE_ID: " + timelineId, new Throwable());

		//푸시알림체크
		String pushYN = PrefUtil.getInstance().getStringPreference(Definitions.PREFKEY.PUSH_YN_STR);
		if (TextUtil.isNull(pushYN) || Definitions.YN.NO.equals(pushYN)) {
			return;
		}

		alarmBadgeUpdate();
		sendNotification();

	}

	NotificationManager nm;
	private NotificationCompat.Builder mCompatBuilder;
	private NotificationCompat.BigPictureStyle bigStyle;
	private NotificationCompat.BigTextStyle bigTextStyle;

	private void sendNotification() {
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mCompatBuilder = new NotificationCompat.Builder(this);
		mCompatBuilder.setSmallIcon(R.drawable.icon_noti);
		mCompatBuilder.setTicker(getString(R.string.app_name));
		mCompatBuilder.setContentTitle(getString(R.string.app_name));
		mCompatBuilder.setWhen(System.currentTimeMillis());
		mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND);        //| Notification.DEFAULT_VIBRATE
		mCompatBuilder.setContentIntent(getPendingIntent());
		mCompatBuilder.setAutoCancel(true);
		mCompatBuilder.setContentText(message);
		bigTextStyle = new NotificationCompat.BigTextStyle(mCompatBuilder);
		bigTextStyle.bigText(message);
		mCompatBuilder.setStyle(bigTextStyle);
		nm.notify(222, mCompatBuilder.build());
	}

	public PendingIntent getPendingIntent() {
		Intent intent = new Intent(this, PushReceiveAct.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(INTENT_KEY.INFO_LINK, infoLink);
		intent.putExtra(INTENT_KEY.MEMB_ID, membId);
		intent.putExtra(INTENT_KEY.TIMELINE_ID, timelineId);

		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		intent.putExtra(Definitions.INTENT_KEY.PUSH_TYPE, pushType);
//		intent.putExtra(Definitions.INTENT_KEY.ROOM_ID, roomId);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
		return pendingIntent;
	}


	public void alarmBadgeUpdate() {
		NetworkRequestFunction netFunc = new NetworkRequestFunction(new NetworkRequestUtil(new NetworkListener() {
			@Override
			public void onNetworkStart(int idx) {
			}

			@Override
			public void onNetworkResult(int idx, NetworkJson json) {
				if (idx == APIKEY.INFO_COUNT) {
					Definitions.ALARM_COUNT = json.INFO_COUNT;
					PlaygreenManager.updateIconBadge(BaseApplication.getContext());
					EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.ALARM_COUNT_UPDATE));
				}
			}

			@Override
			public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
			}
		}));
		netFunc.requestAlarmCount();
	}

	public void sendBroadcast() {
		Intent broadIntent = new Intent();
		broadIntent.setAction(Definitions.INTENT_ACTION.RECEIVER_MESSAGE);
		broadIntent.putExtra("message", message);
		sendBroadcast(broadIntent);
	}
}
