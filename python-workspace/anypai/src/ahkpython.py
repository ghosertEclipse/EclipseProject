# encoding: utf-8
from ctypes import *

ahk = cdll.AutoHotkey

# Option 1: You can either create a file named 'ahkpython.ahk' and put "#Persistent #NoTrayIcon" in it then load it from ahk.ahkdll(filename)
# pyclient = create_string_buffer("ahkpython.ahk")   # no unicode in ahk
# ahk.ahkdll(pyclient)

# Option 2: Or forget the external file, just put "#Persistent #NoTrayIcon" into ahk.ahktextdll(ahk_script_string)
ahk.ahktextdll("""
#Persistent
#NoTrayIcon
""")

# Any String in autohokey script must be converted from utf-8 to Ansi, Since py file is utf-8, but autohotkey is ansi based.
# And only utf-8 characters are allowed to return to python side.
script = create_string_buffer("""
;To resovle the Chinese character issue
;For any target application which accept unicode like Word, wangwang
SendU(string)
{
        if string=
                return
        Ansi2Unicode(string, stru)
        ulen := DllCall("lstrlenW", str, stru) * 2
        offset=0
        Keys=
        keycode=
        oldformat := A_FormatInteger
        SetFormat, integer, d
        while offset < ulen
        {
                keycode := NumGet(stru, offset, "UShort")
                Keys .= "{ASC " . keycode . "}"
                offset+=2
        }
        SetFormat, integer, %oldformat%
        Send % Keys
}

;To resovle the Chinese character issue
;For any target application which accept ascii like Notepad
SendA(Keys)
{
    Len := StrLen(Keys) ; 得到字符串的长度，注意一个中文字符的长度是2
    KeysInUnicode := ""     ; 将要发送的字符序列
    Char1 := ""                 ; 暂存字符1
    Code1 := 0                  ; 字符1的ASCII码，值介于 0x0-0xFF (即1~255)
    Char2 := ""                 ; 暂存字符2
    Index := 1                  ; 用于循环
    Loop
    {
        Code2 := 0                                              ; 字符2的ASCII码
        Char1 := SubStr(Keys, Index, 1)             ; 第一个字符
        Code1 := Asc(Char1)                             ; 得到其ASCII值
        if(Code1 >= 129 And Code1 <= 254 And Index < Len)   ; 判断是否中文字符的第一个字符
        {
            Char2 := SubStr(Keys, Index+1, 1)       ; 第二个字符
            Code2 := Asc(Char2)                         ; 得到其ASCII值
            if(Code2 >= 64 And Code2 <= 254)        ; 若条件成立则说明是中文字符
            {
                Code1 <<= 8                                 ; 第一个字符应放到高8位上
                Code1 += Code2                              ; 第二个字符放在低8位上
            }
            Index++
        }
        if(Code1 <= 255)                                    ; 如果此值仍<=255则说明是非中文字符，否则经过上面的处理必然大于255
            Code1 := "0" . Code1
        KeysInUnicode .= "{ASC " . Code1 . "}"
        if(Code2 > 0 And Code2 < 64)
        {
            Code2 := "0" . Code2
            KeysInUnicode .= "{ASC " . Code2 . "}"
        }
        Index++
        if(Index > Len)
            Break
    }
    Send % KeysInUnicode
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

; =======================================Below is the customized autohokey function, Take most attention on this line below =======================================================

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

sendMessageToWangwang(taobaoId, wangwangUrl, message)
{
    taobaoId := UTF82Ansi(taobaoId)
    wangwangUrl := UTF82Ansi(wangwangUrl)
    message := UTF82Ansi(message)
    
    wangwang_title = %taobaoId% - ghosert
    IfWinNotExist %wangwang_title%
    {
        Run %wangwangUrl%
        WinWait %wangwang_title%
        WinMinimize
    }
    ControlSetText, RichEditComponent1, %message%, %wangwang_title%
    ControlSend, RichEditComponent1, {Enter}, %wangwang_title%
}

exitApp()
{
    ExitApp
}

""")
ahk.addScript(script)

def sendAlipayPassword(alipayPassword):
    return_value = cast(ahk.ahkFunction(create_string_buffer("sendAlipayPassword"), create_string_buffer(alipayPassword)), c_char_p).value
    # jiawzhang XXX Remember to change code page in windows cmd to 'gbk', otherwise, the line below will throw error.
    return return_value.decode('utf-8') if return_value is not None else None

def sendTaobaoPassword(password):
    return_value = cast(ahk.ahkFunction(create_string_buffer("sendTaobaoPassword"), create_string_buffer(password)), c_char_p).value
    return return_value.decode('utf-8') if return_value is not None else None

def sendMessageToWangwang(wangwangId, wangwangUrl, message):
    "Make sure pass in the unicode based characters"
    # jiawzhang XXX This is the way how I pass in unicode characters.
    return_value = cast(ahk.ahkFunction(create_string_buffer("sendMessageToWangwang"), create_string_buffer(wangwangId.encode('utf-8')), create_string_buffer(wangwangUrl.encode('utf-8')), 
                                        create_string_buffer(message.encode('utf-8'))), c_char_p).value
    return return_value.decode('utf-8') if return_value is not None else None

import subprocess as sub

def enumerateWangWang(taobaoIds, myTaobaoId):
    """
    taobaoIds should be a list and each item is something like 'likecider', invoking sample:
    enumerateWangWang([u"代理梦想家80后", u"likecider"], u"ghosert")
    """
    if not taobaoIds:
        return None
    stringIds = ""
    for taobaoId in taobaoIds:
        stringIds = stringIds + u"\"{0} - {1}\" ".format(taobaoId, myTaobaoId)

    consoleString = u'new_enum.exe /force /ErrorStdOut {0}'.format(stringIds)

    p = sub.Popen(consoleString.encode('cp936'), stdout=sub.PIPE, stderr=sub.PIPE)
    output, errors = p.communicate()
    if errors:
        return None
    # jiawzhang TODO: Begin to parse output.
    if output:
        print output
        return

def exitApp():
    "jiawzhang TODO: Since the #Persistent on the top of this file, invoke this exitApp when quiting the whole app to make sure this ahk quits."
    return_value = cast(ahk.ahkFunction(create_string_buffer("exitApp")), c_char_p).value
    return return_value.decode('utf-8') if return_value is not None else None

if __name__ == '__main__':
    # print sendAlipayPassword('011849')
    # exitApp()
    # sendMessageToWangwang(u"代理梦想家80后", u"http://www.taobao.com/webww/?ver=1&&touid=cntaobao代理梦想家80后&siteid=cntaobao&status=2&portalId=&gid=9190349629&itemsId=", u"有货吗？")
    enumerateWangWang([u"代理梦想家80后", u"likecider"], u"ghosert")

