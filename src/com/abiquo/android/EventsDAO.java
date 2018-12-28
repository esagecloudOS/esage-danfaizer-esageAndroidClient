package com.abiquo.android;

import java.util.ArrayList;
import java.util.List;

import com.abiquo.android.EventsDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EventsDAO {

	// Database fields
	private SQLiteDatabase database;
	private EventsSQLiteHelper dbHelper;
	private String[] allColumns = { 
			EventsSQLiteHelper.EVENT_ID,
			EventsSQLiteHelper.EVENT_ACTION,
			EventsSQLiteHelper.EVENT_COMPONENT,
			EventsSQLiteHelper.EVENT_ENTERPRISE,
			EventsSQLiteHelper.EVENT_PERFORMEDBY,
			EventsSQLiteHelper.EVENT_SEVERITY,
			EventsSQLiteHelper.EVENT_STACKTRACE,
			EventsSQLiteHelper.EVENT_TIMESTAMP };

	public EventsDAO(Context context) {
		dbHelper = new EventsSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void purge() throws SQLException {
		dbHelper.purge(database);
	}

	public Event createEvent(Long id,String action, String component, String enterprise,
			String performedby, String severity, String stacktrace, String timestamp) {
		ContentValues values = new ContentValues();
		values.put(EventsSQLiteHelper.EVENT_ID, id);
		values.put(EventsSQLiteHelper.EVENT_ACTION, action);
		values.put(EventsSQLiteHelper.EVENT_COMPONENT, component);
		values.put(EventsSQLiteHelper.EVENT_ENTERPRISE, enterprise);
		values.put(EventsSQLiteHelper.EVENT_PERFORMEDBY, performedby);
		values.put(EventsSQLiteHelper.EVENT_SEVERITY, severity);
		values.put(EventsSQLiteHelper.EVENT_STACKTRACE, stacktrace);
		values.put(EventsSQLiteHelper.EVENT_TIMESTAMP, timestamp);
		database.insert(EventsSQLiteHelper.TABLE_EVENTS, null, values);
		Cursor cursor = database.query(EventsSQLiteHelper.TABLE_EVENTS,
				allColumns, EventsSQLiteHelper.EVENT_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		Event newEvent = cursorToEvent(cursor);
		cursor.close();
		return newEvent;
	}

	public List<Event> getAllEvents() {
		List<Event> events = new ArrayList<Event>();

		Cursor cursor = database.query(EventsSQLiteHelper.TABLE_EVENTS,
				allColumns, null, null, null, null, "id DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Event event = cursorToEvent(cursor);
			Log.w("Abiquo Viewer","Event: "+event.getId());
			events.add(event);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return events;
	}
	
	private Event cursorToEvent(Cursor cursor) {
		Event event = new Event();
		event.setId(cursor.getLong(0));
		event.setActionPerformed(cursor.getString(1));
		event.setComponent(cursor.getString(2));
		event.setEnterprise(cursor.getString(3));
		event.setPerformedBy(cursor.getString(4));
		event.setSeverity(cursor.getString(5));
		event.setStacktrace(cursor.getString(6));
		event.setTimestamp(cursor.getString(7));
		return event;
	}
} 