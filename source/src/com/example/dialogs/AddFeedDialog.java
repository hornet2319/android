package com.example.dialogs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.example.db.DBAdapter;
import com.example.db.RSSFeed;
import com.example.rssreader.MainActivity;
import com.example.rssreader.MainFragment;
import com.example.utils.DButil;

public class AddFeedDialog {

		private Context context;
		private boolean bool=false;
		private MainFragment fragment=null;
		public AddFeedDialog(Context context) {
			this.context=context;
			
		}
		public AddFeedDialog(Context context, boolean bool) {
			this.context=context;
			this.bool=bool;
		}
		public AddFeedDialog(Context context, MainFragment fragment) {
			this.context=context;
			this.fragment=fragment;
		}

		public void show() {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			// Set an EditText view to get user input
			final EditText input = new EditText(context);
			// Set the alert title
			builder.setTitle("Adding new RSS Feed:")
			// Set the alert message
			.setMessage("Please enter a valid RSS URL:")
			// Set the view to the dialog
			.setView(input)
			// Can't exit via back button
			.setCancelable(false)
			// Set the positive button action
			.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
			// Check for a valid URL
			
			
			String RSSFEEDURL = input.getText().toString();
			RSSFeed feed=new RSSFeed();
			feed.setUrl(RSSFEEDURL);
			feed.setName(getDomainName(RSSFEEDURL));
			
			DBAdapter helper=new DBAdapter(context);
			helper.createDatabase();
			helper.open();
			Log.v("FEED","adding feed     "+feed.getName());
			helper.createFeed(feed);
			helper.close();
			if(bool){
				// Start the new activity
				context.startActivity(new Intent(context, MainActivity.class));
				
			}
			if(!(fragment==null)){
				fragment.updateList();
			}
			}
			})
			// Set the negative button actions
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
			// If it's during initial loading
				DButil nFeeds = new DButil(context);
			if(nFeeds.isEmpty()){ //111111111
			// Exit the application
			((Activity)context).finish();
			} else {
			// Otherwise do nothing
			dialog.dismiss();
			}
			}
			});
			// Create dialog from builder
			AlertDialog alert = builder.create();
			// Don't exit the dialog when the screen is touched
			alert.setCanceledOnTouchOutside(false);
			// Show the alert
			alert.show();
			// Center the message
			((TextView)alert.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
			// Center the title of the dialog
			((TextView)alert.findViewById((context.getResources().getIdentifier("alertTitle", "id", "android")))).setGravity(Gravity.CENTER);
			}
			
		private static String getDomainName(String url) {
			String domain = null;
			try {
			domain = new URI(url).getHost();
			} catch (URISyntaxException e) {
			e.printStackTrace();
			}
			return domain.startsWith("www.") ? domain.substring(4) : domain;
			}
			
}

