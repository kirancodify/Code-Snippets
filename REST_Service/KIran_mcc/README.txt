T-110.5121 Mobile Cloud Computing
MCC Assignment Phase 1: Address book REST API

Date: 19-10-2014 Version 1.0

mccgroup21:
Markku Riekkinen
Kiran Kumar

CONTENTS OF THIS FILE
---------------------
 * Introduction
 * Requirements
 * Installation
 * Usage Instruction
 * Architecture
 * REST API

INTRODUCTION
------------

The address book REST API is a simple backend service which
functions as an address book to add, retrieve, modify, store
and delete contacts. The application is developed using Node.js
and Mongo DB. The express.js framework has been used to build
the REST api. HTTP requests - GET, POST, PUT and DELETE - are handled 
by the backend service. The application can handle data requests
in JSON format as well as URL encoded format.

The GET request has been extended to implement a search feature.
A search can be made by ID, Name, Address, Phone Number, Email or 
modification time. The query string has to be passed in the HTTP 
request in the required format to get the search result.

We have multiple extra features in addition to all the required basic 
features. The search function (using GET queries) is more extensive than 
just searching for names, as noted previously. It is possible to modify 
existing contacts with PUT requests is addition to creating new contacts 
with POST requests. Finally, it is possible to search for entries that have 
been modified after a given date, as the database saves the last modification 
date for each contact.

REQUIREMENTS
------------

This module requires the following modules:
 * body-parser Version 1.9.0 or higher
 * express js Version 4.9.5 or higher
 * mongoose Version 3.8.17 or higher
 
INSTALLATION and CONFIGURATION
------------------------------

 * Download and install nodejs from http://nodejs.org/download/
 * Install the latest version of MongoDB from http://docs.mongodb.org/manual/installation/
 * Copy the package.json and app.js source files into a directory
 * Run 'npm install' to install all the dependencies
 * Start Mongodb daemon
 * Run the application using the 'node app.js' command

USAGE INSTRUCTIONS
------------------

Once the application is running and listening on a port,
HTTP requests can be sent to the specific port using a browser,
CURL or any third-party tools to simulate the REST request (e.g., POSTMAN). 
The default port used by the application is 8080.

ARCHITECTURE
------------

We used mongoose as the driver to access MongoDB. Mongoose uses schemas 
to model the data. Knowing the schema is important in order to use 
correct GET queries to search for contacts with the REST API. The schema is 
as follows:

var contactSchema = mongoose.Schema({
  name: {
    first: String,
    last: String
  },
  phoneNumber: String,
  address: {
    line1: String, // street address
    line2: String,
    city: String,
    zipCode: String,
    country: String
  },
  email: String,
  lastModded: { type: Date, default: Date.now }, // last modification time
  // _id primary key is added automatically
});

REST API
--------

This section describes the HTTP method and URL combinations that the 
server supports. 
Contacts in responses are in JSON format. PUT and POST requests may supply 
JSON or URL-encoded bodies. Responses use HTTP status codes according to 
the REST best practices.

Fetch contacts:
GET /contacts
GET /contacts/ID     (replace ID with a real id) 
e.g., GET /contacts/5443de60eb0c366b71349cb1

Search:
GET /contacts?name=john
GET /contacts?name.first=john&address.city=helsi
  (first name contains john and city contains helsi)

Search keys are written like they are in the database model schema 
(dot for nested fields). Search keys name and address are also supported, 
and they will search all the subfields. Name supports a special short-hand 
notation where you can search for first and last name (both must match) without 
writing name.first and name.last separately: GET /contacts?name=john+doe
Field lastModded is a date and uses UTC-0 time. The query must be given in 
format YYYY-MM-DD or YYYY-MM-DDThh:mm. The search will return contacts that 
have been modified after the given date.

Select only some fields to be returned:
GET /contacts?fields=name,address.line1

Add a new contact:
POST /contacts
Request body contains the values for model fields in either JSON or 
URL-encoded format. The request does not have to supply values for all fields. 
JSON uses the same structure as the database schema. 
URL-encoded body uses keys: firstName, lastName, phoneNumber, email, 
stAddress1, stAddress2, city, zipCode, country.

Modify an existing contact:
PUT /contacts/ID     (replace ID with a real id)
Request body uses the same format as POST. If the request does not 
supply a value for a particular field, that field will not be changed 
in the database.

Delete a contact:
DELETE /contact/ID   (replace ID with a real id)
Deletes the contact from the database, if it exists.

Delete all contacts:
DELETE /contacts
Deletes all contacts from the database.

MCC Assignment Phase 2: Address book web frontend using your backend

THe REST API developed in the phase 1 has been further developed to include a Webpage to 
access, create, edit and delete the contacts by any user.

The Webpage supports all the browsers. HTML shim and response.js has been included to support 
Internet Explorer 9. The webpage is built using Bootstrap. On click of search only the First name
and Last name are shown in the list.On click of the name, the details of the contacts are shown
the right hand side of the page. Add



