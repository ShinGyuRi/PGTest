package kr.innisfree.playgreen.fragment.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.ExifUtil;
import com.moyusoft.util.FileUtil;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkJson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.innisfree.playgreen.ImageFilter.FilterAsyncTask;
import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.CameraActivity;
import kr.innisfree.playgreen.data.PlayGreenImageFilter;

public class CameraFilterFrag extends ParentFrag implements View.OnClickListener {

    private final static String TAG = "CameraFilterFrag";

    private int typeFrom;
    private String absolutePath;
    private String tempAbsolutePath;

    private Toolbar mToolbar;
    private TextView txtTitle;
    private ImageView imgContent;

    private ArrayList<LinearLayout> arrayListLlFilterWrapper;
    private LinearLayout llFilter;

    private List<PlayGreenImageFilter> filters;
    private static ArrayList<Bitmap> filterBitmaps = new ArrayList<>();
    private int indexOfCurrentBitmap = 0;
    private Bitmap orinalBitmap;
    private Bitmap convertBitmap;
    private int filterCount = 0;

    private Target target;
    private ProgressDialog progressDialog;

    public CameraFilterFrag() {
    }

    @SuppressLint("ValidFragment")
    public CameraFilterFrag(int typeFrom, String absolutePath) {
        this.typeFrom = typeFrom;
        this.absolutePath = absolutePath;
        this.orinalBitmap = BitmapFactory.decodeFile(absolutePath);
        this.filterBitmaps = new ArrayList<>();
    }

    public static CameraFilterFrag newInstance(int typeFrom, String absolutePath) {
        CameraFilterFrag frag = new CameraFilterFrag(typeFrom, absolutePath);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);

        if (orinalBitmap == null)
            getActivity().finish();

        /** resize and rotate **/
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        if (orinalBitmap.getWidth() > width)
            orinalBitmap = FileUtil.resizeBitmapWithWidth(orinalBitmap, width);
        orinalBitmap = ExifUtil.rotateBitmap(absolutePath, orinalBitmap);
        tempAbsolutePath = CameraActivity.saveImageToSdcard(
                CameraFrag.TYPE_LIBRARY,
                CameraActivity.getOutputMediaFile(CameraFrag.TYPE_LIBRARY),
                orinalBitmap,
                getActivity());

        JYLog.D(TAG + "::orinalBitmap::getWidth::" + orinalBitmap.getWidth(), new Throwable());
        JYLog.D(TAG + "::orinalBitmap::getHeight::" + orinalBitmap.getHeight(), new Throwable());

        if (orinalBitmap != null) {

            FilterAsyncTask filterAsyncTask = new FilterAsyncTask(getActivity(), absolutePath, orinalBitmap, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    Bundle bundle = msg.getData();
                    Bitmap bitmap = bundle.getParcelable("bitmap");

//                    if (filterBitmaps.size() > 0) {
//                        filterBitmaps.add(0, bitmap);
//                    } else {
//                    }
                    filterBitmaps.add(bitmap);
                    filterCount++;
                    JYLog.D(TAG + "::filterCount::" + filterCount, new Throwable());
                    if (filterCount == 9) {
                        JYLog.D(TAG + "::filterCount::" + filterCount, new Throwable());
                        setupListView();
                    }

                }
            });
            filterAsyncTask.execute();

//            progressDialog.show();
//
//            final InniBitmapThread inniBitmapThread = new InniBitmapThread(getActivity(), absolutePath, width, new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//
//                    Bundle bundle = msg.getData();
//                    Bitmap bitmap = bundle.getParcelable("bitmap");
//
//                    if (filterBitmaps.size() > 0) {
//                        filterBitmaps.add(0, bitmap);
//                    } else {
//                        filterBitmaps.add(bitmap);
//                    }
//                    filterCount++;
//                    JYLog.D(TAG + "::inniBitmapThread1::filterCount::" + filterCount, new Throwable());
//                    if (filterCount == 4) {
//                        JYLog.D(TAG + "::inniBitmapThread::filterCount::" + filterCount, new Throwable());
//                        setupListView();
//                    }
//
//                }
//            });
////            new InniBitmapThread(getActivity(), orinalBitmap,
////                    new Handler() {
////                        @Override
////                        public void handleMessage(Message msg) {
////                            super.handleMessage(msg);
////
////                            Bundle bundle = msg.getData();
////                            Bitmap bitmap = bundle.getParcelable("bitmap");
////
////                            if (filterBitmaps.size() > 0) {
////                                filterBitmaps.add(0, bitmap);
////                            } else {
////                                filterBitmaps.add(bitmap);
////                            }
////                            filterCount++;
////                            if (filterCount == 6) {
////                                JYLog.D(TAG + "::inniBitmapThread::filterCount::" + filterCount, new Throwable());
////                                setupListView();
////                            }
////                        }
////                    });
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    inniBitmapThread.start();
//                }
//            }, 0);
//
//            final InniBitmapThread2 inniBitmapThread2 = new InniBitmapThread2(getActivity(), absolutePath, width, new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//
//                    Bundle bundle = msg.getData();
//                    Bitmap bitmap = bundle.getParcelable("bitmap");
//
//                    if (filterBitmaps.size() > 1) {
//                        filterBitmaps.add(1, bitmap);
//                    } else {
//                        filterBitmaps.add(bitmap);
//                    }
//                    filterCount++;
//                    JYLog.D(TAG + "::inniBitmapThread2::filterCount::" + filterCount, new Throwable());
//                    if (filterCount == 4) {
//                        JYLog.D(TAG + "::inniBitmapThread::filterCount::" + filterCount, new Throwable());
//                        setupListView();
//                    }
//
//                }
//            });
//
////            new InniBitmapThread2(getActivity(), ExifUtil.rotateBitmap(absolutePath, orinalBitmap),
////                    new Handler() {
////                        @Override
////                        public void handleMessage(Message msg) {
////                            super.handleMessage(msg);
////
////                            Bundle bundle = msg.getData();
////                            Bitmap bitmap = bundle.getParcelable("bitmap");
////
////                            if (filterBitmaps.size() > 1) {
////                                filterBitmaps.add(1, bitmap);
////                            } else {
////                                filterBitmaps.add(bitmap);
////                            }
////                            filterCount++;
////                            if (filterCount == 6) {
////                                JYLog.D(TAG + "::inniBitmapThread::filterCount::" + filterCount, new Throwable());
////                                setupListView();
////                            }
////
////                        }
////                    });
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    inniBitmapThread2.start();
//                }
//            }, 0);
//
//            final InniBitmapThread3 inniBitmapThread3 = new InniBitmapThread3(getActivity(), absolutePath, width, new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//
//                    Bundle bundle = msg.getData();
//                    Bitmap bitmap = bundle.getParcelable("bitmap");
//
//                    if (filterBitmaps.size() > 2) {
//                        filterBitmaps.add(2, bitmap);
//                    } else {
//                        filterBitmaps.add(bitmap);
//                    }
//                    filterCount++;
//                    JYLog.D(TAG + "::inniBitmapThread3::filterCount::" + filterCount, new Throwable());
//                    if (filterCount == 4) {
//                        JYLog.D(TAG + "::inniBitmapThread::filterCount::" + filterCount, new Throwable());
//                        setupListView();
//                    }
//
//                }
//            });
////                    new InniBitmapThread3(getActivity(), ExifUtil.rotateBitmap(absolutePath, orinalBitmap),
////                    new Handler() {
////                        @Override
////                        public void handleMessage(Message msg) {
////                            super.handleMessage(msg);
////
////                            Bundle bundle = msg.getData();
////                            Bitmap bitmap = bundle.getParcelable("bitmap");
////
////                            if (filterBitmaps.size() > 2) {
////                                filterBitmaps.add(2, bitmap);
////                            } else {
////                                filterBitmaps.add(bitmap);
////                            }
////                            filterCount++;
////                            if (filterCount == 6) {
////                                JYLog.D(TAG + "::inniBitmapThread::filterCount::" + filterCount, new Throwable());
////                                setupListView();
////                            }
////
////                        }
////                    });
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    inniBitmapThread3.start();
//                }
//            }, 0);
//
//            final PlayBitmapThread playBitmapThread = new PlayBitmapThread(getActivity(), absolutePath, width, new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//
//                    Bundle bundle = msg.getData();
//                    Bitmap bitmap = bundle.getParcelable("bitmap");
//
//                    if (filterBitmaps.size() > 3) {
//                        filterBitmaps.add(3, bitmap);
//                    } else {
//                        filterBitmaps.add(bitmap);
//                    }
//                    filterCount++;
//                    JYLog.D(TAG + "::playBitmapThread1::filterCount::" + filterCount, new Throwable());
//                    if (filterCount == 4) {
//                        JYLog.D(TAG + "::playBitmapThread::filterCount::" + filterCount, new Throwable());
//                        setupListView();
//                    }
//
//                }
//            });
//
////                    new PlayBitmapThread(getActivity(), ExifUtil.rotateBitmap(absolutePath, orinalBitmap),
////                    new Handler() {
////                        @Override
////                        public void handleMessage(Message msg) {
////                            super.handleMessage(msg);
////
////                            Bundle bundle = msg.getData();
////                            Bitmap bitmap = bundle.getParcelable("bitmap");
////
////                            if (filterBitmaps.size() > 3) {
////                                filterBitmaps.add(3, bitmap);
////                            } else {
////                                filterBitmaps.add(bitmap);
////                            }
////                            filterCount++;
////                            JYLog.D(TAG + "::playBitmapThread1::filterCount::" + filterCount, new Throwable());
////                            if (filterCount == 6) {
////                                JYLog.D(TAG + "::playBitmapThread::filterCount::" + filterCount, new Throwable());
////                                setupListView();
////                            }
////
////                        }
////                    });
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    playBitmapThread.start();
//                }
//            }, 300);
////
////            final PlayBitmapThread2 playBitmapThread2 = new PlayBitmapThread2(getActivity(), ExifUtil.rotateBitmap(absolutePath, orinalBitmap),
////                    new Handler() {
////                        @Override
////                        public void handleMessage(Message msg) {
////                            super.handleMessage(msg);
////
////                            Bundle bundle = msg.getData();
////                            Bitmap bitmap = bundle.getParcelable("bitmap");
////
////                            if (filterBitmaps.size() > 4) {
////                                filterBitmaps.add(4, bitmap);
////                            } else {
////                                filterBitmaps.add(bitmap);
////                            }
////                            filterCount++;
////                            JYLog.D(TAG + "::playBitmapThread2::filterCount::" + filterCount, new Throwable());
////                            if (filterCount == 6) {
////                                JYLog.D(TAG + "::playBitmapThread::filterCount::" + filterCount, new Throwable());
////                                setupListView();
////                            }
////
////                        }
////                    });
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    playBitmapThread2.start();
////                }
////            }, 400);
////
////            final PlayBitmapThread3 playBitmapThread3 = new PlayBitmapThread3(getActivity(), ExifUtil.rotateBitmap(absolutePath, orinalBitmap),
////                    new Handler() {
////                        @Override
////                        public void handleMessage(Message msg) {
////                            super.handleMessage(msg);
////
////                            Bundle bundle = msg.getData();
////                            Bitmap bitmap = bundle.getParcelable("bitmap");
////
////                            if (filterBitmaps.size() > 5) {
////                                filterBitmaps.add(5, bitmap);
////                            } else {
////                                filterBitmaps.add(bitmap);
////                            }
////                            filterCount++;
////                            JYLog.D(TAG + "::playBitmapThread3::filterCount::" + filterCount, new Throwable());
////                            if (filterCount == 6) {
//////                                JYLog.D(TAG + "::playBitmapThread::filterCount::" + filterCount, new Throwable());
////                                setupListView();
////                            }
////
////                        }
////                    });
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    playBitmapThread3.start();
////                }
////            }, 500);
//
////            final EarthBitmapThread earthBitmapThread = new EarthBitmapThread(getActivity(), orinalBitmap,
////                    new Handler() {
////                        @Override
////                        public void handleMessage(Message msg) {
////                            super.handleMessage(msg);
////
////                            Bundle bundle = msg.getData();
////                            Bitmap bitmap = bundle.getParcelable("bitmap");
////
////                            if (filterBitmaps.size() > 6) {
////                                filterBitmaps.add(6, bitmap);
////                            } else {
////                                filterBitmaps.add(bitmap);
////                            }
////                            filterCount++;
////                            if (filterCount == 9) {
////                                JYLog.D(TAG + "::earthBitmapThread::filterCount::" + filterCount, new Throwable());
////                                setupListView();
////                            }
////
////                        }
////                    });
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    earthBitmapThread.start();
////                }
////            }, 60);
////
////            final EarthBitmapThread2 earthBitmapThread2 = new EarthBitmapThread2(getActivity(), orinalBitmap,
////                    new Handler() {
////                        @Override
////                        public void handleMessage(Message msg) {
////                            super.handleMessage(msg);
////
////                            Bundle bundle = msg.getData();
////                            Bitmap bitmap = bundle.getParcelable("bitmap");
////
////                            if (filterBitmaps.size() > 7) {
////                                filterBitmaps.add(7, bitmap);
////                            } else {
////                                filterBitmaps.add(bitmap);
////                            }
////                            filterCount++;
////                            if (filterCount == 9) {
////                                JYLog.D(TAG + "::earthBitmapThread::filterCount::" + filterCount, new Throwable());
////                                setupListView();
////                            }
////
////                        }
////                    });
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    earthBitmapThread2.start();
////                }
////            }, 70);
////
////            final EarthBitmapThread3 earthBitmapThread3 = new EarthBitmapThread3(getActivity(), orinalBitmap,
////                    new Handler() {
////                        @Override
////                        public void handleMessage(Message msg) {
////                            super.handleMessage(msg);
////
////                            Bundle bundle = msg.getData();
////                            Bitmap bitmap = bundle.getParcelable("bitmap");
////
////                            if (filterBitmaps.size() > 8) {
////                                filterBitmaps.add(8, bitmap);
////                            } else {
////                                filterBitmaps.add(bitmap);
////                            }
////                            filterCount++;
////                            if (filterCount == 9) {
////                                JYLog.D(TAG + "::earthBitmapThread::filterCount::" + filterCount, new Throwable());
////                                setupListView();
////                            }
////
////                        }
////                    });
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    earthBitmapThread3.start();
////                }
////            }, 80);

        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
//                if (typeFrom == CameraFrag.TYPE_CAMERA) {
//                    bitmap = resizeBitmap(bitmap, bitmap.getWidth() / 5);
//                } else {
//                }
//                bitmap = resizeBitmap(bitmap, bitmap.getWidth() / 5);
//                tempAbsolutePath = saveBitmapToFileCache(bitmap);

            }
        });

//        if (orinalBitmap == null) {
//            orinalBitmap = BitmapFactory.decodeFile(absolutePath);
//        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_camera_filter, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getActivity()).clearDiskCache();
            }
        }).start();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Glide.get(getActivity()).clearMemory();
            }
        });

        /** Init Toolbar*/
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.str_camera_filter_title));

        imgContent = (ImageView) view.findViewById(R.id.camera_filter_img_content);

        view.findViewById(R.id.camera_filter_imgbtn_back).setOnClickListener(this);
        view.findViewById(R.id.camera_filter_tv_next).setOnClickListener(this);

        if (orinalBitmap != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    imgContent.setImageBitmap(orinalBitmap);
                }
            });
        } else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtil.isNull(absolutePath))
                        Glide.with(getActivity())
                                .load(absolutePath)
                                .into(new GlideDrawableImageViewTarget(imgContent) {

                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                        super.onResourceReady(resource, animation);
                                        //never called
                                        JYLog.D(TAG + "::onResourceReady()::imgContent", new Throwable());

                                    }

                                    @Override
                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                        super.onLoadFailed(e, errorDrawable);
                                        //never called
                                        JYLog.D(TAG + "::onLoadFailed()::imgContent" + e.getMessage(), new Throwable());
                                    }
                                });
                }
            });
        }

        llFilter = (LinearLayout) view.findViewById(R.id.camera_filter_ll_filter);


        if (filterBitmaps != null && filterBitmaps.size() == 9) {

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    setupListView();
                }
            });

        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        Glide.get(getActivity()).getBitmapPool().clearMemory();

    }

    @Override
    public void onDestroy() {

        if (filterBitmaps != null && filterBitmaps.size() > 0) {
            for (Bitmap bitmap : filterBitmaps) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_filter_imgbtn_back:
                ((CameraActivity) getActivity()).onBackPressed();
                break;
            case R.id.camera_filter_tv_next:
                String tempAbsolutePath;
                if (indexOfCurrentBitmap == 0) {
                    tempAbsolutePath = this.tempAbsolutePath;
                } else {
                    tempAbsolutePath = CameraActivity.saveImageToSdcard(
                            CameraFrag.TYPE_CAMERA_FILTER,
                            CameraActivity.getOutputMediaFile(CameraFrag.TYPE_CAMERA_FILTER),
                            filterBitmaps.get(indexOfCurrentBitmap - 1),
                            getActivity());
                }
                JYLog.D(TAG + "::indexOfCurrentBitmap::" + indexOfCurrentBitmap, new Throwable());
                switch (CameraActivity.mTypeFromAct) {
                    case CameraActivity.TYPE_FROM_PG21_TODAY_MISSION_ACT:
                        Intent intentForPlaygreen21 = new Intent();
                        intentForPlaygreen21.setData(Uri.fromFile(new File(tempAbsolutePath)));
                        getActivity().setResult(Activity.RESULT_OK, intentForPlaygreen21);
                        getActivity().finish();
                        break;
                    case CameraActivity.TYPE_FROM_PROFILE_EDIT_CAMERA:
                    case CameraActivity.TYPE_FROM_PROFILE_EDIT_GALLERY:
                        Intent intentForProfileEdit = new Intent();
                        intentForProfileEdit.putExtra(Definitions.INTENT_KEY.IMAGE_PATH, tempAbsolutePath);
                        getActivity().setResult(Activity.RESULT_OK, intentForProfileEdit);
                        getActivity().finish();
                        break;
                    default:
                        if (!TextUtil.isNull(tempAbsolutePath)) {
                            ((CameraActivity) getActivity()).gotoCameraUploadFrag(tempAbsolutePath, null, null);
                        }
                        break;
                }
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

    private void setupListView() {

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        final TypedArray array = getResources().obtainTypedArray(R.array.array_filter);

        if (array != null) {
            filters = new ArrayList<>();
            arrayListLlFilterWrapper = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                int id = array.getResourceId(i, 0);
                if (id != 0) {
                    TypedArray a = getResources().obtainTypedArray(id);
                    PlayGreenImageFilter filter = new PlayGreenImageFilter();
                    filter.id = a.getInt(0, -1);
                    filter.name = a.getString(1);
                    if (i == 0)
                        filter.isCheck = true;

                    View itemView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_image_filter, null);

                    final LinearLayout llFilterWrapper = (LinearLayout) itemView.findViewById(R.id.image_filter_ll_wrapper);
                    llFilterWrapper.setTag(i);
                    llFilterWrapper.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrayListLlFilterWrapper == null)
                                return;
                            showImageInImageView((int) llFilterWrapper.getTag(), imgContent);
                            for (LinearLayout ll : arrayListLlFilterWrapper) {
                                if (ll != null)
                                    ll.setSelected(false);
                            }
                            llFilterWrapper.setSelected(true);
                        }
                    });
                    arrayListLlFilterWrapper.add(llFilterWrapper);
                    if (i == 0)
                        llFilterWrapper.setSelected(true);
                    ImageView imgFilter = (ImageView) itemView.findViewById(R.id.image_filter_iv_preview);

                    showThumbImageInImageView(i, imgFilter);

                    TextView txtFilterName = (TextView) itemView.findViewById(R.id.image_filter_tv_name);
                    txtFilterName.setText(filter.name);

                    llFilter.addView(itemView);
                    filters.add(filter);
                    a.recycle();
                }
            }
            array.recycle();
        }
    }

    /**
     * show thumbnail list
     *
     * @param position
     * @param imageView
     */
    private void showThumbImageInImageView(final int position, final ImageView imageView) {

        if (position == 0) {
            imageView.setImageBitmap(orinalBitmap);
        } else {
            imageView.setImageBitmap(filterBitmaps.get(position - 1));
        }

//        new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Glide.with(getActivity())
//                                .load(tempAbsolutePath)
////                                .diskCacheStrategy(DiskCacheStrategy.NONE)
////                                .skipMemoryCache(true)
//                                .bitmapTransform(
//                                        new InniTransformation(getActivity())
//                                )
//                                .thumbnail(0.3f)
//                                .into(new GlideDrawableImageViewTarget(imageView) {
//                                    @Override
//                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                                        super.onResourceReady(resource, animation);
//                                        //never called
//                                        JYLog.D(TAG + "::" + position + "::onResourceReady()", new Throwable());
//                                    }
//
//                                    @Override
//                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                        super.onLoadFailed(e, errorDrawable);
//                                        //never called
//                                        JYLog.D(TAG + "::" + position + "::onLoadFailed()", new Throwable());
//                                    }
//                                });
//
//                    }

    }

    /**
     * show thumbnail list
     *
     * @param position
     * @param imageView
     */
    private void showImageInImageView(final int position, final ImageView imageView) {

        if (position == 0) {
            indexOfCurrentBitmap = 0;
            imageView.setImageBitmap(orinalBitmap);
        } else {
            indexOfCurrentBitmap = position;
            imageView.setImageBitmap(filterBitmaps.get(position - 1));
        }

    }


    /*************************************
     * seperate
     *************************************/

    public Bitmap resizeBitmap(Bitmap b, int newWidthDp) {
        Float density = getResources().getDisplayMetrics().density;
        int newWidth = newWidthDp * Math.round(density);

        int width = b.getWidth();
        int height = b.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float ratio = (float) width / newWidth;
        int newHeight = (int) (height / ratio);
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap =
                Bitmap.createBitmap(b, 0, 0,
                        width, height, matrix, true);
        return (resizedBitmap);
    }

    /**
     * make file
     */

    public String makeImageFile(Bitmap bitmap) {

        if (bitmap == null) {
            JYLog.D(TAG + "bitmap is null", new Throwable());
            return null;
        }

        bitmap = resizeBitmap(bitmap, bitmap.getWidth() / 8);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PlayGreen");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                JYLog.D(TAG + "failed to create directory", new Throwable());
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediafile = new File(mediaStorageDir.getPath() + File.separator + "PG_" + timeStamp + ".png");
        OutputStream out = null;

        try {
            mediafile.createNewFile();
            out = new FileOutputStream(mediafile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mediafile.getAbsolutePath();

    }


}
