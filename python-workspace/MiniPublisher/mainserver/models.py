from google.appengine.ext import db

class UserModel(db.Model):
    username = db.StringProperty(required = True)
    password = db.StringProperty(required = True)
    # set this ip each time when the user sign in.
    ip = db.StringProperty(required = True)
    email = db.EmailProperty(required = True)
    createdTime = db.DateTimeProperty(required = True, auto_now_add = True)
    updatedTime = db.DateTimeProperty(required = True, auto_now = True, auto_now_add = True)
    