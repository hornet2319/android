package com.example.rssreader;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.prefs.Preferences;

import com.example.db.DBAdapter;
import com.example.db.RSSFeed;
import com.example.db.RSSItem;
import com.example.dialogs.AddFeedDialog;
import com.example.rssreader.MainFragment.DBTask;
import com.example.utils.LoadRSSFeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;



   /**
     * A placeholder fragment containing saved posts.
     */
    public  class SavedFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
    	static final String ARG_SECTION_NUMBER = "section_number";
    	Context context;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
    	private DBAdapter helper;
    	static final String ATTRIBUTE_ITEM="itemName";
    	static final String ATTRIBUTE_FEED="feedName";
    	
    	private ArrayList<Map<String, String>> list_feed_data;
    	private SimpleAdapter list_adapter;

        public SavedFragment(Context context) {
        	this.context=context;
        }
      
       
       

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	View rootView=null;
				 rootView = inflater.inflate(R.layout.fragment_saved, container, false);
				 helper=new DBAdapter(context);
				 Button btn;
					btn=(Button)rootView.findViewById(R.id.feed_del_btn);
							btn.setOnClickListener(new OnClickListener() {
								
								public void onClick(View v) {
									// TODO Auto-generated method stub
									helper.open();
									helper.deleteAllItems();
									updateList();
						
								}

								
							});
			ListView list=(ListView)rootView.findViewById(R.id.item_list);
			list_feed_data= new ArrayList<Map<String, String>>();
			Map<String, String>m= new HashMap<String, String>();
			m.put(ATTRIBUTE_ITEM, "Loading Data...");
			m.put(ATTRIBUTE_FEED,"Please wait");
		list_feed_data.add(m);
		
		
	    String[] from = { ATTRIBUTE_ITEM, ATTRIBUTE_FEED };
	  
	    int[] to = { R.id.feed_title, R.id.feed_url };
	   
		 list_adapter = new SimpleAdapter(context, list_feed_data, R.layout.feed_list_item, from, to);
		 list.setAdapter(list_adapter);
		 registerForContextMenu(list);
		 list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					
					new LoadRSSFeed(context, list_feed_data.get(arg2).get(ATTRIBUTE_FEED),list_feed_data.get(arg2).get(ATTRIBUTE_ITEM)).execute();
				}
			});
		 updateList();

			
				 
            return rootView;
        }
        private void updateList() {
			// TODO Auto-generated method stub
        	new LoadTask().execute(); 
		}
        public class LoadTask extends AsyncTask<Void, Void, Boolean> {
			
    		
			protected void onPreExecute() {
	            super.onPreExecute();
	           
	        }		 
	
		

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Log.i("БД", "Дані завантажуються");
				 ArrayList<RSSItem> list_feed=new ArrayList<RSSItem>();
				 list_feed.clear();
				DBAdapter helper=new DBAdapter(context); 
				helper.open();
				list_feed=helper.getItems();
				
				list_feed_data.clear();
				Map<String, String>m;
				
				for(int i=0;i<list_feed.size();i++){
					m= new HashMap<String, String>();
					m.put(ATTRIBUTE_ITEM, list_feed.get(i).getDescription());
					m.put(ATTRIBUTE_FEED,""+list_feed.get(i).getFeed_name());
					list_feed_data.add(m);
				}
				helper.close();
				return true;
			}
			 protected void onPostExecute(Boolean bool) {				 
				 
				 list_adapter.notifyDataSetChanged();
			 
				 
			 
		 }
	}
        
        
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

		