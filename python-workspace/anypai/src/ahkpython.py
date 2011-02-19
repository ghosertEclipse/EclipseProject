# encoding: utf-8
from ctypes import *

ahk = cdll.AutoHotkey
pyclient = create_string_buffer("ahkpython.ahk")   # no unicode in ahk
ahk.ahkdll(pyclient)

# Any String in autohokey script must be converted from utf-8 to Ansi, Since py file is utf-8, but autohotkey is ansi based.
# And only utf-8 characters are allowed to return to python side.
script = create_string_buffer("""
sendAlipayPassword(alipayPassword){
;The last parameter is the windows title of the python application.
aString := UTF82Ansi("淘宝随意拍")
ControlSend, Edit1, %alipayPassword%, %aString%
;return "test你好"
}

sendTaobaoPassword(password){
;The last parameter is the windows title of the python application.
aString := UTF82Ansi("淘宝随意拍")
ControlSend, Edit2, %password%, %aString%
}

Ansi2UTF8(sString)
{
   Ansi2Unicode(sString, wString, 0)
   Unicode2Ansi(wString, zString, 65001)
   Return zString
}

UTF82Ansi(zString)
{
   Ansi2Unicode(zString, wString, 65001)
   Unicode2Ansi(wString, sString, 0)
   Return sString
}

Ansi2Unicode(ByRef sString, ByRef wString, CP = 0)
{
     nSize := DllCall("MultiByteToWideChar"
      , "Uint", CP
      , "Uint", 0
      , "Uint", &sString
      , "int", -1
      , "Uint", 0
      , "int", 0)

   VarSetCapacity(wString, nSize * 2)

   DllCall("MultiByteToWideChar"
      , "Uint", CP
      , "Uint", 0
      , "Uint", &sString
      , "int", -1
      , "Uint", &wString
      , "int", nSize)
}

Unicode2Ansi(ByRef wString, ByRef sString, CP = 0)
{
     nSize := DllCall("WideCharToMultiByte"
      , "Uint", CP
      , "Uint", 0
      , "Uint", &wString
      , "int", -1
      , "Uint", 0
      , "int", 0
      , "Uint", 0
      , "Uint", 0)

   VarSetCapacity(sString, nSize)

   DllCall("WideCharToMultiByte"
      , "Uint", CP
      , "Uint", 0
      , "Uint", &wString
      , "int", -1
      , "str", sString
      , "int", nSize
      , "Uint", 0
      , "Uint", 0)
}
""")
ahk.addScript(script)

def sendAlipayPassword(alipayPassword):
    return_value = cast(ahk.ahkFunction(create_string_buffer("sendAlipayPassword"), create_string_buffer(alipayPassword)), c_char_p).value
    # jiawzhang XXX Remember to change code page in windows cmd to 'gbk', otherwise, the line below will throw error.
    return return_value.decode('utf-8')

def sendTaobaoPassword(password):
    return_value = cast(ahk.ahkFunction(create_string_buffer("sendTaobaoPassword"), create_string_buffer(password)), c_char_p).value
    # jiawzhang XXX Remember to change code page in windows cmd to 'gbk', otherwise, the line below will throw error.
    return return_value.decode('utf-8')

if __name__ == '__main__':
    print sendAlipayPassword('011849')

