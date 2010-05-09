
/*
	Author: shiroki@www.cuteqt.com
	License: GPLv2
*/

#include "mainwin.h"
#include <QDebug>
#include <QLabel>
#include <QPainter>
#include <QVBoxLayout>
#include "goodwidget.h"

MainWin::MainWin(QWidget*p, Qt::WindowFlags f)
:QWidget(p,f)
{
	resize(400,260);
	setWindowTitle("www.cuteqt.com");
	setAttribute(Qt::WA_TranslucentBackground);

	QPalette pal = palette();
	pal.setColor(QPalette::Background, QColor(255,0,0,200));
	pal.setColor(QPalette::Window, QColor(255,255,0,200));
	setPalette(pal);

	QVBoxLayout* layout = new QVBoxLayout(this);
	layout->addWidget(new GoodWidget(this, true));
	layout->addWidget(new GoodWidget(this, false));

	label = new QLabel("www.cuteqt.com");
	label->setAttribute(Qt::WA_TranslucentBackground, true);
	layout->addWidget(label);

	label = new QLabel("www.cuteqt.com");
	label->setAttribute(Qt::WA_TranslucentBackground, false);
	label->setAutoFillBackground(true);
	layout->addWidget(label);
}

MainWin::~MainWin()
{
}

