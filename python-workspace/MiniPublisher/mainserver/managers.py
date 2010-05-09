from annotations import transactional
import re

from google.appengine.ext import db
from google.appengine.ext.webapp import template

from models import UserModel
import initial

class SignManager:
    """This manager will be reused by both handler and xmlrpc, so meta 'argument' in every method is added for xmlrpc.
    """
    
    def signIn(self, meta, username, password):
        """Validate the user name and password.
        Return a errorMessages dictionary including keys below:
        missingUsername, missingPassword, wrongPassword, notExistUser
        Return {} if sign in successfully.
        """
        username = username.strip()
        ip = initial.threadlocal.ip
        
        errorMessages = {}
        if not username:
            errorMessages['missingUsername'] = 'Please input your user name.'
        if not password:
            errorMessages['missingPassword'] = 'Please input your password.'
        
        if not errorMessages:
            userModels = db.GqlQuery('select * from UserModel where username = :1', username)
            userModel = userModels.get() # Get first result from the list.
            
            @transactional
            def checkOrUpdateUserModel(userModel, password):
                if userModel:
                    if password != userModel.password:
                        errorMessages['wrongPassword'] = 'The password is wrong.'
                    else:
                        # save the new login ip
                        userModel.ip = ip
                        userModel.put()
                else:
                    errorMessages['notExistUser'] = 'The user name does not exist.'
                    
            checkOrUpdateUserModel(userModel, password)
        
        return errorMessages
    
    def signUp(self, meta, username, email, password, repassword):
        """Validate the user name and password.
        Return a errorMessages dictionary including keys below:
        missingUsername, missingEmail, missingPassword, missingRepassword, notSamePassword, wrongEmailAddressFormat, occupiedUsername
        Return {} if sign in successfully.
        """
        
        username = username.strip()
        email = email.strip()
        ip = initial.threadlocal.ip
        
        errorMessages = {}
        if not username:
            errorMessages['missingUsername'] = 'Please input your user name.'
        if not email:
            errorMessages['missingEmail'] = 'Please input your email.'
        if not password:
            errorMessages['missingPassword'] = 'Please input your password.'
        if not repassword:
            errorMessages['missingRepassword'] = 'Please input your repassword.'
            
        if not errorMessages:
            if password != repassword:
                errorMessages['notSamePassword'] = 'The two passwords are not same.'
            if not re.search('[\w\W]+@[\w\W]+[.][\w\W]', email):
                errorMessages['wrongEmailAddressFormat'] = 'You email address should contain at least one . and @.'
            
        if not errorMessages:
            
            @transactional
            def checkOrSaveUserModel(userModel):
                # Use key_name attribute to get and create Model, otherwise, it maybe create two same Models.
                userModel = UserModel.get_by_key_name(username)
                if userModel:
                    errorMessages['occupiedUsername'] = 'The user named %s has been occupied.' % username
                else:
                    # Save new user with key_name attribute to make sure only one model will be inserted concurrently.
                    # import time
                    # time.sleep(5)
                    userModel = UserModel(key_name = username, username = username, email = email, password = password, ip = ip)
                    userModel.put()
                    
            # set None here to tell transactional annotation not to work here.
            checkOrSaveUserModel(None)
        
        return errorMessages
    
    def deleteUser(self, meta, username):
        userModel = UserModel.gql('where username = :1', username).get()
        
        @transactional
        def deleteUserModel(userModel):
            if userModel:
                userModel.delete()
                
        deleteUserModel(userModel)
    
    def test(self, meta):
        userModels = UserModel.all()
        # userModel = UserModel.all().get()
        # userModels = UserModel.all().fetch(10)
        
        @transactional
        def testUserModel(userModel):
            pass
        
        testUserModel(userModels)