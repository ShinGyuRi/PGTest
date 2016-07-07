package kr.innisfree.playgreen.common;

import android.content.Context;
import android.content.Intent;

import com.BaseApplication;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.AUTH_CHANNEL;
import com.moyusoft.util.Definitions.INTENT_ACTION;
import com.moyusoft.util.Definitions.PREFKEY;
import com.moyusoft.util.PrefUtil;
import com.moyusoft.util.TextUtil;
import com.volley.network.dto.NetworkData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import me.leolin.shortcutbadger.ShortcutBadger;


public class PlaygreenManager {

    public static void saveUserInfo(NetworkData data) {
        if (data == null) return;
        removeUserInfo();
        PrefUtil pref = PrefUtil.getInstance();
        pref.putPreference(PREFKEY.AUTH_TOKEN_STR, data.AUTH_TOKEN);
        pref.putPreference(PREFKEY.AUTH_TOKEN_DUE_DT_STR, data.AUTH_TOKEN_DUE_DT);
        pref.putPreference(PREFKEY.MEMB_NAME_STR, data.MEMB_NAME);
        pref.putPreference(PREFKEY.EMAIL_STR, data.EMAIL);
        pref.putPreference(PREFKEY.MEMB_ID_STR, data.MEMB_ID);
        pref.putPreference(PREFKEY.LAST_LOGIN_DT_LONG, data.LAST_LOGIN_DT);
//		pref.putPreference(PREFKEY.NICK_NAME_STR, data.NICK_NAME);
        pref.putPreference(PREFKEY.PROFILE_IMAGE_STR, data.MEMB_IMG);
        pref.putPreference(PREFKEY.BIRTH_DAY_STR, data.BIRTH_DAY);
        pref.putPreference(PREFKEY.SEX_STR, data.SEX);
        pref.putPreference(PREFKEY.AGE_GROUP_STR, data.AGE_GROUP);
        pref.putPreference(PREFKEY.INFO_OPEN_MODE_STR, data.INFO_OPEN_MODE);
        pref.putPreference(PREFKEY.AUTH_CHANNEL_STR, data.AUTH_CHANNEL);
        pref.putPreference(PREFKEY.REG_DATE_STR, data.REG_DT);
        pref.putPreference(PREFKEY.PUSH_KEY_STR, data.PUSH_KEY);
        pref.putPreference(PREFKEY.BESTGREENER_YN_STR, data.BESTGREENER_YN);
        pref.putPreference(PREFKEY.SUPERGREENER_YN_STR, data.SUPERGREENER_YN);

        pref.putPreference(PREFKEY.INTRO_TEXT_STR, data.INTRO_TEXT);
        pref.putPreference(PREFKEY.STATE_TEXT_STR, data.STATE_TEXT);
        pref.putPreference(PREFKEY.SEX_OPEN_YN_STR, data.SEX_OPEN_YN);
        pref.putPreference(PREFKEY.PHONE_OPEN_YN_STR, data.PHONE_OPEN_YN);
        pref.putPreference(PREFKEY.EMAIL_OPEN_YN_STR, data.EMAIL_OPEN_YN);
        pref.putPreference(PREFKEY.ORIGIN_SAVE_YN_STR, data.ORIGIN_SAVE_YN);
        pref.putPreference(PREFKEY.PUSH_YN_STR, data.PUSH_YN);
        pref.putPreference(PREFKEY.PWD_UPDATE_YN_STR, data.PWD_UPDATE_YN);
        pref.putPreference(PREFKEY.DATA_SAVING_YN_STR, data.DATA_SAVING_YN);
        pref.putPreference(PREFKEY.TEMP_PWD_YN_STR, data.TEMP_PWD_YN);
        pref.putPreference(PREFKEY.PHONE_STR, data.PHONE);
        pref.putPreference(PREFKEY.SNS_ID_STR, data.SNS_ID);
        pref.putPreference(PREFKEY.LOCATION_STR, data.LOCATION);
        pref.putPreference(PREFKEY.LOCATION_OPEN_YN_STR, data.LOCATION_OPEN_YN);
        pref.putPreference(PREFKEY.LOGIN_FAIL_COUNT_INT, data.LOGIN_FAIL_COUNT);

        switch (data.AUTH_CHANNEL) {
            case AUTH_CHANNEL.FACEBOOK:
                pref.putPreference(Definitions.PREFKEY.SNS_ACCOUNT_FACEBOOK_BOOL, true);
                break;
            case AUTH_CHANNEL.KAKAOTALK:
                pref.putPreference(Definitions.PREFKEY.SNS_ACCOUNT_KAKAO_BOOL, true);
                break;
            case AUTH_CHANNEL.INSTAGRAM:
                pref.putPreference(Definitions.PREFKEY.SNS_ACCOUNT_INSTAGRAM_BOOL, true);
                break;
        }
    }

    public static void removeUserInfo() {
        PrefUtil pref = PrefUtil.getInstance();
        pref.removePreference(PREFKEY.AUTH_TOKEN_STR);
        pref.removePreference(PREFKEY.AUTH_TOKEN_DUE_DT_STR);
        pref.removePreference(PREFKEY.MEMB_NAME_STR);
        pref.removePreference(PREFKEY.EMAIL_STR);
//		pref.removePreference(PREFKEY.NICK_NAME_STR);
        pref.removePreference(PREFKEY.MEMB_ID_STR);
        pref.removePreference(PREFKEY.LAST_LOGIN_DT_LONG);
        pref.removePreference(PREFKEY.PROFILE_IMAGE_STR);
        pref.removePreference(PREFKEY.BIRTH_DAY_STR);
        pref.removePreference(PREFKEY.SEX_STR);
        pref.removePreference(PREFKEY.AGE_GROUP_STR);
        pref.removePreference(PREFKEY.INFO_OPEN_MODE_STR);
        pref.removePreference(PREFKEY.AUTH_CHANNEL_STR);
        pref.removePreference(PREFKEY.REG_DATE_STR);
        pref.removePreference(PREFKEY.PUSH_KEY_STR);
        pref.removePreference(PREFKEY.BESTGREENER_YN_STR);
        pref.removePreference(PREFKEY.SUPERGREENER_YN_STR);

        pref.removePreference(PREFKEY.INTRO_TEXT_STR);
        pref.removePreference(PREFKEY.STATE_TEXT_STR);
        pref.removePreference(PREFKEY.SEX_OPEN_YN_STR);
        pref.removePreference(PREFKEY.PHONE_OPEN_YN_STR);
        pref.removePreference(PREFKEY.EMAIL_OPEN_YN_STR);
        pref.removePreference(PREFKEY.ORIGIN_SAVE_YN_STR);
        pref.removePreference(PREFKEY.PUSH_YN_STR);
        pref.removePreference(PREFKEY.PWD_UPDATE_YN_STR);
        pref.removePreference(PREFKEY.DATA_SAVING_YN_STR);
        pref.removePreference(PREFKEY.TEMP_PWD_YN_STR);
        pref.removePreference(PREFKEY.PHONE_STR);
        pref.removePreference(PREFKEY.SNS_ID_STR);
        pref.removePreference(PREFKEY.LOCATION_STR);
        pref.removePreference(PREFKEY.LOCATION_OPEN_YN_STR);
        pref.removePreference(PREFKEY.LOGIN_FAIL_COUNT_INT);

        pref.removePreference(PREFKEY.SNS_ACCOUNT_FACEBOOK_BOOL);
        pref.removePreference(PREFKEY.SNS_ACCOUNT_INSTAGRAM_BOOL);
        pref.removePreference(PREFKEY.SNS_ACCOUNT_KAKAO_BOOL);
        pref.removePreference(PREFKEY.SNS_ACCOUNT_WECHAT_BOOL);
        pref.removePreference(PREFKEY.SNS_ACCOUNT_LINE_BOOL);
//
    }

    public static NetworkData getUserInfo() {
        PrefUtil pref = PrefUtil.getInstance();
        NetworkData userInfo = new NetworkData();
        userInfo.AUTH_TOKEN = pref.getStringPreference(PREFKEY.AUTH_TOKEN_STR);
        userInfo.AUTH_TOKEN_DUE_DT = pref.getStringPreference(PREFKEY.AUTH_TOKEN_DUE_DT_STR);
        userInfo.EMAIL = pref.getStringPreference(PREFKEY.EMAIL_STR);
        userInfo.MEMB_NAME = pref.getStringPreference(PREFKEY.MEMB_NAME_STR);
//		userInfo.NICK_NAME = pref.getStringPreference(PREFKEY.NICK_NAME_STR);
        userInfo.MEMB_ID = pref.getStringPreference(PREFKEY.MEMB_ID_STR);
        userInfo.LAST_LOGIN_DT = pref.getLongPreference(PREFKEY.LAST_LOGIN_DT_LONG);
        userInfo.MEMB_IMG = pref.getStringPreference(PREFKEY.PROFILE_IMAGE_STR);
        userInfo.BIRTH_DAY = pref.getStringPreference(PREFKEY.BIRTH_DAY_STR);
        userInfo.SEX = pref.getStringPreference(PREFKEY.SEX_STR);
        userInfo.AGE_GROUP = pref.getStringPreference(PREFKEY.AGE_GROUP_STR);
        userInfo.INFO_OPEN_MODE = pref.getStringPreference(PREFKEY.INFO_OPEN_MODE_STR);
        userInfo.AUTH_CHANNEL = pref.getStringPreference(PREFKEY.AUTH_CHANNEL_STR);
        userInfo.REG_DT = pref.getLongPreference(PREFKEY.REG_DATE_STR);
        userInfo.PUSH_KEY = pref.getStringPreference(PREFKEY.PUSH_KEY_STR);
        userInfo.BESTGREENER_YN = pref.getStringPreference(PREFKEY.BESTGREENER_YN_STR);
        userInfo.SUPERGREENER_YN = pref.getStringPreference(PREFKEY.SUPERGREENER_YN_STR);

        userInfo.INTRO_TEXT = pref.getStringPreference(PREFKEY.INTRO_TEXT_STR);
        userInfo.STATE_TEXT = pref.getStringPreference(PREFKEY.STATE_TEXT_STR);
        userInfo.SEX_OPEN_YN = pref.getStringPreference(PREFKEY.SEX_OPEN_YN_STR);
        userInfo.PHONE_OPEN_YN = pref.getStringPreference(PREFKEY.PHONE_OPEN_YN_STR);
        userInfo.EMAIL_OPEN_YN = pref.getStringPreference(PREFKEY.EMAIL_OPEN_YN_STR);
        userInfo.ORIGIN_SAVE_YN = pref.getStringPreference(PREFKEY.ORIGIN_SAVE_YN_STR);
        userInfo.PUSH_YN = pref.getStringPreference(PREFKEY.PUSH_YN_STR);
        userInfo.PWD_UPDATE_YN = pref.getStringPreference(PREFKEY.PWD_UPDATE_YN_STR);
        userInfo.DATA_SAVING_YN = pref.getStringPreference(PREFKEY.DATA_SAVING_YN_STR);
        userInfo.TEMP_PWD_YN = pref.getStringPreference(PREFKEY.TEMP_PWD_YN_STR);
        userInfo.PHONE = pref.getStringPreference(PREFKEY.PHONE_STR);
        userInfo.SNS_ID = pref.getStringPreference(PREFKEY.SNS_ID_STR);
        userInfo.LOCATION = pref.getStringPreference(PREFKEY.LOCATION_STR);
        userInfo.LOCATION_OPEN_YN = pref.getStringPreference(PREFKEY.LOCATION_OPEN_YN_STR);
        userInfo.LOGIN_FAIL_COUNT = pref.getIntPreference(PREFKEY.LOGIN_FAIL_COUNT_INT);

        userInfo.isFacebookConnected = pref.getBooleanPreference(PREFKEY.SNS_ACCOUNT_FACEBOOK_BOOL);
        userInfo.isInstagramConnected = pref.getBooleanPreference(PREFKEY.SNS_ACCOUNT_INSTAGRAM_BOOL);
        userInfo.isKakaoConnected = pref.getBooleanPreference(PREFKEY.SNS_ACCOUNT_KAKAO_BOOL);
        userInfo.isLineConnected = pref.getBooleanPreference(PREFKEY.SNS_ACCOUNT_LINE_BOOL);
        userInfo.isWechatConnected = pref.getBooleanPreference(PREFKEY.SNS_ACCOUNT_WECHAT_BOOL);

        return userInfo;
    }

    public static String getAuthToken() {
        String accessToken = PrefUtil.getInstance().getStringPreference(PREFKEY.AUTH_TOKEN_STR);
        if (!TextUtil.isNull(accessToken)) {
            return accessToken;
        } else {
            Intent i = new Intent(INTENT_ACTION.MEMBER_CODE_IS_NULL);
            BaseApplication.getContext().sendBroadcast(i);
            return null;
        }
    }

    public static String getLocation() {
        return Locale.getDefault().getCountry();        //KR
    }

    /**
     * 서버에서 지맘대로 정한 언어코드 리턴
     *
     * @return
     */
    public static String getLanguagePG() {
        String language = Locale.getDefault().getLanguage();
        switch (language) {
            case "ko":
                return "KO";
            case "en":
                return "EN";
            case "zh":
                return "ZH";
            default:
                return "EN";
        }
    }

    public static String getCountryName(String countryCode) {
        Locale locale = new Locale("", countryCode);        //대한민국
        return locale.getDisplayCountry();
    }

    public static String getLanguageName(String languageCode) {
        Locale locale = new Locale(languageCode, "");
        return locale.getDisplayLanguage();                    //한국어
    }

    public static String getDistance(int distanceMeter) {
        if (distanceMeter > 1000) {
            float dis = (float) distanceMeter / 1000;
            return String.format("%.1f", dis) + "km";
        } else {
            return distanceMeter + "m";
        }
    }

    public static String parseBirthDay(String birthday) {
        StringBuffer buffer = new StringBuffer();
        if (birthday.length() == 8) {
            buffer.append(birthday.substring(0, 4));
            buffer.append("-");
            buffer.append(birthday.substring(4, 6));
            buffer.append("-");
            buffer.append(birthday.substring(6));

            return buffer.toString();
        } else {
            return birthday;
        }
    }

    public static String getTimeStampToDate(long timestamp, boolean includeTime) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();    //get your local time zone.

        SimpleDateFormat sdf;
        if (includeTime) {
            String currentLanguage = Locale.getDefault().getLanguage();
            if (currentLanguage.equals(Locale.ENGLISH.getLanguage())) {
                sdf = new SimpleDateFormat("yyyy.MM.dd a hh:mm", Locale.ENGLISH);
            } else if (currentLanguage.equals(Locale.CHINA.getLanguage())
                    || currentLanguage.equals(Locale.CHINESE.getLanguage())
                    || currentLanguage.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())
                    || currentLanguage.equals(Locale.TRADITIONAL_CHINESE.getLanguage())) {
                sdf = new SimpleDateFormat("yyyy.MM.dd a hh:mm", Locale.CHINA);
            } else {
                sdf = new SimpleDateFormat("yyyy.MM.dd a hh:mm", Locale.KOREA);
            }
        } else {
            sdf = new SimpleDateFormat("yyyy.MM.dd");
        }

        sdf.setTimeZone(tz);    //set time zone.
        String localTime = sdf.format(new Date(timestamp * 1000));
        return localTime;
    }


    public static void updateIconBadge(Context context) {
        if (Definitions.ALARM_COUNT > 0) {
            ShortcutBadger.applyCount(context, Definitions.ALARM_COUNT);
        } else {
            ShortcutBadger.removeCount(context);
        }
    }

}
