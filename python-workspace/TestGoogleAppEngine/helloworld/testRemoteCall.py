#!/usr/bin/python
import code
import getpass
import sys

sys.path.append("g:/google_appengine")
sys.path.append("g:/google_appengine/lib/webob")
sys.path.append("g:/google_appengine/lib/yaml/lib")

from google.appengine.ext.remote_api import remote_api_stub
from google.appengine.ext import db

def getUserPass():
    return ('ghosert@gmail.com', '011849')

if len(sys.argv) < 2:
    print "Usage: %s app_id [host]" % (sys.argv[0],)
app_id = sys.argv[1]
if len(sys.argv) > 2:
    host = sys.argv[2]
else:
    host = '%s.appspot.com' % app_id
    
remote_api_stub.ConfigureRemoteDatastore(app_id, '/remote_api', getUserPass, host)

import helloworld

greetings = helloworld.testRemoteCall()
for greeting in greetings:
    print greeting.author, greeting.ip, greeting.content, greeting.date
    
greetings = helloworld.testRemoteCall()
for greeting in greetings:
    print greeting.author, greeting.ip, greeting.content, greeting.date
