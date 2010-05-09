# xmlrpcserver, a simple and complete XML-RPC server module for python
# Copyright (C) 2004-2005  Julien Oster <julien-xmlrpc@julien-oster.de>

# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.

# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.

# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the
# Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

#
#
# xmlrpcserver is a small and simple but fairly complete XML-RPC server
# module for Python written by Julien Oster, implemented on top of the
# standard module xmlrpclib.

# See http://www.julien-oster.de/projects/xmlrpcserver/ for documentation,
# download and additional information.


"""
xmlrpcserver - a simple and complete XML-RPC server module for python
Copyright 2004-2005 Julien Oster <julien-xmlrpc@julien-oster.de>

xmlrpcserver is a small and simple but fairly complete XML-RPC server
module for Python written by Julien Oster, implemented on top of the standard
module xmlrpclib.

See http://www.julien-oster.de/projects/xmlrpcserver/ for documentation,
download and additional information.
"""



import sys
import xmlrpclib

from StringIO import StringIO
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler

__version__ = '0.99.2'
__revision__ = '0.99.2'
__author__ = 'Julien Oster <julien-xmlrpc@julien-oster.de>'
__copyright__ = 'Copyright 2004-2005 Julien Oster'

class XmlRpcServer:
    """implements a generic XML-RPC Server"""
    
    systemerrors = {
        -32000: "I could not understand your request? :(",
        -32500: "oops, that's a crash :(",
        -32600: "that request looks wrong to me :(",
        -32601: "that method doesn't even exist.",
        -32602: "you called wrong!" }

    capabilities = {
        'xmlrpc': ['http://www.xmlrpc.com/spec', 1],
        'faults_interop':
        ['http://xmlrpc-epi.sourceforge.net/specs/rfc.fault_codes.php',
         20010516] }

    def __init__ (self, rpcmethods={}, rpcclasses={}):
        self.systemerrors = XmlRpcServer.systemerrors
        self.capabilities = XmlRpcServer.capabilities
        
        self.rpcmethods = rpcmethods
        self.rpcclasses = rpcclasses

        self.register('system.getCapabilities', self.system_getCapabilities)
        self.register('system.listMethods', self.system_listMethods)
        self.register('system.methodHelp', self.system_methodHelp)

    def register (self, methodname, method):
        """registers a method with full method name"""
        
        self.rpcmethods[methodname] = method

    def unregister (self, methodname):
        """unregisters a method using the full method name"""
        
        if methodname in self.rpcmethods:
            del self.rpcmethods[methodname]
            return True
        else:
            return False

    def register_class (self, classname, classorinstance):
        """registers a class"""
        
        self.rpcclasses[classname] = classorinstance

    def unregister_class (self, classname):
        """unregisters a class"""
        
        if classname in self.rpcclasses:
            del self.rpcclasses[classname]
            return True
        else:
            return False

    def resolve (self, rpccall):
        rpcclass = rpcmethod = None
        try:
            rpcclass, rpcmethod = rpccall.split('.')
        except:
            pass

        if rpccall in self.rpcmethods:
            return self.rpcmethods[rpccall]
        elif rpcclass in self.rpcclasses and rpcmethod[0] != '_':
            try:
                return getattr(self.rpcclasses[rpcclass],
                                 rpcmethod)
            except AttributeError:
                raise xmlrpclib.Fault(-32601, self.systemerrors[-32601])
        else:
            raise xmlrpclib.Fault(-32601, self.systemerrors[-32601])

    def execute (self, request=sys.stdin, response=sys.stdout,
                        meta=None):
        """processes a request and dispatches the call"""
        
        if not meta:
            meta = {}

        meta['xmlrpcserver'] = self

        params = method = result = None

        try:
            params, rpccall = xmlrpclib.loads(request.read())
        except:
            response.write(
                xmlrpclib.dumps(xmlrpclib.Fault
                                (-32000, self.systemerrors[-32000]),
                                methodresponse = True))
            return

        try:

            result = self.resolve(rpccall)(meta, *params)

            response.write(
                xmlrpclib.dumps((result,), methodresponse = True,
                                allow_none=True))
        except xmlrpclib.Fault, fault:
            response.write(
                xmlrpclib.dumps(fault, methodresponse = True))
        except:
            response.write(
                xmlrpclib.dumps(xmlrpclib.Fault
                                (-32500, self.systemerrors[-32500]),
                                methodresponse = True))
            raise

        return (rpccall, result, params)

    def system_getCapabilities (self, meta, *params):
        """internal method system.getCapabilities()"""
        
        if len(params) != 0:
            raise xmlrpclib.Fault(-32602, self.systemerrors[-32602])

        result = {}

        for capkey in self.capabilities.keys():
            cap = self.capabilities[capkey]
            result[capkey] = {'specUrl': cap[0],
                              'specVersion': cap[1]}

        return result

    def system_listMethods (self, meta, *params):
        """internal method returning a list of registered methods"""

        if len(params) != 0:
            raise xmlrpclib.Fault(-32602, self.systemerrors[-32602])

        methods = self.rpcmethods.keys()
        
        [[methods.append(cl + '.' + method)
          for method in dir(self.rpcclasses[cl]) if method[0] != '_']
         for cl in self.rpcclasses.keys()]

        return methods

    def system_methodHelp (self, meta, methodname):
        """internal method returning the docstring for a given method"""

        doc = self.resolve(methodname).__doc__

        if doc:
            return doc
        else:
            return ''

class XmlRpcHTTPHandler (BaseHTTPRequestHandler):
    """HTTP Handler for XmlRpcHTTPServer"""
    
    server_version = 'XmlRpcServer/1.0'
    protocol_version = 'HTTP/1.1'

    def do_POST(self):
        meta = {
            'client_address': self.client_address,
            'path': self.path,
            'request_version': self.request_version,
            'headers': self.headers }

        request = StringIO(
            self.rfile.read(int(self.headers.getheader('Content-length'))))

        request.seek(0)

        response = StringIO()

        try:
            self.server.execute(request, response, meta)
        finally:
            response.seek(0)
            rstr = response.read()

            self.send_response(200)
            self.send_header('Content-type', 'text/xml')
            self.send_header('Content-length', len(rstr))
            self.end_headers()

            self.wfile.write(rstr)
    
class XmlRpcHTTPServer (HTTPServer, XmlRpcServer):
    def __init__ (self, server_address, rpcmethod={}, rpcclasses={}):
        HTTPServer.__init__(self, server_address, XmlRpcHTTPHandler)
        XmlRpcServer.__init__(self, rpcmethod, rpcclasses)
        
if __name__ == '__main__':
    class testc:
        def testm(self, meta, *args):
            """docstring"""
            return str(meta)+' '+str(args)

        def testfault(self, meta):
            raise xmlrpclib.Fault(42, "don't panic.")

        def testexception(self, meta):
            raise ValueError
    
    def testm (meta, *args):
        return str(meta)+' '+str(args)

    server = XmlRpcHTTPServer(('localhost', 8181))

    c = testc()
    server.register_class('testc', c)
    server.register('testm', testm)

    server.serve_forever()

