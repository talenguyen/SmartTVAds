package com.anvy.test.smarttvads.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.anvy.test.smarttvads.R;
import com.anvy.test.smarttvads.config.Settings;
import com.anvy.test.smarttvads.data.AdsItem;
import com.anvy.test.smarttvads.util.Logger;

public class LoadingActivity extends Activity {

	public static final String KEY_ACTIVE_ITEM_POSITION = "key:activeItemPosition";
	public static final String SHARED_PREFS_NAME = "app:sharedprefs";
	
	private static final String KEY_SCREEN_WIDTH = "key:screenWidth";
	private static final String TAG = LoadingActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_loading);

		SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_NAME,
				MODE_PRIVATE);
		Settings.sScreenWidth = prefs.getInt(KEY_SCREEN_WIDTH, 0);
		if (Settings.sScreenWidth == 0) {
			Settings.sScreenWidth = getScreenWidth();
			prefs.edit().putInt(KEY_SCREEN_WIDTH, Settings.sScreenWidth)
					.commit();
		}

		Settings.sActiveItemPosition = prefs.getInt(KEY_ACTIVE_ITEM_POSITION,
				1);
		Logger.log(TAG, "Active Item Position: " + Settings.sActiveItemPosition);
		
		Settings.sScreenHeight = (int) (Settings.sScreenWidth / Settings.SCREEN_RATIO);

		if (Settings.CONTENT_DIR.exists()
				&& Settings.CONTENT_DIR.list().length > 0) {
			Settings.sAdsItems = getAdsItems(Settings.CONTENT_DIR);
			startActivity(new Intent(this, AdsItemListActivity.class));
		} else {
			Toast.makeText(this, R.string.no_content, Toast.LENGTH_LONG).show();
		}
		finish();
	}

	private int getScreenWidth() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		if (displaymetrics.widthPixels > displaymetrics.heightPixels) {
			return displaymetrics.widthPixels;
		} else {
			return (int) (displaymetrics.widthPixels * Settings.SCREEN_RATIO);
		}
	}

	private List<AdsItem> getAdsItems(File adsDir) {
		List<AdsItem> items = new ArrayList<AdsItem>();
		for (File file : adsDir.listFiles()) {
			final String name = file.getName();
			final String imgPreviewPath = file.getPath() + "/"
					+ Settings.IMG_PREVIEW_NAME;
			final String htmlPath = file.getPath() + "/" + Settings.HTML_NAME;
			if (name.toLowerCase().startsWith("land_")) {
				items.add(new AdsItem(Settings.Orientation.LANDSCAPE, name,
						imgPreviewPath, htmlPath));
			} else if (name.toLowerCase().startsWith("port_")) {
				items.add(new AdsItem(Settings.Orientation.PORTRAIT, name,
						imgPreviewPath, htmlPath));
			}
		}
		return items;
	}
}
