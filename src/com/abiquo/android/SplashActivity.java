package com.abiquo.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	/*
	 *  Time while splash screen is shown
	 */
	private long splashDelay = 2000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove notification and title bar		
		this.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		this.getActionBar().setDisplayShowTitleEnabled(false);
		this.getActionBar().setDisplayShowHomeEnabled(false);
		
		setContentView(R.layout.splashactivity);	
			    
		TimerTask task = new TimerTask() {
	      @Override
	      public void run() {
	    	/*
	    	 * After show splash screen for splashDelay launch MainActivity
	    	 */	    	  
	        Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
	        startActivity(mainIntent);
	        
	        /*
	         *  Destroy splash activity to avoid user watch it again
	         *  by pressing back button
	         */
	        //
	        finish();
	      }
	    };

	    Timer timer = new Timer();
	    timer.schedule(task, splashDelay);
	}
}
