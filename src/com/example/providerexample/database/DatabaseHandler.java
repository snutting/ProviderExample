package com.example.providerexample.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static DatabaseHandler singleton;
	
	public static DatabaseHandler getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandler(context);
        }
        return singleton;
    }
	
	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "providerExample";
    
    private final Context context;
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Good idea to use process context here
        this.context = context;
        //this.context = context.getApplicationContext();
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Item.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public synchronized Item getItem(final long id) {
		final SQLiteDatabase db = this.getReadableDatabase();
		final Cursor cursor = db.query(Item.TABLE_NAME, 
				Item.FIELDS, Item.COL_ID + " IS ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor == null || cursor.isAfterLast()) {
        return null;
		}
		
		Item item = null;
		if (cursor.moveToFirst()) {
			item = new Item(cursor);
		}
		cursor.close();
		
		return item;
    }

    public synchronized boolean putItem(final Item item) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();
        
        if (item.id > -1) {
        	result += db.update(Item.TABLE_NAME, item.getContent(), 
        			Item.COL_ID + " IS ?",
        			new String[] {String.valueOf(item.id) });
        }
        
        if (result > 0) {
        	success = true;
        } else {
        	// Update failed or wasn't possible, insert instead
        	final long id = db.insert(Item.TABLE_NAME, null, 
        			item.getContent());
        	
        	if (id > -1) {
        		item.id = id;
        		success = true;
        	}
        }
        // Calls the notifyProviderOnItemChange added below.
        if (success) {
            notifyProviderOnItemChange();
        }
        
        return success;
    }

    public synchronized int removeItem(final Item item) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(Item.TABLE_NAME, 
        		Item.COL_ID + " IS ?",
        		new String[] {Long.toString(item.id) });
        // Calls the notifyProviderOnItemChange added below.
        if (result > 0) {
            notifyProviderOnItemChange();
        }
        return result;
    }
    
    // Method to notify Content Provider when the database changes.
    private void notifyProviderOnItemChange() {
        context.getContentResolver().notifyChange(
                ItemProvider.URI_ITEMS, null, false);
    }
    
    
}
