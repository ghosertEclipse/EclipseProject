
/*
	Author: shiroki@www.cuteqt.com
	License: GPLv2
*/

#include <QApplication>
#include "mainwin.h"

int main(int argc, char* argv[])
{
	QApplication app(argc, argv);

	MainWin mw;
	mw.show();
	return app.exec();
}
