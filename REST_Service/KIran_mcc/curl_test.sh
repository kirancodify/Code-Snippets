# copy-paste a command from here and execute one at a time

curl -i -X GET 'http://localhost:8080/contacts'
curl -i -X GET 'http://localhost:8080/contacts?fields=name.first&fields=phoneNumber'
curl -i -X GET 'http://localhost:8080/contacts?fields=name.first,phoneNumber,address.city'
curl -i -X GET 'http://localhost:8080/contacts?name=joh'
curl -i -X GET 'http://localhost:8080/contacts?name=o&address.city=hels'
curl -i -X GET 'http://localhost:8080/contacts?address=hel'
curl -i -X GET 'http://localhost:8080/contacts?phoneNumber=040'
curl -i -X GET 'http://localhost:8080/contacts?name=o'


curl -i -X GET 'http://localhost:8080/contacts/SOMEID'
curl -i -X GET 'http://localhost:8080/contacts/SOMEID?fields=name.last'


# no fields
curl -i -X POST 'http://localhost:8080/contacts'
# some fields in JSON
curl -i -X POST -d '{"name": {"first": "Adam"}, "email": "adam.smo@ex.com", "address": {"line1": "A street 3"}}' -H 'Content-Type: application/json' 'http://localhost:8080/contacts'
# POST with all fields
curl -i -X POST -d '{"name": {"first": "Olle", "last": "West"}, "email": "olle.w@ex.com", "phoneNumber": "0705432", "address": {"line1": "Gatan 3", "line2": "B 4", "city": "Stockholm", "zipCode": "08432", "country": "Sweden"}}' -H 'Content-Type: application/json' 'http://localhost:8080/contacts'

# URL-encoded POST
curl -i -X POST -d 'firstName=Paula&lastName=Tuuli&phoneNumber=020123&stAddress1=Erottaja+2&city=Helsinki&zipCode=020002&country=Finland&email=paula@post.fi' 'http://localhost:8080/contacts'
curl -i -X POST -d 'firstName=Ulla&phoneNumber=06764321&stAddress1=Bulevardi+8' 'http://localhost:8080/contacts'

# illegal URL
curl -i -X POST 'http://localhost:8080/contacts/SOMEID'

# PUT
curl -i -X PUT -d '{"name": {"first": "Kalle", "last": "Huitto"}, "email": "kh@fin.com", "phoneNumber": "0705432", "address": {"line1": "Blatan 3", "line2": "C 5", "city": "Stockholm", "zipCode": "08432", "country": "Sweden"}}' -H 'Content-Type: application/json' 'http://localhost:8080/contacts/SOMEID'

# modify only some fields
curl -i -X PUT -d '{"name": {"first": "Uuno"}, "email": "uuno@new.com", "address": {"line1": "Newstreet 3", "line2": "C 5"}}' -H 'Content-Type: application/json' 'http://localhost:8080/contacts/SOMEID'

curl -i -X PUT -d 'firstName=Ulla&phoneNumber=06764321&stAddress1=Bulevardi+8' 'http://localhost:8080/contacts/SOMEID'


# DELETE
curl -i -X DELETE 'http://localhost:8080/contacts/SOMEID'

# delete all
curl -i -X DELETE 'http://localhost:8080/contacts/'


