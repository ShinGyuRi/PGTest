package com.moyusoft.util;


import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class BitmapHeightResize implements Transformation{

	private	int height;

	public BitmapHeightResize(int height){
		this.height = height;
	}
	
	@Override
	public String key() {
		return "square()";
	}

	@Override
	public Bitmap transform(Bitmap arg0) {
		Bitmap loadedImage = FileUtil.resizeBitmapWithHeight(arg0, height);
		if (arg0 != loadedImage) {
			arg0.recycle();
		}
		return loadedImage;
	}

}
