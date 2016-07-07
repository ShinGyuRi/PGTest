package com.moyusoft.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import com.BaseApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class FileUtil {

	public static String getFilePath(int type, String fileName) {
		String filePath = null;
		String dirPath = null;
		switch (type) {
			case ToforUtil.FILE_TYPE_IMG:
				dirPath = ToforUtil.LOCAL_FILE_PATH + ToforUtil.IMAGE_PATH;
				break;
			case ToforUtil.FILE_TYPE_TEXT:
				dirPath = ToforUtil.LOCAL_FILE_PATH + ToforUtil.TXT_PATH;
				break;
		}
		File fFile = new File(dirPath);
		if (!fFile.exists())
			fFile.mkdirs();
		File fRearFile = new File(fFile, fileName);
		filePath = fRearFile.getAbsolutePath();

		return filePath;
	}

	public static void deleteFile(String filePath) {
		File fFile = new File(filePath);
		if (fFile.exists())
			fFile.delete();
	}

	public static boolean isExistFile(String filePath) {
		File fFile = new File(filePath);
		if (fFile != null && fFile.exists())
			return true;
		return false;
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static String getTxtFileString(String filePath) {
		String path = null;
		String resultString = null;
		FileInputStream inputStream = null;
		byte[] data = null;

		File fFile = null;
		fFile = new File(filePath);

		try {
			if (fFile.exists()) {
				path = fFile.getAbsolutePath();
				inputStream = new FileInputStream(path);
				data = new byte[inputStream.available()];
				while (inputStream.read(data) != -1) {
				}
				;
				resultString = new String(data);

			} else {
				resultString = null;
			}

		} catch (Exception err) {
			resultString = null;

		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception err) {
			}

			inputStream = null;
			path = null;
			data = null;

		}

		return resultString;
	}

	/**
	 * @param comment
	 */
	public static boolean saveText(String comment, String filePath) {

		boolean isSuccess = true;
		File fFile = null;

		OutputStream outStream = null;
		if (filePath == null || filePath.equals(""))
			return false;

		fFile = new File(filePath);

		try {
			outStream = new FileOutputStream(fFile);
			outStream.write(comment.getBytes());
			outStream.close();
		} catch (Exception err) {
			Debug.showDebug(err.toString());
			filePath = null;
			fFile = null;
			outStream = null;
			isSuccess = false;
		}
		filePath = null;
		fFile = null;
		outStream = null;

		return isSuccess;

	}

	/**
	 * 이미지 저장하기
	 *
	 * @param filePath 파일 풀 경로
	 * @param image    파일 이미지
	 * @return
	 */
	public static boolean saveImage(String filePath, Bitmap image, Context context) {
		boolean rFlag = true;
		try {
			File file = null;
			file = new File(filePath);

			if (file.exists()) {
				file.delete();
			}

			FileOutputStream fos = new FileOutputStream(file.getPath());
//			if (filePath.contains(ToforUtil.IMG_PNG_FORMAT)) {
//				image.compress(CompressFormat.PNG, 100, fos);
//			} else {
//				image.compress(CompressFormat.JPEG, 100, fos);
//			}
			boolean isSuccess = image.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			fos = null;
			file = null;
		} catch (Exception e) {
			rFlag = false;
		}
		return rFlag;
	}

	public static void savePicture(Bitmap bm, String imgName, Context context) {
		OutputStream fOut = null;
		String strDirectory = Environment.getExternalStorageDirectory().toString();

		File f = new File(strDirectory, imgName);
		try {
			fOut = new FileOutputStream(f);

			/** Compress image **/
			bm.compress(CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();

			/** Update image to gallery **/
			MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), f.getName(), f.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 저장된 이미지 얻기
	 *
	 * @param filePath 파일 풀 경로
	 * @param width    스케일할 널이
	 * @param height   스케일할 높이
	 * @return
	 */
	public static Bitmap getSavedImg(String filePath, int width, int height) {
		Bitmap tempBm = null;
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		options.inSampleSize = 1;
		BitmapFactory.decodeFile(filePath, options);

		int orgImgWidth = options.outWidth;
		int orgImgHeight = options.outHeight;

		if (width == 0) {
			options.inSampleSize = 1;
		} else {
			if (orgImgHeight * orgImgWidth >= width * height) {
				int tempWidthSampleSize = (int) Math.pow(2, (int) Math.round(Math.log(width / (double) orgImgWidth) / Math.log(0.5)));
				int tempHeightSampleSize = (int) Math.pow(2, (int) Math.round(Math.log(height / (double) orgImgHeight) / Math.log(0.5)));

				if (tempWidthSampleSize > tempHeightSampleSize) {
					options.inSampleSize = tempWidthSampleSize;
				} else {
					options.inSampleSize = tempHeightSampleSize;
				}
			} else {
				options.inSampleSize = 1;
			}
		}

		options.inJustDecodeBounds = false;
		tempBm = BitmapFactory.decodeFile(filePath, options);
		try {

			bm = Bitmap.createScaledBitmap(tempBm, tempBm.getWidth(), tempBm.getHeight(), false);
		} catch (Exception err) {
			bm = null;
		}

		options = null;
		tempBm = null;

		return bm;
	}

	/**
	 * 마운트가 됬는지 안됬는지 확인하는 메소드
	 *
	 * @return
	 */
	public static boolean externalMemoryAvailable() {
		boolean isExternalMemory = false;
		try {
			isExternalMemory = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		} catch (Exception err) {

		}
		return isExternalMemory;
	}

	/**
	 * 외장 SD카드 남은 용량
	 *
	 * @return
	 */
	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) { // ����Ʈ�� �Ǿ��ִ��� Ȯ���ϱ�.
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();

			path = null;
			stat = null;
			return availableBlocks * blockSize;
		} else {
			return 1;
		}
	}

	/**
	 * 텍스트 경로 얻기
	 *
	 * @param fileName
	 * @return
	 */
	public static String getTxtFilePath(String fileName) {
		if (!TextUtil.isNull(ToforUtil.LOCAL_TXT_FILE_PATH)) {
			return ToforUtil.LOCAL_TXT_FILE_PATH + File.separatorChar + fileName;
		}
		return null;
	}

	/**
	 * 이미지 경로 얻기
	 *
	 * @param fileName
	 * @return
	 */
	public static String getImgFilePath(String fileName) {
		if (!TextUtil.isNull(ToforUtil.LOCAL_IMG_FILE_PATH)) {
			return ToforUtil.LOCAL_IMG_FILE_PATH + File.separatorChar + fileName;
		}
		return null;
	}

	public static File getTempImageFile(Context context) {
		if(context==null)	context = BaseApplication.getContext();
		File path = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/temp/");
		if (!path.exists()) {
			path.mkdirs();
		}
		File file = new File(path, "tempimage.png");
		return file;
	}

	public static void deleteTempImageFile(Context context) {
		if(context==null)	context = BaseApplication.getContext();
		File path = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/temp/");
		if (!path.exists()) {
			path.mkdirs();
		}
		File file = new File(path, "tempimage.png");
		if(file.exists()) {
			JYLog.D("exist",new Throwable());
			file.delete();
		}
	}


	public static void copyUriToFile(Context context, Uri srcUri, File target) {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try {
			inputStream = (FileInputStream) context.getContentResolver().openInputStream(srcUri);
			outputStream = new FileOutputStream(target);

			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();

			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fcout.close();
			} catch (IOException ioe) {
			}
			try {
				fcin.close();
			} catch (IOException ioe) {
			}
			try {
				outputStream.close();
			} catch (IOException ioe) {
			}
			try {
				inputStream.close();
			} catch (IOException ioe) {
			}
		}
	}

	public static Bitmap loadImageWithSampleSize(File file, int mImageSizeBoundary) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		int width = options.outWidth;
		int height = options.outHeight;
		int longSide = Math.max(width, height);
		int sampleSize = 1;
		if (longSide > mImageSizeBoundary) {
			sampleSize = longSide / mImageSizeBoundary;
		}
		options.inJustDecodeBounds = false;
		options.inSampleSize = sampleSize;
		options.inPurgeable = true;
		options.inDither = false;

		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		return bitmap;
	}

	public static Bitmap rotateImage(Bitmap bitmap, int degrees) {
		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
			try {
				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
				if (bitmap != converted) {
					bitmap.recycle();
					bitmap = converted;
				}
			} catch (OutOfMemoryError ex) {
			}
		}
		return bitmap;
	}

	public static int[] resizeImageWithinBoundary(int w, int h, int mImageSizeBoundary) {
		int[] imgResize = new int[2];
		int width = w;
		int height = h;

		if (width > height) {
			if (width > mImageSizeBoundary) {
				imgResize = resizeBitmapWithWidth(w, h, mImageSizeBoundary);
			}
		} else {
			if (height > mImageSizeBoundary) {
				imgResize = resizeBitmapWithHeight(w, h, mImageSizeBoundary);
			}
		}
		return imgResize;
	}

	public static int[] resizeBitmapWithHeight(int w, int h, int wantedHeight) {

		int[] imgResize = new int[2];
		int width = w;
		int height = h;

		float resizeFactor = wantedHeight * 1f / height;

		int targetWidth, targetHeight;
		targetWidth = (int) (width * resizeFactor);
		targetHeight = (int) (height * resizeFactor);

		imgResize[0] = targetWidth;
		imgResize[1] = targetHeight;

		return imgResize;
	}

	public static int[] resizeBitmapWithWidth(int w, int h, int wantedWidth) {

		int[] imgResize = new int[2];
		int width = w;
		int height = h;

		float resizeFactor = wantedWidth * 1f / width;

		int targetWidth, targetHeight;
		targetWidth = (int) (width * resizeFactor);
		targetHeight = (int) (height * resizeFactor);

		imgResize[0] = targetWidth;
		imgResize[1] = targetHeight;

		return imgResize;
	}

	public static Bitmap resizeImageWithinBoundary(Bitmap bitmap, int mImageSizeBoundary) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		if (width > height) {
			if (width > mImageSizeBoundary) {
				bitmap = resizeBitmapWithWidth(bitmap, mImageSizeBoundary);
			}
		} else {
			if (height > mImageSizeBoundary) {
				bitmap = resizeBitmapWithHeight(bitmap, mImageSizeBoundary);
			}
		}
		return bitmap;
	}

	public static Bitmap resizeBitmapWithHeight(Bitmap source, int wantedHeight) {
		if (source == null)
			return null;

		int width = source.getWidth();
		int height = source.getHeight();

		float resizeFactor = wantedHeight * 1f / height;

		int targetWidth, targetHeight;
		targetWidth = (int) (width * resizeFactor);
		targetHeight = (int) (height * resizeFactor);

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true);

		return resizedBitmap;
	}

	public static Bitmap resizeBitmapWithWidth(Bitmap source, int wantedWidth) {
		if (source == null)
			return null;

		int width = source.getWidth();
		int height = source.getHeight();

		float resizeFactor = wantedWidth * 1f / width;

		int targetWidth, targetHeight;
		targetWidth = (int) (width * resizeFactor);
		targetHeight = (int) (height * resizeFactor);

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true);

		return resizedBitmap;
	}

	public static void saveBitmapToFile(Bitmap bitmap, Context context) {
		File target = FileUtil.getTempImageFile(context);
		try {
			FileOutputStream fos = new FileOutputStream(target, false);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Assets 폴더에 저장되어 있는 파일은 해당 경로에 저장
	 *
	 * @param ctx
	 * @param dstPath 저장 경로
	 */
	public static void AssetsToLocalSpaceSave(Context ctx, String soutceFileName, String dstPath) {
		AssetManager assManager = ctx.getAssets();
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		BufferedInputStream bis = null;

		File file = new File(dstPath);

		try {
			is = assManager.open(soutceFileName);
			bis = new BufferedInputStream(is);

			if (file.exists()) // �̹� ����Ǿ� �ִٸ� �����ϰ� �ٽ� ������ ���Ѵ�.
			{
				file.delete();
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int read = -1;
			byte[] buffer = new byte[1024];
			while ((read = bis.read(buffer, 0, 1024)) != -1) {
				bos.write(buffer, 0, read);
			}
			bos.flush();

		} catch (Exception err) {
		} finally {
			try {
				if (bos != null)
					bos.close();
				if (fos != null)
					fos.close();
				if (bis != null)
					bis.close();
				if (is != null)
					is.close();
			} catch (Exception err) {
			}
			;
			fos = null;
			bos = null;
			is = null;
			bis = null;
			file = null;
			assManager = null;
		}
	}

	public static Bitmap resizeBitmap(Bitmap source, int wantedWidth, int wantedHeight) {
		if (source == null)
			return null;

		int width = source.getWidth();
		int height = source.getHeight();

		float scaleWidth = wantedWidth * 1f / width;
		float scaleHeight = wantedHeight * 1f / height;
		float scale;

		if (scaleWidth > scaleHeight) scale = scaleWidth;
		else scale = scaleHeight;

		int targetWidth, targetHeight;
		targetWidth = (int) (width * scale);
		targetHeight = (int) (height * scale);

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true);

		return resizedBitmap;
	}


}
