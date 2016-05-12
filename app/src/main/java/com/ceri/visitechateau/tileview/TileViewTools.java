package com.ceri.visitechateau.tileview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceri.visitechateau.R;
import com.ceri.visitechateau.entities.chateau.InterestPoint;
import com.ceri.visitechateau.interestpoint.InterestPointActivity;
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

    public static void addPin(TileView tv, Context c, InterestPoint IP) {
        ImageView imageView = new ImageView(c);
        imageView.setTag(R.id.TAG_IP_ID, IP);
        imageView.setImageResource(R.drawable.maps_marker_blue);
        tv.addMarker(imageView, IP.getCoordX(), IP.getCoordY());
    }

    public static MarkerEventListener markerEventListener = new MarkerEventListener() {
        @Override
        public void onMarkerTap(View v, int x, int y) {
            InterestPoint IP = (InterestPoint) v.getTag(R.id.TAG_IP_ID);
            Toast.makeText(MainActivity.getContext(), "You tapped a pin: " + IP.getName(), Toast.LENGTH_LONG).show();
            launchVisitIP(IP);
        }
    };

    public static void launchVisitIP(InterestPoint IP) {
        Intent intent = new Intent(MainActivity.m_Activity, InterestPointActivity.class);
        intent.putExtra("InterestPoint", IP);
        ActivityCompat.startActivity(MainActivity.m_Activity, intent, null);
    }
}
