import re
with open('ui_main.py', 'r') as f:
    str = f.read()
    str = re.sub(r'setupUi\(self, MainDialog\)', 'setupUi(self, MainDialog, UserTableWidget, BookListWidget, VoteTreeWidget)',str)
    str = re.sub(r'QtGui.QTableWidget\(self.groupBox\)', 'UserTableWidget(self.groupBox)', str)
    str = re.sub(r'QtGui.QListWidget\(self.groupBox_2\)', 'BookListWidget(self.groupBox_2)', str)
    str = re.sub(r'QtGui.QTreeWidget\(self.groupBox_3\)', 'VoteTreeWidget(self.groupBox_3)', str)

with open('ui_main.py', 'w') as f:
    f.write(str)

