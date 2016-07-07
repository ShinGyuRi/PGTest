package kr.innisfree.playgreen.sdk;

import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;
import com.moyusoft.util.JYLog;

/**
 * Created by preparkha on 2015. 12. 7..
 */
public class SessionCallback implements ISessionCallback {

	private KaKaoSessionCallbackListener kaKaoSessionCallbackListener;

	public SessionCallback() {
	}

	public SessionCallback(KaKaoSessionCallbackListener kaKaoSessionCallbackListener) {
		this.kaKaoSessionCallbackListener = kaKaoSessionCallbackListener;
	}

	@Override
	public void onSessionOpened() {
		JYLog.D("KakaoTalk Login onSessionOpened()", null);
		if (kaKaoSessionCallbackListener != null)
			kaKaoSessionCallbackListener.onSessionResult(null);
	}

	@Override
	public void onSessionOpenFailed(KakaoException exception) {
		JYLog.D("KakaoTalk Login onSessionOpenedFailed()", null);
		if (kaKaoSessionCallbackListener != null) {
			if (exception == null) exception = new KakaoException("Session Close");
			kaKaoSessionCallbackListener.onSessionResult(exception);
		}

	}

}
