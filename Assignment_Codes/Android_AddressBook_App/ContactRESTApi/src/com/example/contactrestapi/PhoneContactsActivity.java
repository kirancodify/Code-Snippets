package com.example.contactrestapi;

import android.app.Activity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract.Contacts;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Activity for listing contacts from the device address book 
 * that match a search string (given in intent).
 *
 */
public class PhoneContactsActivity extends Activity implements 
	LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
	
	private static final String TAG = "PhoneContactsActivity";
	public static final String EXTRA_CONTACT_KEY = "com.example.contactrestapi.PhoneContactsActivity.CONTACT_KEY";

    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                Contacts.DISPLAY_NAME_PRIMARY :
                Contacts.DISPLAY_NAME
    };
    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    private final static int[] TO_IDS = {
        android.R.id.text1
    };
    
    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
        {
            Contacts._ID,
            Contacts.LOOKUP_KEY,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    Contacts.DISPLAY_NAME_PRIMARY :
                    Contacts.DISPLAY_NAME

        };
    
    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the LOOKUP_KEY column
    private static final int LOOKUP_KEY_INDEX = 1;
    
    // Defines the phone contacts search expression, name will replace ?
    @SuppressLint("InlinedApi")
    private static final String SELECTION =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
            Contacts.DISPLAY_NAME + " LIKE ?";
    
    // Define global mutable variables
    // Define a ListView object
    private ListView mContactsList;
    // Define variables for the contact the user selects
    // The contact's _ID value
    private long mContactId;
    // The contact's LOOKUP_KEY
    private String mContactKey;
    // A content URI for the selected contact
    private Uri mContactUri;
    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter mCursorAdapter;
    
    // Defines a variable for the search string
    private String mSearchString;
    // Defines the array to hold values that replace the ?
    private String[] mSelectionArgs = { mSearchString };
    
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_contacts);
		
		mSearchString = getIntent().getStringExtra(MainActivity.EXTRA_SEARCH_NAME);
		
        mContactsList = (ListView) findViewById(R.id.phone_contacts_list);
        // Gets a CursorAdapter
        mCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.phonecontacts_listitem,
                null,
                FROM_COLUMNS, TO_IDS,
                0);
        // Sets the adapter for the ListView
        mContactsList.setAdapter(mCursorAdapter);
        // Initializes the loader
        getLoaderManager().initLoader(0, null, this);
        
        mContactsList.setOnItemClickListener(this);
	}
    
    @Override
    public void onItemClick(AdapterView<?> parent, View item, int position, long rowID) {
        // Get the Cursor
        Cursor cursor = ((CursorAdapter)parent.getAdapter()).getCursor();
        // Move to the selected contact
        cursor.moveToPosition(position);
        // Get the _ID value
        mContactId = cursor.getLong(CONTACT_ID_INDEX);
        // Get the selected LOOKUP KEY
        mContactKey = cursor.getString(LOOKUP_KEY_INDEX);
        // Create the contact's content Uri
        mContactUri = Contacts.getLookupUri(mContactId, mContactKey);
        /*
         * You can use mContactUri as the content URI for retrieving
         * the details for a contact.
         */
        //parent.getItemAtPosition(position);
        
        // TODO get details and add to our service
        Log.w(TAG, String.valueOf(mContactId) + ", " + mContactKey);
        Intent in = new Intent(this, AddFromPhoneActivity.class);
        in.putExtra(EXTRA_CONTACT_KEY, mContactKey);
        startActivity(in);
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        /*
         * Makes search string into pattern and
         * stores it in the selection array
         */
        mSelectionArgs[0] = "%" + mSearchString + "%";
        // Starts the query
        return new CursorLoader(
                this,
                Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                mSelectionArgs,
                null
        );
    }
	
	@Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Put the result Cursor in the adapter for the ListView
        mCursorAdapter.swapCursor(cursor);
    }
	
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor
        mCursorAdapter.swapCursor(null);

    }

}
