package com.example.contactrestapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	/* Key for intent extras to specify the name that is searched in the phone contacts. */
	public final static String EXTRA_SEARCH_NAME = "com.example.contactrestapi.SEARCH_NAME"; 
	
	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String url ="http://130.233.42.187:8080/contacts";

	// JSON Node names
	public static final String TAG_CONTACTS = "contacts";
	public static final String TAG_ID = "_id";
	public static final String TAG_NAME = "name";
	public static final String TAG_EMAIL = "email";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_FIRST = "first";
	public static final String TAG_PHONENUMBER = "phoneNumber";
	public static final String TAG_LAST = "last";
	public static final String TAG_LINE1 = "line1";
	public static final String TAG_LINE2 = "line2";
	public static final String TAG_CITY = "city";
	public static final String TAG_ZIPCODE = "zipCode";
	public static final String TAG_COUNTRY = "country";
	public static final String TAG_LASTMOD = "lastModded";
	// Hashmap for ListView
	ArrayList<HashMap<String, String>> contactList;
	
	// Event handler for the search phone contacts button
	public void addFromPhoneContacts(View view) {
		Intent intent = new Intent(this, PhoneContactsActivity.class);
		EditText editText = (EditText) findViewById(R.id.phone_contacts_search);
		String nameToSearch = editText.getText().toString();
		intent.putExtra(EXTRA_SEARCH_NAME, nameToSearch);
		startActivity(intent);
	}


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contactList = new ArrayList<HashMap<String, String>>();
		ListView lv = getListView();
		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String first = ((TextView) view.findViewById(R.id.first))
						.getText().toString();
				String last = ((TextView) view.findViewById(R.id.last))
						.getText().toString();
				String _id = ((TextView) view.findViewById(R.id._id))
						.getText().toString();
				String email = ((TextView) view.findViewById(R.id.email))
						.getText().toString();
				String phoneNumber = ((TextView) view.findViewById(R.id.phoneNumber))
						.getText().toString();
				String line1 = ((TextView) view.findViewById(R.id.line1))
						.getText().toString();	
				String line2 = ((TextView) view.findViewById(R.id.line2))
						.getText().toString();
				String city = ((TextView) view.findViewById(R.id.city))
						.getText().toString();				
				String country = ((TextView) view.findViewById(R.id.country))
						.getText().toString();
				String zipCode = ((TextView) view.findViewById(R.id.zipCode))
						.getText().toString();
				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						SingleContactActivity.class);
				in.putExtra(TAG_FIRST, first);
				in.putExtra(TAG_LAST, last);
				in.putExtra(TAG_ID, _id);
				in.putExtra(TAG_EMAIL, email);
				in.putExtra(TAG_PHONENUMBER, phoneNumber);
				in.putExtra(TAG_LINE1, line1);
				in.putExtra(TAG_LINE2, line2);
				in.putExtra(TAG_CITY, city);
				in.putExtra(TAG_COUNTRY, country);
				in.putExtra(TAG_ZIPCODE, zipCode);
				startActivity(in);

			}
		});

		// Calling async task to get json
		new GetContacts().execute();
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
			if (jsonStr != null) {
				try {
					JSONArray jsonArray = new JSONArray(jsonStr);
					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						String first = "";
						String last = "";
						String phoneNumber = "";
						String email = "";
						String _id = "";
						String line1 = "";
						String line2 = "";
						String city = "";
						String country = "";
						String zipCode = "";
						JSONObject c = jsonArray.getJSONObject(i);
						if(c.has(TAG_ID)){
						_id = c.getString(TAG_ID);
						}
						if (!TAG_PHONENUMBER.equals(null)){
						phoneNumber = c.getString(TAG_PHONENUMBER);
						}
						if(c.has(TAG_EMAIL)){
						email = c.getString(TAG_EMAIL);
						}
						JSONObject name = c.getJSONObject(TAG_NAME);
						first = name.getString(TAG_FIRST);
						last = name.getString(TAG_LAST);
						if(c.has(TAG_ADDRESS)){
						JSONObject address = c.getJSONObject(TAG_ADDRESS);
						if(address.has(TAG_LINE1)){
						line1 = address.getString(TAG_LINE1);
						}
						if(address.has(TAG_LINE2)){
						line2 = address.getString(TAG_LINE2);
						}
						if(address.has(TAG_CITY)){
						city = address.getString(TAG_CITY);
						}
						if(address.has(TAG_COUNTRY)){
						country = address.getString(TAG_COUNTRY);
						}
						if(address.has(TAG_ZIPCODE)){
						zipCode = address.getString(TAG_ZIPCODE);
						}
						}
						HashMap<String, String> contact = new HashMap<String, String>();
						contact.put(TAG_ID, _id);
						contact.put(TAG_FIRST, first);
						contact.put(TAG_LAST, last);
						contact.put(TAG_EMAIL, email.toString());
						contact.put(TAG_PHONENUMBER, phoneNumber);
						contact.put(TAG_LINE1, line1);
						contact.put(TAG_LINE2, line2);
						contact.put(TAG_CITY, city);
						contact.put(TAG_COUNTRY,country );
						contact.put(TAG_ZIPCODE, zipCode);
						contactList.add(contact);
						System.out.println(contactList);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, contactList,
					R.layout.list_item, new String[] { TAG_ID,TAG_FIRST, TAG_LAST, TAG_EMAIL,TAG_PHONENUMBER,TAG_LINE1,
							TAG_LINE2,TAG_CITY,TAG_COUNTRY,TAG_ZIPCODE}, 
							new int[] { R.id._id,R.id.first,R.id.last,R.id.email, R.id.phoneNumber, R.id.line1,R.id.line2
							,R.id.city,R.id.country,R.id.zipCode});
			setListAdapter(adapter);
		}

	}
	public void myClickMethod(View view){

		Intent in = new Intent(this, CreateContactActivity.class);
		startActivity(in);
	
	}
	

}