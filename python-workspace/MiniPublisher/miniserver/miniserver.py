from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app

class Test(webapp.RequestHandler):
    def get(self):
        # import time
        # time.sleep(5)
        self.response.out.write(template.render('./index.html', {'name' : 'jiaweizhang'}))

application = webapp.WSGIApplication([('/', Test)], debug = True)

def main():
    run_wsgi_app(application)
            
if __name__ == '__main__':
    main()