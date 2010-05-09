import os
import sys

from PyQt4.QtGui import *
from PyQt4.QtCore import *

DIR_PATH = 'G:\google_appengine'
SCRIPT_DIR = os.path.join(DIR_PATH, 'google', 'appengine', 'tools')

EXTRA_PATHS = [
  DIR_PATH,
  os.path.join(DIR_PATH, 'lib', 'antlr3'),
  os.path.join(DIR_PATH, 'lib', 'django'),
  os.path.join(DIR_PATH, 'lib', 'webob'),
  os.path.join(DIR_PATH, 'lib', 'yaml', 'lib'),
]

def run_file(file_path, globals_, script_dir=SCRIPT_DIR):
    """Execute the file at the specified path with the passed-in globals."""
    sys.path = EXTRA_PATHS + sys.path
    script_path = os.path.join(script_dir, "dev_appserver_main.py")
    execfile(script_path, globals_)

class MainWindow(QDialog):
    def __init__(self, parent = None):
        QDialog.__init__(self, parent)
        lbUserName = QLabel()
        lbUserName.setText('username:')
        self.leUserName = QLineEdit()
        lbPassword = QLabel()
        lbPassword.setText('password:')
        self.lePassword = QLineEdit()
        self.lePassword.setEchoMode(QLineEdit.Password)
        gridLayout = QGridLayout()
        btConfirm = QPushButton()
        btConfirm.setText('Connect To Server')
        self.connect(btConfirm, SIGNAL('clicked()'), self.connectToServer)
        gridLayout.addWidget(lbUserName)
        gridLayout.addWidget(self.leUserName)
        gridLayout.addWidget(lbPassword)
        gridLayout.addWidget(self.lePassword)
        gridLayout.addWidget(btConfirm)
        self.setLayout(gridLayout)
    
    def connectToServer(self):
        import xmlrpclib
        s = xmlrpclib.Server('http://localhost/xmlrpc')
        s.SignManager.signIn(unicode(self.leUserName.text()), unicode(self.lePassword.text()))

if __name__ == '__main__':
    
    # Set argv for Goole App Engine here
    argsList = ['-d', '-p', '8080', '-a', '0.0.0.0', './']
    sys.argv.extend(argsList)
    
    # Start Google App Engine
    import threading
    class MiniServerThread(threading.Thread):
        def run(self):
            run_file(__file__, globals())
    miniServerThread = MiniServerThread()
    miniServerThread.setDaemon(True)
    miniServerThread.start()

    import time
    time.sleep(5)
  
    # Start Qt App.
    app = QApplication(sys.argv)
    
    main = MainWindow()
    main.show()
    
    app.exec_()
    
