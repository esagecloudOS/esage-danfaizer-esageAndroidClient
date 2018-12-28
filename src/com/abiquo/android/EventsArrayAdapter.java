package com.abiquo.android;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

class EventsAdapter extends BaseAdapter implements ListAdapter {

	private final List<Event> eventsArray;
	EventsAdapter (List<Event> eventList) {
		assert eventList != null;

		this.eventsArray = eventList;
	}
	@Override
	public int getCount() {
		Log.i("Abiquo Viewer","Number of events: "+eventsArray.size());
		if (eventsArray == null)
			return 0;
		else			
			return eventsArray.size();
	}

	@Override
	public Object getItem(int position) {
		return eventsArray.get(position);		
	}
	@Override
	public long getItemId(int position) {
		Event event = eventsArray.get(position);
		return event.getId();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) AbiquoApplication.getAbiquoAppContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			convertView = inflater.inflate(R.layout.eventlistitem, null);
		}
		TextView eventAction =(TextView)convertView.findViewById(R.id.eventAction);
		TextView eventPerformedby =(TextView)convertView.findViewById(R.id.eventPerformedby);
		TextView eventTimestamp =(TextView)convertView.findViewById(R.id.eventTimestamp);
		ImageView eventSeverity = (ImageView)convertView.findViewById(R.id.eventSeverity);
		Event event = (Event) getItem(position);  
		if(null!=event){
			eventAction.setText(event.getActionPerformed());
			eventTimestamp.setText(event.getTimestamp());
			eventPerformedby.setText(event.getPerformedBy());
		}
		if (event.getSeverity().equalsIgnoreCase("INFO")) {			
			eventSeverity.setBackgroundColor(AbiquoApplication.getAbiquoAppContext().getResources().getColor(R.color.eventinfo));
			eventSeverity.setImageDrawable(AbiquoApplication.getAbiquoAppContext().getResources().getDrawable(R.drawable.infoicon));
		} else if  (event.getSeverity().equalsIgnoreCase("WARN")) {
			eventSeverity.setBackgroundColor(AbiquoApplication.getAbiquoAppContext().getResources().getColor(R.color.eventwarn));
			eventSeverity.setImageDrawable(AbiquoApplication.getAbiquoAppContext().getResources().getDrawable(R.drawable.alerticon));
		} else if (event.getSeverity().equalsIgnoreCase("ERROR")) {
			eventSeverity.setBackgroundColor(AbiquoApplication.getAbiquoAppContext().getResources().getColor(R.color.eventerror));
			eventSeverity.setImageDrawable(AbiquoApplication.getAbiquoAppContext().getResources().getDrawable(R.drawable.erroricon));
		}
		return convertView;		
	}

}