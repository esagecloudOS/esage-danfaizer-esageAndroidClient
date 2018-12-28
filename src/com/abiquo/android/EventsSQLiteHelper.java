package com.abiquo.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventsSQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_EVENTS = "events";
  public static final String EVENT_ID = "id";  
  public static final String EVENT_ACTION = "actionPerformed";
  public static final String EVENT_COMPONENT = "component";
  public static final String EVENT_ENTERPRISE = "enterprise";
  public static final String EVENT_PERFORMEDBY = "performedBy";
  public static final String EVENT_SEVERITY = "severity";
  public static final String EVENT_STACKTRACE= "stacktrace";
  public static final String EVENT_TIMESTAMP= "timestamp";

  private static final String DATABASE_NAME = "events.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_EVENTS + "(" + 
      EVENT_ID + " integer primary key, " + 
      EVENT_ACTION + " text not null, " +
      EVENT_COMPONENT + " text not null, " +
      EVENT_ENTERPRISE + " text not null, " +
      EVENT_PERFORMEDBY + " text not null, " +
      EVENT_SEVERITY + " text not null, " +
      EVENT_STACKTRACE + " text not null, " +
      EVENT_TIMESTAMP + " text not null);";      

  public EventsSQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w("Abiquo Viewer",EventsSQLiteHelper.class.getName()+": Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
    onCreate(db);
  }
  
  public void purge(SQLiteDatabase db) {
    Log.w("Abiquo Viewer",EventsSQLiteHelper.class.getName()+" database data has been purged");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
    onCreate(db);
  }

} 