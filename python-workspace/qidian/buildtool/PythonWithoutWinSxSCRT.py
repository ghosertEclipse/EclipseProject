import os

def mtExecuteSearch():
    mtCommandTemplate = r'buildtool\mt.exe -nologo -manifest buildtool\extracted.manifest -outputresource:{0};{1}'
    mtList = ('.exe', '.dll', '.pyd')
    mtSearchPath = ('dist\\Python26\\DLLs', 'dist\\Python26\\Lib')

    for path in mtSearchPath:
        for root, dirs, files in os.walk(path):
            for file in files:
                ext = os.path.splitext(file)[1]
                if ext in mtList:
                    mtCommand = mtCommandTemplate.format(root + os.path.sep + file, '1' if ext == '.exe' else '2')
                    print mtCommand
                    os.system(mtCommand)

