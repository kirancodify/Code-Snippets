var googleapis = require('googleapis');
var OAuth2Client = googleapis.auth.OAuth2;
var GoogleContacts = require('google-contacts').GoogleContacts;
var contacts, currentRequest, accessToken;
var GoogleToken = require('gapitoken');
var addContact = function(ev, contactId, next) {
var SERVICE_ACCOUNT_EMAIL = '1009521748687-sglm95945hss174tl3c1qivkh2h0r9uj@developer.gserviceaccount.com';
var SERVICE_ACCOUNT_KEY_FILE = './key.pem';
var jwt = new googleapis.auth.JWT( SERVICE_ACCOUNT_EMAIL, SERVICE_ACCOUNT_KEY_FILE, null, ['https://www.googleapis.com/auth/contacts']);


googleapis.discover('contacts', 'v3')
    .execute(function(err, client) {
      jwt.authorize(function(err, result) {
        client.contacts.insert({ contactId: contactId},
          { 
            name: {
              givenName: "First",
	      fullName: "Last"
            }
          }) 
          .withAuthClient(jwt)
          .execute(function(err, result) {
            if(err) next(err);
            else {
              console.log("Result: " + result);
              next(null);
            } 
          });
      });
    });
};

