/*
	Author: shiroki@www.cuteqt.com
	License: GPLv2
*/

#ifndef GOODWIDGET_H 
#define	GOODWIDGET_H

#include <QWidget>
class GoodWidget:public QWidget
{
Q_OBJECT
public:
	GoodWidget(QWidget*p = NULL, bool transparent = true);
	~GoodWidget();
protected slots:
private:
	void paintEvent(QPaintEvent*e);
};

#endif
