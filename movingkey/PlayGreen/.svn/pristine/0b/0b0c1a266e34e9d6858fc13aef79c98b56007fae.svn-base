package com.moyusoft.util;


import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class BitmapWidthResize implements Transformation{

	private	int width;
	
	public BitmapWidthResize(int width){
		this.width = width;
	}
	
	@Override
	public String key() {
		return "square()";
	}

	@Override
	public Bitmap transform(Bitmap arg0) {
		Bitmap loadedImage = FileUtil.resizeBitmapWithWidth(arg0, width);
		if (arg0 != loadedImage) {
			arg0.recycle();
		}
		return loadedImage;
	}

}
