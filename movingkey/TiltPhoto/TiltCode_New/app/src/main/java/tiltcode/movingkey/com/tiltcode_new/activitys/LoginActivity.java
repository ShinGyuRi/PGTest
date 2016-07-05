package tiltcode.movingkey.com.tiltcode_new.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tiltcode.movingkey.com.tiltcode_new.Model.SignInResult;
import tiltcode.movingkey.com.tiltcode_new.Model.SignUpResult;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.library.BaseApplication;
import tiltcode.movingkey.com.tiltcode_new.library.ParentActivity;
import tiltcode.movingkey.com.tiltcode_new.library.util.Debug;
import tiltcode.movingkey.com.tiltcode_new.library.util.Definitions.SNSTYPE_CODE;
import tiltcode.movingkey.com.tiltcode_new.library.util.DeviceUtil;
import tiltcode.movingkey.com.tiltcode_new.library.util.JsinPreference;
import tiltcode.movingkey.com.tiltcode_new.library.util.NetworkUtil;
import tiltcode.movingkey.com.tiltcode_new.library.util.TextUtil;
import tiltcode.movingkey.com.tiltcode_new.library.util.Util;

/**
 * Created by Gyul on 2016-06-20.
 */
public class LoginActivity extends ParentActivity implements View.OnClickListener, TextWatcher {

    public static String TAG = LoginActivity.class.getSimpleName();

    EditText etUsername, etPassword;
    Button btnLogin, btnLoginGuest;
    LinearLayout layoutFacebook;
    LoginManager loginManager;
    TextView tvCreateAccount, tvCancel;
    ImageView imgCancel, imgForgetAccount;
    LinearLayout layoutUsername, layoutPassword;
    CheckBox cbKeepSigned;

    private ProfileTracker profileTracker;
    private AccessToken accessToken;
    private String facebookReferenceId = null;
    private String facebookImagePath = null;
    private String facebookName = null;
    private String facebookEmail = null;

    private String inputEmail, inputPW;

    private String id, name, email, gender, birthday;
    private Profile profile;

    CallbackManager callbackManager;

    String udid, model, language;

    SignUpActivity signUpActivity;

    JsinPreference jsinPreference;

    Bitmap profileBitmap;

//    LoginManager.getInstance().logOut();  facebook logout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText)findViewById(R.id.et_username);
        etPassword = (EditText)findViewById(R.id.et_password);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnLoginGuest = (Button)findViewById(R.id.btn_login_guest);
        layoutFacebook = (LinearLayout)findViewById(R.id.layout_facebook);
        tvCreateAccount = (TextView)findViewById(R.id.tv_create_account);
        tvCancel = (TextView)findViewById(R.id.tv_cancel);
        imgCancel = (ImageView)findViewById(R.id.img_cancel);
        imgForgetAccount = (ImageView)findViewById(R.id.img_forget_account);
        layoutUsername = (LinearLayout)findViewById(R.id.layout_username);
        layoutPassword = (LinearLayout)findViewById(R.id.layout_password);
        cbKeepSigned = (CheckBox)findViewById(R.id.check_keep_signed);

        etUsername.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        btnLogin.setOnClickListener(this);
        btnLoginGuest.setOnClickListener(this);
        btnLogin.setEnabled(false);
        layoutFacebook.setOnClickListener(this);
        tvCreateAccount.setOnClickListener(this);
        layoutUsername.setOnClickListener(this);
        layoutPassword.setOnClickListener(this);
        etUsername.setOnClickListener(this);
        etPassword.setOnClickListener(this);
        imgForgetAccount.setOnClickListener(this);

        jsinPreference = new JsinPreference(BaseApplication.getContext());

    }

    public void keepSigned()    {
        if(cbKeepSigned.isChecked())    {
        }
    }

    private AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            Profile.fetchProfileForCurrentAccessToken();
        }
    };

    public void requestFacebookLogin()  {

        loginManager = LoginManager.getInstance();
        loginManager.logOut();
        callbackManager = CallbackManager.Factory.create();
//        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
//                "user_birthday", "public_profile", "AccessToken");

        loginManager.registerCallback(callbackManager,
                new FacebookCallback < LoginResult > () {@Override
                public void onSuccess(LoginResult loginResult) {

                    Log.d(TAG, "token : \n" + loginResult.getAccessToken().getToken());
                    Log.d(TAG, "profileTracker : " + profileTracker.isTracking(), new Throwable());
                    if (profileTracker.isTracking() == false) profileTracker.startTracking();
                    accessToken = loginResult.getAccessToken();
                    Profile.fetchProfileForCurrentAccessToken();

                    profile = Profile.getCurrentProfile().getCurrentProfile();
                    if (profile != null) {
                        // user has logged in
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Util.moveActivity(LoginActivity.this, intent, -1, -1, false, false);
                    } else {
                        // user has not logged in
                        Toast.makeText(LoginActivity.this, R.string.str_toast_login_error01, Toast.LENGTH_SHORT).show();
                    }
                }

                    @Override
                    public void onCancel() {
                        profileTracker.stopTracking();
                        accessTokenTracker.stopTracking();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception != null)
                            Debug.showDebug(exception.toString());
                        profileTracker.stopTracking();
                        accessTokenTracker.stopTracking();
                    }
                });

        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("user_friends");
        permissions.add("public_profile");
        permissions.add("email");
        //permissions.add("publish_actions");
        loginManager.logInWithReadPermissions(this, permissions);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    facebookReferenceId = currentProfile.getId();
                    facebookImagePath = currentProfile.getProfilePictureUri(500, 500).toString();
                    facebookName = currentProfile.getName();
                    GraphRequest request = GraphRequest.newMeRequest(accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    if (response != null)
                                        Log.d(TAG, response.toString(), new Throwable());
                                    try {
                                        facebookEmail = object.getString("email");
                                        facebookLoginCheck();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    // fields : id, name, gender, picture(profile's image url) in public_profile
                    // fields : email in email
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
                profileTracker.stopTracking();
            }
        };
    }

    public void facebookLoginCheck()  {
        Log.d(TAG, facebookEmail + facebookReferenceId + facebookName + facebookImagePath, new Throwable());
        jsinPreference.put("username", facebookReferenceId);
        jsinPreference.put("loginType", SNSTYPE_CODE.FACEBOOK);
        Log.d(TAG, "jsinpreference username"+jsinPreference.getValue("username", ""));
        getFacebookProfilePicture(facebookReferenceId);
        requestSignUp();

    }

    public void getFacebookProfilePicture(String userID)
    {
        Picasso.with(LoginActivity.this)
                .load("http://graph.facebook.com/"+userID+"/picture?type=large")
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Log.d(TAG, " getFacebookProfilePicture: "+bitmap);
                        profileBitmap = bitmap;
//                        requestSignUp(null, facebookReferenceId, SNSTYPE_CODE.FACEBOOK);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    public void requestSignUp() {

        NetworkUtil.getHttpSerivce().snsSignUp(null, facebookReferenceId, SNSTYPE_CODE.FACEBOOK,
                new Callback<SignUpResult>() {
                    @Override
                    public void success(SignUpResult signUpResult, Response response) {
                        Log.d(TAG, "signUpResult.message: " + signUpResult.message, new Throwable());

                        if(signUpResult.message.equals(R.string.str_signup_sns_success_message))   {

                        }
                        else if (signUpResult.message.equals(R.string.str_signup_sns_overlap_message))  {

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "error.getLocalizedMessage()" + error.getLocalizedMessage());

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        btnLogin.setEnabled(checkValidation());

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())  {
            case R.id.btn_login:
                showLoading();
                if(TextUtil.isNameCheck(etUsername.getText().toString()) && TextUtil.isPassworkdCheck(etPassword.getText().toString()))
                    requestLogin();
                break;
            case R.id.btn_login_guest:
                showLoading();
                requestLoginGuest();
                break;
            case R.id.layout_facebook:
                showLoading();
                requestFacebookLogin();
                break;
            case R.id.tv_create_account:
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                Util.moveActivity(LoginActivity.this, intent, -1, -1, false, false);
                break;
            case R.id.img_forget_account:
                intent = new Intent(LoginActivity.this, FindAccountActivity.class);
                Util.moveActivity(LoginActivity.this, intent, -1, -1, false, false);
                break;
            case R.id.et_username:
                imgCancel.setVisibility(View.INVISIBLE);
                tvCancel.setVisibility(View.INVISIBLE);
                layoutUsername.setBackgroundResource(R.drawable.text_field_01);
                break;
            case R.id.et_password:
                imgCancel.setVisibility(View.INVISIBLE);
                tvCancel.setVisibility(View.INVISIBLE);
                layoutPassword.setBackgroundResource(R.drawable.text_field_01);
                break;
        }
        
    }

    private void requestLoginGuest() {

        udid = DeviceUtil.getDeviceIdObtain(BaseApplication.getContext());
        model = DeviceUtil.getDeviceModel();
        language = getLanguage();

        NetworkUtil.getHttpSerivce().enterUser(udid, model, "null", language, "null", "null",
                new Callback<SignInResult>() {
                    @Override
                    public void success(SignInResult signInResult, Response response) {
                        Log.d(TAG, "signInResult.result: " + signInResult.result, new Throwable());

                        if(signInResult.result.equals("success"))   {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Util.moveActivity(LoginActivity.this, intent, -1, -1, false, false);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    public String getLanguage()  {
        Locale locale = getResources().getConfiguration().locale;
        language = locale.getLanguage();

        return language;
    }

    private void requestLogin() {

        NetworkUtil.getHttpSerivce().signIn(etUsername.getText().toString(), etPassword.getText().toString(),
                new Callback<SignInResult>() {
                    @Override
                    public void success(SignInResult signInResult, Response response) {

                        Log.d(TAG, "signInResult.message: " + signInResult.message, new Throwable());
                        if(signInResult.result.equals("success"))   {
                            jsinPreference.put("username", etUsername.getText().toString());
                            jsinPreference.put("loginType", SNSTYPE_CODE.TILTCODE);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Util.moveActivity(LoginActivity.this, intent, -1, -1, false, false);
                        }
                        else if(signInResult.message.equals("Login failed"))   {
                            imgCancel.setImageResource(R.drawable.cancel_1);
                            tvCancel.setText(R.string.str_text_login_error);
                            etPassword.setText(null);
                            layoutPassword.setBackgroundResource(R.drawable.text_field_error);
                            imgCancel.setVisibility(View.VISIBLE);
                            tvCancel.setVisibility(View.VISIBLE);

                        }
                        else if(signInResult.message.equals("username does not exist."))  {
                            imgCancel.setImageResource(R.drawable.cancel_1);
                            tvCancel.setText(R.string.str_text_login_error);
                            etUsername.setText(null);
                            etPassword.setText(null);
                            layoutUsername.setBackgroundResource(R.drawable.text_field_error);
                            imgCancel.setVisibility(View.VISIBLE);
                            tvCancel.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Log.d(TAG, "error.getError: " + error.getLocalizedMessage(), new Throwable());

                    }
                });

    }

    public boolean checkValidation() {
        inputEmail = etUsername.getText().toString();
        inputPW = etPassword.getText().toString();
        if (!TextUtil.isNull(inputEmail) && !TextUtil.isNull(inputPW) && inputPW.length() > 3) {
            return true;
        }
        return false;
    }


}
