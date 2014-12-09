var googleapis = require('googleapis');
var OAuth2Client = googleapis.auth.OAuth2;
var CLIENT_ID = '1009521748687-dauomp7lncb4eqtd323lmq7967couj6d.apps.googleusercontent.com';
var CLIENT_SECRET = 'dt7UtxedGpOryJUe5s5ngGVV';
var REDIRECT_URL =  'http://localhost:8080';

var oauth2Client = new OAuth2Client(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
var GoogleContacts = require('google-contacts').GoogleContacts;
var contacts, currentRequest, accessToken;

var dao = require('./dao');
var collectionName = 'contacts';
var request = require('request');
var contactXml = require('./contactXML');
var _ = require('underscore');

var url = oauth2Client.generateAuthUrl({
    access_type: 'offline', 
    scope: 'https://www.googleapis.com/auth/contacts'
});

exports.requestImportContacts = function(req, res) {
  currentRequest = 'import';
	res.redirect(url);	
};

exports.requestExportContacts = function(req, res) {
  currentRequest = 'export';
  res.redirect(url);
}

exports.oauthCallback = function(req, res) {
	setAccessToken(req.query.code, function() { 
    if(currentRequest === 'export') {
      exportContacts(res);
    }
    if(currentRequest === 'import') {
      importContacts(res); 
    }		
	});
};


function updateOrCreateContact(contact) {
	dao.findOne(collectionName, {email: contact.email}, function (existingContact) {    
    if(existingContact) {
      replaceExistingContact(existingContact, contact);
    } else {
      createNewContact(contact);
    }
  });
};

function replaceExistingContact(existingContact, newContact) {
	dao.update(collectionName, existingContact._id, newContact);
};

function createNewContact(newContact) {
	dao.create(collectionName, newContact);
};



function exportContacts(res) {
  contacts.getContacts(function (error, allGoogleContacts) {
    dao.findAll(collectionName, {}, function (allContacts) {
      var contactsToCreate = getContactsThatDontExistInGoogle(allContacts, allGoogleContacts);
      for(var i = 0; i < contactsToCreate.length; i++) {
        createContactInGoogleContacts(contactsToCreate[i]);
      }
    });
   });
};

function getContactsThatDontExistInGoogle(contacts, googleContacts) {
  return _.filter(contacts, function (contact) {
      var existingGoogleContact = _.find(googleContacts, function (googleContact) {
        return googleContact.email === contact.email;
      });
      return existingGoogleContact == undefined;
  });
}

function createContactInGoogleContacts(contact) {
  var postBody = contactXml.generateXML(contact);
  var requestOptions = {
    url: 'https://www.google.com/m8/feeds/contacts/default/full',
    body: postBody,
    headers: {
      'Authorization': 'OAuth ' + accessToken,
      'GData-Version': '3.0',
      'Content-Type': 'application/atom+xml'
    }
  };
  request.post(requestOptions);
};
