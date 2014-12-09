T-110.5121 Mobile Cloud Computing
MCC Assignment Phase 2: Address book web app

Date: 09-11-2014 Version 1.0

mccgroup21:
Markku Riekkinen
Kiran Kumar

INSTALLATION AND USAGE
----------------------

See phase 1 README.txt for installing Node.js and MongoDB. 
Install required node modules with 'npm install' (uses the package.json file). 
Run the application with 'node app.js'. This starts the server that serves 
the REST API calls and the static web content. 

Using the web app:
The main web page is served at URL /, e.g., 
http://localhost:8080 
Open it with a web browser. 
In the web page you should see the contacts list on the left and selected 
contact details on the right. Click an item in the contacts list to open it. 
The add contact button will open a dialogue where you can create a new contact, 
and in the contact details the edit button will allow you to modify the selected 
contact (the delete button similarly deletes the contact). The edit and delete 
buttons are approriately disabled if no contact is selected. The search field 
in the contacts list allows you to search for the given name in your contacts. 
The search results are displayed in the same contacts list, and they replace the 
previous content of the list. To see all contacts, search for empty value or 
refresh the whole web page. 

You may ignore the "Authenticate to Google Contacts" button entirely, as the 
Google Contacts synchronization does not work. 


ARCHITECTURE
------------

The web app backend is combined with the phase 1 REST server, which 
basically just means that the same server serves the web static content 
in addition to the phase 1 REST calls. The web app is implemented as a single 
web page that defines the structure of the page, and client-side Javascript code 
that fetches the contacts from the server with AJAX through the REST API. AJAX 
is similarly used to do all other operations as well (adding and editing contacts). 
The web app uses Bootstrap framework for the user interface and jQuery Javascript 
library for AJAX and dynamic DOM manipulation, such as inserting the fetched 
contacts into the web page. 

The web html and Javascript code are located in the www directory in the submission.


KNOWN PROBLEMS
--------------

The Google Contacts synchronization does not work. We tried to implement syncing 
to Google in our backend, but we were not able to get it working. In our app.js, 
you can see some commented out code in the POST method, which was supposed to 
create the new contact in Google Contacts after creating it in our database. 
We believe we have a working implementation on creating the Google OAuth2 tokens. 
In the web page, you can press the "authenticate to Google" button and it will 
take you to Google login. It will confirm that you want to share your contacts 
with our service and it also redirects you back to our service (in the cloud instance, 
since redirecting back to localhost address is not possible). 

We were not able to implement syncing from Google to our service either. 

We were, however, able to implement all the other requirements (other than 
Google syncing). The web UI looks good and works well. 

