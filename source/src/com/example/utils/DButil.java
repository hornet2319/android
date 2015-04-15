package com.example.utils;

import java.util.ArrayList;
import com.example.db.DBAdapter;
import com.example.db.RSSFeed;

import android.content.Context;

public class DButil {
	private Context context;
	
	public DButil(Context context){
		this.context=context;
	}
	
	public boolean isEmpty(){
		 DBAdapter helper;
		 ArrayList<RSSFeed> feeds;
		helper=new DBAdapter(context);
		feeds=new ArrayList<RSSFeed>(); 
		
		helper.createDatabase();
		helper.open();
			feeds= helper.getRSS();
		
		
		helper.close();
		if(feeds.isEmpty()){
		return true;
		}
		else return false;
	}
}
