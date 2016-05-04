package com.ceri.visitechateau;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ceri.visitechateau.entities.chateau.Visit;
import com.ceri.visitechateau.files.FileManager;
import com.ceri.visitechateau.tool.ScreenParam;
import com.ceri.visitechateau.tool.TakePicture;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.ceri.visitechateau.tool.ConnectionManager.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {

	private int updateActivityNb = 0;
	Resources resources;
	private Context m_Context;
	private Activity m_Activity;
	private static MainActivity instance;
	private FragmentManager m_FragmentManager;

	@Bind(R.id.coordinator)
	CoordinatorLayout m_CoordinatorLayout;

	@Bind(R.id.drawer_layout)
	DrawerLayout m_DrawerLayout;

	@Bind(R.id.navigationView)
	NavigationView m_NavigationView;

	@Bind(R.id.toolbar)
	Toolbar m_Toolbar;

	ActionBarDrawerToggle m_DrawerToggle;
	ScreenParam param;
	private FileManager FM;

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
		ButterKnife.bind(this);
		FM = FileManager.getInstance();
		resources = getResources();
		m_Context = getContext();
		m_FragmentManager = getSupportFragmentManager();
		AppParams.getInstance().setM_FragmentManager(m_FragmentManager);
		m_Activity = MainActivity.this;
		param = new ScreenParam();
		param.paramWindowFullScreen(getWindow());
		param.paramSetSupportActionBar(m_Toolbar, this);
		m_DrawerToggle = new ActionBarDrawerToggle(this, m_DrawerLayout, 0, 0);
		setDrawer();
		presentTheDrawer();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.map, new TileViewActivity(), "TileViewActivity");
		ft.commit();
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
				//CurrentState.getInstance().removeFragment();
				prepareVisit(title);
				//CurrentState.getInstance().setM_fragment(true);
			} else
				Toast.makeText(m_Context, resources.getString(R.string.memory_access_error), Toast.LENGTH_LONG).show();
		} else {
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
		super.onResume();
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
