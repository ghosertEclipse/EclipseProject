import xmlrpclib
s = xmlrpclib.Server('http://localhost/xmlrpc')
print s.SignManager.signIn('ghosert', '1111')
