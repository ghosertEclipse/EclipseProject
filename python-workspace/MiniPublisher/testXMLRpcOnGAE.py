import xmlrpclib
s = xmlrpclib.Server('http://minipublisher.appspot.com/xmlrpc')
print s.SignManager.signIn('test', 'lll')
