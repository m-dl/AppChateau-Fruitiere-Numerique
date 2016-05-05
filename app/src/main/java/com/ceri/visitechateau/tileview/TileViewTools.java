package com.ceri.visitechateau.tileview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceri.visitechateau.R;
import com.ceri.visitechateau.main.MainActivity;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerEventListener;

/**
 * Created by Maxime
 */
public class TileViewTools {
    /**
     * This is a convenience method to moveToAndCenter after layout (which won't happen if called directly in onCreate
     * see https://github.com/moagrius/TileView/wiki/FAQ
     */
    public static void frameTo(final TileView tv, final double x, final double y) {
        if(tv != null) {
            tv.post(new Runnable() {
                @Override
                public void run() {
                    tv.moveToAndCenter(x, y);
                }
            });
        }
    }

    public static void addPin(TileView tv, Context c, double x, double y) {
        ImageView imageView = new ImageView(c);
        imageView.setTag(new double[]{x, y});
        imageView.setImageResource(R.drawable.maps_marker_blue);
        tv.addMarker(imageView, x, y);
    }

    public static MarkerEventListener markerEventListener = new MarkerEventListener() {
        @Override
        public void onMarkerTap(View v, int x, int y) {
            double[] position = (double[]) v.getTag();
            double _x = position[0];
            double _y = position[1];
            Toast.makeText(MainActivity.getContext(), "You tapped a pin: " + _x + _y, Toast.LENGTH_LONG).show();
        }
    };
}
