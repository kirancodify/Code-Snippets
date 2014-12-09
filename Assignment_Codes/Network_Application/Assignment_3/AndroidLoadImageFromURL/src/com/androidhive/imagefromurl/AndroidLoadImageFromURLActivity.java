/** This Android program is part of the application development framework assignment 3.
 * The soruce code for this exercise is obtained and reused from the online page 
 * http://www.androidhive.info/2012/07/android-loading-image-from-url-http
 */

package com.androidhive.imagefromurl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class AndroidLoadImageFromURLActivity extends Activity {
	
	TextView textMsg, textPrompt;
	final String textSource = "https://playground.cs.hut.fi/t-110.5140/hello2.txt";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textPrompt = (TextView)findViewById(R.id.textprompt);
        textMsg = (TextView)findViewById(R.id.textmsg);
       
        textPrompt.setText("Wait...");
       
        URL textUrl;
   try {
    textUrl = new URL(textSource);
    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(textUrl.openStream()));
    String StringBuffer;
          String stringText = "";
    while ((StringBuffer = bufferReader.readLine()) != null) {
     stringText += StringBuffer;
    }
          bufferReader.close();
          textMsg.setText(stringText);
   } catch (MalformedURLException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    textMsg.setText(e.toString());
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    textMsg.setText(e.toString());
   }
   
   textPrompt.setText("Finished!");

    
        
        // Loader image - will be shown before loading image
        int loader = R.drawable.loader;
        
        // Imageview to show
        ImageView image = (ImageView) findViewById(R.id.image);
        
        // Image url
        String image_url = "https://playground.cs.hut.fi/t-110.5140/hello.jpg";
        
        // ImageLoader class instance
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
        
        // whenever you want to load an image from url
        // call DisplayImage function
        // url - image url to load
        // loader - loader image, will be displayed before getting image
        // image - ImageView 
        imgLoader.DisplayImage(image_url, loader, image);
    }
}