package com.example.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;

import com.example.db.RSSFeed;
import com.example.rssreader.ItemActivity;
import com.example.rssreader.R;

/**
 * Loads an RSS feed from a given URL and writes the object
 * to a file in the application's /data directory. Parses 
 * through the feed and starts the main fragment control
 * upon completion.
 * 
 
 */
public class LoadRSSFeed extends AsyncTask<Void, Void, Void> {

	// The parent context
	private Context parent;
	// Dialog displaying a loading message
	private ProgressDialog refreshDialog;
	// The RSSFeed object
	private RSSFeed feed;
	private String name;
	// The URL we're parsing from
	private String RSSFEEDURL;

	public LoadRSSFeed(Context c, String url,String name){
		// Set the parent
		parent = c;
		// Set the feed URL
		RSSFEEDURL = url;
		this.name=name;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// Parse the RSSFeed and save the object
		feed = new DOMParser(name).parseXML(RSSFEEDURL);
		return null;
	}

	@Override
	protected void onPreExecute(){
		// Create a new dialog
		refreshDialog = new ProgressDialog(new ContextThemeWrapper(parent, R.style.AppTheme));
		// Inform of the refresh
		refreshDialog.setMessage("Loading feed...");
		// Spin the wheel whilst the dialog exists
		refreshDialog.setIndeterminate(false);
		// Don't exit the dialog when the screen is touched
		refreshDialog.setCanceledOnTouchOutside(false);
		// Don't exit the dialog when back is pressed
		refreshDialog.setCancelable(true);
		// Show the dialog
		refreshDialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		
		RSSUtil.getInstance().setFeed(feed);
		
		// Dismiss the dialog
		refreshDialog.dismiss();
		// Start the main fragment control
		parent.startActivity(new Intent(parent, ItemActivity.class));
	}
}