package com.example.contactrestapi;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
//import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds;

public class AddFromPhoneActivity extends Activity 
	implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "AddFromPhoneActivity";
	
	private static final String[] PROJECTION =
         {
             Data._ID,
             Data.MIMETYPE,
             Data.DATA1, // TODO unvalid column data1
             Data.DATA2,
             Data.DATA3,
             Data.DATA4,
             Data.DATA5,
             Data.DATA6,
             Data.DATA7,
             Data.DATA8,
             Data.DATA9,
             Data.DATA10,
             Data.DATA11,
             Data.DATA12,
             Data.DATA13,
             Data.DATA14,
             Data.DATA15
         };
	private static final String[] PROJECTION_ = 
		{
		Data._ID,
		CommonDataKinds.StructuredName.DISPLAY_NAME,
		CommonDataKinds.Email.ADDRESS, // many data1's
		CommonDataKinds.Phone.NUMBER,
		};
	
	// Defines the selection clause
    private static final String SELECTION = Contacts.LOOKUP_KEY + " = ?"; // TODO is key correct?
    /*
     * Defines a variable to contain the selection value. Once you
     * have the Cursor from the Contacts table, and you've selected
     * the desired row, move the row's LOOKUP_KEY value into this
     * variable.
     */
    private String mLookupKey;
    /*
     * Defines a string that specifies a sort order of MIME type
     */
    private static final String SORT_ORDER = Data.MIMETYPE;
    // Defines a constant that identifies the loader
    private static final int QUERY_ID_NAME = 0;
    private static final int QUERY_ID_EMAIL = 1;
    private static final int QUERY_ID_PHONE = 2;
    private static final int QUERY_ID_ADDRESS = 3;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_from_phone);
		
		Intent in = getIntent(); // extra for lookupkey
		mLookupKey = in.getStringExtra(PhoneContactsActivity.EXTRA_CONTACT_KEY);
		
		// Initializes the loader framework
        getLoaderManager().initLoader(QUERY_ID_NAME, null, this);
        getLoaderManager().initLoader(QUERY_ID_EMAIL, null, this);
        getLoaderManager().initLoader(QUERY_ID_PHONE, null, this);
        getLoaderManager().initLoader(QUERY_ID_ADDRESS, null, this);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		CursorLoader loader = null;
		// Assigns the selection parameter
		String[] selectionArgs = { mLookupKey };
		
		switch (loaderId) {
		
		case QUERY_ID_NAME:
			// Starts the query
			loader = new CursorLoader(
						this,
						//Uri.withAppendedPath(Contacts.CONTENT_LOOKUP_URI, Uri.encode(mLookupKey)), 
						Contacts.CONTENT_URI,
						//Uri.withAppendedPath(ContentUris.withAppendedId(RawContacts.CONTENT_URI, 0), RawContacts.Entity.CONTENT_DIRECTORY), // TODO 0 raw contact id
						
						//PROJECTION,
						null,
						
						SELECTION,
						selectionArgs,
						null //SORT_ORDER // TODO 
				);
			break;
		case QUERY_ID_EMAIL:
			// Starts the query
			loader = new CursorLoader(
					this,
					CommonDataKinds.Email.CONTENT_URI,
					
					//PROJECTION,
					null,
					
					SELECTION,
					selectionArgs,
					null //SORT_ORDER // TODO 
			);
			break;
		case QUERY_ID_PHONE:
			// Starts the query
			loader = new CursorLoader(
					this,
					CommonDataKinds.Phone.CONTENT_URI,

					//PROJECTION,
					null,

					SELECTION,
					selectionArgs,
					null //SORT_ORDER // TODO 
					);
			break;
		case QUERY_ID_ADDRESS:
			// Starts the query
			loader = new CursorLoader(
					this,
					CommonDataKinds.StructuredPostal.CONTENT_URI,

					//PROJECTION,
					null,

					SELECTION,
					selectionArgs,
					null //SORT_ORDER // TODO 
					);
			break;


		default:
			break;
		}
		
		return loader;
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cur) {
		// Process the resulting Cursor here.

		switch (loader.getId()) {
		case QUERY_ID_NAME:
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(Contacts.DISPLAY_NAME));
				
				Log.w(TAG, "id: " + id);
				Log.w(TAG, "name: " + name);
				
			}
			break;

		case QUERY_ID_EMAIL:
			while (cur.moveToNext()) {
				String email = cur.getString(cur.getColumnIndex(CommonDataKinds.Email.ADDRESS));
				
				Log.w(TAG, "email: " + email);
				
			}
			break;

		case QUERY_ID_PHONE:
			while (cur.moveToNext()) {
				String phone = cur.getString(cur.getColumnIndex(CommonDataKinds.Phone.NUMBER));
				
				Log.w(TAG, "phone: " + phone);
				
			}
			break;
		case QUERY_ID_ADDRESS:
			while (cur.moveToNext()) {
				String street = cur.getString(cur.getColumnIndex(CommonDataKinds.StructuredPostal.STREET));
				String city = cur.getString(cur.getColumnIndex(CommonDataKinds.StructuredPostal.CITY));
				String zipCode = cur.getString(cur.getColumnIndex(CommonDataKinds.StructuredPostal.POSTCODE));
				String country = cur.getString(cur.getColumnIndex(CommonDataKinds.StructuredPostal.COUNTRY));
				
				Log.w(TAG, "address street: " + street);
				Log.w(TAG, "address city: " + city);
				Log.w(TAG, "address zipCode: " + zipCode);
				Log.w(TAG, "address country: " + country);
				
			}
			break;

		default:
			break;
		}
		
		
		/*
		TODO remove
		Log.w(TAG, "ROWS: " + cur.getCount());
		
		String[] cols = cur.getColumnNames();
		//cur.moveToFirst();
		for (int i = 0; i < cols.length; i++) {
			
			Log.w(TAG, "COLUMN: " + cols[i]  + " VALUE: " + cur.getColumnIndexOrThrow(cols[i]));//cur.getType(cur.getColumnIndexOrThrow(cols[i]))); //cur.getString(cur.getColumnIndexOrThrow(cols[i])));
		}
		//Log.w(TAG, cur.getString(cur.getColumnIndexOrThrow("display_name"))); // CursorIndexOutOfBoundsException index -1 requested with a size of 1 because no moveToFirst()

		while (cur.moveToNext()) {
			String id = cur.getString(cur.getColumnIndex(Contacts._ID));
			String name = cur.getString(cur.getColumnIndex(Contacts.DISPLAY_NAME));
			
			Log.w(TAG, "id: " + id);
			Log.w(TAG, "name: " + name);
			
			//if (Integer.parseInt(cur.getString(cur.getColumnIndex(Contacts.HAS_PHONE_NUMBER))) > 0) {
				//Query phone here.  Covered next
			//}
		}
*/

	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// no references to the Cursor, so no need to remove them 
	}
}
