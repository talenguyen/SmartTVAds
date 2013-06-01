package com.anvy.test.smarttvads.config;

import java.io.File;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.os.Environment;

import com.anvy.test.smarttvads.data.AdsItem;

public class Settings {

	// ======================= Static public =======================
	public static final float SCREEN_RATIO = (float) 16 / 9;
	public static final int DEGREE_OFFSET = 180;

	public static final File CONTENT_DIR = new File(
			Environment.getExternalStorageDirectory() + "/AnvyAds");
	
	public static final String IMG_PREVIEW_NAME = "preview.jpg";
	public static final String HTML_NAME = "index.html";

	// ======================= Static ======================= 
	public static int sScreenWidth = 0;
	public static int sScreenHeight = 0;
	public static int sRotationDegree = 0;
	public static int sActiveItemPosition = 1;
	
	public static List<AdsItem> sAdsItems = null;

	public class Orientation {
		public static final int PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		public static final int LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	}
	
	public static AdsItem getActivatedItem() {
		if (sAdsItems == null || sActiveItemPosition < 0 || sActiveItemPosition >= sAdsItems.size()) {
			return null;
		}
		
		return sAdsItems.get(sActiveItemPosition);
	}

}
