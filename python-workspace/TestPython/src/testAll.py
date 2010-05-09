# coding: utf-8

import sys

from PyQt4.QtGui import *
from PyQt4.QtWebKit import *

app = QApplication(sys.argv)

main = QMainWindow()
main.show()

web = QWebElement()
web.addClass('jiawei')

QMainWindow().show()

app.exec_()
