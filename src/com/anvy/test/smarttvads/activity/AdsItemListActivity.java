package com.anvy.test.smarttvads.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.anvy.test.smarttvads.R;
import com.anvy.test.smarttvads.config.Settings;
import com.anvy.test.smarttvads.fragment.AdsItemDetailFragment;
import com.anvy.test.smarttvads.fragment.AdsItemListFragment;
import com.anvy.test.smarttvads.image.ImageCache;
import com.anvy.test.smarttvads.image.ImageWorker;
import com.anvy.test.smarttvads.util.Logger;

/**
 * An activity representing a list of AdsItems. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link AdsItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link AdsItemListFragment} and the item details (if present) is a
 * {@link AdsItemDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link AdsItemListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class AdsItemListActivity extends FragmentActivity implements
		AdsItemListFragment.Callbacks {

	private static final String TAG = AdsItemListActivity.class.getSimpleName();
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private ImageWorker mImageWorker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adsitem_twopane);
		
		final Intent i = new Intent(this, DisplayActivity.class);
		startActivity(i);

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();
		cacheParams.setMemCacheSizePercent(this, 0.25f); // Set memory cache to
															// 25% of mem class

		mImageWorker = new ImageWorker(this);
		mImageWorker.addImageCache(getSupportFragmentManager(), cacheParams);
		mImageWorker.setLoadingImage(R.drawable.empty_photo);

		if (findViewById(R.id.adsitem_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((AdsItemListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.adsitem_list))
					.setActivateOnItemClick(true);
		}
	}
	
	/**
	 * Get the {@link ImageWorker} object that is managed by this
	 * {@link Activity}
	 * 
	 * @return {@link ImageWorker} object
	 */
	public ImageWorker getImageWorker() {
		return mImageWorker;
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageWorker.setExitTasksEarly(false);
		onItemSelected(Settings.sActiveItemPosition);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mImageWorker.setExitTasksEarly(true);
		
		Logger.log(TAG, "{onPause} save active position" + Settings.sActiveItemPosition);
		// Save active item position
		getSharedPreferences(LoadingActivity.SHARED_PREFS_NAME, MODE_PRIVATE)
				.edit()
				.putInt(LoadingActivity.KEY_ACTIVE_ITEM_POSITION,
						Settings.sActiveItemPosition).commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageWorker.clearCache();
	}

	/**
	 * Callback method from {@link AdsItemListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int position) {
		Settings.sActiveItemPosition = position;
		if (mTwoPane) {
			AdsItemDetailFragment fragment = new AdsItemDetailFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.adsitem_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, AdsItemDetailActivity.class);
			startActivity(detailIntent);
		}
	}
}
