package com.example.db;

import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter 
{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;
    
    /*константи для sql запитів*/
    // Table Names
    private static final String TABLE_FEED="Feed";
    private static final String TABLE_ITEM = "item";
  
    // Common column names
    private static final String KEY_ID = "id";
    
    // Feed Table - column names
    private static final String FEED_NAME = "name";
    private static final String FEED_URL = "url";
    
    // item Table - column names
    private static final String ITEM_TITLE = "title";
    private static final String ITEM_DESC = "description";
    private static final String ITEM_DATE = "date";
    private static final String ITEM_THUMB = "thumb";
    private static final String ITEM_AUTHOR = "title";
    private static final String ITEM_URL = "url";
    private static final String ITEM_FEED_NAME = "feed_name";
    
 
    

    public DBAdapter(Context context) 
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DBAdapter createDatabase() throws SQLException 
    {
        try 
        {
            mDbHelper.createDataBase();
        } 
        catch (IOException mIOException) 
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DBAdapter open() throws SQLException 
    {
        try 
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getWritableDatabase();
        } 
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() 
    {
        mDbHelper.close();
    }
    
    /*get all feeds*/
    public ArrayList<RSSFeed> getRSS(){
    	ArrayList<RSSFeed> feeds = new ArrayList<RSSFeed>();
 
     String selectQuery = "SELECT  * FROM " + TABLE_FEED ;
     
   
     Log.e(TAG, selectQuery);
     
        Cursor c = mDb.rawQuery(selectQuery, null);
        
     
        if (c.moveToFirst()) {
            do {
           	 
        RSSFeed feed = new RSSFeed();
        
        feed.setName((c.getString(c.getColumnIndex(FEED_NAME))));
        feed.setUrl((c.getString(c.getColumnIndex(FEED_URL))));
        feeds.add(feed);
            } while (c.moveToNext());
        }
        return feeds;
    }
    /*get all saved items*/
    public ArrayList<RSSItem> getItems(){
    	ArrayList<RSSItem> items = new ArrayList<RSSItem>();
 
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM;
     
        Log.e(TAG, selectQuery);
     
        Cursor c = mDb.rawQuery(selectQuery, null);
     
        if (c.moveToFirst()) {
            do {
     
        RSSItem item = new RSSItem();
        item.setTitle(c.getString(c.getColumnIndex(ITEM_TITLE)));
        item.setDescription(c.getString(c.getColumnIndex(ITEM_DESC)));
        item.setDate(c.getString(c.getColumnIndex(ITEM_DATE)));
        item.setThumb(c.getString(c.getColumnIndex(ITEM_THUMB)));
        item.setAuthor(c.getString(c.getColumnIndex(ITEM_AUTHOR)));
        item.setURL(c.getString(c.getColumnIndex(ITEM_URL)));
        item.setFeed_name(c.getString(c.getColumnIndex(ITEM_FEED_NAME)));
        items.add(item);
            } while (c.moveToNext());
        }
        return items;
    }
    public void saveItem(RSSItem item){
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
   	 
        ContentValues values = new ContentValues();
        values.put(ITEM_TITLE, item.getTitle());
        values.put(ITEM_DESC,item.getDescription());
        values.put(ITEM_DATE, item.getDate());
        values.put(ITEM_THUMB, item.getThumb());
        values.put(ITEM_AUTHOR, item.getAuthor());
        values.put(ITEM_URL, item.getURL());
        values.put(ITEM_FEED_NAME, item.getFeed_name());
     
        // insert row
        db.insert(TABLE_ITEM, null, values);
        db.close();
    }
    public void deleteItem(String title){
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
    	db.delete(TABLE_ITEM, ITEM_TITLE + "=?", new String[] { title });
    }
    /* set new feed*/
    public void createFeed(RSSFeed feed){
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(FEED_NAME, feed.getName());
        values.put(FEED_URL, feed.getUrl());
     
        // insert row
        db.insert(TABLE_FEED, null, values);
        db.close();
       
    }
    public void deleteAllItems(){
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
    	db.delete(TABLE_ITEM, null, null);
    }
    /*delete selected feed*/
    public void deleteFeed(String name){
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
    	db.delete(TABLE_FEED, FEED_NAME + "=?", new String[] { name });

    }
}