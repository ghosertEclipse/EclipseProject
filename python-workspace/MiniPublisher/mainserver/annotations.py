from google.appengine.ext import db

def transactional(function):
    """The function should contain the at least one db.Model or db.Query or None class as the first parameter.
    None means tell this annotation not to work.
    """
    def new(keyModel, *args):
        key = None
        if keyModel:
            if isinstance(keyModel, db.Model):
                key = keyModel.key()
            elif isinstance(keyModel, list) or isinstance(keyModel, db.Query):
                key = []
                for model in keyModel:
                    key.append(model.key())
            elif isinstance(keyModel, unicode):
                key = db.Key(keyModel)
        def retryfunction():
            model = db.get(key) if key else None
            function(model, *args)
        return db.run_in_transaction(retryfunction) # retry 3 times if optimistic locking collision happens.
    return new