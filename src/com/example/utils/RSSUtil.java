package com.example.utils;

import com.example.db.RSSFeed;

public class RSSUtil {
	private static RSSUtil util;
	private RSSFeed feed;
	private ListAdapter adapter;
	
	private RSSUtil(){}
	
	public static RSSUtil getInstance(){
		if(util==null){
			util=new RSSUtil();
		}
		return util;
	}

	public RSSFeed getFeed() {
		return feed;
	}

	public void setFeed(RSSFeed feed) {
		this.feed = feed;
	}

	public ListAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ListAdapter adapter) {
		this.adapter = adapter;
	}
	

	
}
