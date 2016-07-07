package com.moyusoft.util;

import android.graphics.Typeface;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2015-10-15.
 */
public class Definitions {

	public static int APP_CURRENT_VERSION;
	public static int APP_RECENT_VERSION;
	public static boolean IS_GCM_CHECKED;
	public static int ALARM_COUNT;

	public static Typeface NanumGothic;
	public static Typeface NanumGothicBold;
	public static Typeface InnisfreeGothic;
	public static Typeface InnisfreeGothicBold;
	public static Typeface Gotham;

	public interface PREFKEY {
		String IS_VISIT_EXPERIENCE_BOOL = "IS_VISIT_EXPERIENCE_BOOL";
		String COVER_PAGE_VIEW_DATE_STR = "COVER_PAGE_VIEW_DATE_STR";
		String IS_GUIDE_VIEW_NEVER_SEE_BOOL = "IS_GUIDE_VIEW_NEVER_SEE_BOOL";
		String IS_PGNOW_GUIDE_VIEW_NEVER_SEE_BOOL = "IS_PGNOW_GUIDE_VIEW_NEVER_SEE_BOOL";

		String AUTH_TOKEN_STR = "AUTH_TOKEN_STR";
		String AUTH_TOKEN_DUE_DT_STR = "AUTH_TOKEN_DUE_DT_STR";
		String MEMB_NAME_STR = "MEMB_NAME_STR";
		String EMAIL_STR = "EMAIL_STR";
		String NICK_NAME_STR = "NICK_NAME_STR";
		String PROFILE_IMAGE_STR = "PROFILE_IMAGE_STR";
		String BIRTH_DAY_STR = "BIRTH_DAY_STR";
		String SEX_STR = "SEX_STR";
		String AGE_GROUP_STR = "AGE_GROUP_STR";
		String INFO_OPEN_MODE_STR = "INFO_OPEN_MODE_STR";
		String AUTH_CHANNEL_STR = "AUTH_CHANNEL_STR";
		String REG_DATE_STR = "REG_DATE_STR";
		String PUSH_KEY_STR = "PUSH_KEY_STR";
		String BESTGREENER_YN_STR = "BESTGREENER_YN_STR";
		String SUPERGREENER_YN_STR = "SUPERGREENER_YN_STR";
		String SNS_ACCOUNT_FACEBOOK_BOOL = "SNS_ACCOUNT_FACEBOOK_BOOL";
		String SNS_ACCOUNT_INSTAGRAM_BOOL = "SNS_ACCOUNT_INSTAGRAM_BOOL";
		String SNS_ACCOUNT_KAKAO_BOOL = "SNS_ACCOUNT_KAKAO_BOOL";
		String SNS_ACCOUNT_WECHAT_BOOL = "SNS_ACCOUNT_WECHAT_BOOL";
		String SNS_ACCOUNT_LINE_BOOL = "SNS_ACCOUNT_LINE_BOOL";

		String INTRO_TEXT_STR = "INTRO_TEXT_STR";
		String STATE_TEXT_STR = "STATE_TEXT_STR";
		String SEX_OPEN_YN_STR = "SEX_OPEN_YN_STR";
		String PHONE_OPEN_YN_STR = "PHONE_OPEN_YN_STR";
		String EMAIL_OPEN_YN_STR = "EMAIL_OPEN_YN_STR";
		String ORIGIN_SAVE_YN_STR = "ORIGIN_SAVE_YN_STR";
		String PUSH_YN_STR = "PUSH_YN_STR";
		String PWD_UPDATE_YN_STR = "PWD_UPDATE_YN_STR";
		String DATA_SAVING_YN_STR = "DATA_SAVING_YN_STR";
		String TEMP_PWD_YN_STR = "TEMP_PWD_YN_STR";
		String PHONE_STR = "PHONE_STR";
		String SNS_ID_STR = "SNS_ID_STR";
		String LOCATION_STR = "LOCATION_STR";
		String LOCATION_OPEN_YN_STR = "LOCATION_OPEN_YN_STR";
		String LOGIN_FAIL_COUNT_INT = "LOGIN_FAIL_COUNT_INT";


		String MEMB_ID_STR = "MEMB_ID_STR";
		String LAST_LOGIN_DT_LONG = "LAST_LOGIN_DT_LONG";

		String GCM_REGISTATION_ID_STR = "GCM_REGISTATION_ID_STR";
		String PREF_TMP_LAT = "PREF_TMP_LAT";
		String PREF_TMP_LNG = "PREF_TMP_LNG";
		String SERVER_SEND_LOCATION_INFO_TIME = "SERVER_SEND_LOCATION_INFO_TIME";

		String RECENT_HASH_TAG = "RECENT_HASH_TAG";

		String PG_NOW_MAIN_LINK = "PG_NOW_MAIN_LINK";

	}

	public interface AUTH_CHANNEL {
		String FACEBOOK = "FA";
		String KAKAOTALK = "KA";
		String EMAIL = "EM";
		String TWITTER = "TW";
		String LINE = "LI";
		String WECHAT = "WI";
		String WATSAPP = "WA";
		String INSTAGRAM = "IN";
	}

	public interface MAIN_TAB {
		String HOME = "HOME";
		String TIMELINE = "TIMELINE";
		String MYPAGE = "MYPAGE";
	}

	public interface SEARCH_PREFKEY {
		String KEYWORD_STR = "KEYWORD_STR";
		String DATE_STR = "DATE_STR";
		String REGION_STR = "REGION_STR";
		String REGION_CODE_INT = "REGION_CODE_INT";
		String ORDER_TYPE_STR = "ORDER_TYPE_STR";
	}

	public interface GENDER {
		String FEMALE = "F";
		String MALE = "W";
	}

	public interface BESTPICK_TYPE {
		String TODAY = "T";
		String BEST = "B";
		String SUPERGREENER = "S";
	}

	public interface PGNOW_CATEGORY {
		String EARTH_BOX = "E";
		String CLASS = "C";
		String FESTIVAL = "F";
		String NEWS = "N";
		String ECO_HANKIE = "H";
		String GREEN_CULTURE = "G";
	}

	public interface USER_LIST_TYPE {
		String FOLLOWING = "I";
		String FOLLOWER = "E";
		String BLOCK_USER = "B";
		String ALL = "A";
	}

	public interface POLICY_TYPE {
		int USE_POLICY = R.string.str_use_policy;
		int PRIVACY_POLICY = R.string.str_privacy_policy;
		int LOCATION_POLICY = R.string.str_location_policy;
		int MANAGE_POLICY = R.string.str_manage_plicy;
		int LEGAL_NOTICE = R.string.str_legal_notice;
	}

	public interface ALARM_CATEGORY{
		String REGIST_PICTURE = "T";
		String REGIST_COMMENT = "C";
		String LIKE = "L";
		String FOLLOW = "MF";
		String BESTPIC0K_SELECTION = "B";
	}

	public interface GOTO{
		int PG21 = 1;
		int COMMENT_LIST = 2;
		int LIKE_USER_LIST = 3;

		int REPORT = 20;
		int POLICY = 21;

		int BESTPICK = 30;
		int PGNOW = 31;
	}

	public interface LINK{
		String PROFILE_DETAIL  = "MYPAGE";
		String TIMELINE_DETAIL = "TIMELINE";
		String PG21 = "PG21";
		String BESTPICK = "BP";
		String NOTICE = "NOTICE";
		String PGNOW_CLASS = "PGNOW_C";
		String PGNOW_NEWS = "PGNOW_N";
		String PGNOW_FESTIVAL = "PGNOW_F";
		String PGNOW_HANGKI = "PGNOW_H";
	}

	/*********************************
	 * 공통
	 *****************************************************/
	public interface INTENT_ACTION {
		String GPS_CHANGE = "com.airfactory.gps.change";
		String ACTION_SERVICE_CHECK = "kr.tofor.service_check";
		String ACTION_TIMER_START = "kr.tofor.timerstart";
		String ACTION_TIMER_STOP = "kr.tofor.timerstop";

		String RECEIVER_TIME = "kr.tofor.receiver_time";
		String RECEIVER_NETWORK_START = "kr.tofor.networkstart";
		String RECEIVER_NETWORK_ERROR = "kr.tofor.networkerror";
		String RECEIVER_NETWORK_RESULT = "kr.tofor.networkresult";
		String RECEIVER_NETWORK_FINISH = "kr.tofor.networkfinish";
		String MEMBER_CODE_IS_NULL = "kr.airfactory.error.membercode.isnull";
		String RECEIVER_MESSAGE = "kr.tofor.receive_message";
	}

	public interface INTENT_KEY {
		String FROM_LOGIN_BOOL = "FROM_LOGIN_BOOL";
		String MISSION_ID_STR = "MISSION_ID_STR";
		String MISSION_TITLE_STR = "MISSION_TITLE_STR";
		String DATA = "DATA";
		String GRADE_INT = "GRADE_INT";
		String USER_TYPE = "USER_TYPE";
		String MEMB_ID = "MEMB_ID";
		String TIMELINE_ID = "TIMELINE_ID";
		String CATEGORY = "CATEGORY";

		String SUPERGREENER_ID = "SUPERGREENER_ID";

		String IMAGE_URL = "IMAGE_URL";
		String EMAIL = "EMAIL";
		String NICKNAME = "NICKNAME";

		String CAMERA_TYPE = "CAMERA_TYPE";
		String CAMERA_FILE_ABSOLUTE_PATH = "CAMERA_FILE_ABSOLUTE_PATH";
		String FROM_ACTIVITY = "FROM_ACTIVITY";

		String IMAGE_PATH = "IMAGE_PATH";

		String INFO_LINK = "INFO_LINK";
		String IS_FROM_PUSH = "IS_FROM_PUSH";
	}

	public interface ACTIVITY_REQUEST_CODE {
		int PICK_GALLERY = 1;
		int PICK_CAMERA = 2;
		int PICK_CROP = 3;

		int EMAIL_LOGIN = 10;
		int IS_LOGOUT = 11;
		int SNS_ACCOUNT_INPUT = 12;
		int FACEBOOK_CONNECT = 64206;
		int FACEBOOK_SHARE = 64207;

		int PG21_TODAY_MISSION_UPLOAD = 20;
		int PG21_TODAY_MISSION_UPLOAD_COMPLETE = 22;

		int PERMISSION_ABOUT_GALLERY = 100;
		int PERMISSION_ABOUT_CAMERA = 101;
		int PERMISSION_CONTACT = 102;
		int PERMISSION_CUSTOM_CAMERA = 103;
		int PERMISSION_LOCATION = 104;

		int CAMERA_ACT = 500;

	}

	public interface YN {
		String NO = "N";
		String YES = "Y";
	}

	public interface DIALOG_TYPE {
		int APP_FINISH = 1;
		int APP_FORCE_UPDATE = 2;
		int APP_SERVICE_CHECK = 3;
		int LOGOUT = 4;

		int SELECT_IMAGE = 7;
		int DELETE_CONFIRM = 10;
		int EXPAND_IMAGE = 11;

		int SELECT_FILTER_CAR = 20;
		int SELECT_FILTER_USER = 21;

		int SHOW_PROFILE_IMAGE = 30;

		int ASK_FOR_DATE = 40;
		int ZERO_HEART = 41;
		int GIVE_HEARTS = 42;

		int WRITE_REVIEW = 50;
		int DELETE_RIDING = 51;
		int DELETE_DRIVING = 52;

		int CHECK_INTERNET_CONECTTION = 60;
	}

	public interface DIALOG_SELECT {
		int CONFIRM = 1;
		int CANCEL = 2;

		int CAMERA = 10;
		int GALLERY = 11;

		int TYPE_IMAGE = 12;
		int TYPE_TEXT = 13;

		int DO_VOTE = 14;

		int SEARCH = 15;

		int SHARE_COPY_URL = 20;
		int SHARE_FACEBOOK = 21;
		int SHARE_INSTAGRAM = 22;
		int SHARE_KAKAOSTORY = 23;
	}

	public interface FOLLOW_TYPE {
		int FOLLOWING = 900;
		int FOLLOWER = 901;
	}

	public interface PUSH_TYPE {
		int PUSH_TYPE_MESSAGE_RECEIVE = 100;
		int PUSH_TYPE_RESERVATION_REQUEST = 200;
		int PUSH_TYPE_RESERVATION_REJECT = 300;
		int PUSH_TYPE_RESERVATION_CONFIRM = 400;
		int PUSH_TYPE_RESERVATION_24_TIMEOUT = 500;
		int PUSH_TYPE_RESERVATION_24_TIMEOUT_PAY = 600;
		int PUSH_TYPE_RESERVATION_COMPLETE = 700;
		int PUSH_TYPE_HART_RECEIVE = 800;
	}


}
