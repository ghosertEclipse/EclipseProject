# Put all the global one time initialized instances in this file.

import threading
from managers import SignManager
from libs.xmlrpcserver import XmlRpcServer

signManager = SignManager()

threadlocal = threading.local()

# Add remote object instance here.
registerRemotedClasses = [signManager]

# Register the remote object instance.
rpcserver = XmlRpcServer()
for instance in registerRemotedClasses:
    rpcserver.register_class(instance.__class__.__name__, instance)