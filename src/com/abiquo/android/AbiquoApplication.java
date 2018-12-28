package com.abiquo.android;

import android.app.Application;
import android.content.Context;

public class AbiquoApplication extends Application {
	private static Context context;
	
    public void onCreate(){
      context=getApplicationContext();
    }

    public static Context getAbiquoAppContext(){
      return context;
    } 
}
