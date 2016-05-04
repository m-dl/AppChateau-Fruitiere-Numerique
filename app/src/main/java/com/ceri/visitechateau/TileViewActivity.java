package com.ceri.visitechateau;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerEventListener;

public class TileViewActivity extends Fragment {

	private TileView tileView;
	
	@Override
	public TileView onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return initMap();
	}

	private TileView initMap() {
		tileView = new TileView(MainActivity.getContext());

		// size of original image at 100% scale
		tileView.setSize(2736, 2880);

		// detail levels
		tileView.addDetailLevel(1.000f, "tiles/plans/1000/%col%_%row%.jpg", "samples/plans.JPG");
		tileView.addDetailLevel(0.500f, "tiles/plans/500/%col%_%row%.jpg", "samples/plans.JPG");
		tileView.addDetailLevel(0.250f, "tiles/plans/250/%col%_%row%.jpg", "samples/plans.JPG");
		tileView.addDetailLevel(0.125f, "tiles/plans/125/%col%_%row%.jpg", "samples/plans.JPG");

		// let's use 0-1 positioning...
		tileView.defineRelativeBounds(0, 0, 1, 1);

		// center markers along both axes
		tileView.setMarkerAnchorPoints(-0.5f, -0.5f);

		tileView.addMarkerEventListener(markerEventListener);

		// add some pins...
		addPin(0.25, 0.25);
		addPin(0.25, 0.75);
		addPin(0.75, 0.25);
		addPin(0.75, 0.75);
		addPin(0.50, 0.50);

		// scale it down to manageable size
		tileView.setScale(0.5);

		// center the frame
		frameTo(0.5, 0.5);

		return tileView;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		tileView.clear();
	}

	@Override
	public void onResume() {
		super.onResume();
		tileView.resume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tileView.destroy();
		tileView = null;
	}
	
	public TileView getTileView(){
		return tileView;
	}
	
	/**
	 * This is a convenience method to moveToAndCenter after layout (which won't happen if called directly in onCreate
	 * see https://github.com/moagrius/TileView/wiki/FAQ
	 */
	public void frameTo(final double x, final double y) {
		if(getTileView() != null) {
			getTileView().post(new Runnable() {
				@Override
				public void run() {
					getTileView().moveToAndCenter(x, y);
				}
			});
		}
	}

	private void addPin(double x, double y) {
		ImageView imageView = new ImageView(MainActivity.getContext());
		imageView.setTag(new double[]{x, y});
		imageView.setImageResource(R.drawable.push_pin);
		getTileView().addMarker(imageView, x, y);
	}

	private MarkerEventListener markerEventListener = new MarkerEventListener() {
		@Override
		public void onMarkerTap(View v, int x, int y) {
			double[] position = (double[]) v.getTag();
			double _x = position[0];
			double _y = position[1];
			Toast.makeText(MainActivity.getContext(), "You tapped a pin: " + _x + _y, Toast.LENGTH_LONG).show();
		}
	};
}
