#encoding: utf-8
from subprocess import call
import os

# Ahk2exe.exe /in "MyScript.ahk" /icon "MyIcon.ico" /pass "CustomPassword" /NoDecompile
# CompiledScript.exe /force /ErrorStdOut "likecider - ghosert"

def readContent(filename):
    content = ''
    with open(filename) as f:
        line = f.readline()
        while line:
            content = content + line
            line = f.readline()
    return content

enum_ahk = readContent('enum.ahk')
enum_js = readContent('enum.js')

import re

new_enum_js = re.sub(r'\n', '', enum_js)
new_enum_ahk = re.sub(r'REPLACE_WITH_ENUM_JS', new_enum_js, enum_ahk)

with open('new_enum.ahk', 'w') as f:
    f.write(new_enum_ahk)

consoleString = '"C:\Program Files\AutoHotkey\Compiler\Ahk2exe.exe" /in "new_enum.ahk" /NoDecompile'
call(consoleString)

os.system('del new_enum.ahk')

# run for testing purpose.
consoleString = u'"new_enum.exe /force /ErrorStdOut "likecider - ghosert" "代理梦想家80后 - ghosert"'.encode('cp936')
os.system(consoleString)

