package com.example.contactrestapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Intents;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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


public class CreateContactActivity extends Activity{
	
	private String first;
	private String last;
	private String email;
	private String phoneNumber;
	private String line1;
	private String line2;
	private String city;
	private String country;
	private String zipCode;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        System.out.println("ENte");
        setContentView(R.layout.contact_create);
             }
		
    public void createContact(View button) {

		
		final EditText firstNameField = (EditText) findViewById(R.id.EditTextFirstName);
		first = firstNameField.getText().toString();
		
		final EditText lastNameField = (EditText) findViewById(R.id.EditTextLastName);
		last = lastNameField.getText().toString();
			
		final EditText emailField = (EditText) findViewById(R.id.EditTextEmail);
		email = emailField.getText().toString();
		
		final EditText phoneNumberField = (EditText) findViewById(R.id.EditTextPhoneNumber);
		phoneNumber = phoneNumberField.getText().toString();
		
		final EditText line1Field = (EditText) findViewById(R.id.EditTextLine1);
		line1 = line1Field.getText().toString();
		
		final EditText line2Field = (EditText) findViewById(R.id.EditTextLine2);
		line2 = line2Field.getText().toString();
		
		final EditText cityField = (EditText) findViewById(R.id.EditTextCity);
		city = cityField.getText().toString();
		
		final EditText countryField = (EditText) findViewById(R.id.EditTextCountry);
		country = countryField.getText().toString();
		
		final EditText zipcodeField = (EditText) findViewById(R.id.EditTextZipCode);
		zipCode = zipcodeField.getText().toString();

		Thread thread = new Thread(new Runnable(){
	    @Override
	    public void run() {
	        try {
	        	JSONObject contact = null;
				JSONObject name = null;
				JSONObject address = null;
				contact = new JSONObject();
				name = new JSONObject();
				name.put("first", first);
				name.put("last", last);
				address = new JSONObject();
				address.put("line1",line1);
				address.put("line2",line2);
				address.put("city",city);
				address.put("country",country);
				address.put("zipCode",zipCode);
				contact.put("name", name);
				contact.put("address", address);
				contact.put("email", email);
				contact.put("phoneNumber", phoneNumber);
    		
			
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext httpContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost("http://130.233.42.187:8080/contacts");
			    StringEntity se = new StringEntity(contact.toString());
			    httpPost.setEntity(se);
			    httpPost.setHeader("Accept", "application/json");
			    httpPost.setHeader("Content-type", "application/json");
			    HttpResponse response = httpClient.execute(httpPost, httpContext); //execute your request and parse response
			    HttpEntity entity = response.getEntity();
			    //String jsonString = EntityUtils.toString(entity); //if response in JSON format

			} catch (Exception e) {
			    e.printStackTrace();
			}
	    
    }
   });

   thread.start(); 
   Intent in = new Intent(this, MainActivity.class);
   startActivity(in);
			
    }	

}
