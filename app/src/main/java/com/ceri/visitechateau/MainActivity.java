package com.ceri.visitechateau;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceri.visitechateau.tool.FileManager;
import com.ceri.visitechateau.tool.ScreenParam;
import com.ceri.visitechateau.tool.TakePicture;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.ceri.visitechateau.tool.ConnectionManager.isNetworkAvailable;

public class MainActivity extends TileViewActivity {

	private int updateActivityNb = 0;
	private Context m_Context;
	private Activity m_Activity;
	private static MainActivity instance;

	@Bind(R.id.navigationView)
	NavigationView m_NavigationView;

	@Bind(R.id.drawer_layout)
	DrawerLayout m_DrawerLayout;

	@Bind(R.id.toolbar)
	Toolbar m_Toolbar;

	ActionBarDrawerToggle m_DrawerToggle;
	ScreenParam param;

	// Constructor
	public MainActivity() {
		instance = this;
	}

	// Getter
	public static Context getContext() {
		if (instance == null) {
			instance = new MainActivity();
		}
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// auto pin the app
		startLockTask();
		// empty visitors pictures folder
		TakePicture.updateVisitorPhoto();
		// if wifi -> try to updata media
		if(isNetworkAvailable()) {
			FileManager.UpdateMedia();
		}
		// create visits dynamically and the menu
		//FileManager.ListVisits(m_NavigationView, AppParams.getInstance().getM_french());
		initObjects();
		initMap();
		selectLanguage();

		//NavigationView dl = (NavigationView) findViewById(R.id.navigationView);
		//tileView.addView(dl, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
	}

	private void initObjects() {
		ButterKnife.bind(this);
		m_Context = getContext();
		m_Activity = MainActivity.this;
		param = new ScreenParam();
		param.paramWindowFullScreen(getWindow());
		param.paramSetSupportActionBar(m_Toolbar, this);
		m_DrawerToggle = new ActionBarDrawerToggle(this, m_DrawerLayout, 0, 0);
		setDrawer();
	}

	private void initMap() {
		// multiple references
		TileView tileView = getTileView();

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

		// add a marker listener
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
	}

	private void addPin(double x, double y) {
		ImageView imageView = new ImageView(this);
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
			Toast.makeText(m_Context, "You tapped a pin: " + _x + _y, Toast.LENGTH_LONG).show();
		}
	};

	private void selectLanguage() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.set_english_app_title);
		alertDialogBuilder
				.setMessage(R.string.set_english_app)
				.setCancelable(false)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						AppParams.getInstance().setM_french(false);
						m_NavigationView.getMenu().clear();
						FileManager.ListVisits(m_NavigationView, AppParams.getInstance().getM_french());
						dialog.cancel();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						AppParams.getInstance().setM_french(true);
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void prepareVisit(String title) {
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setTitle(title);
	}

	public void setDrawer() {
		m_DrawerLayout.setDrawerListener(m_DrawerToggle);
		m_DrawerLayout.isDrawerOpen(m_NavigationView);
		m_NavigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					MenuItem m_menuItem;

					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						m_DrawerLayout.closeDrawers();
						m_menuItem = menuItem;
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								m_menuItem.setChecked(true);
								navigationDrawerItemSelected(m_menuItem.getItemId(), m_menuItem.getTitle().toString());
							}
						}, 250);
						return false;
					}
				});
	}

	public void navigationDrawerItemSelected(int position, String title) {
		if (position > 0) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				//CurrentState.getInstance().removeFragment();
				//prepareVisit(title);
				//CurrentState.getInstance().setM_fragment(true);
			} else
				Toast.makeText(m_Context, "Je ne peux accèder à la mémoire", Toast.LENGTH_LONG).show();
		} else {
			Resources resources = getResources();
			if (title.equals(resources.getString(R.string.action_section_1)) || title.equals(resources.getString(R.string.action_section_english_1))) {
				TakePicture takePicture = new TakePicture(m_Activity);
				takePicture.photo();
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();/*
		// reload menu if update medias - it's activity nb < 3
		if(updateActivityNb < 3) {
			m_NavigationView.getMenu().clear();
			FileManager.ListVisits(m_NavigationView, AppParams.getInstance().getM_french());
			updateActivityNb++;
		}*/
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
