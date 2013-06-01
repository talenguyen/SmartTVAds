package com.anvy.test.smarttvads.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anvy.test.smarttvads.R;
import com.anvy.test.smarttvads.activity.AdsItemDetailActivity;
import com.anvy.test.smarttvads.activity.AdsItemListActivity;
import com.anvy.test.smarttvads.activity.DisplayActivity;
import com.anvy.test.smarttvads.config.Settings;
import com.anvy.test.smarttvads.data.AdsItem;

/**
 * A fragment representing a single AdsItem detail screen. This fragment is
 * either contained in a {@link AdsItemListActivity} in two-pane mode (on
 * tablets) or a {@link AdsItemDetailActivity} on handsets.
 */
public class AdsItemDetailFragment extends Fragment {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private AdsItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AdsItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mItem = Settings.getActivatedItem();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_adsitem_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			int imgWidth = 0, imgHeight = 0;
			if (mItem.getOrientation() == Settings.Orientation.LANDSCAPE) {
				imgWidth = Settings.sScreenHeight - 100;
				imgHeight = (int) (imgWidth / Settings.SCREEN_RATIO);
			} else {
				imgHeight = Settings.sScreenHeight - 100;
				imgWidth = (int) (imgHeight / Settings.SCREEN_RATIO);
			}
			ImageView imageView = (ImageView) rootView
					.findViewById(R.id.imgView);
			((AdsItemListActivity) getActivity()).getImageWorker().loadImage(
					mItem.getImgPreviewPath(), imageView, imgWidth, imgHeight);
		}
		
		rootView.findViewById(R.id.apply).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Intent i = new Intent(getActivity(), DisplayActivity.class);
				startActivity(i);
			}
		});

		return rootView;
	}
}
