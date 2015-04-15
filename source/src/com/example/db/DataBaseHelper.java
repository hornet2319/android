package com.example.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper
{
private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window
//destination path (location) of our database on device
private static String DB_NAME ="rss_db.sqlite";// Database name
private SQLiteDatabase mDataBase; 
private final Context mContext;

public DataBaseHelper(Context context) 
{
    super(context, DB_NAME, null, 1);// 1? its Database Version
  
    this.mContext = context;
}   

public void createDataBase() throws IOException
{
    //If database not exists copy it from the assets

    boolean mDataBaseExist = checkDataBase();
    if(!mDataBaseExist)
    {
        this.getReadableDatabase();
        this.close();
        
        
        try 
        {
            //Copy the database from assests
            copyDataBase();
            Log.e("DataBase", "Database copied");
        } 
        catch (IOException mIOException) 
        {
            throw new Error("ErrorCopyingDataBase");
        }
    }
}
    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(mContext.getDatabasePath(DB_NAME).toString());
        
        Log.v("dbFile", dbFile + "   "+ dbFile.exists());
    	
        
        return  dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = mContext.getDatabasePath(DB_NAME).toString();
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
       // String mPath = DB_PATH + DB_NAME;
      //  Log.v("mPath", mPath);
        
        mDataBase = SQLiteDatabase.openDatabase(mContext.getDatabasePath(DB_NAME).toString(), null, SQLiteDatabase.OPEN_READWRITE);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() 
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}