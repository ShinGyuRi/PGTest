package kr.innisfree.playgreen.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.golshadi.majid.core.DownloadManagerPro;
import com.golshadi.majid.report.listener.DownloadManagerListener;
import com.jy.sns.kakaolink.StoryLink;
import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.moyusoft.util.Definitions.DIALOG_SELECT;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.Util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.listener.DialogListener;

/**
 * Created by jooyoung on 2016-04-26.
 */
public class ShareToSNSManager {

	public final String FILE_TYPE_IMAGE = "image/*";
	public final String FILE_TYPE_VIDEO = "video/*";

	public Activity mActivity;
	public String shareImageUrl, contentType;
	//public String shareVideoUrl;
	private ProgressDialog mProgressDialog;
	private int snsType;
	private Uri shareUri;
	private File shareFolder, shareFile;
	private String shareFilePath;

	private DialogListener dialogListener;

	public ShareToSNSManager(Activity activity, String imageUrl) {
		this.mActivity = activity;
		this.shareImageUrl = imageUrl;
		//this.shareVideoUrl = videoUrl;
		init();
	}

	public ShareToSNSManager(Activity activity, File file) {
		this.mActivity = activity;
		this.shareFile = file;
		contentType = FILE_TYPE_IMAGE;
		shareFilePath = shareFile.getAbsolutePath();
	}

	public void setDialogListener(DialogListener dialogListener) {
		this.dialogListener = dialogListener;
	}

	public void init() {
		//TODO 널체크 자세히~
//        if (TextUtil.isNull(shareVideoUrl) == false) {
////			contentType = FILE_TYPE_VIDEO;
////			shareUri = Uri.parse(shareVideoUrl);
//            contentType = FILE_TYPE_IMAGE;
//            shareUri = Uri.parse(shareVideoUrl);
//        } else {
//            contentType = FILE_TYPE_IMAGE;
//            shareUri = Uri.parse(shareImageUrl);
//        }
		contentType = FILE_TYPE_IMAGE;
		shareUri = Uri.parse(shareImageUrl);

		shareFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PlayGreen");        // .PlayGreen으로 하면 숨김폴더가 된다
//		shareFolder = new File(Environment.getExternalStorageDirectory(), "PlayGreen");        // .PlayGreen으로 하면 숨김폴더가 된다
		if (shareFolder.exists() == false) shareFolder.mkdir();

		shareFile = new File(shareFolder.getAbsoluteFile(), new File(shareUri.toString()).getName());
		shareFilePath = shareFile.getAbsolutePath();

	}


	public void shareToInstagram() {
		snsType = DIALOG_SELECT.SHARE_INSTAGRAM;
		if (verifyInstagram()) {
			if (shareFile != null && shareFile.exists() && shareFile.length() != 0) {
				JYLog.D(shareFile.getAbsolutePath(), new Throwable());
				shareSNS();
			} else {
				DownloadManagerPro dm = new DownloadManagerPro(mActivity);
				dm.init(shareFilePath, 16, downloadManagerListener);

				JYLog.D("file path:" + shareFilePath + ", file:" + shareFile + ", uri:" + shareUri.toString(), new Throwable());

				int taskToken = dm.addTask(FilenameUtils.removeExtension(shareFile.getName()), String.valueOf(shareUri), 16, shareFolder.getAbsolutePath(), true, true);
				try {
					dm.startDownload(taskToken);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {  
			installInstagram();
		}
	}

	public void shareToKakaoStory() {
		snsType = DIALOG_SELECT.SHARE_KAKAOSTORY;
		if (verifyKakaoStory()) {
			if (shareFile.exists() && shareFile.length() != 0) {
				shareSNS();
			} else {
				DownloadManagerPro dm = new DownloadManagerPro(mActivity);
				dm.init(shareFilePath, 16, downloadManagerListener);

				JYLog.D("file path:" + shareFilePath + ", file:" + shareFile + ", uri:" + shareUri.toString(), new Throwable());

				int taskToken = dm.addTask(FilenameUtils.removeExtension(shareFile.getName()), String.valueOf(shareUri), 16, shareFolder.getAbsolutePath(), true, true);
				try {
					dm.startDownload(taskToken);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			installKakaoStory();
		}
	}

	private DownloadManagerListener downloadManagerListener = new DownloadManagerListener() {
		@Override
		public void OnDownloadStarted(long taskId) {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mProgressDialog = new ProgressDialog(mActivity);
					mProgressDialog.setMessage(mActivity.getString(R.string.str_download_share_file));
					mProgressDialog.setIndeterminate(true);
					mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					mProgressDialog.setCancelable(false);
					mProgressDialog.show();
				}
			});
		}

		@Override
		public void OnDownloadPaused(long taskId) {
		}

		@Override
		public void onDownloadProcess(long taskId, final double percent, long downloadedLength) {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(mProgressDialog!=null){
						mProgressDialog.setIndeterminate(false);
						mProgressDialog.setMax(100);
						mProgressDialog.setProgress((int) percent);
					}
				}
			});
		}

		@Override
		public void OnDownloadFinished(long taskId) {
			JYLog.D(new Throwable());
			mProgressDialog.dismiss();
		}

		@Override
		public void OnDownloadRebuildStart(long taskId) {
			JYLog.D(new Throwable());
		}

		@Override
		public void OnDownloadRebuildFinished(long taskId) {
			JYLog.D(new Throwable());
		}

		@Override
		public void OnDownloadCompleted(long taskId) {
			JYLog.D(new Throwable());
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.parse("file://" + shareFilePath);
			intent.setData(uri);
			mActivity.sendBroadcast(intent);
			shareSNS();
		}

		@Override
		public void connectionLost(long taskId) {
			JYLog.D(new Throwable());
		}
	};

	public void shareSNS() {
		switch (snsType) {
			case DIALOG_SELECT.SHARE_INSTAGRAM:
				createInstagramIntent(contentType, shareFilePath);
				break;
			case DIALOG_SELECT.SHARE_KAKAOSTORY:
				createKakaoStoryIntent(contentType, shareFilePath);
				break;
		}
	}


	/**
	 * INSTAGRAM
	 */
	public boolean verifyInstagram() {
		PackageManager pm = mActivity.getPackageManager();
		boolean installed = false;
		try {
			pm.getApplicationInfo("com.instagram.android", 0);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	public void installInstagram() {
		if (mActivity == null) return;
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder
				.setMessage(R.string.str_install_instagram_message)
				.setPositiveButton(R.string.str_install, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//패키지 최신 버젼으로 업데이트하기
						Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
						intentUpdate.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
						Util.moveActivity(mActivity, intentUpdate, -1, -1, false, false);
					}
				})
				.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (dialogListener != null) {
							DialogData dialogData = new DialogData();
							dialogData.isCancle = true;
							dialogListener.onSendDlgData(dialogData);
						}
					}
				})
				.show();
	}

	public void createInstagramIntent(String type, String mediaPath) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType(type);
//		File media = new File(mediaPath);
//		Uri uri = Uri.fromFile(shareFile);
		share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
//		share.setDataAndType(Uri.fromFile(shareFile), type);
		share.setPackage("com.instagram.android");
		mActivity.startActivity(share);
	}

	/**
	 * KAKAO STORY
	 */
	public boolean verifyKakaoStory() {
		boolean installed = false;
		try {
			ApplicationInfo info = mActivity.getPackageManager().getApplicationInfo("com.kakao.story", 0);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	public void installKakaoStory() {
		if (mActivity == null) return;
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder
				.setMessage(R.string.str_install_kakaostory_message)
				.setPositiveButton(R.string.str_install, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//패키지 최신 버젼으로 업데이트하기
						Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
						intentUpdate.setData(Uri.parse("market://details?id=" + "com.kakao.story"));
						Util.moveActivity(mActivity, intentUpdate, -1, -1, false, false);
					}
				})
				.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (dialogListener != null) {
							DialogData dialogData = new DialogData();
							dialogData.isCancle = true;
							dialogListener.onSendDlgData(dialogData);
						}
					}
				})
				.show();
	}

	public void createKakaoStoryIntent(String type, String mediaPath) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType(type);
		File media = new File(mediaPath);
		Uri uri = Uri.fromFile(media);
		share.putExtra(Intent.EXTRA_STREAM, uri);
		share.setPackage("com.kakao.story");
		mActivity.startActivity(share);
	}

	public void sendPostingLink() {
		Map<String, Object> urlInfoAndroid = new Hashtable<String, Object>(1);
		urlInfoAndroid.put("title", "(광해) 실제 역사적 진실은?");
		urlInfoAndroid.put("desc", "(광해 왕이 된 남자)의 역사성 부족을 논하다.");
		urlInfoAndroid.put("imageurl", new String[]{"http://m1.daumcdn.net/photo-media/201209/27/ohmynews/R_430x0_20120927141307222.jpg"});
		urlInfoAndroid.put("type", "article");

		// Recommended: Use application context for parameter.
		StoryLink storyLink = StoryLink.getLink(mActivity);

		// check, intent is available.
		if (!storyLink.isAvailableIntent()) {
			//   KakaoToast.makeToast(this, "Not installed KakaoStory.", Toast.LENGTH_SHORT);
			return;
		}

		storyLink.openKakaoLink(mActivity,
				"http://m.media.daum.net/entertain/enews/view?newsid=20120927110708426",
				mActivity.getPackageName(),
				"1.0",
				"미디어디음",
				"UTF-8",
				urlInfoAndroid);
	}

	private KakaoLink mKakaoLink;
	private KakaoTalkLinkMessageBuilder mKakaoTalkLinkMessageBuilder;

	private void go_kakaotalkLink() {
		try {
			mKakaoLink = KakaoLink.getKakaoLink(mActivity);
			mKakaoTalkLinkMessageBuilder = mKakaoLink.createKakaoTalkLinkMessageBuilder();
			mKakaoTalkLinkMessageBuilder.addText("TEST 메시지");
			mKakaoTalkLinkMessageBuilder.addImage("http://test.com/test.jpg", 128, 128);
			mKakaoTalkLinkMessageBuilder.addWebLink("홈 페이지 이동", "http://innisfree.co.kr");
			mKakaoTalkLinkMessageBuilder.addAppButton("테스트 앱 열기", new AppActionBuilder()
					.setAndroidExecuteURLParam("target=main")
					.setIOSExecuteURLParam("target=main", AppActionBuilder.DEVICE_TYPE.PHONE).build());
			mKakaoTalkLinkMessageBuilder.build();
			mKakaoLink.sendMessage(mKakaoTalkLinkMessageBuilder, mActivity);

		} catch (KakaoParameterException e) {
			e.printStackTrace();
		}

	}
}
