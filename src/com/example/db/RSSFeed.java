package com.example.db;

import java.io.Serializable;
import java.util.ArrayList;

public class RSSFeed implements Serializable {

	
		private int itemCount = 0;
		private ArrayList<RSSItem> itemList;
		private String name;
		private String url;
		// Serializable ID
		private static final long serialVersionUID = 1L;

		public RSSFeed() {
			// Initialize the item list
			itemList = new ArrayList<RSSItem>(0);
		}

		public void addItem(RSSItem item) {
			// Add an item to the Vector
			itemList.add(item);
			// Increment the item count
			itemCount++;
		}

		public RSSItem getItem(int position) {
			// Return the item at the chosen position
			return itemList.get(position);
		}

		public int getItemCount() {
			// Return the number of items in the feed
			return itemCount;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
}
