package tiltcode.movingkey.com.tiltcode_new.library.util;

/**
 * Created by Gyul on 2016-06-16.
 */
public class Definitions {

    public interface ACTIVITY_REQUEST_CODE {
        int PICK_GALLERY = 1;
        int PICK_CAMERA = 2;
        int PICK_CROP = 3;
        int PICK_FILE = 4;

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

    public interface MAIN_TAB {
        String HOME = "HOME";
        String COUPON = "COUPON";
    }

    public interface SNSTYPE_CODE    {
        String FACEBOOK = "Facebook";
        String TILTCODE = "Tiltcode";
    }
}
