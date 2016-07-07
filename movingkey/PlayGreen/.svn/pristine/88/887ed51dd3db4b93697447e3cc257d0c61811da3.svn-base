/***
 * 7  Copyright (c) 2013 CommonsWare, LLC
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.innisfree.playgreen.fragment.camera;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.Parameters;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.commonsware.cwac.camera.CameraFragment;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraUtils;
import com.commonsware.cwac.camera.CameraView;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.moyusoft.util.JYLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.CameraActivity;

public class SubCameraFrag extends CameraFragment implements OnSeekBarChangeListener {

    private static final String KEY_USE_FFC = "com.commonsware.cwac.camera.demo.USE_FFC";
    private MenuItem singleShotItem = null;
    private MenuItem autoFocusItem = null;
    private MenuItem takePictureItem = null;
    private MenuItem recordItem = null;
    private MenuItem mirrorFFC = null;
    private boolean singleShotProcessing = false;
    private SeekBar zoom = null;
    private long lastFaceToast = 0L;
    private boolean useFFC = false;

    /**
     * use
     **/
    private PlayGreenCameraHost playGreenCameraHost;
    private CameraView cameraView;
    private View btnShotCamera = null;
    private boolean isFlash = false;
    private String flashMode = null;
    private int orientation;

    private ProgressDialog progressDialog;

    static SubCameraFrag newInstance(boolean useFFC, View btnShotCamera) {
        SubCameraFrag f = new SubCameraFrag();
        Bundle args = new Bundle();

        args.putBoolean(KEY_USE_FFC, useFFC);
        f.setArguments(args);
        f.btnShotCamera = btnShotCamera;

        return (f);
    }

    public void setIsFlash(boolean isFlash) {
        this.isFlash = isFlash;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        playGreenCameraHost = new PlayGreenCameraHost(getActivity());
        SimpleCameraHost.Builder builder = new SimpleCameraHost.Builder(playGreenCameraHost);
        setCameraHost(builder.useFullBleedPreview(true).build());

        OrientationEventListener orientationListener = new OrientationEventListener(getActivity(), SensorManager.SENSOR_DELAY_UI) {
            public void onOrientationChanged(int orientation) {
//            if(canShow(orientation)){
//                show();
//            } else if(canDismiss(orientation)){
//                dismiss();
//            }
//                JYLog.D("TAG::" + orientation, new Throwable());
                SubCameraFrag.this.orientation = orientation;
            }
        };

        orientationListener.enable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        cameraView = (CameraView) super.onCreateView(inflater, container, savedInstanceState);

        cameraView.lockToLandscape(false); // lock to landscape
//        cameraView.initPreview(cameraView.getWidth(), cameraView.getHeight());

        View results = inflater.inflate(R.layout.frag_camera_view, container, false);

        ((ViewGroup) results.findViewById(R.id.camera)).addView(cameraView);

        cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnShotCamera != null)
                    btnShotCamera.setClickable(false);

                if (cameraView.isAutoFocusAvailable())
                    autoFocus();

            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        setRecordingItemVisibility();

        return results;
    }

    @Override
    public void onPause() {
        super.onPause();

        cancelTimer();

    }

//  @Override
//  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//    inflater.inflate(R.menu.camera, menu);
//
//    takePictureItem=menu.findItem(R.id.camera);
//    singleShotItem=menu.findItem(R.id.single_shot);
//    singleShotItem.setChecked(getContract().isSingleShotMode());
//    autoFocusItem=menu.findItem(R.id.autofocus);
//    flashItem=menu.findItem(R.id.flash);
//    recordItem=menu.findItem(R.id.record);
//    stopRecordItem=menu.findItem(R.id.stop);
//    mirrorFFC=menu.findItem(R.id.mirror_ffc);
//
//    if (isRecording()) {
//      recordItem.setVisible(false);
//      stopRecordItem.setVisible(true);
//      takePictureItem.setVisible(false);
//    }
//
//    setRecordingItemVisibility();
//  }
//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    switch (item.getItemId()) {
//      case R.id.camera:
//        takeSimplePicture();
//
//        return(true);
//
//      case R.id.record:
//        try {
//          record();
//          getActivity().invalidateOptionsMenu();
//        }
//        catch (Exception e) {
//          Log.e(getClass().getSimpleName(),
//                "Exception trying to record", e);
//          Toast.makeText(getActivity(), e.getMessage(),
//                         Toast.LENGTH_LONG).show();
//        }
//
//        return(true);
//
//      case R.id.stop:
//        try {
//          stopRecording();
//          getActivity().invalidateOptionsMenu();
//        }
//        catch (Exception e) {
//          Log.e(getClass().getSimpleName(),
//                "Exception trying to stop recording", e);
//          Toast.makeText(getActivity(), e.getMessage(),
//                         Toast.LENGTH_LONG).show();
//        }
//
//        return(true);
//
//      case R.id.autofocus:
//        takePictureItem.setEnabled(false);
//        autoFocus();
//
//        return(true);
//
//      case R.id.single_shot:
//        item.setChecked(!item.isChecked());
//        getContract().setSingleShotMode(item.isChecked());
//
//        return(true);
//
//      case R.id.show_zoom:
//        item.setChecked(!item.isChecked());
//        zoom.setVisibility(item.isChecked() ? View.VISIBLE : View.GONE);
//
//        return(true);
//
//      case R.id.flash:
//      case R.id.mirror_ffc:
//        item.setChecked(!item.isChecked());
//
//        return(true);
//    }
//
//    return(super.onOptionsItemSelected(item));
//  }

    boolean isSingleShotProcessing() {
        return (singleShotProcessing);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (fromUser) {
            zoom.setEnabled(false);
            zoomTo(zoom.getProgress()).onComplete(new Runnable() {
                @Override
                public void run() {
                    zoom.setEnabled(true);
                }
            }).go();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // ignore
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // ignore
    }

    void setRecordingItemVisibility() {
        if (zoom != null && recordItem != null) {
            if (getDisplayOrientation() != 0
                    && getDisplayOrientation() != 180) {
                recordItem.setVisible(false);
            }
        }
    }

    Contract getContract() {
        return ((Contract) getActivity());
    }

    public void takeSimplePicture() {

        if (btnShotCamera == null)
            return;

        btnShotCamera.setEnabled(false);

        CameraHost host = getCameraHost();
        PictureTransaction xact = new PictureTransaction(host);

        xact.displayOrientation(displayOrientation(orientation));
        if (isFlash) {
            xact.flashMode(flashMode);
        }

        takePicture(xact);

        if (progressDialog != null)
            progressDialog.show();

    }

    public void takeSimpleVideo() {

        if (!isRecording()) {
            try {
                record();
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                startTimer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (CameraActivity.duration <= 0) {
                    stopRecording();
                    if (playGreenCameraHost != null && playGreenCameraHost.getVideo() != null) {
//                        ((CameraActivity) getActivity()).gotoCameraUploadActivity(CameraFrag.TYPE_CINEMAGRAPH, playGreenCameraHost.getVideo().getAbsolutePath());
                    }
                } else {
                    Toast.makeText(getActivity(), "최소 10초 이상 촬영을 하여야합니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    interface Contract {
        boolean isSingleShotMode();

        void setSingleShotMode(boolean mode);
    }

    class PlayGreenCameraHost extends SimpleCameraHost implements Camera.FaceDetectionListener {

        private boolean supportsFaces = false;
        private File photo;
        private File video;

        public PlayGreenCameraHost(Context _ctxt) {
            super(_ctxt);
        }

        public File getPhoto() {
            return photo;
        }

        public File getVideo() {
            return video;
        }

        /**
         * about photo path
         **/

        @Override
        protected File getPhotoDirectory() {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PlayGreen");
        }

        @Override
        protected String getPhotoFilename() {
            String ts =
                    new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

            return ("PG_Photo_" + ts + ".png");
        }

        @Override
        protected File getPhotoPath() {
            File dir = getPhotoDirectory();

            if (!dir.exists())
                dir.mkdirs();

            photo = new File(dir, getPhotoFilename());

            return photo;
        }

        /**
         * about video path
         **/

        protected File getVideoDirectory() {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES + "/PlayGreen");
        }

        protected String getVideoFilename() {
            String ts =
                    new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

            return ("Video_" + ts + ".mp4");
        }

        protected File getVideoPath() {
            File dir = getVideoDirectory();

            if (!dir.exists())
                dir.mkdirs();

            video = new File(dir, getVideoFilename());

            return video;
        }

        @Override
        public boolean useFrontFacingCamera() {
            if (getArguments() == null) {
                useFFC = false;
                return (false);
            }
            useFFC = (getArguments().getBoolean(KEY_USE_FFC));
            return useFFC;
        }

        @Override
        public boolean useSingleShotMode() {
            if (singleShotItem == null) {
                return (false);
            }

            return (singleShotItem.isChecked());
        }

        @Override
        public void saveImage(PictureTransaction xact, byte[] image) {

            if (useSingleShotMode()) {
                singleShotProcessing = false;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (btnShotCamera != null)
                            btnShotCamera.setEnabled(true);
                    }
                });
            } else {
                super.saveImage(xact, image);
//                saveBitmap(xact, image, getPhotoPath());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnShotCamera.setEnabled(true);
                    }
                });

                if (photo != null) {
//                    rotateBitmap(orientation, photo);
                    if (progressDialog != null && progressDialog.isShowing())
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
//                    ((CameraActivity) getActivity()).go(CameraFrag.TYPE_CAMERA, photo.getAbsolutePath());
                } else {
                    if (progressDialog != null && progressDialog.isShowing())
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                }
            }

//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    btnShotCamera.setEnabled(true);
//                }
//            });
//            if (photo != null) {
//                rotateBitmap(orientation, photo);
////                if (progressDialog != null && progressDialog.isShowing())
////                    getActivity().runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            progressDialog.dismiss();
////                        }
////                    });
////                ((CameraActivity) getActivity()).gotoCameraUploadActivity(CameraFrag.TYPE_CAMERA, photo.getAbsolutePath());
//            } else {
//                if (progressDialog != null && progressDialog.isShowing())
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressDialog.dismiss();
//                        }
//                    });
//            }

        }

        @Override
        public void autoFocusAvailable() {
            if (cameraView != null) {
                cameraView.setClickable(true);

                if (supportsFaces)
                    startFaceDetection();
            }
        }

        @Override
        public void autoFocusUnavailable() {
            if (autoFocusItem != null) {
                stopFaceDetection();

                if (supportsFaces)
                    cameraView.setClickable(false);
//                    autoFocusItem.setEnabled(false);
            }
        }

        @Override
        public void onCameraFail(CameraHost.FailureReason reason) {
            super.onCameraFail(reason);

            Toast.makeText(getActivity(),
                    "Sorry, but you cannot use the camera now!",
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public Parameters adjustPreviewParameters(Parameters parameters) {
            flashMode =
                    CameraUtils.findBestFlashModeMatch(parameters,
                            Parameters.FLASH_MODE_RED_EYE,
                            Parameters.FLASH_MODE_ON);

            if (parameters.getMaxNumDetectedFaces() > 0) {
                supportsFaces = true;
            } else {
//                Toast.makeText(getActivity(),
//                        "Face detection not available for this camera",
//                        Toast.LENGTH_LONG).show();
            }

            return (super.adjustPreviewParameters(parameters));
        }

        @Override
        public void onFaceDetection(Face[] faces, Camera camera) {
            if (faces.length > 0) {
                long now = SystemClock.elapsedRealtime();

                if (now > lastFaceToast + 10000) {
//                    Toast.makeText(getActivity(), "I see your face!",
//                            Toast.LENGTH_LONG).show();
                    lastFaceToast = now;
                }
            }
        }

        @Override
        @TargetApi(16)
        public void onAutoFocus(boolean success, Camera camera) {
            super.onAutoFocus(success, camera);

            if (btnShotCamera != null)
                btnShotCamera.setClickable(true);

            if (cameraView != null)
                cameraView.setClickable(false);
        }

        @Override
        public boolean mirrorFFC() {
            return (true);
        }
    }

    private boolean isRecording = false;
    private Timer timer;

    private boolean isStartTimer() {
        return (timer != null);
    }

    private void startTimer() {
        isRecording = true;
        timer = new Timer();
        timer.schedule(measureDuration, 1000, 1000);
    }

    private void cancelTimer() {
        if (timer != null) {
            CameraActivity.duration = 10;
            timer.cancel();
            timer = null;
        }
    }

    private TimerTask measureDuration = new TimerTask() {
        @Override
        public void run() {
            CameraActivity.duration--;
            JYLog.D("TAG::" + CameraActivity.duration, new Throwable());
            if (CameraActivity.duration == 0) {
                /** 자동으로 화면 넘어감. **/
                takeSimpleVideo();
                cancelTimer();
            }
        }
    };

    private int displayOrientation(int orientation) {
        JYLog.D("TAG::" + orientation, new Throwable());
        if (85 <= orientation && 95 >= orientation) {
            /** left orienation **/
            return -90;
        } else if (272 <= orientation && 282 >= orientation) {
            /** right orienation **/
            return 90;
        } else if (177 <= orientation && 187 >= orientation) {
            /** below orienation **/
            return 180;
        } else {
            return 0;
        }
    }

    private void rotateBitmap(int orientation, final File photo) {

        JYLog.D("TAG::" + orientation, new Throwable());
        final Matrix matrix = new Matrix();
        if (80 <= orientation && 100 >= orientation) {
            /** left orienation **/
            matrix.setRotate(90);
        } else if (270 <= orientation && 285 >= orientation) {
            /** right orienation **/
            matrix.setRotate(-90);
        } else if (175 <= orientation && 190 >= orientation) {
            /** below orienation **/
            matrix.setRotate(180);
//            matrix.postScale(-1, 1);
        } else {
            if (progressDialog != null && progressDialog.isShowing())
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
//            ((CameraActivity) getActivity()).gotoCameraUploadActivity(CameraFrag.TYPE_CAMERA, photo.getAbsolutePath());
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath());
                    JYLog.D("TAG::width::" + bitmap.getWidth(), new Throwable());
                    JYLog.D("TAG::height::" + bitmap.getHeight(), new Throwable());
                    Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap.recycle();
                    saveBitmapToFile(oriented, photo);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void saveBitmapToFile(Bitmap bitmap, File photo) {

        File file = new File(photo.getAbsolutePath());

        try {
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            bitmap.recycle();

            if (progressDialog != null && progressDialog.isShowing())
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
//            ((CameraActivity) getActivity()).gotoCameraUploadActivity(CameraFrag.TYPE_CAMERA, photo.getAbsolutePath());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveBitmap(PictureTransaction xact, byte[] image, File photo) {

        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());
            Bitmap picture = BitmapFactory.decodeByteArray(image, 0, image.length);
            Matrix matrix = new Matrix();
            if (useFFC) {
                matrix.setScale(-1, 1);  // 좌우반전

            }

            if (85 <= orientation && 95 >= orientation) {
                /** left orienation **/
                matrix.setRotate(90);
            } else if (272 <= orientation && 282 >= orientation) {
                /** right orienation **/
                matrix.setRotate(-90);
            } else if (177 <= orientation && 187 >= orientation) {
                /** below orienation **/
                matrix.setRotate(180);
            }

            Bitmap resized =
                    Bitmap.createBitmap(
                            picture, // bitmap
                            0, 0, // x, y
                            picture.getWidth(), picture.getHeight(), // width, height
                            matrix, // metrix
                            true // filter
                    );
            resized.compress(Bitmap.CompressFormat.PNG, 100, fos);
            picture.recycle();

//            OutputStream outStream = new FileOutputStream(fos);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            bos.write(image);
            bos.flush();
            fos.getFD().sync();
            bos.close();

//            if (true) {
//                MediaScannerConnection.scanFile(ctxt,
//                        new String[]{photo.getPath()},
//                        SCAN_TYPES, null);
//            }
        } catch (java.io.IOException e) {
//            handleException(e);
        }
    }
}