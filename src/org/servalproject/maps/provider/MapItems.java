/*
 * Copyright (C) 2012 The Serval Project
 *
 * This file is part of the Serval Maps Software
 *
 * Serval Maps Software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.servalproject.maps.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * A content provider that provides access to the map item data
 */
public class MapItems extends ContentProvider {
	
	// private class level constants
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private final int LOCATION_DIR_URI = 0;
	private final int LOCATION_ITEM_URI = 1;
	
	private final String TAG = "MapItems";
	private final boolean V_LOG = true;
	
	// private class level variables
	private MainDatabaseHelper databaseHelper;
	private SQLiteDatabase database;
	
	/*
	 * undertake initialisation tasks
	 * 
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		
		// define URis that we'll match against
		//uriMatcher.addURI(MapItemsContract.Locations.CONTENT_URI, LOCATION_DIR_URI);
		uriMatcher.addURI(MapItemsContract.AUTHORITY, MapItemsContract.Locations.CONTENT_URI_PATH, LOCATION_DIR_URI);
		uriMatcher.addURI(MapItemsContract.AUTHORITY, MapItemsContract.Locations.CONTENT_URI_PATH + "/#", LOCATION_ITEM_URI);
		
		// create the database connection
		databaseHelper = new MainDatabaseHelper(getContext());
		
		return true;
	}
	
	/*
	 * execute a query
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selctionArgs, String sortOrder) {
		
		// choose the table name and sort order based on the URI
		switch(uriMatcher.match(uri)) {
		case LOCATION_DIR_URI:
			// uri matches all of the table
			if(TextUtils.isEmpty(sortOrder) == true) {
				sortOrder = MapItemsContract.Locations.Table._ID + " ASC";
			}
			break;
		case LOCATION_ITEM_URI:
			// uri matches one record
			if(TextUtils.isEmpty(selection) == true) {
				selection = MapItemsContract.Locations.Table._ID + " = " + uri.getLastPathSegment();
			} else {
				selection += "AND " + MapItemsContract.Locations.Table._ID + " = " + uri.getLastPathSegment();
			}
			break;
		default:
			// unknown uri found
			Log.e(TAG, "unknown URI detected on query: " + uri.toString());
			throw new IllegalArgumentException("unknwon URI detected");
		}
		
		Cursor mResults = null;
		
		// return the results
		return mResults;
	}
	
	/*
	 * insert data into the database
	 * 
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		Uri mResults = null;
		String mTable = null;
		Uri mContentUri = null;
		
		// chose the table name
		switch(uriMatcher.match(uri)) {
		case LOCATION_DIR_URI:
			mTable = MapItemsContract.Locations.CONTENT_URI_PATH;
			mContentUri = MapItemsContract.Locations.CONTENT_URI;
			break;
		default:
			// unknown uri found
			Log.e(TAG, "unknown URI detected on insert: " + uri.toString());
			throw new IllegalArgumentException("unknwon URI detected");
		}
		
		// get a connection to the database
		database = databaseHelper.getWritableDatabase();
		
		long mId = database.insertOrThrow(mTable, null, values);
		
		// play nice and tidy up
		database.close();
		
		mResults = ContentUris.withAppendedId(mContentUri, mId);
		getContext().getContentResolver().notifyChange(mResults, null);
		
		return mResults;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		
		// choose the mime type
		switch(uriMatcher.match(uri)) {
		case LOCATION_DIR_URI:
			return MapItemsContract.Locations.CONTENT_TYPE_LIST;
		case LOCATION_ITEM_URI:
			return MapItemsContract.Locations.CONTENT_TYPE_ITEM;
		default:
			// unknown uri found
			Log.e(TAG, "unknown URI detected on getType: " + uri.toString());
			throw new IllegalArgumentException("unknwon URI detected");
		}
	}
	

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
