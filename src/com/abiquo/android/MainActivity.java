package com.abiquo.android;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

/**
 * Activities should extend from BaseActivity to allow use custom
 * methods (for example, GoogleAnalytincs, session management, etc.)
 */
public class MainActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Check if is 1st time application is run so we
		 * can show Preferences screen and show a disclaimer
		 * if is required, etc.
		 */		
		if (AndroidUtils.firstTimeRun(AbiquoApplication.getAbiquoAppContext())){
			Log.i("AbiquoAndroidClient","INFO: First time Abiquo Android Client is run");
			AndroidUtils.setRunned(AbiquoApplication.getAbiquoAppContext());
			Intent i = new Intent(MainActivity.this, PreferencesActivity.class);
			startActivity(i);
		}

		//		FragmentManager fragmentManager = getFragmentManager();
		//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		if (AndroidUtils.getLastFragment() != null)
			Log.i("Abiquo Viewer","Last fragment id: " +AndroidUtils.getLastFragment().toString() );

		switch(AndroidUtils.getLastFragment()) {
		case R.id.eventsButton:
			loadEventsFragment();
			break;
		case R.id.virtualappliancesButton:
			loadVirtualAppliancesFragment();
			break;
		case R.id.virtualdatacenterButton:
			loadVirtualDataCenterFragment();
			break;
		case R.id.enterprisesButton:
			loadEnterprisesFragment();
			break;
		default:
			loadResourcesFragment();
		}
		/*
		ResourcesFragment fragment = new ResourcesFragment();
		fragmentTransaction.add(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
		 */
		/**
		 * 
		 */

		ImageButton resourcesBtn = (ImageButton) findViewById(R.id.resourcesButton);
		resourcesBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity host = (Activity) v.getContext();
				disableMenuButton(host,v.getId());
				enableLastMenuButton(host,v.getId());
				AndroidUtils.setLastFragment(v.getId());
				loadResourcesFragment();
			}
		});

		ImageButton virtualdatacenterBtn = (ImageButton) findViewById(R.id.virtualdatacenterButton);
		virtualdatacenterBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity host = (Activity) v.getContext();
				disableMenuButton(host,v.getId());
				enableLastMenuButton(host,v.getId());
				AndroidUtils.setLastFragment(v.getId());
				loadVirtualDataCenterFragment();
			}
		});

		ImageButton eventsBtn = (ImageButton) findViewById(R.id.eventsButton);
		eventsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity host = (Activity) v.getContext();
				disableMenuButton(host,v.getId());
				enableLastMenuButton(host,v.getId());
				AndroidUtils.setLastFragment(v.getId());
				loadEventsFragment();
			}
		});

		ImageButton virtualappliancesBtn = (ImageButton) findViewById(R.id.virtualappliancesButton);
		virtualappliancesBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity host = (Activity) v.getContext();
				disableMenuButton(host,v.getId());
				enableLastMenuButton(host,v.getId());
				AndroidUtils.setLastFragment(v.getId());
				loadVirtualAppliancesFragment();
			}
		});

		ImageButton enterprisesBtn = (ImageButton) findViewById(R.id.enterprisesButton);
		enterprisesBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity host = (Activity) v.getContext();
				disableMenuButton(host,v.getId());
				enableLastMenuButton(host,v.getId());
				AndroidUtils.setLastFragment(v.getId());
				loadEnterprisesFragment();   	 
			}
		});
	}
	
	private void disableMenuButton(Activity activity, Integer buttonId){				
		ImageButton resourcesButton = (ImageButton) activity.findViewById(buttonId);
		resourcesButton.setClickable(false);
		resourcesButton.setBackgroundColor(activity.getResources().getColor(R.color.barbuttonactivecategory));		
	}
	
    private void enableLastMenuButton(Activity activity,Integer buttonId){
    	SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(AbiquoApplication.getAbiquoAppContext());
        ImageButton resourcesButton = (ImageButton) activity.findViewById(appPreferences.getInt("preflastfragment",AbiquoApplication.getAbiquoAppContext().getResources().getInteger(R.id.resourcesButton)));
        resourcesButton.setClickable(true);
        resourcesButton.setBackgroundColor(activity.getResources().getColor(R.color.barbuttoncategory));
    }

	private void loadEnterprisesFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		EnterprisesFragment fragment = new EnterprisesFragment();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();	
	}

	private void loadVirtualAppliancesFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		VirtualAppliancesFragment fragment = new VirtualAppliancesFragment();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();	
	}

	private void loadEventsFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		EventsFragment fragment = new EventsFragment();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();	
	}

	private void loadVirtualDataCenterFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		VirtualDataCenterFragment fragment = new VirtualDataCenterFragment();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();	
	}

	private void loadResourcesFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		ResourcesFragment fragment = new ResourcesFragment();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();	
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent i = new Intent(this, PreferencesActivity.class);            
			startActivity(i);
			break;
		default:
			// Propagate item selection to Fragments
			return super.onOptionsItemSelected(item);        
		}

		return true;
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.mainactivity;
	}


}
