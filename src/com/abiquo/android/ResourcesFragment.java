package com.abiquo.android;

import java.lang.ref.WeakReference;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class ResourcesFragment extends Fragment implements GenericAsyncTaskListener {
			
	@SuppressWarnings("unused")
	private WeakReference<GenericAsyncTask> asyncTaskWeakRef;
	private GenericAsyncTask asyncTask;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        return inflater.inflate(R.layout.resourcesfragment, container, false);
       
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
     super.onActivityCreated(savedInstanceState);
     setRetainInstance(true);
     // Once category is load button is disabled
     AndroidUtils.enableProgressSpinner(this);  
     startNewAsyncTask("login","application/vnd.abiquo.user+json");
     
     Log.v("DetailFragment", "onActivityCreated()");
    }
    
    @Override
//    public void onDestroyView() {
   	public void onPause() {
     super.onDestroyView();
     asyncTask.cancel(true);
    }
    
    private void startNewAsyncTask(String ... params) {
        asyncTask = new GenericAsyncTask(this);
        this.asyncTaskWeakRef = new WeakReference<GenericAsyncTask >(asyncTask);
        asyncTask.execute(params);
    }

    private void setText(String text){
        TextView textView = (TextView) getView().findViewById(R.id.resourcesTextView);
        textView.setText(text);
    }
    

	@Override
	public void onTaskComplete(String result) {
		AndroidUtils.disableProgressSpinner(this);
		setText(result);
	}

}