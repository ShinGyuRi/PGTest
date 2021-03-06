package kr.innisfree.playgreen.activity.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ParentAct;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.FileUtil;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.navercorp.volleyextensions.volleyer.Volleyer;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;
import com.squareup.picasso.Picasso;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkErrorUtill;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.BridgeAct;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.fragment.setting.CountrySelectFrag;

/**
 * Created by jooyoung on 2016-04-28.
 */
public class SNSAccountInputAct extends ParentAct implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {

    private FrameLayout layoutProfileImage;
    private ImageView imgProfile;
    private EditText editName, editEmail, editIntroduce, editPhone;
    private TextView btnConfirm, txtBirthday, txtCountry, txtIntroduceWarning;
    private RelativeLayout layoutUsePolicy, layoutPrivacyPolicy;
    private CheckBox cbUsePolicy, cbPrivacyPolicy;

    private String profileImageUrl;
    private String inputName, inputEmail, inputIntroduce, inputPhone, inputBirthday, inputCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sns_account_input);
        setLoading(this);
        FileUtil.deleteTempImageFile(this);
        initToolbar();

        layoutProfileImage = (FrameLayout) findViewById(R.id.layout_profile);
        imgProfile = (ImageView) findViewById(R.id.img_profile);
        editName = (EditText) findViewById(R.id.edit_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editIntroduce = (EditText) findViewById(R.id.edit_introduce);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        layoutUsePolicy = (RelativeLayout) findViewById(R.id.layout_use_policy);
        layoutPrivacyPolicy = (RelativeLayout) findViewById(R.id.layout_privacy_policy);
        cbUsePolicy = (CheckBox) findViewById(R.id.check_use_policy);
        cbPrivacyPolicy = (CheckBox) findViewById(R.id.check_privacy_policy);
        txtBirthday = (TextView) findViewById(R.id.txt_birthday);
        txtCountry = (TextView) findViewById(R.id.txt_country);
        txtIntroduceWarning = (TextView) findViewById(R.id.txt_introduce_warning);
        btnConfirm = (TextView) findViewById(R.id.txt_confirm);
        btnConfirm.setEnabled(false);

        profileImageUrl = getIntent().getStringExtra(INTENT_KEY.IMAGE_URL);
        inputName = getIntent().getStringExtra(INTENT_KEY.NICKNAME);
        inputEmail = getIntent().getStringExtra(INTENT_KEY.EMAIL);

        if (!TextUtil.isNull(profileImageUrl))
            Picasso.with(this).load(profileImageUrl)
                    .skipMemoryCache()
                    .transform(new BitmapCircleResize(this, getResources().getDimensionPixelOffset(R.dimen.dp_100)))
                    .into(imgProfile);
        if (!TextUtil.isNull(inputName))
            editName.setText(inputName);
        if (!TextUtil.isNull(inputEmail))
            editEmail.setText(inputEmail);
        if (!TextUtil.isNull(inputBirthday))
            txtBirthday.setText(inputBirthday);
        if (!TextUtil.isNull(inputCountry))
            txtCountry.setText(inputCountry);

        cbUsePolicy.setOnCheckedChangeListener(this);
        cbPrivacyPolicy.setOnCheckedChangeListener(this);

        layoutUsePolicy.setOnClickListener(this);
        layoutPrivacyPolicy.setOnClickListener(this);
        layoutProfileImage.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        txtBirthday.setOnClickListener(this);
        txtCountry.setOnClickListener(this);
        editName.addTextChangedListener(this);
        editEmail.addTextChangedListener(this);
        editIntroduce.addTextChangedListener(this);
        editPhone.addTextChangedListener(this);
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.layout_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText(R.string.str_title_input_user_info);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        btnConfirm.setEnabled(checkValidation());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                onBackPressed();
                break;
            case R.id.layout_profile:
                showImageAlert();
                break;
            case R.id.txt_confirm:
                if (!TextUtil.isNull(inputEmail) && TextUtil.isEmailCheck(inputEmail) == false) {
                    Toast.makeText(SNSAccountInputAct.this, R.string.str_toast_invalid_email_form, Toast.LENGTH_SHORT).show();
                    return;
                }
                requestUserUpdate();
                break;
            case R.id.txt_birthday:
                showPickerDialog();
                break;
            case R.id.txt_country:
                CountrySelectFrag frag = CountrySelectFrag.newInstance();
                frag.setCountrySelectListener(new CountrySelectFrag.OnCountrySelectListener() {
                    @Override
                    public void OnCountrySelect(NetworkData data) {
                        txtCountry.setText(data.country);
                        inputCountry = data.country;
                        JYLog.D(data.country, new Throwable());
                    }
                });
                switchContent(frag, R.id.container, true, false);
                break;
            case R.id.layout_use_policy:
            case R.id.layout_privacy_policy:
                Intent intent = new Intent(this, BridgeAct.class);
                if (v.getId() == R.id.layout_use_policy)
                    intent.putExtra(INTENT_KEY.CATEGORY, Definitions.POLICY_TYPE.USE_POLICY + "");
                else
                    intent.putExtra(INTENT_KEY.CATEGORY, Definitions.POLICY_TYPE.PRIVACY_POLICY + "");
                intent.putExtra(INTENT_KEY.DATA, Definitions.GOTO.POLICY);
                Util.moveActivity(this, intent, -1, -1, false, false);
                break;
        }
    }

    @Override
    public void takePicture(Bitmap bm) {
        super.takePicture(bm);
        Picasso.with(this).load(FileUtil.getTempImageFile(this))
                .skipMemoryCache()
                .transform(new BitmapCircleResize(this, getResources().getDimensionPixelOffset(R.dimen.dp_100)))
                .into(imgProfile);
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
                txtBirthday.setText(date);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                inputBirthday = format.format(calendar.getTime());
            }
        };

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 14);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog alert = new DatePickerDialog(this, mDateSetListener, 1980, 0, 1);
        alert.getDatePicker().setMaxDate(cal.getTime().getTime());
        alert.show();
    }

    public void requestUserUpdate() {
        showLoading();
        final RequestQueue q = DefaultRequestQueueFactory.create(this);
        q.start();
        PostBuilder pb = Volleyer.volleyer(q).post(URLS.UPDATE_MEMBER)
                .addStringPart("AUTH_TOKEN", PlaygreenManager.getAuthToken())
                .addStringPart("LANGUAGE", Locale.getDefault().getLanguage())
                .addStringPart("OS", "Android OS " + Build.VERSION.SDK_INT)
                .addStringPart("MOBILE", Build.MODEL);

        if (!TextUtil.isNull(inputEmail))
            pb.addStringPart("EMAIL", inputEmail);
        if (!TextUtil.isNull(inputName))
            pb.addStringPart("MEMB_NAME", inputName);
        if (!TextUtil.isNull(inputCountry))
            pb.addStringPart("LOCATION", inputCountry);
        if (!TextUtil.isNull(inputIntroduce))
            pb.addStringPart("STATE_TEXT", inputIntroduce);
        if (!TextUtil.isNull(inputPhone))
            pb.addStringPart("PHONE", inputPhone);
        if (!TextUtil.isNull(inputBirthday))
            pb.addStringPart("BIRTH_DAY", inputBirthday);

        if (FileUtil.getTempImageFile(this).exists())
            pb.addFilePart("MEMB_IMG", FileUtil.getTempImageFile(this));
        pb.withListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JYLog.D("NETWORK", response.trim(), new Throwable());
                if (response.contains("DOCTYPE") || response.contains("doctype")) {
                    hideLoading();
                    Toast.makeText(SNSAccountInputAct.this, R.string.str_server_error_occured, Toast.LENGTH_SHORT).show();
                    return;
                }
                hideLoading();
                Gson gson = new Gson();
                NetworkJson networkJson = gson.fromJson(response, NetworkJson.class);
                if (NetworkErrorUtill.isJsonResultCheck(networkJson)) {
                    PlaygreenManager.saveUserInfo(networkJson.DATA);
                    setResult(Activity.RESULT_OK);    // ==> LoginAct.onActivityResult()호출
                    finish();
                } else {
                }
                q.stop();
            }
        }).withErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                JYLog.D("NETWORK", arg0.getMessage(), new Throwable());
                hideLoading();
                q.stop();
            }
        }).execute();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        btnConfirm.setEnabled(checkValidation());
    }

    public boolean checkValidation() {
        inputName = editName.getText().toString();
        inputEmail = editEmail.getText().toString();
        inputIntroduce = editIntroduce.getText().toString();
        inputPhone = editPhone.getText().toString();

        if (!TextUtil.isNull(inputIntroduce)) {
            if (inputIntroduce.length() > 48) {
                txtIntroduceWarning.setVisibility(View.VISIBLE);
                editIntroduce.removeTextChangedListener(this);
                editIntroduce.setText(inputIntroduce.substring(0, inputIntroduce.length() - 1));
                editIntroduce.setSelection(editIntroduce.getText().toString().length());
                editIntroduce.addTextChangedListener(this);
            } else {
                txtIntroduceWarning.setVisibility(View.GONE);
            }
        } else {
            txtIntroduceWarning.setVisibility(View.GONE);
        }

        //TODO 일단 이메일은 필수조건에서 뺌
        if (!TextUtil.isNull(inputName)
                //&& !TextUtil.isNull(inputEmail)
                && !TextUtil.isNull(inputIntroduce)
                && !TextUtil.isNull(inputPhone)
                && !TextUtil.isNull(inputCountry)
                && !TextUtil.isNull(inputBirthday)
                && cbUsePolicy.isChecked() == true
                && cbPrivacyPolicy.isChecked() == true
                ) {
            return true;
        }

        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        hiddenKeyboard();
    }
}
