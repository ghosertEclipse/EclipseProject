from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
import logging
from StringIO import StringIO
import traceback

import initial

class XMLRpcHandler(webapp.RequestHandler):
    def post(self):
        # add ip into thread local
        threadlocal = initial.threadlocal
        threadlocal.ip = self.request.remote_addr
        
        request = StringIO(self.request.body)
        request.seek(0)
        response = StringIO()
        try:
            initial.rpcserver.execute(request, response, None)
        except Exception, e:
            logging.error('Error executing: ' + str(e))
            for line in traceback.format_exc().split('\n'):
                logging.error(line)
        finally:
            response.seek(0)
                
        # delete thread local
        del(threadlocal.ip)
        
        rstr = response.read()
        self.response.headers['Content-type'] = 'text/xml'
        self.response.headers['Content-length'] = "%d" % len(rstr)
        self.response.out.write(rstr)
        
application = webapp.WSGIApplication([('/xmlrpc', XMLRpcHandler)], debug=True)

def main():
    run_wsgi_app(application)
    
if __name__ == "__main__":
    main()
    