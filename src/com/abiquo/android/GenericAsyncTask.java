package com.abiquo.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class GenericAsyncTask extends AsyncTask<String, Void, String> {

	private ProgressDialog dialog;
	private GenericAsyncTaskListener callback;

	public GenericAsyncTask(Fragment resourcesFragment) {
		this.callback = (GenericAsyncTaskListener)resourcesFragment;
	}


	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected String doInBackground(String... params) {
		HashMap<String, String> apiConnection = AbiquoUtils.apiConnectionDetails();
		
		if (apiConnection != null) {
			String api_protocol = "https";
			if (apiConnection.get("api_ssl").equalsIgnoreCase("no")) {  api_protocol= "http"; }
			
    	    String resourcePath = params[0];
    	    String acceptHeader = params[1];

			String uri =  api_protocol+"://"+apiConnection.get("api_url")+":"+apiConnection.get("api_port")+apiConnection.get("api_path")+"/"+resourcePath;
			Log.i("AbiquoViewer","HTTP Request URI: "+uri);
			
        	try {	        		
        	    CredentialsProvider credProvider = new BasicCredentialsProvider();
        	    credProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
    	        new UsernamePasswordCredentials(apiConnection.get("api_user"),apiConnection.get("api_password")));

        	    CustomHTTPClient http = new CustomHTTPClient();
        	    http.setCredentialsProvider(credProvider);
        	    HttpGet get = new HttpGet(uri);

        	    get.addHeader("Accept", acceptHeader);
        	    HttpResponse response = http.execute(get);        	    
        	    
        	    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        	    String jsonDataString = reader.readLine();	        	    	        	    
        	    return jsonDataString;
        	} catch (ClientProtocolException e) {
        	    Log.d("AbiquoViewer", "ERROR: Client protocol exception", e);
        	} catch (IOException e) {
        	    Log.d("AbiquoViewer", "ERROR: IOException", e);
        	}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (null != dialog && dialog.isShowing()) {
			dialog.dismiss();
		}
		callback.onTaskComplete(result);
	}

}
