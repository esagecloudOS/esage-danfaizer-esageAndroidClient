package com.abiquo.android;


import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;


public final class AbiquoUtils {

	/**
	 * Private constructor to prevent instantiation
	 */
	public AbiquoUtils() {}

	/*
	 * apiConnectionDetails returns in a HashMap api URI, api PORT, api SSL enabled,api USER, api PASSWORD
	 * extracted from shared preferences
	 */
	public static HashMap<String, String> apiConnectionDetails() {
		SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(AbiquoApplication.getAbiquoAppContext());

		HashMap<String,String> apiConnection = new HashMap<String,String>();

		String api_url = appPreferences.getString("prefapiurl", "");
		String api_port = appPreferences.getString("prefapiport", "443");
		String api_path = appPreferences.getString("prefapipath", "/api");
		String api_ssl = appPreferences.getString("prefapissl", "yes");
		String api_user = appPreferences.getString("prefuserusername", "admin");
		String api_password = appPreferences.getString("prefuserpassword", "xabiquo");

		if (api_ssl.trim() != "" && api_url.trim() != "" && api_port.trim() != "" && api_user.trim() != "" && api_password.trim() != "") {
			apiConnection.put("api_url", api_url);
			apiConnection.put("api_port", api_port);
			apiConnection.put("api_path", api_path);
			apiConnection.put("api_ssl", api_ssl);
			apiConnection.put("api_user", api_user);
			apiConnection.put("api_password", api_password);
		}
		else { apiConnection = null; }

		return apiConnection;		
	}

	public static String abiquoVersion(){
		SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(AbiquoApplication.getAbiquoAppContext());
		return appPreferences.getString("prefabiquoversion","2.6");
	}

	public static Boolean persistAndUpdateEvents(Fragment fragment, String result){
		EventsDAO eventsdatasource;
		eventsdatasource = new EventsDAO(AbiquoApplication.getAbiquoAppContext());
		eventsdatasource.open();
		Boolean operationResult = true;

		eventsdatasource.purge();
		try{
			JSONObject EventsJSONObject = new JSONObject(result);
			JSONArray EventsJSONArray = (JSONArray) EventsJSONObject.get("collection");				
			for(int i=0;i<EventsJSONArray.length();i++){
				JSONObject EventJSONObject = EventsJSONArray.getJSONObject(i);
				eventsdatasource.createEvent(EventJSONObject.getLong("id"),
						EventJSONObject.getString("actionPerformed"),
						EventJSONObject.getString("component"),
						EventJSONObject.getString("enterprise"),
						EventJSONObject.getString("performedBy"),
						EventJSONObject.getString("severity"),
						EventJSONObject.getString("stacktrace"),
						EventJSONObject.getString("timestamp"));
			}
			inflateEventsListView(fragment);
			Log.i("Abiquo Viewer", "Events database data refreshed");
		} catch(JSONException e){  
			Log.e("Abiquo Viewer", "Error parsing Events JSON Data "+e.toString());
			operationResult = false;
		} finally {
			setLastEventsRefresh();
			eventsdatasource.close();
		}
		return operationResult;
	}

	public static void inflateEventsListView(Fragment fragment) {
		EventsDAO eventsdatasource;
		eventsdatasource = new EventsDAO(AbiquoApplication.getAbiquoAppContext());
		eventsdatasource.open();
		ListView lstTest= (ListView)fragment.getActivity().findViewById(R.id.lstText);
		EventsAdapter eventsAdapter = new EventsAdapter (eventsdatasource.getAllEvents());
		lstTest.setAdapter(eventsAdapter);
		eventsdatasource.close();
	}
	
	public static Long getLastEventsRefresh(){
		SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(AbiquoApplication.getAbiquoAppContext());
		return appPreferences.getLong("preflasteventsrefresh",0);
	}
	
	public static void setLastEventsRefresh(){
		SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(AbiquoApplication.getAbiquoAppContext());
		SharedPreferences.Editor edit = appPreferences.edit();
		edit.putLong("preflasteventsrefresh", System.currentTimeMillis());
		edit.commit();
		Log.i("Abiquo Viewer","Updated events refresh timestamp");
	}

}
