# encoding: utf-8

from PyQt4.QtGui import *
from PyQt4.QtCore import *

import sys

class EButton(QWidget):
    def __init__(self, parent = None):
        QWidget.__init__(self, parent)
        
        # This line should be co-operate with the def minimumSizeHint(self) method below,
        # without minimumSizeHint method will cause the drawn lines disappear.
        # width should be grown from minimum and height should be fixed.
        self.setSizePolicy(QSizePolicy(QSizePolicy.MinimumExpanding, QSizePolicy.Fixed))
        
        # Receive the mouse move event even no mouse buttons are pressed.
        self.setMouseTracking(True)
        # TODO: is this wrong? self.setWindowFlags(self.windowFlags() | Qt.FramelessWindowHint)
        # self.setWindowFlags(Qt.FramelessWindowHint)
        
        # Make all the window transparent while you can find new api in 4.5 to make certain widget transparent.
        self.setWindowOpacity(0.6)
        
    def paintEvent(self, event = None):
        painter = QPainter(self)
        # Without the clause below will cause some line disappear.
        painter.setRenderHint(QPainter.Antialiasing)
        painter.setRenderHint(QPainter.TextAntialiasing)
        
        brush = QBrush(Qt.SolidPattern) # The default one is Qt.NoBrush
        brush.setColor(QColor(0, 0, 0))
        pen = QPen(Qt.SolidLine) # The default one is Qt.SolidLine
        painter.setBrush(brush)
        painter.setPen(pen)
        # painter.drawRect(self.rect())
        painter.drawRoundRect(self.rect(), 50, 50)
        painter.setOpacity(1)
        painter.setPen(QColor(255, 255, 255, 255))
        painter.drawText(20, 20, 'Jiawei')
        
    def sizeHint(self):
        return self.minimumSizeHint()

    def minimumSizeHint(self):
        return QSize(100, 100)
    
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
    
    def resizeEvent(self, event):
        # Set the shape of the window.
        maskedRegion = QRegion(0, 0, self.width(), self.height(), QRegion.Rectangle)
        self.setMask(maskedRegion)

class MainWindow(QDialog):
    def __init__(self, parent = None):
        QDialog.__init__(self, parent)
        layout = QGridLayout()
        button = EButton(self)
        button.setToolTip(u'Text String')
        layout.addWidget(button)
        self.setLayout(layout)
        self.resize(400, 300)

app = QApplication(sys.argv)

# main = MainWindow()
main = EButton()
main.show()
main.setWindowTitle(u'中国')


app.exec_()
