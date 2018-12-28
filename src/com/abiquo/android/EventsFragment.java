package com.abiquo.android;

import java.lang.ref.WeakReference;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class EventsFragment extends Fragment implements GenericAsyncTaskListener {

	private WeakReference<GenericAsyncTask> asyncTaskWeakRef;
	private GenericAsyncTask asyncTask;
	private static boolean asynccallrunning = false;
	private static boolean asyncrunned = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {    	
		return inflater.inflate(R.layout.eventsfragment, container, false);       
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.eventsrefresh:
			Log.d("Abiquo Viewer","Refresh events view");
			AndroidUtils.enableProgressSpinner(this);
			startNewAsyncTask("events","application/vnd.abiquo.events+json");
			return true;    
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		// Once category is load button is disabled
		AndroidUtils.enableProgressSpinner(this);
		Long currentts = System.currentTimeMillis();
		Long lastrefreshts = AbiquoUtils.getLastEventsRefresh();
		if (lastrefreshts.equals(0)) {
			startNewAsyncTask("events","application/vnd.abiquo.events+json");     
			Log.i("Abiquo Viewer", "Loading latest events data from Abiquo API");	
		} else if (currentts - lastrefreshts >= 15*60*1000) {
			startNewAsyncTask("events","application/vnd.abiquo.events+json");     
			Log.i("Abiquo Viewer", "Refreshing events data from Abiquo API");				
		} else {
			AbiquoUtils.inflateEventsListView(this);
			AndroidUtils.disableProgressSpinner(this);
		}		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.eventsmenu, menu);
		super.onCreateOptionsMenu(menu,inflater);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (asyncrunned) 
			asyncTask.cancel(true);
		asynccallrunning = false;		
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (asyncrunned) 
			asyncTask.cancel(true);
		asynccallrunning = false;		
	}


	@Override
	public void onPause() {
		super.onPause();
		if (asyncrunned) 
			asyncTask.cancel(true);
		asynccallrunning = false;		
	}

	private void startNewAsyncTask(String ... params) {
		asyncrunned = true;
		if (!asynccallrunning) {			
			asyncTask = new GenericAsyncTask(this);
			this.asyncTaskWeakRef = new WeakReference<GenericAsyncTask >(asyncTask);
			asynccallrunning = true;
			asyncTask.execute(params);
		}
	}

	@Override
	public void onTaskComplete(String result) {
		AndroidUtils.disableProgressSpinner(this);
		asynccallrunning = false;		
		if (result != null) {
			AbiquoUtils.persistAndUpdateEvents(this,result);
		} else {
			// TO-DO: Show screen indicating no data can be shown
		}
	}

}