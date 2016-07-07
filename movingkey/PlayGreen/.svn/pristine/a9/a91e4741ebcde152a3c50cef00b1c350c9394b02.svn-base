package kr.innisfree.playgreen.fragment.camera;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.sprylab.android.widget.TextureVideoView;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkJson;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.CameraActivity;
import kr.innisfree.playgreen.adapter.VideoPreviewAdapter;
import kr.innisfree.playgreen.widget.EraseView;

public class CameraCgToolFrag extends ParentFrag implements View.OnClickListener {

    private final static String TAG = "CameraCgToolFrag";

    private String absolutePath;
    private String firstFrameAbsolutePath;

    private AppBarLayout appBarLayout;
    private FrameLayout flWrappterToolbar;
    private Toolbar mToolbar;
    private TextView txtTitle, txtCgGuide;
    public LinearLayout llBrush, llErase, llEdit;
    private RangeSeekBar rangeSeekBar;
    private RecyclerView recyclerview;
    private VideoPreviewAdapter adapter;
    public FrameLayout llcgTollDefault;

    /**
     * media
     **/
    private TextureVideoView videoView;
    private Bitmap bitmapOverlay;
    private LinearLayout llWrapperEraseView;
    public EraseView wsvImgOverlay;
    private ArrayList<Bitmap> rev = new ArrayList<Bitmap>();
    private boolean isSpiltVideo = false;
    private boolean isComplited = true;
    private int startTime = 0;
    private int endTime = 10;
    private BitmapsFromVideoAsync bitmapsFromVideoAsync;
    private TrimAsyncTask trimAsyncTask;

    private Handler playHandler;
    private ControlVideoView controlVideoView;
    private int countSeek = 0;
    private Handler updateHandler;

    private Handler uiHandler;

    private CameraActivity cameraActivity;

    public CameraCgToolFrag() {
    }

    @SuppressLint("ValidFragment")
    public CameraCgToolFrag(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public static CameraCgToolFrag newInstance(String absolutePath) {
        CameraCgToolFrag frag = new CameraCgToolFrag(absolutePath);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** for progress **/
        bitmapsFromVideoAsync = new BitmapsFromVideoAsync();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            bitmapsFromVideoAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            bitmapsFromVideoAsync.execute();

        if (getActivity() instanceof CameraActivity)
            cameraActivity = (CameraActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_camera_cg_tool, null);

        /** Init Toolbar*/
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appBarLayout);
        flWrappterToolbar = (FrameLayout) view.findViewById(R.id.camera_cg_tool_fl_wrapper_toolbar);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.str_camera_cg_tool_title));

        /** handlers **/
        uiHandler = new Handler();
        playHandler = new Handler();
        controlVideoView = new ControlVideoView();
        updateHandler = new Handler();

        /** video view to play .mp4 file **/
        videoView = (TextureVideoView) view.findViewById(R.id.camera_cg_tool_video_view_preview);
        videoView.setVideoURI(Uri.parse(absolutePath));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (isComplited) {
                            countSeek = 0;
                            videoView.seekTo(startTime);
                            videoView.start();
                        }
                    }
                });

            }
        });
//        videoView.setOn
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
                rangeSeekBar.setPlayIndicator(0);
                updateHandler.postDelayed(updateVideoTime, 200);
            }
        });
        videoView.seekTo(0);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                videoView.start();
            }
        });

        /**
         * wrapper for scratch view
         * We make wrapper linearlayout to add EraseView that is used by overlay image.
         */
        llWrapperEraseView = (LinearLayout) view.findViewById(R.id.camera_cg_tool_ll_wrapper_eraseview);


        /** guide **/
        txtCgGuide = (TextView) view.findViewById(R.id.camera_cg_tool_tv_cg_guide);

        /**
         * footer
         * There are two footer.
         * First footer include brush, erase and edit button so I call it default footer
         * Second footer include frames of video and rangeseekbar so we can trim video.
         */
        llcgTollDefault = (FrameLayout) view.findViewById(R.id.camera_cg_tool_footer_camera_cg_tool_default);
        llBrush = (LinearLayout) view.findViewById(R.id.camera_cg_tool_layout_brush);
        llErase = (LinearLayout) view.findViewById(R.id.camera_cg_tool_layout_erase);
        llEdit = (LinearLayout) view.findViewById(R.id.camera_cg_tool_layout_edit);

        view.findViewById(R.id.camera_cg_tool_imgbtn_back).setOnClickListener(this);
        view.findViewById(R.id.camera_cg_tool_tv_next).setOnClickListener(this);
        llBrush.setOnClickListener(this);
        llErase.setOnClickListener(this);
        llEdit.setOnClickListener(this);
        setSelected(llBrush);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new VideoPreviewAdapter(getActivity());

        recyclerview = (RecyclerView) view.findViewById(R.id.camera_cg_tool_rv_preview);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(adapter);
        recyclerview.setHasFixedSize(true);
        recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        recyclerview.requestDisallowInterceptTouchEvent(true);

        rangeSeekBar = (RangeSeekBar) view.findViewById(R.id.camera_cg_tool_rsb_preview);
        rangeSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    videoView.pause();
                    if (updateHandler != null && updateVideoTime != null)
                        updateHandler.removeCallbacks(updateVideoTime);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    startTime = (int) rangeSeekBar.getSelectedMinValue();
                    endTime = (int) rangeSeekBar.getSelectedMaxValue();

                    JYLog.D("time::startTime::" + startTime, new Throwable());
                    JYLog.D("time::endTime::" + endTime, new Throwable());

                    if (playHandler != null) {
                        playHandler.removeCallbacks(controlVideoView);
                        playHandler.post(controlVideoView);
                    }

                }
                return false;
            }
        });
        rangeSeekBar.setRangeValues(0, 10000);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        isComplited = true;

    }

    @Override
    public void onPause() {
        super.onPause();

        isComplited = false;
        videoView.stopPlayback();

        if (bitmapsFromVideoAsync != null)
            bitmapsFromVideoAsync.cancel(true);

    }

    @Override
    public void onDestroy() {

        if (trimAsyncTask != null) {
            trimAsyncTask.cancel(true);
            trimAsyncTask = null;
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_cg_tool_imgbtn_back:
                ((CameraActivity) getActivity()).onBackPressed();
                break;
            case R.id.camera_cg_tool_tv_next:
                isComplited = false;
                videoView.stopPlayback();

                Uri[] uris = new Uri[2];
                uris[0] = Uri.parse(new File(absolutePath).toString());
                uris[1] = Uri.parse(new File(absolutePath).toString());

                trimAsyncTask = new TrimAsyncTask();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    trimAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uris);
                else
                    trimAsyncTask.execute(uris);

                break;
            case R.id.camera_cg_tool_layout_brush:
                txtCgGuide.setText(getString(R.string.str_camera_cg_tool_guide_brush));
                setSelected(v);
//                wsvImgOverlay.setScratchable(true);
//                wsvImgOverlay.setmIsEraseable(false);
                wsvImgOverlay.setIsScratchable(true);
                break;
            case R.id.camera_cg_tool_layout_erase:
//                setSelected(v);
//                wsvImgOverlay.setScratchable(false);
//                wsvImgOverlay.resetView();
//                wsvImgOverlay.setIsScratchable(false);
                wsvImgOverlay.reset();
                break;
            case R.id.camera_cg_tool_layout_edit:
                txtCgGuide.setText(getString(R.string.str_camera_cg_tool_guide_trim));
//                setSelected(v);
//                wsvImgOverlay.setScratchable(false);
                wsvImgOverlay.setIsScratchable(false);
                llcgTollDefault.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onNetworkResult(int idx, NetworkJson json) {
        super.onNetworkResult(idx, json);
        switch (idx) {
            case APIKEY.NOTIFY_REGIST:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
        super.onNetworkError(idx, error, json);
        if (json == null || json.RESULT_CODE == null) return;
        switch (json.RESULT_CODE) {
            case NetworkConstantUtil.NETWORK_RESULT_CODE.NOT_EXIST_DATA:
                break;
        }
    }

    private void setSelected(View layoutTool) {

        if (layoutTool == null)
            return;

        llBrush.setSelected(false);
        llErase.setSelected(false);
        llEdit.setSelected(false);

        layoutTool.setSelected(true);
    }

    /**
     * This is aynctask to get first frame and the frame each a second.
     */
    private class BitmapsFromVideoAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isSpiltVideo = true;
        }

        @Override
        protected Void doInBackground(Void... params) {

            final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(absolutePath);

            bitmapOverlay = retriever.getFrameAtTime(10000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            JYLog.D(TAG + "::size::overlaybitmap::width::" + bitmapOverlay.getWidth(), new Throwable());
            JYLog.D(TAG + "::size::overlaybitmap::height::" + bitmapOverlay.getHeight(), new Throwable());
//            bitmapOverlay = Bitmap.createScaledBitmap(bitmapOverlay, bitmapOverlay.getWidth(), bitmapOverlay.getHeight(), false);
//            overlayBitmap = retriever.getFrameAtTime(10000);
            CameraCgToolFrag.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (bitmapOverlay != null) {
                        wsvImgOverlay = new EraseView(CameraCgToolFrag.this.getActivity());
                        wsvImgOverlay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        wsvImgOverlay.setWidth(llWrapperEraseView.getWidth());
                        wsvImgOverlay.setHeight(llWrapperEraseView.getHeight());
                        wsvImgOverlay.setFrontBitmap(bitmapOverlay);
                        llWrapperEraseView.addView(wsvImgOverlay);
                    }
//                    wsvImgOverlay.setFirstFrameFileName("pg_first_frame.png");
                }
            });

            String millis = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            if (TextUtil.isNull(millis))
                return null;

            //Get every first seconds , second seconds of second of moment bitmap Bi Ru
            for (int i = 0; i < Integer.valueOf(millis); i += 1000) {
                Bitmap bitmap = retriever.getFrameAtTime(i * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                bitmap = resizeBitmap(bitmap, bitmap.getWidth() / 15);
                rev.add(bitmap);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.setAdapterArrayList(rev);
            adapter.notifyDataSetChanged();

            isSpiltVideo = false;

        }
    }

    public Bitmap resizeBitmap(Bitmap b, int newWidthDp) {
        Float density = getContext().getResources().getDisplayMetrics().density;
        int newWidth = newWidthDp * Math.round(density);

        int width = b.getWidth();
        int height = b.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float ratio = (float) width / newWidth;
        int newHeight = (int) (height / ratio);
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(b, 0, 0, width, height, matrix, true);

        return (resizedBitmap);
    }

    private class TrimAsyncTask extends AsyncTask<Uri, Void, Uri> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Uri doInBackground(Uri... uris) {

            if (uris.length < 2) {
                return null;
            }

            Uri inputUri = uris[0];
            Uri outputUri = uris[1];
//            JYLog.D("inputUri::" + inputUri.getPath(), new Throwable());
//            JYLog.D("getSelectedMaxValue::" + rangeSeekBar.getSelectedMaxValue().intValue(), new Throwable());
//            JYLog.D("getSelectedMinValue::" + rangeSeekBar.getSelectedMinValue().intValue(), new Throwable());

//            VideoResampler resampler = new VideoResampler();
//            SamplerClip clip = new SamplerClip( inputUri );
//            clip.setStartTime( 0 );
//            clip.setEndTime( 90000 );
//
////            clip.setStartTime( mTrimStart );
////            clip.setEndTime( mTrimEnd );
//
//            resampler.addSamplerClip( clip );
//
//            // resampler.setInput( inputUri );
//            resampler.setOutput( outputUri );
//
//            // resampler.setStartTime( mTrimStart );
//            // resampler.setEndTime( mTrimEnd );
//
//            try {
//                resampler.start();
//            } catch ( Throwable e ) {
//                e.printStackTrace();
//            }

            if (startTime != 0 || endTime != 10000) {
                doShorten();
            }

            return outputUri;
        }

        @Override
        protected void onPostExecute(Uri outputUri) {

            progressDialog.dismiss();

//            ((CameraActivity) getActivity()).gotoCameraUploadFrag(absolutePath, saveBitmapToFileCache(wsvImgOverlay.getmScratchedTestBitmap()), wsvImgOverlay.getFirstFrameAbsoultePath());

            ((CameraActivity) getActivity()).gotoCameraUploadFrag(
                    absolutePath,
                    CameraActivity.saveImageToSdcard(
                            CameraFrag.TYPE_CINEMAGRAPH_OVERAY,
                            CameraActivity.getOutputMediaFile(CameraFrag.TYPE_CINEMAGRAPH_OVERAY),
                            wsvImgOverlay.getBitmap(),
                            getActivity()),
                    wsvImgOverlay.getFirstFrameAbsoultePath());

        }
    }

    private ExecutorService mThreadExecutor = null;
    static final long[] ROTATE_0 = new long[]{1, 0, 0, 1, 0, 0, 1, 0, 0};
    static final long[] ROTATE_90 = new long[]{0, 1, -1, 0, 0, 0, 1, 0, 0};
    static final long[] ROTATE_180 = new long[]{-1, 0, 0, -1, 0, 0, 1, 0, 0};
    static final long[] ROTATE_270 = new long[]{0, -1, 1, 0, 0, 0, 1, 0, 0};

    private long[] rotate0 = new long[]{0x00010000, 0, 0, 0, 0x00010000, 0, 0, 0, 0x40000000};
    private long[] rotate90 = new long[]{0, 0x00010000, 0, -0x00010000, 0, 0, 0, 0, 0x40000000};
    private long[] rotate180 = new long[]{0x00010000, 0, 0, 0, 0x00010000, 0, 0, 0, 0x40000000};
    private long[] rotate270 = new long[]{-0x00010000, 0, 0, 0, -0x00010000, 0, 0, 0, 0x40000000};

    private void doShorten() {
//        mProgressDialog = Ut.ShowWaitDialog(mCxt, 0);

//        this.mThreadExecutor.execute(new Runnable() {
//            public void run() {
//
//
//            }
//        });

        try {
//                    File folder = Ut.getTestMp4ParserVideosDir(mCxt);

//                    if (!folder.exists()) {
//                        Log.d(TAG, "failed to create directory");
//                    }

            //Movie movie = new MovieCreator().build(new RandomAccessFile("/home/sannies/suckerpunch-distantplanet_h1080p/suckerpunch-distantplanet_h1080p.mov", "r").getChannel());
//			        Movie movie = MovieCreator.build(new FileInputStream("/home/sannies/CSI.S13E02.HDTV.x264-LOL.mp4").getChannel());

            Movie movie = MovieCreator.build(new FileInputStream(absolutePath).getChannel());

            List<Track> tracks = movie.getTracks();
            movie.setTracks(new LinkedList<Track>());
            // remove all tracks we will create new tracks from the old

            double startTime = this.startTime;
//            double endTime = (double) getDuration(tracks.get(0)) / tracks.get(0).getTrackMetaData().getTimescale();
            double endTime = this.endTime;
            JYLog.D("TAG::startTime::" + startTime, new Throwable());
            JYLog.D("TAG::endTime::" + endTime, new Throwable());

            boolean timeCorrected = false;

            // Here we try to find a track that has sync samples. Since we can only start decoding
            // at such a sample we SHOULD make sure that the start of the new fragment is exactly
            // such a frame
            for (Track track : tracks) {
                if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                    if (timeCorrected) {
                        // This exception here could be a false positive in case we have multiple tracks
                        // with sync samples at exactly the same positions. E.g. a single movie containing
                        // multiple qualities of the same video (Microsoft Smooth Streaming file)

                        throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                    }
                    startTime = correctTimeToSyncSample(track, startTime, false);
                    endTime = correctTimeToSyncSample(track, endTime, true);
                    timeCorrected = true;
                }
            }

            for (Track track : tracks) {
                long currentSample = 0;
                double currentTime = 0;
                long startSample = -1;
                long endSample = -1;

                for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
                    TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
                    for (int j = 0; j < entry.getCount(); j++) {
                        // entry.getDelta() is the amount of time the current sample covers.

                        if (currentTime <= startTime) {
                            // current sample is still before the new starttime
                            startSample = currentSample;
                        }
                        if (currentTime <= endTime) {
                            // current sample is after the new start time and still before the new endtime
                            endSample = currentSample;
                        } else {
                            // current sample is after the end of the cropped video
                            break;
                        }
                        currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
                        currentSample++;
                    }
                }
                movie.addTrack(new CroppedTrack(track, startSample, endSample));
            }


            long start1 = System.currentTimeMillis();

//            IsoFile temp = new DefaultMp4Builder().build(movie);
//            Movie m = new Movie();
//            List<TrackBox> trackBoxes = temp.getMovieBox().getBoxes(TrackBox.class);
//            for (TrackBox trackBox : trackBoxes) {
//                trackBox.getTrackHeaderBox().setMatrix(ROTATE_90);
//                m.addTrack(new Mp4TrackImpl(trackBox));
//            }

            IsoFile out = new DefaultMp4Builder().build(movie);
            out.getMovieBox().getMovieHeaderBox().setMatrix(ROTATE_180);
            FileOutputStream fos = new FileOutputStream(CameraActivity.getOutputMediaFile(CameraFrag.TYPE_CINEMAGRAPH));

            FileChannel fc = fos.getChannel();
            out.getBox(fc);
            fc.close();
            fos.close();

//            Movie inMovies = new Movie();
//            File file = CameraActivity.getOutputMediaFile(CameraFrag.TYPE_CINEMAGRAPH);
//            if (file.exists()) {
////                FileInputStream fis = new FileInputStream(file);
////                //Set rotation I tried to experiment with this instruction but is not working
////                inMovies.setTracks(Matrix.ROTATE_90);
////                inMovies = MovieCreator.build(fis.getChannel());
//
//                IsoFile isoFile = new IsoFile(file);
//                Movie m = new Movie();
//
//                List<TrackBox> trackBoxes = isoFile.getMovieBox().getBoxes(TrackBox.class);
//
//                for (TrackBox trackBox : trackBoxes) {
//
//                    trackBox.getTrackHeaderBox().setMatrix(ROTATE_90);
//                    m.addTrack(new Mp4TrackImpl(trackBox));
//
//                }
//
//                inMovies = m;
//
//                IsoFile out2 = new DefaultMp4Builder().build(m);
//
//                //out.getMovieBox().getMovieHeaderBox().setMatrix(Matrix.ROTATE_180); //set orientation, default merged video have wrong orientation
//                // Create a media file name
//
//                FileChannel fc123 = new RandomAccessFile(file.getName(), "rw").getChannel();
//                out2.getBox(fc123);
//                fc.close();
//
//                //don't leave until the file is on his place
//                File file1 = CameraActivity.getOutputMediaFile(CameraFrag.TYPE_CINEMAGRAPH);
//                do {
//                    if (!file1.exists()) {
//                        JYLog.D("PM" + " Result file not ready", new Throwable());
//                    }
//                } while (!file1.exists());
//                //
//                JYLog.D("PM" + " Merge process finished", new Throwable());
//            }
//            long start3 = System.currentTimeMillis();
//            System.err.println("Building IsoFile took : " + (start2 - start1) + "ms");
//            System.err.println("Writing IsoFile took  : " + (start3 - start2) + "ms");
//            System.err.println("Writing IsoFile speed : " + (new File(String.format("TMP4_APP_OUT-%f-%f", startTime, endTime)).length() / (start3 - start2) / 1000) + "MB/s");

//                    Message.obtain(mHandler, R.id.shorten, 1, 0, filename).sendToTarget();
        } catch (FileNotFoundException e) {
//                    Message.obtain(mHandler, R.id.shorten, 0, 0, e.getMessage()).sendToTarget();
            e.printStackTrace();
        } catch (IOException e) {
//                    Message.obtain(mHandler, R.id.shorten, 0, 0, e.getMessage()).sendToTarget();
            e.printStackTrace();
        }

    }


    private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
            TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
            for (int j = 0; j < entry.getCount(); j++) {
                if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                    // samples always start with 1 but we start with zero therefore +1
                    timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
                }
                currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }


    protected static long getDuration(Track track) {
        long duration = 0;
        for (TimeToSampleBox.Entry entry : track.getDecodingTimeEntries()) {
            duration += entry.getCount() * entry.getDelta();
        }
        return duration;
    }

    private class ControlVideoView implements Runnable {

        @Override
        public void run() {
            if (videoView != null) {
                videoView.pause();
                videoView.seekTo(startTime);
                videoView.start();
                updateHandler.postDelayed(updateVideoTime, 200);
                if (endTime != 10000) {
                    playHandler.postDelayed(controlVideoView, endTime - startTime - 30);
                }
            }
        }

    }

    private Runnable updateVideoTime = new Runnable() {
        public void run() {
//            JYLog.D("fjdlkajflajdlkjfaljdlfkjadkljfakljwkejrwkjklfad::" + startTime, new Throwable());
//            JYLog.D("fjdlkajflajdlkjfaljdlfkjadkljfakljwkejrwkjklfad::" + videoView.getCurrentPosition(), new Throwable());
//            JYLog.D("fjdlkajflajdlkjfaljdlfkjadkljfakljwkejrwkjklfad::" + endTime, new Throwable());
            long currentPosition = videoView.getCurrentPosition();
            rangeSeekBar.setPlayIndicator((int) currentPosition / 8 + 60);
            updateHandler.postDelayed(this, 200);
        }
    };

}
