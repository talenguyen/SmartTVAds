package com.anvy.test.smarttvads.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.anvy.test.smarttvads.BuildConfig;
import com.anvy.test.smarttvads.R;
import com.anvy.test.smarttvads.config.Settings;
import com.anvy.test.smarttvads.config.Settings.Orientation;
import com.anvy.test.smarttvads.data.AdsItem;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class DisplayActivity extends Activity {

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AdsItem item = Settings.getActivatedItem();
		if (item == null) {
			finish();
			return;
		}

		if (item.getOrientation() == Orientation.PORTRAIT) {
			setRequestedOrientation(Orientation.PORTRAIT);
		}

		getActionBar().hide();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_display);

		final WebView webView = (WebView) findViewById(R.id.webView);
		configWebView(webView);
		webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

		webView.loadUrl("file://" + item.getHtmlPath());
		webView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				webView.postDelayed(new Runnable() {
					@Override
					public void run() {
						webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
					}
				}, 1000);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (event.getX() >= Settings.sScreenWidth - 100
							&& event.getY() < 100) {
						finish();
						return true;
					}
				}
				return false;
			}
		});

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void configWebView(WebView webView) {
		// Configure the webview
		webView.setWebChromeClient(new WebChromeClient());
		WebSettings s = webView.getSettings();
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		s.setSavePassword(true);
		s.setSaveFormData(true);
		s.setJavaScriptEnabled(true);

		// enable navigator.geolocation
		s.setGeolocationEnabled(true);
		s.setGeolocationDatabasePath(new File(getFilesDir(), getPackageName()
				+ "/databases/").getPath());

		// enable Web Storage: localStorage, sessionStorage
		s.setDomStorageEnabled(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if (keyCode == KeyEvent.KEYCODE_S) {
				finish();
				return true;
			} else {
				if (BuildConfig.DEBUG) {
					Toast.makeText(
							getApplicationContext(),
							"Your pressed on key: "
									+ keyCode
									+ " system was maped wrong key set. Please touch on right-top corner to close!",
							Toast.LENGTH_LONG).show();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
