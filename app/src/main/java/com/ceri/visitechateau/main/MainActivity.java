package com.ceri.visitechateau.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ceri.visitechateau.R;
import com.ceri.visitechateau.entities.chateau.Visit;
import com.ceri.visitechateau.files.FileManager;
import com.ceri.visitechateau.info.InfoActivity;
import com.ceri.visitechateau.params.AppParams;
import com.ceri.visitechateau.tileview.TileViewTools;
import com.ceri.visitechateau.tool.ScreenParam;
import com.ceri.visitechateau.tool.TakePicture;
import com.qozix.tileview.TileView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ceri.visitechateau.tool.ConnectionManager.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {

	private int updateActivityNb = 0;
	private Resources resources;
	private Context m_Context;
	private Activity m_Activity;
	private static MainActivity instance;
	private ActionBarDrawerToggle m_DrawerToggle;
	private ScreenParam param;
	private FileManager FM;

	@Bind(R.id.drawer_layout)
	DrawerLayout m_DrawerLayout;

	@Bind(R.id.navigationView)
	NavigationView m_NavigationView;

	@Bind(R.id.toolbar)
	Toolbar m_Toolbar;

	@Bind(R.id.info)
	FloatingActionButton m_FABInfo;

	@Bind(R.id.map_floors_up)
	FloatingActionButton m_FABFloorsUp;

	@Bind(R.id.map_floors_down)
	FloatingActionButton m_FABFloorsDown;

	private TileView tileView;
	private LinearLayout linearLayout;

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
		initObjects();
		selectLanguage();
		// create visits dynamically and the menu
		FileManager.ListVisits(m_NavigationView, AppParams.getInstance().getM_french());
	}

	private void initObjects() {
		setContentView(R.layout.main);
		initMap();
		ButterKnife.bind(this);
		FM = FileManager.getInstance();
		resources = getResources();
		m_Context = getContext();
		m_Activity = MainActivity.this;
		param = new ScreenParam();
		param.paramWindowFullScreen(getWindow());
		param.paramSetSupportActionBar(m_Toolbar, this);
		m_DrawerToggle = new ActionBarDrawerToggle(this, m_DrawerLayout, 0, 0);
		setDrawer();
		presentTheDrawer();
	}

	private void initMap() {
		linearLayout = (LinearLayout) findViewById(R.id.map);
		// multiple references
		tileView = new TileView(this);
		LinearLayout.LayoutParams tileViewLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
		linearLayout.addView(tileView, tileViewLayout);
		// size of original image at 100% scale
		tileView.setSize(4000, 3561);
		// detail levels
		tileView.addDetailLevel(1.000f, "maps/floor_1/%col%_%row%.jpg", "maps/floor_1/planchateau.jpg");
		// let's use 0-1 positioning...
		tileView.defineRelativeBounds(0, 0, 1, 1);
		// center markers along both axes
		tileView.setMarkerAnchorPoints(-0.5f, -0.5f);
		// add marker event when touched
		tileView.addMarkerEventListener(TileViewTools.markerEventListener);
		// scale it down to manageable size
		tileView.setScale(0.5);
		// center the frame
		TileViewTools.frameTo(tileView, 0.5, 0.5);
		// add some pins...
		TileViewTools.addPin(tileView, getContext(), 0.5125,0.39666666666666667);
		TileViewTools.addPin(tileView, getContext(), 0.1175, 0.39666666666666667);
		TileViewTools.addPin(tileView, getContext(), 0.895, 0.13);
	}

	private void initMap2() {
		linearLayout = (LinearLayout) findViewById(R.id.map);
		// multiple references
		tileView = new TileView(this);
		LinearLayout.LayoutParams tileViewLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
		linearLayout.addView(tileView, tileViewLayout);
		// size of original image at 100% scale
		tileView.setSize(4000, 2656);
		// detail levels
		tileView.addDetailLevel(1.000f, "maps/floor_2/%col%_%row%.jpg", "maps/floor_2/planchateau.jpg");
		// let's use 0-1 positioning...
		tileView.defineRelativeBounds(0, 0, 1, 1);
		// center markers along both axes
		tileView.setMarkerAnchorPoints(-0.5f, -0.5f);
		// add marker event when touched
		tileView.addMarkerEventListener(TileViewTools.markerEventListener);
		// scale it down to manageable size
		tileView.setScale(0.5);
		// center the frame
		TileViewTools.frameTo(tileView, 0.5, 0.5);
		// add some pins...
		TileViewTools.addPin(tileView, getContext(), 0.2125,0.39666666666666667);
		TileViewTools.addPin(tileView, getContext(), 0.5175, 0.39666666666666667);
		TileViewTools.addPin(tileView, getContext(), 0.695, 0.13);
	}

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
						renameActionBar(resources.getString(R.string.app_name_en));
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

	private void presentTheDrawer() {
		m_DrawerLayout.openDrawer(GravityCompat.START);
	}

	private void prepareVisit(String title) {
		Visit visit = FM.getChateauWorkspace().searchVisit(title, AppParams.getInstance().getM_french());
		Toast.makeText(m_Context, "Visite choisie:" + visit.getName(), Toast.LENGTH_SHORT).show();
		renameActionBar(title);
	}

	private void renameActionBar(String s) {
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setTitle(s);
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
				prepareVisit(title);
			} else
				Toast.makeText(m_Context, resources.getString(R.string.memory_access_error), Toast.LENGTH_LONG).show();
		} else {
			if (title.equals(resources.getString(R.string.action_section_1)) || title.equals(resources.getString(R.string.action_section_english_1))) {
				TakePicture takePicture = new TakePicture(m_Activity);
				takePicture.photo();
			}
		}
	}

	@OnClick(R.id.info)
	public void onInfoClick() {
		// launch visit info activity
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
				m_Activity,
				Pair.create((View) m_FABInfo, getResources().getString(R.string.ImageTransition))
		);
		launchVisitInfo(options);
	}

	@OnClick(R.id.map_floors_up)
	public void onFloorUpClick() {
		// remove all view or it doesn't work
		linearLayout.removeAllViewsInLayout();
		// rebuild the new map
		initMap2();
	}

	@OnClick(R.id.map_floors_down)
	public void onFloorDownClick() {
		// remove all view or it doesn't work
		linearLayout.removeAllViewsInLayout();
		// rebuild the new map
		initMap2();
	}

	private void launchVisitInfo(ActivityOptionsCompat options) {
		Intent intent = new Intent(m_Activity, InfoActivity.class);
		ActivityCompat.startActivity(m_Activity, intent, options.toBundle());
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
		// reload menu if update medias - it's activity nb < 3
		if(updateActivityNb < 3) {
			m_NavigationView.getMenu().clear();
			FileManager.ListVisits(m_NavigationView, AppParams.getInstance().getM_french());
			FM.Init();
			updateActivityNb++;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tileView.destroy();
		tileView = null;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		m_DrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return m_DrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}
}
