package com.abiquo.android;

import java.lang.ref.WeakReference;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class VirtualAppliancesFragment extends Fragment implements GenericAsyncTaskListener {
	
	@SuppressWarnings("unused")
	private WeakReference<GenericAsyncTask> asyncTaskWeakRef;
	private GenericAsyncTask asyncTask;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        return inflater.inflate(R.layout.virtualappliancesfragment, container, false);
       
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
     super.onActivityCreated(savedInstanceState);
     setRetainInstance(true);
     // Once category is load button is disabled
     AndroidUtils.enableProgressSpinner(this);
     disableMenuButton();
     startNewAsyncTask();
     
     Log.v("DetailFragment", "onActivityCreated()");
    }
    
    @Override
    public void onDestroyView() {
     super.onDestroyView();
     enableMenuButton();
     asyncTask.cancel(true);
    }
    
    private void startNewAsyncTask() {
        asyncTask = new GenericAsyncTask(this);
        this.asyncTaskWeakRef = new WeakReference<GenericAsyncTask >(asyncTask);
        asyncTask.execute();
    }
    
	private void disableMenuButton(){
        ImageButton resourcesButton = (ImageButton) getActivity().findViewById(R.id.virtualappliancesButton);
        resourcesButton.setClickable(false);
        resourcesButton.setBackgroundColor(getResources().getColor(R.color.barbuttonactivecategory));
    }
    
    private void enableMenuButton(){
        ImageButton resourcesButton = (ImageButton) getActivity().findViewById(R.id.virtualappliancesButton);
        resourcesButton.setClickable(true);
        resourcesButton.setBackgroundColor(getResources().getColor(R.color.barbuttoncategory));
    }

	@Override
	public void onTaskComplete(String result) {
		AndroidUtils.disableProgressSpinner(this);
		Log.v("AndroidViewer", "Async tasc executed");
	}

}