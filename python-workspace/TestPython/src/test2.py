# encoding: utf-8

import res.qrc_resources

from PyQt4.QtGui import *
from PyQt4.QtCore import *

import sys

class ELabel(QLabel):
    def __init__(self, parent = None):
        QLabel.__init__(self, parent)
        self.setMouseTracking(True)
    
    def mousePressEvent(self, event):
        if event.button() == Qt.LeftButton:
            self.dragPosition = event.globalPos() - self.frameGeometry().topLeft()
            event.accept()
        else:
            QWidget.mousePressEvent(self, event)

    def mouseMoveEvent(self, event):
        if event.buttons() == Qt.LeftButton:
            self.move(event.globalPos() - self.dragPosition)
            event.accept()
        else:
            QWidget.mouseMoveEvent(self, event)

app = QApplication(sys.argv)

topLevelLabel = ELabel() 
pixmap = QPixmap(":/cal.png")
topLevelLabel.setPixmap(pixmap);
topLevelLabel.setMask(pixmap.mask()); 

topLevelLabel.show()

app.exec_()
