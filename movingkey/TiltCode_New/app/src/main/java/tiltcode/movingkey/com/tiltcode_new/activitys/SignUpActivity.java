package tiltcode.movingkey.com.tiltcode_new.activitys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tiltcode.movingkey.com.tiltcode_new.Model.SignUpResult;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.library.ParentActivity;
import tiltcode.movingkey.com.tiltcode_new.library.listener.TakePictureListener;
import tiltcode.movingkey.com.tiltcode_new.library.util.BitmapCircleResize;
import tiltcode.movingkey.com.tiltcode_new.library.util.Definitions.SNSTYPE_CODE;
import tiltcode.movingkey.com.tiltcode_new.library.util.FileUtil;
import tiltcode.movingkey.com.tiltcode_new.library.util.NetworkUtil;
import tiltcode.movingkey.com.tiltcode_new.library.util.TextUtil;

/**
 * Created by Gyul on 2016-06-22.
 */
public class SignUpActivity extends ParentActivity implements View.OnClickListener, TextWatcher{

    public static String TAG = SignUpActivity.class.getSimpleName();

    private ImageView imgProfile, imgCancel;
    private EditText etUsername, etFirstName, etLastName, etDOB, etSex, etEmail, etPassword;
    private Button btnSignUp;
    private TextView tvCancel;

    private String inputUsername, inputFirstName, inputLastName, inputDOB, inputSex, inputEmail, inputPassword;

    private String latitude, longitude;

    private Toolbar mToolbar;

    private byte[] profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initToolbar();
        setTakePictureListener(takePictureListener);

        imgProfile = (ImageView)findViewById(R.id.img_profile);
        etUsername = (EditText)findViewById(R.id.et_signup_username);
        etFirstName = (EditText)findViewById(R.id.et_firstname);
        etLastName = (EditText)findViewById(R.id.et_lastname);
        etDOB = (EditText)findViewById(R.id.et_dob);
        etSex = (EditText)findViewById(R.id.et_sex);
        etEmail = (EditText)findViewById(R.id.et_email);
        etPassword = (EditText)findViewById(R.id.et_signup_password);
        btnSignUp = (Button)findViewById(R.id.btn_signup);
        imgCancel = (ImageView)findViewById(R.id.img_cancel);
        tvCancel = (TextView)findViewById(R.id.tv_cancel);
        btnSignUp.setEnabled(false);
        etDOB.setKeyListener(null);

        imgProfile.setOnClickListener(this);
        etUsername.addTextChangedListener(this);
        etFirstName.addTextChangedListener(this);
        etLastName.addTextChangedListener(this);
        etDOB.setOnClickListener(this);
        etDOB.addTextChangedListener(this);
        etSex.addTextChangedListener(this);
        etEmail.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        btnSignUp.setOnClickListener(this);

    }


    private TakePictureListener takePictureListener = new TakePictureListener() {
        @Override
        public void takePicture(Bitmap bm) {
            Picasso.with(SignUpActivity.this).load(FileUtil.getTempImageFile(SignUpActivity.this))
                    .skipMemoryCache()
                    .transform(new BitmapCircleResize(SignUpActivity.this, getResources().getDimensionPixelOffset(R.dimen.img_profile_dp)))
                    .into(imgProfile);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.et_dob:
                showPickerDialog();
                break;
            case R.id.btn_signup:
                if (imgProfile != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageviewToByteArray(imgProfile).compress(Bitmap.CompressFormat.PNG, 100, stream);
                    profileImage = stream.toByteArray();
                }
                if(checkPasswordValidation(inputPassword))
                    requestSignUp();
                showLoading();
                break;
            case R.id.img_profile:
                showImageAlert();
                break;
        }

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void requestSignUp() {
        latitude = null;
        longitude = null;

        NetworkUtil.getHttpSerivce().signUp(latitude, longitude, null, inputSex, inputDOB, "", SNSTYPE_CODE.TILTCODE, inputEmail, inputUsername, inputFirstName, inputLastName, inputPassword,
                new Callback<SignUpResult>() {
                    @Override
                    public void success(SignUpResult signUpResult, Response response) {
                        Log.d(TAG, "signUpResult.message: " + signUpResult.message, new Throwable());
                        if(signUpResult.message.equals(R.string.str_signup_tiltcode_success_message)) {
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();
                            hideLoading();
                            finish();
                        }
                        else if(signUpResult.message.equals(R.string.str_signup_tiltcode_overlap_message))    {
                            imgCancel.setImageResource(R.drawable.cancel_1);
                            tvCancel.setText(R.string.str_text_signup_error);
                            etUsername.setText(null);

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "error.getLocalizedMessage()" + error.getLocalizedMessage());

                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        btnSignUp.setEnabled(checkValidation());

    }

    public void showImageAlert() {
        String[] imageChoice = new String[2];
        imageChoice[0] = getString(R.string.str_take_picture_from_camera);
        imageChoice[1] = getString(R.string.str_take_picture_from_gallery);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(imageChoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 0) {
                    goCamera(0);
                } else if (which == 1) {
                    goGallery(0);
                }
            }
        });
        builder.show();
    }

    public void showPickerDialog() {
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = getString(R.string.str_birthday_format, year, (monthOfYear + 1), dayOfMonth);
                etDOB.setText(date);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                inputDOB = format.format(calendar.getTime());

            }
        };

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 14);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog alert = new DatePickerDialog(this, mDateSetListener, 1990, 0, 1);
        alert.getDatePicker().setMaxDate(cal.getTime().getTime());
        alert.show();
    }

    private boolean checkValidation() {
        inputUsername = etUsername.getText().toString();
        inputFirstName = etFirstName.getText().toString();
        inputLastName = etLastName.getText().toString();
        inputDOB = etDOB.getText().toString();
        inputSex = etSex.getText().toString();
        inputEmail = etEmail.getText().toString();
        inputPassword = etPassword.getText().toString();

        if(!TextUtil.isNull(inputUsername)
                && !TextUtil.isNull(inputFirstName)
                && !TextUtil.isNull(inputLastName)
                && !TextUtil.isNull(inputDOB)
                && !TextUtil.isNull(inputSex)
                && !TextUtil.isNull(inputEmail)
                && !TextUtil.isNull(inputPassword)) {
            return true;
        }

        return false;

    }

        public boolean checkPasswordValidation(String inputPW) {
        if (TextUtils.isEmpty(inputPW)) {
            Toast.makeText(this, R.string.str_warning_pw_min_8, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (inputPW.length() < 8) {    //|| inputPW.length()>16{
            Toast.makeText(this, R.string.str_warning_pw_min_8, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (inputPW.contains(" ")) {
            Toast.makeText(this, R.string.str_warning_pw_rule_01, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtil.isPassworkdCheck(inputPW) == false) {
            Toast.makeText(this, R.string.str_warning_pw_rule_02, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public Bitmap imageviewToByteArray(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        return  bitmap;
    }
}
