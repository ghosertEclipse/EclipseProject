import os, shutil
import py_compile

def copytree2(src, dst):
    "Copy all the files and folders under src to dst, excluding src itself."
    for root, dirs, files in os.walk(src):
        for dir in dirs:
            shutil.copytree(root + os.path.sep + dir, dst + os.path.sep + dir)
        for file in files:
            shutil.copyfile(root + os.path.sep + file, dst + os.path.sep + file)
        break

def compilesrc(src):
    "Compile all the py, pyw files under src to pyc, pywc"
    for root, dirs, files in os.walk(src):
        for file in files:
            if os.path.splitext(file)[1] in ('.py', '.pyw'):
                py_compile.compile(root + os.path.sep + file)

print 'remove old dist'
shutil.rmtree('dist', ignore_errors = True)

print 'copy Python26 to dist\Python26'
shutil.copytree('Python26', 'dist\\Python26')

print 'copy the files and folders under extralibs to dist\Python26'
copytree2('extralibs', 'dist\\Python26')

print 'compile the src, convert py, pyw to pyc, pywc'
compilesrc('src')

print 'copy the files and folders under src to dist\Python26'
copytree2('src', 'dist')

print 'copy start.bat to dist\start.bat'
shutil.copyfile('start.bat', 'dist\\start.bat')

answer = raw_input('Do you want to keep data file(y/n)?')
if answer != 'y':
    os.system('del dist\\data')

print 'reduce the size of dist'
deleteList = ('.py', '.pyw', '.ui')
for root, dirs, files in os.walk('dist'):
    for file in files:
        if os.path.splitext(file)[1] in deleteList:
            sysDeleteFile = 'del {0}'.format(root + os.path.sep + file)
            print sysDeleteFile
            os.system(sysDeleteFile)

import sys
sys.path.append('buildtool')

import PythonWithoutWinSxSCRT
PythonWithoutWinSxSCRT.mtExecuteSearch()

