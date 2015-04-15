package com.example.rssreader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.db.DBAdapter;
import com.example.db.RSSFeed;
import com.example.dialogs.AddFeedDialog;
import com.example.utils.LoadRSSFeed;




   /**
     * A placeholder fragment containing a simple view.
     */
    public  class MainFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
    	Context context;
    	static final String ARG_SECTION_NUMBER = "section_number";
    	static final String ATTRIBUTE_NAME="name";
    	static final String ATTRIBUTE_URL="url";
    	
    	private ArrayList<Map<String, String>> list_feed_data;
    	private SimpleAdapter list_adapter;
    	
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        

        public MainFragment(Context context) {
        	this.context=context;
        }
        
        public MainFragment() {
        		
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	View rootView=null;
				rootView=inflater.inflate(R.layout.fragment_main, container, false);  
				if(context==null){
					context=getActivity();
				}
		Button btn;
		btn=(Button)rootView.findViewById(R.id.feed_add_btn);
				btn.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AddFeedDialog feed= new AddFeedDialog(context, getInstance());
						feed.show();
			
					} 
				});
		ListView listview = (ListView)rootView.findViewById(R.id.feed_list);
		list_feed_data= new ArrayList<Map<String, String>>();
			Map<String, String>m= new HashMap<String, String>();
			m.put(ATTRIBUTE_NAME, "Loading Data...");
			m.put(ATTRIBUTE_URL,"Please wait");
		list_feed_data.add(m);
		
		
	    String[] from = { ATTRIBUTE_NAME, ATTRIBUTE_URL };
	  
	    int[] to = { R.id.feed_title, R.id.feed_url };
	   
		 list_adapter = new SimpleAdapter(context, list_feed_data, R.layout.feed_list_item, from, to);
		 listview.setAdapter(list_adapter);
		 //adding context menu
		 registerForContextMenu(listview);
		 
		 listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// Start the new activity and pass on the feed item
					new LoadRSSFeed(context, list_feed_data.get(arg2).get(ATTRIBUTE_URL),list_feed_data.get(arg2).get(ATTRIBUTE_NAME)).execute();
				}
			});
		 updateList();
            return rootView;
        }
        //this class reads data from SQLite DB
       public class DBTask extends AsyncTask<Void, Void, Boolean> {
			
		
			protected void onPreExecute() {
	            super.onPreExecute();
	           
	        }		 

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Log.i("БД", "Дані завантажуються");
				 ArrayList<RSSFeed> list_feed=new ArrayList<RSSFeed>();
				 list_feed.clear();
				DBAdapter helper=new DBAdapter(context); 
				helper.open();
				list_feed=helper.getRSS();
				list_feed_data.clear();
				Map<String, String>m;
				
				for(int i=0;i<list_feed.size();i++){
					m= new HashMap<String, String>();
					m.put(ATTRIBUTE_NAME, list_feed.get(i).getName());
					m.put(ATTRIBUTE_URL,""+list_feed.get(i).getUrl());
					list_feed_data.add(m);
				}
				helper.close();
				return true;
			}
			 protected void onPostExecute(Boolean bool) {				 
				 
				 list_adapter.notifyDataSetChanged();
		 }
	}
      
        
        //creating context menu
        public void onCreateContextMenu(ContextMenu menu, View v,
        	    ContextMenuInfo menuInfo) {
        	  if (v.getId()==R.id.feed_list) {
        	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        	    menu.setHeaderTitle(list_feed_data.get(info.position).get(ATTRIBUTE_NAME));
        	    String[] menuItems = getResources().getStringArray(R.array.feed_context_menu);
        	    for (int i = 0; i<menuItems.length; i++) {
        	      menu.add(Menu.NONE, i, i, menuItems[i]);
        	    }
        	  }
        	}
        // listener for items of context menu
        public boolean onContextItemSelected(MenuItem item) {
        	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        	  int menuItemIndex = item.getItemId();
        	  String[] menuItems = getResources().getStringArray(R.array.feed_context_menu);
        	  String menuItemName = menuItems[menuItemIndex];
        	  String listItemName = list_feed_data.get(info.position).get(ATTRIBUTE_NAME);
        	  DBAdapter helper=new DBAdapter(context); 
        	  helper.open();
        	  helper.deleteFeed(listItemName);
        	  helper.close();
        	  updateList();
        	  return true;
        	}
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
        //update list of feeds
        public   void updateList(){
        	 new DBTask().execute(); 
        }
        
        private MainFragment getInstance(){
        	return this;
        }
    }