package tiltcode.movingkey.com.tiltcode_new.library;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.activitys.SplashActivity;
import tiltcode.movingkey.com.tiltcode_new.library.dialog.CommonLoadingDialog;
import tiltcode.movingkey.com.tiltcode_new.library.listener.TakePictureListener;
import tiltcode.movingkey.com.tiltcode_new.library.util.Definitions.ACTIVITY_REQUEST_CODE;
import tiltcode.movingkey.com.tiltcode_new.library.util.FileUtil;

/**
 * Created by Gyul on 2016-06-16.
 */
public class ParentActivity extends AppCompatActivity {

    private int IMAGE_SIZE = 500;

    private InputMethodManager inputManager;
    private CommonLoadingDialog loading;

    public static ArrayList<Activity> activityList;

    public void takePicture(Bitmap bm) {
    }

    public void onUIRefresh() {
    }

    private TakePictureListener takePickerListener;

    public void setTakePictureListener(TakePictureListener listener) {
        this.takePickerListener = listener;
    }

    public void hiddenKeyboard() {
        if (inputManager == null)
            inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager.isActive()) {
            inputManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
        }
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseApplication.setCurrentActivity(this);
        if (activityList == null) {
            activityList = new ArrayList<Activity>();
        }
        if(this instanceof SplashActivity == false)
            activityList.add(this);

        if (savedInstanceState != null) {
            activityList = null;
            Intent i = new Intent(BaseApplication.getContext(), SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }


    }

    public void showLoading() {
        try {
            if (loading == null)
                return;
            loading.show();
        } catch (Exception e) {
        }
    }

    public void hideLoading() {
        try {
            if (loading == null)
                return;
            loading.dismiss();
        } catch (Exception e) {
        }
    }

    public void switchContent(Fragment fragment, int contentId, boolean isHistory, boolean isAni) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isHistory)
            ft.addToBackStack(null);
        if (isAni) {
            ft.setCustomAnimations(R.anim.left, R.anim.left2, R.anim.right2, R.anim.right);
        }
        ft.replace(contentId, fragment).commit();
    }


    public boolean permissionCheck(String[] reqPermission, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isDenied = false;
            for (String req : reqPermission) {
                if (ActivityCompat.checkSelfPermission(this, req) == PackageManager.PERMISSION_DENIED) {
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
        switch (requestCode) {
            case ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_CAMERA:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        showPermissionDialog(R.string.str_permission_message_camera);
                        return;
                    }
                }
                goCamera(0);
                break;
            case ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_GALLERY:
                goGallery(0);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void showPermissionDialog(int messageRes) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(messageRes);
        alertDialog.setNegativeButton(R.string.str_close, null);
        alertDialog.setPositiveButton(R.string.str_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();

                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    startActivity(intent);
                }
            }
        });
        alertDialog.show();
    }

    /*******************************
     * 사진관련
     *******************************/

//    public void gotoCameraActivity(int typeFramActivity) {
//        Intent i = new Intent(this, CameraActivity.class);
//        i.putExtra(Definitions.INTENT_KEY.FROM_ACTIVITY, typeFramActivity);
//        Util.moveActivity(this, i, -1, -1, false, false, ACTIVITY_REQUEST_CODE.CAMERA_ACT);
//    }

    public int cropRatio;

    public void goGallery(int cropRatio) {
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean isValid = permissionCheck(permission, ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_GALLERY);
        if (isValid == false) return;
        this.cropRatio = cropRatio;
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(MediaStore.Images.Media.CONTENT_TYPE);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, ACTIVITY_REQUEST_CODE.PICK_GALLERY);
    }

    public void goCamera(int cropRatio) {
        String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean isValid = permissionCheck(permission, ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_CAMERA);
        if (isValid == false) return;
        this.cropRatio = cropRatio;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FileUtil.getTempImageFile(this)));
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE.PICK_CAMERA);
    }

    private void takePictureFromGallery()
    {
        startActivityForResult(
                Intent.createChooser(
                        new Intent(Intent.ACTION_GET_CONTENT)
                                .setType("image/*"), "Choose an image"),
                ACTIVITY_REQUEST_CODE.PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_REQUEST_CODE.PICK_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) return;
            Uri uri = data.getData();
            FileUtil.copyUriToFile(this, uri, FileUtil.getTempImageFile(this));
            correctCameraOrientation(FileUtil.getTempImageFile(this));
            if (cropRatio > 0) jyCrop();
            else doFinalProcess();
        } else if (requestCode == ACTIVITY_REQUEST_CODE.PICK_CAMERA && resultCode == Activity.RESULT_OK) {
            correctCameraOrientation(FileUtil.getTempImageFile(this));
            if (cropRatio > 0) jyCrop();
            else doFinalProcess();
        } else if (requestCode == ACTIVITY_REQUEST_CODE.PICK_CROP && resultCode == Activity.RESULT_OK) {
            doFinalProcess();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void doFinalProcess() {
        Bitmap bm = BitmapFactory.decodeFile(FileUtil.getTempImageFile(this).getAbsolutePath());
        if (takePickerListener != null) {
            takePickerListener.takePicture(bm);
        } else {
            takePicture(bm);
        }
    }

    private void jyCrop() {
    }

    private void correctCameraOrientation(File imgFile) {
        Bitmap bitmap = FileUtil.loadImageWithSampleSize(imgFile, IMAGE_SIZE);
        try {
            ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifRotateDegree = exifOrientationToDegrees(exifOrientation);
            bitmap = FileUtil.rotateImage(bitmap, exifRotateDegree);
            FileUtil.saveBitmapToFile(bitmap, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

//    public boolean checkPasswordValidation(String inputPW) {
//        if (TextUtils.isEmpty(inputPW)) {
//            Toast.makeText(this, R.string.str_warning_pw_min_8, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (inputPW.length() < 8) {    //|| inputPW.length()>16{
//            Toast.makeText(this, R.string.str_warning_pw_min_8, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (inputPW.contains(" ")) {
//            Toast.makeText(this, R.string.str_warning_pw_rule_01, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (TextUtil.isPassworkdCheck(inputPW) == false) {
//            Toast.makeText(this, R.string.str_warning_pw_rule_02, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }

//    public boolean checkValidation() {
//        inputEmail = editEmail.getText().toString();
//        inputPW = editPW.getText().toString();
//        if (!TextUtil.isNull(inputEmail) && !TextUtil.isNull(inputPW) && inputPW.length() > 3) {
//            return true;
//        }
//        return false;
//    }

//    public void showImageAlert() {
//        String[] imageChoice = new String[2];
//        imageChoice[0] = getString(R.string.str_take_picture_from_camera);
//        imageChoice[1] = getString(R.string.str_take_picture_from_gallery);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setItems(imageChoice, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                if (which == 0) {
//                    ((ParentAct) getActivity()).goCamera(0);
//                } else if (which == 1) {
//                    ((ParentAct) getActivity()).goGallery(0);
//                }
//            }
//        });
//        builder.show();
//    }
}
