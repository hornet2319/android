package com.example.rssreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.db.DBAdapter;
import com.example.db.RSSFeed;
import com.example.db.RSSItem;
import com.example.utils.ListAdapter;
import com.example.utils.LoadRSSFeed;
import com.example.utils.RSSUtil;
import com.example.utils.ReloadRSSFeed;

@SuppressLint("NewApi")
public class ItemActivity extends Activity {

	// Check if we refreshed
	private boolean isRefresh = false;
	// The adapter for the list
	private ListAdapter adapter;
	// The list to display it in
	private ListView list;
	// The RSSFeed of the site
	private RSSFeed feed;
	private String filter="";
	Context context;

	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Create a new ViewGroup for the fragment
		setContentView(R.layout.activity_list);
		// If we're above Honeycomb
		if(android.os.Build.VERSION.SDK_INT >= 11){
			// Remove the icon from the ActionBar
			getActionBar().setDisplayShowHomeEnabled(false);
		}
		feed=RSSUtil.getInstance().getFeed();
		// Get feed from the passed bundle
		context=this;

		// Find the ListView we're using
		list = (ListView)findViewById(R.id.item_listView);
		// Set the vertical edges to fade when scrolling
		list.setVerticalFadingEdgeEnabled(true);

		// Create a new adapter
		adapter = new ListAdapter(this, feed);
		// Set the adapter to the list
		list.setAdapter(adapter);
		registerForContextMenu(list);
		// Set on item click listener to the ListView
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// Start the new activity and pass on the feed item
				startActivity(new Intent(getBaseContext(), PostActivity.class).putExtra("pos", arg2)); //Starting Post activity 
			}
		});
	}

		@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create a "change" option to change the feed URL
		MenuItem change = menu.add(Menu.NONE, 0, Menu.NONE, "CHANGE FEED");
		// Create a "reload" menu option to reload the feed
		MenuItem reload = menu.add(Menu.NONE, 1, Menu.NONE, "RELOAD");
		// If we're above Honeycomb
		if(android.os.Build.VERSION.SDK_INT >= 11){
			// Set the change button to show always
			change.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			// Set the reload button to show always
			reload.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// We're refreshing
		isRefresh = true;
		// Depending on what's pressed
		switch (item.getItemId()) {
		case 0:
			// Change the URL
			onBackPressed();
			return true;
		case 1:
			// Start parsing the feed again
			RSSUtil.getInstance().setAdapter(adapter);
			new ReloadRSSFeed(context, feed.getUrl(), filter).execute();
		
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Exit instead of going to splash screen
	//	adapter.notifyDataSetChanged();
	}

	@Override 
	public void onResume(){
		super.onResume();
		// This is awful, but It`s works (currently finding another way)
		if(isRefresh){
			feed=RSSUtil.getInstance().getFeed();
			adapter = new ListAdapter(ItemActivity.this, feed);
			list.setAdapter(adapter); 
			isRefresh = false;
		}
	}
	  public void onCreateContextMenu(ContextMenu menu, View v,
      	    ContextMenuInfo menuInfo) {
      	  if (v.getId()==R.id.item_listView) {
      	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    
      	    String[] menuItems = getResources().getStringArray(R.array.item_context_menu);
      	    for (int i = 0; i<menuItems.length; i++) {
      	      menu.add(Menu.NONE, i, i, menuItems[i]);
      	    }
      	  }
      	}
      public boolean onContextItemSelected(MenuItem item) {
      	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
      	  int menuItemIndex = item.getItemId();
      	  String[] menuItems = getResources().getStringArray(R.array.item_context_menu);
      	  String menuItemName = menuItems[menuItemIndex];
      	  RSSItem listItem = RSSUtil.getInstance().getFeed().getItem((info.position));
      	  DBAdapter helper=new DBAdapter(context); 
      	  helper.open();
      	  helper.saveItem(listItem);
      	  helper.close();
      	  Toast.makeText(context, "Item saved", Toast.LENGTH_SHORT).show();
      	  
      	  return true;
      	}
}