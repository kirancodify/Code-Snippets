#!/usr/bin/env python


# Importing SSL Module from Open SSL Python library.
# Importing Socket and system modules for the client function
import socket, sys
from OpenSSL import SSL

# Getting host and port from the command line arguments
host,port = sys.argv[1:]

# Defining a function to display the certificates. The X 509 Certificate 
# objects are being formed as a key value pair datatype in python
# based on the information from http://pyopenssl.sourceforge.net/pyOpenSSL.html/openssl-x509name.html
def displayCertificate(x509):
    """Display an X.509 certificate"""
    x509Name_Object = {'C': 'Country',
        'ST': 'State/Province',
        'L': 'Locality',
        'O': 'Organization',
        'OU': 'Organizational Unit',
        'CN': 'Common Name',
        'email': 'E-Mail'}

    for key, val in x509Name_Object.items():
        try:
            print "%s: %s" % (val, getattr(x509, key))
        except:
            pass

# Defining a function to verify the certificates obtained on the 
# Http get request. Open SSL library have inbuilt function to get
# Issuer and Subject details. Reference :http://pyopenssl.sourceforge.net/pyOpenSSL.html/openssl-x509.html
# Once the details are obtained, the certificate is printed by an
# another function defined above - Display Certificate
def verifyCert(connection, certificate, errnum, depth,  ok):

    subject = certificate.get_subject()
    issuer = certificate.get_issuer()
    

    print " -------------This Certificate is from:-------------"
    displayCertificate(subject)
    print "--------------This Certificate is Issued by:--------"
    displayCertificate(issuer)
    if not ok:
        print "----------Certificate cannot be Verified--------"
        return 0

    return 1 

# SSL module provides a class, ssl.SSLSocket, which is derived from the 
# socket.socket type, and provides a socket-like wrapper that also 
# encrypts and decrypts the data going over the socket with SSL  
# The code of this socket creation is referred from 
# http://stackoverflow.com/questions/13811173/python-ssl-socket-receiving-and-sending-from-both-server-and-client  
context = SSL.Context(SSL.SSLv23_METHOD)
context.load_verify_locations("/etc/ssl/certs/ca-certificates.crt")
context.set_verify(SSL.VERIFY_PEER | SSL.VERIFY_FAIL_IF_NO_PEER_CERT, verifyCert)

# Creating Client functions create socket,connect,receive and closing socket 
print "Creating Open SSL socket...\n",
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print "Connecting to socket...\n"
ssl = SSL.Connection(context, sock)
ssl.connect((host, int(port)))
print "Get method HTTP Request:\n"
ssl.sendall("GET / HTTP/1.1\r\n\r\n")
print "OK"
while True:
    try:
        print repr(ssl.recv(4096))
	break
    except SSL.ZeroReturnError:
        break
ssl.close()

