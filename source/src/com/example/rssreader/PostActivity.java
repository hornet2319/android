package com.example.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.db.RSSFeed;
import com.example.utils.InlineBrowser;
import com.example.utils.RSSUtil;

public class PostActivity extends InlineBrowser {
	// Position of the clicked item
	private int position;
	// Our RSS feed object
	private RSSFeed feed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// Get the feed object
	feed = RSSUtil.getInstance().getFeed();
	// Get the position from the intent
	position = getIntent().getExtras().getInt("pos");
	// Set the title based on the post
	setTitle(feed.getItem(position).getTitle());
	// To check if offline reading is needed
	browser.setWebViewClient(new WebViewClient(){
	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	if(errorCode != -1){
	startActivity(new Intent(getBaseContext(), OfflineActivity.class).putExtra("pos", position));
	}
	}
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
	view.loadUrl(url);
	return true;
	}
	});
	// Load the URL
	browser.loadUrl(feed.getItem(position).getURL());
	}
	}
