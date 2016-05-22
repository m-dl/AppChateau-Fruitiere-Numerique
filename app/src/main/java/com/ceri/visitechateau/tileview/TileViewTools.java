package com.ceri.visitechateau.tileview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;

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
     * https://github.com/moagrius/TileView/wiki/FAQ
     */

    // center the screen map in the middle
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

    // add an IP marker on the map
    public static void addPin(TileView tv, Context c, InterestPoint IP) {
        ImageView imageView = new ImageView(c);
        imageView.setTag(R.id.TAG_IP_ID, IP);
        // change this picture to change the marker picture - very small and neat picture needed
        imageView.setImageResource(R.drawable.marker);
        tv.addMarker(imageView, IP.getCoordX(), IP.getCoordY());
    }

    // clic listener on the marker
    public static MarkerEventListener markerEventListener = new MarkerEventListener() {
        @Override
        public void onMarkerTap(View v, int x, int y) {
            InterestPoint IP = (InterestPoint) v.getTag(R.id.TAG_IP_ID);
            // when clicked, launch IP visit activity
            launchVisitIP(IP);
        }
    };

    // start the IP activity to show text, pictures...
    public static void launchVisitIP(InterestPoint IP) {
        Intent intent = new Intent(MainActivity.m_Activity, InterestPointActivity.class);
        intent.putExtra("InterestPoint", IP);
        ActivityCompat.startActivity(MainActivity.m_Activity, intent, null);
    }
}
