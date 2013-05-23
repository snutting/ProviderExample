package com.example.providerexample.database;

import android.content.ContentValues;
import android.database.Cursor;

/** Adding an Item class and attempting to make
 *  it analogous to Person class. 
 * @author Scott
 *
 */
public class Item {
	
	//SQL convention says Table name should be singular
	public static final String TABLE_NAME = "Item";
	// Naming the id column with an underscore is good
	// to be consistent with other Android things...
	public static final String COL_ID = "_id";
	// Other item fields
	public static final String COL_COMPLETE = "complete";
	public static final String COL_ITEM_TITLE = "title";
	public static final String COL_DESC = "description";
	
	// For database projection so order is consistent
	public static final String[] FIELDS = { COL_ID, COL_COMPLETE, 
		COL_ITEM_TITLE, COL_DESC };
	
	/* 
	 * The SQL code that creates a Table for storing Items in.
	 * Note that the last row does NOT en in a comma like the others.
	 * This is a common source of error.
	 */

    /* CREATE TABLE Item
     *  (_id INTEGER PRIMARY KEY,
     *  complete BOOLEAN DEFAULT 0,
     *  title TEXT NOT NULL DEFAULT '',
     *  description TEXT NOT NULL DEFAULT '')
     */
	public static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
			+ COL_ID + " INTEGER PRIMARY KEY,"
			+ COL_COMPLETE + " BOOLEAN DEFAULT 0,"
			+ COL_ITEM_TITLE + " TEXT NOT NULL DEFAULT '',"
			+ COL_DESC + " TEXT NOT NULL DEFAULT ''"
			+ ")";
	
	// Fields corresponding to database columns
	public long id = -1;
	public boolean complete;
	public String title = "";
	public String description = "";

	/**
     * No need to do anything, fields are already set to default values above
     */
	
	public Item() {
	}
	
	/**
     * Convert information from the database into a Item object.
     */
	public Item(final Cursor cursor) {
		// Indices expected to match order in FIELDS!
		this.id = cursor.getLong(0);
		this.complete = (cursor.getInt(1) == 1);
		this.title = cursor.getString(2);
		this.description = cursor.getString(3);
		
	}
	
	/**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
	
	public ContentValues getContent() {
		final ContentValues values = new ContentValues();
		//Note that ID is NOT included here.
		values.put(COL_COMPLETE, complete);
		values.put(COL_ITEM_TITLE, title);
		values.put(COL_DESC, description);
		
		return values;
	}
}
