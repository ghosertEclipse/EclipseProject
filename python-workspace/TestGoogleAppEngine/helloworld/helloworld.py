import os
import cgi
import logging

from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext import db

class Greeting(db.Model):
    author = db.UserProperty()
    content = db.StringProperty(multiline=True)
    date = db.DateTimeProperty(auto_now_add=True)
    ip = db.StringProperty(multiline=False)
    
class MainPage(webapp.RequestHandler):
    def get(self):
        # There are two ways to access database: 1. iterator over query: for item in Greeting.all() 2. fetch method: Greeting.all().fetch(10)
        #
        # iterator over query: every 20 items will make a new batch database call, avoid this, especially in a remote call. (caution to use this way.)
        #
        # Compared above, setting larger size on fetch method will obtain the better performance if you know the size. (for known size and <=1000 limitation)
        #
        # If you don't know the size, use the way below, additionally, it avoid the 1000 limitation as well. (for unknown size or 1000 limitation)
        #
        # 1000 limitation: Whatever the fetch method and iterator, single query results were limited to 1000 items.
        greetings_query = Greeting.all()
        greetings = greetings_query.fetch(1000)
        total_greetings = []
        while greetings:
            for greeting in greetings:
                total_greetings.append(greeting)
            greetings = Greeting.all().filter('__key__ >', greetings[-1].key()).fetch(1000)
        
        if users.get_current_user():
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'
        
        template_values = {
            'greetings': total_greetings,
            'url': url,
            'url_linktext': url_linktext,
            }
        
        path = os.path.join(os.path.dirname(__file__), 'index.html')
        self.response.out.write(template.render(path, template_values))
        
class Guestbook(webapp.RequestHandler):
    def post(self):
        
        # Add transaction action.
        def save():
            greeting = Greeting()
            
            if users.get_current_user():
                greeting.author = users.get_current_user()
                
            greeting.content = self.request.get('content')
            greeting.ip = self.request.remote_addr
        
            greeting.put()
            # raise Exception('unknown exception')
            return 'Return something.'
        
        result = db.run_in_transaction(save)
        logging.debug(result)
        
        self.redirect('/')

class Accumulator(db.Model):
    counter = db.IntegerProperty() 
    versionId = db.IntegerProperty()
    
class OptimisticLockingError(Exception):
    pass

class TestOptimisticLocking(webapp.RequestHandler):
    def post(self):
        def increment_counter(key, amount, versionId): 
            obj = db.get(key) 
            if versionId != obj.versionId:
                raise db.Rollback
            obj.versionId = obj.versionId + 1
            obj.counter += amount
            logging.debug("prepared to save counter: %d, versionId %d." % (obj.counter, obj.versionId))
            # import time
            # time.sleep(5)
            obj.put() 
            logging.debug("post to save counter: %d, versionId %d." % (obj.counter, obj.versionId))
            return obj.counter, obj.versionId
     
        key_name = self.request.get('key_name')
        versionId = self.request.get('versionId')
        increment = self.request.get('increment') 
        if not key_name:
            q = db.GqlQuery("SELECT * FROM Accumulator") 
            acc = q.get() 
            if not acc:
                acc = Accumulator(counter = 0, versionId = 0)
                acc.put()
            key_name = str(acc.key())
            versionId = str(acc.versionId)
        
        key = db.Key(key_name)
     
        result = db.run_in_transaction(increment_counter, key, int(increment), int(versionId))
        # result = db.run_in_transaction_custom_retries(0, increment_counter, key, int(increment), int(versionId))
        if not result: # return None if db.Rollback exception is raised.
            raise OptimisticLockingError('self defined version id concurrent error happens.')
        else:
            counter, versionId = result
        
        path = os.path.join(os.path.dirname(__file__), 'index.html')
        self.response.out.write(template.render(path, {'counter' : counter, 'key_name' : key_name, 'versionId' : versionId}))
        
    def handle_exception(self, exception, debug_mode):
        # Uncomment the line below, because it seems IE8 will ignore the response value if 500 happens.
        # self.error(500)
        logging.exception(exception)
        if debug_mode:
            import traceback
            import sys
            lines = ''.join(traceback.format_exception(*sys.exc_info()))
            self.response.clear()
            self.response.out.write('<pre>%s</pre>' % (cgi.escape(lines, quote=True)))
    
def testRemoteCall():
    greetings = Greeting.all().order('-date').fetch(10)
    # greetings = db.GqlQuery("SELECT * FROM Greeting ORDER BY date DESC LIMIT 10")
    return greetings
    
application = webapp.WSGIApplication([('/', MainPage), ('/sign', Guestbook), ('/increment', TestOptimisticLocking)], debug=True)

icount = 1

def main():
    """Define a main() method here is a standard google webapp way to cache the handlers."""
    global icount
    icount = icount + 1
    logging.debug('===========Because of main function defined here, global variable application & icount are cached=================')
    logging.debug('===========But it seems classes above are not cached, so do not define any instance variable in the classes above=================')
    logging.debug('===========You can import a global py file instead, all the variables in import file are cached=================')
    run_wsgi_app(application)
    
if __name__ == '__main__':
    main()
