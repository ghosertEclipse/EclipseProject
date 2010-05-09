import sys

from PyQt4.QtGui import *
from PyQt4.QtCore import *

class CustomItem(QGraphicsItem):
    def __init__(self, parent = None):
        QGraphicsItem.__init__(self, parent)
        self.isMoveIn = False
        self.rect = QRectF(100, 100, 300, 400)
        self.timeLine = QTimeLine(1000)
        self.timeLine.setCurveShape(QTimeLine.EaseInCurve)
        self.object = QObject()
        self.object.connect(self.timeLine, SIGNAL("valueChanged(qreal)"), self.showUp)
        self.showUpValue = 0
        self.timeLine.start()
    
    def showUp(self, value):
        self.showUpValue = value
        self.update()
        if self.showUpValue == 1:
            self.object.disconnect(self.timeLine, SIGNAL("valueChanged(qreal)"), self.showUp)
            self.object.connect(self.timeLine, SIGNAL("valueChanged(qreal)"), self.flash)
            self.timeLine.setDirection(QTimeLine.Backward)
            self.setAcceptHoverEvents(True)
            
    def flash(self, value):
        self.showUpValue = value
        self.update()
        if self.showUpValue >= 0.9:
            self.timeLine.setDirection(QTimeLine.Backward)
        if self.showUpValue <= 0.1:
            self.timeLine.setDirection(QTimeLine.Forward)

    def boundingRect(self):
        return self.rect

    def paint(self, painter, option, widget = None):
        if not self.isMoveIn:
            
            painter.setOpacity(0.5 * self.showUpValue)
            painter.setBrush(QBrush(QColor(0, 0, 0, 255)))
            painter.setPen(QColor(0, 0, 0))
            painter.drawRoundedRect(self.rect.adjusted(0, 0, 0, -1 * self.rect.height() * (1 - self.showUpValue)), 5, 5)
            
            painter.setPen(QColor(255, 255, 255))
            painter.setOpacity(1)
            painter.setFont(QFont('Consolas', 11))
            painter.drawText(120, 120, 'Login')
            
        else:
            painter.setOpacity(0.5 * self.showUpValue)
            painter.setBrush(QBrush(QColor(0, 0, 0, 255)))
            painter.setPen(QColor(0, 0, 0))
            painter.drawRoundedRect(self.rect, 5, 5)
            
            painter.setPen(QColor(255, 255, 255))
            painter.setOpacity(1)
            painter.setFont(QFont('Consolas', 11))
            painter.drawText(120, 120, 'Login')
    
    def hoverEnterEvent(self, event):
        # QGraphicsSceneEvent.
        self.isMoveIn = True
        self.timeLine.setLoopCount(0)
        self.timeLine.start()
        QGraphicsItem.hoverEnterEvent(self, event)
        
    def hoverLeaveEvent(self, event):
        # QGraphicsSceneEvent.
        self.isMoveIn = False
        self.timeLine.stop()
        self.showUpValue = 1
        QGraphicsItem.hoverLeaveEvent(self, event)
        
class GraphicsView(QGraphicsView):

    def __init__(self, parent=None):
        super(GraphicsView, self).__init__(parent)
        # self.setDragMode(QGraphicsView.RubberBandDrag)
        self.setRenderHint(QPainter.Antialiasing)
        self.setRenderHint(QPainter.TextAntialiasing)
        
        # Using OpenGL, you can also use Direct3D or software rendering mechanism.
        # See Qt/4.5.2/demos/qtdmeo/mainwindow.cpp and the Qt documentation.
        # if QtOpenGL.QGLFormat.hasOpenGL():
            # self.setViewport(QtOpenGL.QGLWidget(QtOpenGL.QGLFormat(QtOpenGL.QGL.SampleBuffers)))

    def wheelEvent(self, event):
        factor = 1.41 ** (-event.delta() / 240.0)
        self.scale(factor, factor)

class MainWindow(QDialog):
    def __init__(self, parent = None):
        QDialog.__init__(self, parent)
        view = GraphicsView(self)
        lg = QLinearGradient(0, 0, 800, 600)
        lg.setColorAt(0, QColor(255, 255, 255))
        lg.setColorAt(1, QColor(255, 255, 255))
        view.setBackgroundBrush(QBrush(lg))
        view.setFrameShape(QFrame.NoFrame)
        scene = QGraphicsScene(self)
        scene.setSceneRect(0, 0, 800, 600)
        view.setScene(scene)
        
        scene.addItem(CustomItem())
        
        # self.setWindowFlags(Qt.FramelessWindowHint)

app = QApplication(sys.argv)

main = MainWindow()
main.setGeometry(200, 200, 800, 600)
main.show()

app.exec_()
