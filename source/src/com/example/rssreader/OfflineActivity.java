package com.example.rssreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.example.db.RSSFeed;
import com.example.utils.RSSUtil;

public class OfflineActivity extends Activity {

	// The RSS Feed item
	private RSSFeed feed;
	// The webview to display in
	private WebView browser;


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_reading);
		// If we're above Honeycomb
		if(android.os.Build.VERSION.SDK_INT >= 11){
			// Remove the icon from the ActionBar
			getActionBar().setDisplayShowHomeEnabled(false);
		}
		// Enable the vertical fading edge (by default it is disabled)
		((ScrollView)findViewById(R.id.scrollview)).setVerticalFadingEdgeEnabled(true);
		// Get the feed object
		feed = RSSUtil.getInstance().getFeed();
		// Get the position from the intent
		int position = getIntent().getExtras().getInt("pos");
		// Set the title based on the post
		setTitle(feed.getItem(position).getTitle());
		// Initialize the views
		browser = (WebView)findViewById(R.id.browser);
		// Set the background transparent
		browser.setBackgroundColor(Color.TRANSPARENT);
		// Set our webview properties
		WebSettings browserSettings = browser.getSettings();
		browserSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		browserSettings.setPluginState(PluginState.ON);
		browserSettings.setJavaScriptEnabled(true);
		
		
	}

	@Override
	public void onBackPressed(){
		super.onBackPressed();
		startActivity(new Intent(this, ListActivity.class));
		finish();
	}
	
	@SuppressLint("NewApi" )
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create a "back" menu option to go back to the parent activity
		MenuItem back = menu.add(Menu.NONE, 0, Menu.NONE, "BACK");
		// If we're on Honeycomb or above
		if(android.os.Build.VERSION.SDK_INT >= 11){
			// Show the back button always
			back.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}