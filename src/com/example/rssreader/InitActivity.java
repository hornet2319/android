package com.example.rssreader;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.os.Bundle;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;


import com.example.dialogs.AddFeedDialog;
import com.example.utils.DButil;
import com.example.utils.InternetUtil;

public class InitActivity extends Activity {
	// Keep track of when feed exists
	private SharedPreferences prefs;
	private DButil nFeeds;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//DBTask task= new DBTask();
	//task.execute();
	nFeeds=new DButil(this);
	// Check if a feed exists
	if(nFeeds.isEmpty()){
		
		setContentView(R.layout.splash);
		InternetUtil net=new InternetUtil(this);
		
		if(!net.checkInternetConnection()){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
			// Tell the user what happened
			builder.setMessage("Unable to reach server.\nPlease check your connectivity.")
			// Alert title
			.setTitle("Connection Error")
			// Can't exit via back button
			.setCancelable(false)
			// Create exit button
			.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
			// Exit the application
			finish();
			}
			});
			// Create dialog from builder
			AlertDialog alert = builder.create();
			// Show dialog
			alert.show();
			// Center the message of the dialog
			((TextView)alert.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
			// Center the title of the dialog
			((TextView)alert.findViewById((getResources().getIdentifier("alertTitle", "id", "android")))).setGravity(Gravity.CENTER);
		
		}
		else {
			AddFeedDialog feed = new AddFeedDialog(this, true);
			feed.show();
			// Start the new activity
		
		}
	
	} 
	else {
		Log.v("FEEDS", "feeds is empty   "+nFeeds.isEmpty());
		// Start the new activity
		startActivity(new Intent(this, MainActivity.class));
		// Kill this one
		finish();
	}
	
}
	
}

