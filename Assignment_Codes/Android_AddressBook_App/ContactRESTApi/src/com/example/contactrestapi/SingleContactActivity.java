package com.example.contactrestapi;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Intents;
import android.view.View;
import android.widget.TextView;


public class SingleContactActivity  extends Activity {
	
	// Fields to store the details of a single contact.
	private String first;
	private String last;
	private String email;
	private String phoneNumber;
	private String line1;
	private String line2;
	private String city;
	private String country;
	private String zipCode;
	private String _id;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        first = in.getStringExtra(MainActivity.TAG_FIRST);
        last = in.getStringExtra(MainActivity.TAG_LAST);
        email = in.getStringExtra(MainActivity.TAG_EMAIL);
        phoneNumber = in.getStringExtra(MainActivity.TAG_PHONENUMBER);
        line1 = in.getStringExtra(MainActivity.TAG_LINE1);
        line2 = in.getStringExtra(MainActivity.TAG_LINE2);
        city = in.getStringExtra(MainActivity.TAG_CITY);
        country = in.getStringExtra(MainActivity.TAG_COUNTRY);
        zipCode = in.getStringExtra(MainActivity.TAG_ZIPCODE);
        
        
        // Displaying all values on the screen
        TextView lblfirst = (TextView) findViewById(R.id.first);
        TextView lbllast = (TextView) findViewById(R.id.last);
        TextView lblEmail = (TextView) findViewById(R.id.email_label);
        TextView lblphoneNumber = (TextView) findViewById(R.id.phoneNumber_label);
        TextView lblline1 = (TextView) findViewById(R.id.addressline1_label);
        TextView lblline2 = (TextView) findViewById(R.id.addressline2_label);
        TextView lblCity = (TextView) findViewById(R.id.city_label);
        TextView lblCountry = (TextView) findViewById(R.id.country_label);
        TextView lblzip = (TextView) findViewById(R.id.zipcode_label); 
        
        lblfirst.setText(first);
        lbllast.setText(last);
        lblline1.setText(line1);
        lblEmail.setText(email);
        lblphoneNumber.setText(phoneNumber);
        lblline1.setText(line1);
        lblline2.setText(line2);
        lblCity.setText(city);
        lblCountry.setText(country);
        lblzip.setText(zipCode);
    }
	
	public String getContactFullName() {
		String fullName = "";
		if (first != null) {
			fullName = first + " ";
		}
		if (last != null) {
			fullName += last;
		}
		return fullName;
	}
	
	public String getContactFullStreetAddress() {
		String fullStreet = "";
		if (line1 != null) {
			fullStreet = line1 + " ";
		}
		if (line2 != null) {
			fullStreet += line2;
		}
		return fullStreet;
	}
	
	public void addToPhoneContacts(View view) {
		// Create a new Intent to insert a contact into the phone address book, 
		// by using the android built-in contacts app to provide the UI for the insert action. 
		Intent intent = new Intent(Intents.Insert.ACTION);
		// Sets the MIME type to match the Contacts Provider
		intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
		
		/*
		 * Inserts new data into the Intent. This data is passed to the
		 * contacts app's Insert screen
		 */
		intent.putExtra(Intents.Insert.NAME, getContactFullName())
		
			  .putExtra(Intents.Insert.POSTAL, getContactFullStreetAddress())
			  .putExtra(Intents.Insert.POSTAL_TYPE, CommonDataKinds.StructuredPostal.STREET)
			  .putExtra(Intents.Insert.POSTAL, zipCode)
			  .putExtra(Intents.Insert.POSTAL_TYPE, CommonDataKinds.StructuredPostal.POSTCODE)
			  .putExtra(Intents.Insert.POSTAL, city)
			  .putExtra(Intents.Insert.POSTAL_TYPE, CommonDataKinds.StructuredPostal.CITY)
			  .putExtra(Intents.Insert.POSTAL, country)
			  .putExtra(Intents.Insert.POSTAL_TYPE, CommonDataKinds.StructuredPostal.COUNTRY)
			  
		      .putExtra(Intents.Insert.EMAIL, email)
		      .putExtra(Intents.Insert.EMAIL_TYPE, CommonDataKinds.Email.TYPE_WORK)
		      .putExtra(Intents.Insert.PHONE, phoneNumber)
		      .putExtra(Intents.Insert.PHONE_TYPE, CommonDataKinds.Phone.TYPE_WORK);
		// TODO test if all fields are really pre-filled in the Android contacts add screen, 
		// and that the contact is saved correctly in the phone contacts. 
		// It looks suspicious that POSTAL key is used several times; does it save all POSTAL fields or only one? 
		// (The API has only one POSTAL key so nothing we could do about it...)
		
		// This call opens a screen in the contacts app that allows users to enter a new contact.
		startActivity(intent);
	}
	
	public void deleteContact(View button){
		// New thread to run delete operation
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		        	Intent in = getIntent();   
		        	String delete_url = "http://130.233.42.187:8080/contacts/"+_id;
    
                    //Creating new http client request        
			 		HttpClient httpClient = new DefaultHttpClient();
					HttpContext httpContext = new BasicHttpContext();
					HttpDelete httpDelete = new HttpDelete(delete_url);

				    httpDelete.setHeader("Content-Type", "application/x-www-form-urlencoded");
				    HttpResponse response = httpClient.execute(httpDelete, httpContext); //execute your request and parse response		     
               
	               } catch (Exception e) {
					    e.printStackTrace();
					}
			   }
	   });

	   thread.start(); 
	   Intent intent = new Intent(this, MainActivity.class);
	   startActivity(intent);
	}
	
	
}
