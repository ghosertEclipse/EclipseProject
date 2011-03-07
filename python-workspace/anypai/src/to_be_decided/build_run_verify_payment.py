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

verify_payment_ahk = readContent('verify_payment.ahk')
verify_payment_js = readContent('verify_payment.js')

import re

new_verify_payment_js = re.sub(r'\n', '', verify_payment_js)
new_verify_payment_ahk = re.sub(r'REPLACE_WITH_ENUM_JS', new_verify_payment_js, verify_payment_ahk)

with open('new_verify_payment.ahk', 'w') as f:
    f.write(new_verify_payment_ahk)

consoleString = '"C:\Program Files\AutoHotkey\Compiler\Ahk2exe.exe" /in "new_verify_payment.ahk" /NoDecompile'
call(consoleString)

os.system('del new_verify_payment.ahk')

# run for testing purpose.
consoleString = u'"new_verify_payment.exe" /force /ErrorStdOut'
os.system(consoleString)

