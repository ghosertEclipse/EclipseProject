Build Usage:

1. Double click build.py to produce dist folder.

2. RAR dist folder manually and release it to client.

3. There is no environment required for the target machine.

Explanation:

1. The files in extralibs folder is necessary for python.exe(VC90CRT) and python windows extension module.

2. The qt.conf in Python26 folder is necessary for pyqt module, the path in qt.conf file should be correct.

3. You should already install .net framework 2.0 to run the mt.exe under buildtool folder

4. buildtool\extracted.manifest is extracted from python.exe by
"mt.exe -inputresource:python.exe;#1 -out:extracted.manifest"

5. The content under "<dependentAssembly></dependentAssembly>" node in extracted.manifest file has been removed.

6. PythonWithoutWinSxSCRT.py will re-embed the modified extracted.manifest to all the dll, exe, pyd under dist\Python26\DLLs, dist\Python26\Lib so that all the modules for python will not search the CRT in c:\windows\winsxs\ folder in target machine. Otherwise, the target machine has to be installed "Microsoft Visual C++ 2008 Redistributable Package (x86)"