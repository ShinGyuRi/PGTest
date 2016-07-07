package kr.innisfree.playgreen.fragment.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.dto.NetworkJson;

import net.yazeed44.imagepicker.model.AlbumEntry;
import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.ui.ImagesThumbnailAdapter;
import net.yazeed44.imagepicker.ui.SpacesItemDecoration;
import net.yazeed44.imagepicker.util.Events;
import net.yazeed44.imagepicker.util.LoadingAlbumsRequest;
import net.yazeed44.imagepicker.util.OfflineSpiceService;
import net.yazeed44.imagepicker.util.Picker;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.CameraActivity;
import kr.innisfree.playgreen.adapter.AlbumAdapter;
import kr.innisfree.playgreen.transformation.ScaledResizeBitmapTransformation;
import uk.co.senab.photoview.PhotoView;

public class LibraryFrag extends ParentFrag implements View.OnClickListener, AdapterView.OnItemSelectedListener {

	public Picker pickOption;
	protected ArrayList<AlbumEntry> mAlbumList;
	protected SpiceManager mSpiceManager = new SpiceManager(OfflineSpiceService.class);
	public RecyclerView recyclerView;
	public Spinner albumSpinner;
	public PhotoView imgSelectPicture;
	public AlbumAdapter albumAdapter;

	private AlbumEntry selectAlbum;
	private ImageEntry selectImage;

	public LibraryFrag() {
	}

	public static LibraryFrag newInstance() {
		LibraryFrag frag = new LibraryFrag();
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_camera_library, null);

		imgSelectPicture = (PhotoView) view.findViewById(R.id.img_upload_pic);
		imgSelectPicture.setZoomable(false);
		albumSpinner = (Spinner) view.findViewById(R.id.spinner_album);
		albumSpinner.setVisibility(View.GONE);
//		albumAdapter = new AlbumAdapter();
//		albumSpinner.setAdapter(albumAdapter);
//		albumSpinner.setOnItemSelectedListener(this);

		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.num_columns_images));
		gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1)));

		Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		view.findViewById(R.id.btn_close).setOnClickListener(this);
		view.findViewById(R.id.txt_confirm).setOnClickListener(this);
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

		pickOption = new Picker.Builder(getContext(), pickListener, R.style.MIP_theme).build();
		setupAdapter();

		return view;
	}

	@Override
	public void onStart() {
		mSpiceManager.start(getActivity());
		super.onStart();
	}

	@Override
	public void onStop() {
		if (mSpiceManager.isStarted()) {
			mSpiceManager.shouldStop();
		}
		super.onStop();
	}


	public void setupAdapter() {
		//이거 지우면 매번 업데이트함
//        if (mAlbumList == null) {
//            final Events.OnAlbumsLoadedEvent albumLoadedEvent = EventBus.getDefault().getStickyEvent(Events.OnAlbumsLoadedEvent.class);
//            if (albumLoadedEvent != null) mAlbumList = albumLoadedEvent.albumList;
//        }
		if (mAlbumList == null) {
			final LoadingAlbumsRequest loadingRequest = new LoadingAlbumsRequest(getActivity(), pickOption);
			mSpiceManager.execute(loadingRequest, requestListener);
		} else {
			setAllImage();
			//albumAdapter.setItemArray(mAlbumList);
		}
	}

	//
	public void setAllImage() {
		if (mAlbumList == null || mAlbumList.size() == 0) return;
//		for(AlbumEntry albumEntry : mAlbumList){
//			if(!TextUtil.isNull(albumEntry.name) && albumEntry.name.equals(R.string.str_all_photos)){
//				selectAlbum = albumEntry;
//			}
//		}
		selectAlbum = mAlbumList.get(0);
		JYLog.D(selectAlbum.name, new Throwable());
		if (selectAlbum == null || selectAlbum.imageList == null) return;
		for (ImageEntry imageEntry : selectAlbum.imageList) {
			imageEntry.isPicked = false;
		}
		if (selectAlbum.imageList.size() > 0) {
			selectAlbum.imageList.get(0).isPicked = true;
			selectImage = selectAlbum.coverImage;
		}
		if (selectAlbum != null) {
			recyclerView.setAdapter(new ImagesThumbnailAdapter(this, selectAlbum, recyclerView, pickOption, pickImageListener));
			Glide.with(getContext()).load(selectAlbum.coverImage.path).into(imgSelectPicture);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		selectAlbum = (AlbumEntry) albumAdapter.getItem(position);
		if (selectAlbum == null || selectAlbum.imageList == null) return;
		for (ImageEntry imageEntry : selectAlbum.imageList) {
			imageEntry.isPicked = false;
		}
		if (selectAlbum.imageList.size() > 0) {
			selectAlbum.imageList.get(0).isPicked = true;
			selectImage = selectAlbum.coverImage;
		}
		if (selectAlbum != null) {
			recyclerView.setAdapter(new ImagesThumbnailAdapter(this, selectAlbum, recyclerView, pickOption, pickImageListener));
//			imgSelectPicture.setImageBitmap(BitmapFactory.decodeFile(selectAlbum.coverImage.path));
			Glide.with(getContext())
					.load(selectAlbum.coverImage.path)
					.bitmapTransform(
							new ScaledResizeBitmapTransformation(getActivity())
					)
					.into(imgSelectPicture);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	private ImagesThumbnailAdapter.OnPickImageListener pickImageListener = new ImagesThumbnailAdapter.OnPickImageListener() {
		@Override
		public void onPickImage(ImageEntry entry, int position) {
			if (selectAlbum == null || selectAlbum.imageList == null || entry == null) return;
			selectImage = entry;
			//JYLog.D(selectImage.orientation+"", new Throwable());
			for (ImageEntry imageEntry : selectAlbum.imageList) {
				if (imageEntry.imageId == entry.imageId) {
					imageEntry.isPicked = true;
				} else {
					imageEntry.isPicked = false;
				}
			}
			recyclerView.getAdapter().notifyDataSetChanged();
			Glide.with(getContext()).load(entry.path).into(imgSelectPicture);
		}
	};

	private RequestListener<ArrayList> requestListener = new RequestListener<ArrayList>() {
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			JYLog.E(spiceException.getMessage(), new Throwable());
		}

		@Override
		public void onRequestSuccess(final ArrayList albumEntries) {
			if (hasLoadedSuccessfully(albumEntries)) {
				mAlbumList = albumEntries;
				for (AlbumEntry entry : mAlbumList) {
					JYLog.D(entry.name, new Throwable());
				}
				EventBus.getDefault().postSticky(new Events.OnAlbumsLoadedEvent(mAlbumList));
				setupAdapter();
//				mAlbumsRecycler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						if (!mAlbumsRecycler.hasPendingAdapterUpdates()) {
//							pickLatestCapturedImage();
//							mShouldPerformClickOnCapturedAlbum = false;
//						} else {
//							mAlbumsRecycler.postDelayed(this, 100);
//						}
//					}
//				}, 100);
			}

		}
	};

	private boolean hasLoadedSuccessfully(final ArrayList albumList) {
		return albumList != null && albumList.size() > 0;
	}

	private Picker.PickListener pickListener = new Picker.PickListener() {
		@Override
		public void onPickedSuccessfully(ArrayList<ImageEntry> images) {
		}

		@Override
		public void onCancel() {
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_close:
				getActivity().onBackPressed();
				break;
			case R.id.txt_confirm:
				if (selectImage == null || TextUtil.isNull(selectImage.path)) return;
				((CameraActivity) getActivity()).gotoToolFrag(CameraFrag.TYPE_LIBRARY, selectImage.path);
				break;
		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
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

}
