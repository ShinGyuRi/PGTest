package tiltcode.movingkey.com.tiltcode_new.library;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import tiltcode.movingkey.com.tiltcode_new.R;

/**
 * Created by Gyul on 2016-06-16.
 */
public abstract class ParentFragment extends Fragment {
    private InputMethodManager inputManager;

    public void onUIRefresh(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showKeyboard(final View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public void hiddenKeyboard() {
        if (inputManager == null)
            inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager.isActive()) {
            inputManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
        }
    }

    public void showImageAlert() {
        String[] imageChoice = new String[2];
        imageChoice[0] = getString(R.string.str_take_picture_from_camera);
        imageChoice[1] = getString(R.string.str_take_picture_from_gallery);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(imageChoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 0) {
                    ((ParentActivity) getActivity()).goCamera(0);
                } else if (which == 1) {
                    ((ParentActivity) getActivity()).goGallery(0);
                }
            }
        });
        builder.show();
    }

//    public void showPickerDialog() {
//        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                String date = getString(R.string.str_birthday_format, year, (monthOfYear + 1), dayOfMonth);
//                txtBirthday.setText(date);
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, monthOfYear, dayOfMonth);
//                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//                inputBirthday = format.format(calendar.getTime());
//
//            }
//        };
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 14);
//        cal.set(Calendar.MONTH, Calendar.JANUARY);
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        DatePickerDialog alert = new DatePickerDialog(getActivity(), mDateSetListener, 1980, 0, 1);
//        alert.getDatePicker().setMaxDate(cal.getTime().getTime());
//        alert.show();
//    }

//    public boolean checkValidation() {
//        inputName = editName.getText().toString();
//        inputPW = editPassword.getText().toString();
//        inputIntroduce = editIntroduce.getText().toString();
//        inputPhone = editPhone.getText().toString();
//
//        if (!TextUtil.isNull(inputIntroduce)) {
//            if (inputIntroduce.length() > 48) {
//                txtIntroduceWarning.setVisibility(View.VISIBLE);
//                editIntroduce.removeTextChangedListener(this);
//                editIntroduce.setText(inputIntroduce.substring(0, inputIntroduce.length() - 1));
//                editIntroduce.setSelection(editIntroduce.getText().toString().length());
//                editIntroduce.addTextChangedListener(this);
//            } else {
//                txtIntroduceWarning.setVisibility(View.GONE);
//            }
//        } else {
//            txtIntroduceWarning.setVisibility(View.GONE);
//        }
//
//        if (!TextUtil.isNull(inputName)
//                && !TextUtil.isNull(inputPW)
//                && !TextUtil.isNull(emailAddr)
//                && !TextUtil.isNull(inputIntroduce)
//                && !TextUtil.isNull(inputPhone)
//                && !TextUtil.isNull(inputCountry)
//                && !TextUtil.isNull(inputBirthday)
//                && cbUsePolicy.isChecked() == true
//                && cbPrivacyPolicy.isChecked() == true
//                ) {
//            return true;
//        }
//
//        return false;
//    }

//    public boolean checkPasswordValidation(String inputPW) {
//        if (TextUtils.isEmpty(inputPW)) {
//            Toast.makeText(getContext(), R.string.str_warning_pw_min_8, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (inputPW.length() < 8) {    //|| inputPW.length()>16{
//            Toast.makeText(getContext(), R.string.str_warning_pw_min_8, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (inputPW.contains(" ")) {
//            Toast.makeText(getContext(), R.string.str_warning_pw_rule_01, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (TextUtil.isPassworkdCheck(inputPW) == false) {
//            Toast.makeText(getContext(), R.string.str_warning_pw_rule_02, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }

    public void setCutOffBackgroundTouch(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
    }


    public void showLoading() {
        if (getActivity() != null && getActivity() instanceof ParentActivity)
            ((ParentActivity) getActivity()).showLoading();
    }

    public void hideLoading() {
        if (getActivity() != null && getActivity() instanceof ParentActivity)
            ((ParentActivity) getActivity()).hideLoading();
    }



    public boolean permissionCheck(String[] reqPermission, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isDenied = false;
            for (String req : reqPermission) {
                if (ActivityCompat.checkSelfPermission(getContext(), req) == PackageManager.PERMISSION_DENIED) {
                    isDenied = true;
                    break;
                }
            }
            if (isDenied) {
                requestPermissions(reqPermission, reqCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//		for(String permission: permissions)
//			JYLog.D(requestCode +" permission: "+ permission, new Throwable());
//		for(int result : grantResults)
//			JYLog.D(requestCode +" result: "+ result, new Throwable());
//		switch (requestCode){
//			case Definitions.ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_CAMERA:
//				for(int result : grantResults){
//					if(result != PackageManager.PERMISSION_GRANTED){
//						showPermissionDialog(R.string.str_permission_message_camera);
//						return;
//					}
//				}
//				goCamera(0);
//				break;
//			case Definitions.ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_GALLERY:
//				goGallery(0);
//				break;
//		}
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
