package kr.innisfree.playgreen.activity;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

import com.ParentAct;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;

import kr.innisfree.playgreen.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by jooyoung on 2016-04-25.
 */
public class ExpandImageAct extends ParentAct {

	private String imageURL;
	private PhotoView imgExpand;
	private float imageScale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLoading(this);
		setContentView(R.layout.act_expand_image);

		findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		imageURL = getIntent().getStringExtra(Definitions.INTENT_KEY.DATA);

		if (savedInstanceState != null)
			imageScale = savedInstanceState.getFloat("scale");

		imgExpand = (PhotoView) findViewById(R.id.img_expand);
		imgExpand.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {
			@Override
			public void onMatrixChanged(RectF rect) {
				imageScale = imgExpand.getScale();
			}
		});

		if (savedInstanceState != null)
			imgExpand.setScale(imageScale);

		if (!TextUtil.isNull(imageURL)) {
			if (imageURL.contains("graph.facebook.com")) {
				imageURL = imageURL.replace("http://", "https://");
			}
			//transform(new BitmapWidthResize(ToforUtil.PHONE_W)).
			Picasso.with(this).load(imageURL).into(imgExpand);
			//new BitmapWidthResize(getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_288))).into(imgExpand);
		}
	}


}
