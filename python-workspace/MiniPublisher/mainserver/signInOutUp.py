from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app

from models import UserModel

import initial

class SignIn(webapp.RequestHandler):
    def post(self):
        username = self.request.get('username')
        password = self.request.get('password')
        
        # add ip into thread local, signManager need it.
        threadlocal = initial.threadlocal
        threadlocal.ip = self.request.remote_addr
        
        # Validate the user name and password.
        errorMessages = initial.signManager.signIn(None, username, password)
        
        # delete thread local
        del(threadlocal.ip)
        
        if errorMessages:
            template_values = {'errorMessages' : errorMessages.values()}
            self.response.out.write(template.render('./index.html', template_values))
        else:
            template_values = {'successMessage' : 'Sign in successfully.', 'username' : username}
            self.response.out.write(template.render('./index.html', template_values))

class MainPage(webapp.RequestHandler):
    def get(self):
        userModels = UserModel.all()
        self.response.out.write(template.render('./index.html', {'userModels': userModels}))

class SignUp(webapp.RequestHandler):
    def post(self):
        username = self.request.get('username')
        email = self.request.get('email')
        password = self.request.get('password')
        repassword = self.request.get('repassword')
        
        # add ip into thread local
        threadlocal = initial.threadlocal
        threadlocal.ip = self.request.remote_addr
        
        # Validate the user name, email, password and repassword, signManager need it.
        errorMessages = initial.signManager.signUp(None, username, email, password, repassword)
        
        # delete thread local
        del(threadlocal.ip)
        
        if errorMessages:
            template_values = {'errorMessages' : errorMessages.values()}
            self.response.out.write(template.render('./index.html', template_values))
        else:
            self.redirect('/')
    
class SignOut(webapp.RequestHandler):
    def get(self):
        pass

class DeleteUser(webapp.RequestHandler):
    def get(self):
        username = self.request.get('username')
        initial.signManager.deleteUser(None, username)
        self.redirect('/')

class Test(webapp.RequestHandler):
    def get(self):
        initial.signManager.test(None)
        self.redirect('/')

application = webapp.WSGIApplication([('/', MainPage), ('/signIn', SignIn), ('/signUp', SignUp),
                                      ('/signOut', SignOut), ('/deleteUser', DeleteUser), ('/test', Test)], debug = True)

def main():
    run_wsgi_app(application)
            
if __name__ == '__main__':
    main()
    