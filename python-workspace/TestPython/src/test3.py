# encoding: utf-8

from PyQt4.QtGui import *
from PyQt4.QtCore import *

import sys

class CustomItem(QWidget):
    def __init__(self, parent = None):
        QWidget.__init__(self, parent)
        self.setSizePolicy(QSizePolicy(QSizePolicy.MinimumExpanding, QSizePolicy.MinimumExpanding))
        self.setMouseTracking(True)
        self.isMoveIn = False
    
    def paintEvent(self, event = None):
        if not self.isMoveIn:
            painter = QPainter(self)
            painter.save()
            painter.setRenderHint(QPainter.Antialiasing)
            brush = QBrush()
            painter.setBrush(brush)
            painter.setPen(QColor(140, 141, 142))
            painter.pen().setWidth(2)
            
            # painter.drawRect(0, 0, self.width(), self.height())
            painter.drawRoundRect(0, 0, self.width(), self.height(), 10, 10)
            painter.restore()
            font = QFont()
            font.setBold(True)
            font.setPointSize(9)
            painter.setFont(font)
            painter.setPen(QColor(74, 81, 96))
            painter.drawText(20, 20, QString('Test Text'))
        else:
            painter = QPainter(self)
            painter.setRenderHint(QPainter.Antialiasing)
            linearGradient = QLinearGradient(0, 0, 0, self.height())
            linearGradient.setColorAt(0, QColor(210, 215, 221))
            linearGradient.setColorAt(0.2, QColor(208, 213, 212))
            linearGradient.setColorAt(0.21, QColor(186, 192, 202))
            linearGradient.setColorAt(1, QColor(216, 222, 225))
            brush = QBrush(linearGradient)
            painter.setBrush(brush)
            painter.setPen(QColor(140, 141, 142))
            painter.pen().setWidth(2)
            painter.drawRoundRect(0, 0, self.width(), self.height(), 10, 10)
        
    def sizeHint(self):
        return self.minimumSizeHint()

    def minimumSizeHint(self):
        return QSize(100, 100)
    
    # def mousePressEvent(self, event):
        # QWidget.mousePressEvent(self, event)

    def enterEvent(self, event):
        self.isMoveIn = True
        self.update()
    
    def leaveEvent(self, event):
        self.isMoveIn = False
        self.update()
            
class CustomBar(QWidget):
    def __init__(self, parent = None):
        QWidget.__init__(self, parent)
        self.resize(100, 100)
    
    def paintEvent(self, event = None):
        painter = QPainter(self)
        painter.setRenderHint(QPainter.Antialiasing)
        linearGradient = QLinearGradient(0, 0, 0, self.height())
        linearGradient.setColorAt(0, QColor(210, 215, 221))
        linearGradient.setColorAt(0.2, QColor(208, 213, 212))
        linearGradient.setColorAt(0.21, QColor(186, 192, 202))
        linearGradient.setColorAt(1, QColor(216, 222, 225))
        brush = QBrush(linearGradient)
        painter.setBrush(brush)
        painter.setPen(QColor(140, 141, 142))
        painter.pen().setWidth(2)
        
        # painter.drawRect(0, 0, self.width(), self.height())
        painter.drawRoundRect(0, 0, self.width(), self.height(), 10, 10)
        
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

class MainWindow(QMainWindow):
    def __init__(self, parent = None):
        QMainWindow.__init__(self, parent)
        bar = CustomBar(self)
        bar.move(100, 100)
        item = CustomItem(self)
        item.move(300, 300)
        brush = QBrush(Qt.SolidPattern)
        brush.setColor(QColor(250, 250, 250))
        self.palette().setBrush(QPalette.Background, brush)
        self.setAutoFillBackground(True)

        
app = QApplication(sys.argv)

main = MainWindow()
main.setWindowTitle('Customize Widgets')
main.setGeometry(200, 200, 800, 600)
main.show()

app.exec_()
