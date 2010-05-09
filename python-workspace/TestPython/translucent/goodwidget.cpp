
/*
	Author: shiroki@www.cuteqt.com
	License: GPLv2
*/

#include "goodwidget.h"
#include <QDebug>
#include <QEvent>
#include <QPixmap>
#include <QPainter>

GoodWidget::GoodWidget(QWidget*p, bool trans)
:QWidget(p)
{
	setAttribute(Qt::WA_TranslucentBackground, trans);//off
	setAutoFillBackground(true);
}

GoodWidget::~GoodWidget()
{
}

void GoodWidget::paintEvent( QPaintEvent*e)
{
	QPainter p(this);
	p.setClipRect(rect());

	QPixmap pixmap("./pic.png");
	p.drawPixmap(pixmap.rect(), pixmap);
}

