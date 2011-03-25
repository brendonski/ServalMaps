/*
 * This file is part of the Serval Mapping Services app.
 *
 *  Serval Mapping Services app is free software: you can redistribute it 
 *  and/or modify it under the terms of the GNU General Public License 
 *  as published by the Free Software Foundation, either version 3 of 
 *  the License, or (at your option) any later version.
 *
 *  Serval Mapping Services app is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Serval Mapping Services app.  
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package org.servalproject.mappingservices.content;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Manages the database used to store location information
 * @author corey.wallis@servalproject.org
 *
 */
public class LocationOpenHelper extends SQLiteOpenHelper {
	
	/**
	 * Version of the database supported by this class
	 */
	public static final int DATABASE_VERSION = 1;
	
	/**
	 * Name of the database file
	 */
	public static final String DATABASE_NAME = "serval-maps-locations.db";
	
	/**
	 * Name of the locations table
	 */
	public static final String TABLE_NAME = "locations";
	
	/**
	 * Name of the id field
	 */
	public static final String _ID = BaseColumns._ID;
	
	/**
	 * Name of the phone number field
	 */
	public static final String PHONE_NUMBER_FIELD = "phone_number";
	
	/**
	 * Name of the latitude field
	 */
	public static final String LATITUDE_FIELD = "latitude";
	
	/**
	 * Name of the longitude field
	 */
	public static final String LONGITUDE_FIELD = "longitude";
	
	/**
	 * Name of the time stamp field
	 */
	public static final String TIMESTAMP_FIELD = "timestamp";
	
	/**
	 * Name of the time zone field
	 */
	public static final String TIMEZONE_FIELD = "timezone";
	
	/**
	 * Name of the hash field
	 */
	public static final String HASH_INDEX_FIELD = "hash";
	
	
	/*
	 * private class variables
	 */
	private Context context;
	
	/*
	 * private class constants
	 */
	private final boolean V_LOG = true;
	private final String TAG = "ServalMaps-LOH";
	
	public LocationOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
		this.context = context;
		
		// output some debug text
		if(V_LOG) {
			Log.v(TAG, "database file created");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// build the sql to create the table
		String mSql = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		            + PHONE_NUMBER_FIELD + " int, " + LATITUDE_FIELD + " real, " + LONGITUDE_FIELD + " real,"
		            + TIMESTAMP_FIELD + " int, " + TIMEZONE_FIELD + " text, " + HASH_INDEX_FIELD + " text)";
		
		// execute the sql
		try {
			db.execSQL(mSql);
		} catch (SQLException e) {
			Log.e(TAG, "unable to create table", e);
		}
		
		// build the sql to create the indexes
		mSql = "CREATE INDEX idx_phone_number ON " + TABLE_NAME + " ON (" + PHONE_NUMBER_FIELD + ")";
		
		try {
			db.execSQL(mSql);
		} catch (SQLException e) {
			Log.e(TAG, "unable to create phone number index", e);
		}
		
		mSql = "CREATE UNIQUE INDEX idx_hash ON " + TABLE_NAME + " ON (" + HASH_INDEX_FIELD + ")";
		
		try {
			db.execSQL(mSql);
		} catch (SQLException e) {
			Log.e(TAG, "unable to create has field index", e);
		}
		
		// output some debug text
		if(V_LOG) {
			Log.v(TAG, "database tables and indexes created");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
