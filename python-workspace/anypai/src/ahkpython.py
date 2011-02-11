# encoding: utf-8
from ctypes import *

ahk = cdll.AutoHotkey
pyclient = create_string_buffer("ahkpython.ahk")   # no unicode in ahk
ahk.ahkdll(pyclient)

script = create_string_buffer("""
fx2(msg){
WinActivate %msg%
msgbox in function fx2 with %msg% from python
return "success 你好"
}
""")
ahk.addScript(script)
return_value = cast(ahk.ahkFunction(create_string_buffer("fx2"), create_string_buffer("ghosert")), c_char_p).value
# jiawzhang XXX Remember to change code page in windows cmd to 'gbk', otherwise, the line below will throw error.
print return_value.decode('utf-8')
print len(return_value.decode('utf-8'))

