# encoding: utf-8
from ctypes import *

ahk = cdll.AutoHotkey
pyclient = create_string_buffer("ahkpython.ahk")   # no unicode in ahk
ahk.ahkdll(pyclient)

# Any String in autohokey script must be converted from utf-8 to Ansi, Since py file is utf-8, but autohotkey is ansi based.
# And only utf-8 characters are allowed to return to python side.
script = create_string_buffer("""
#Include acc.ahk
#Include com.ahk

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

; Function Name: IE_InjectJS
; Parameters:
; hWnd_MainWindow    - REQUIRED   - the hWnd of the main window containing the "Internet Explorer_Server" control : EXAMPLE (using active window) - WinExist("A")
; JS_to_Inject          - REQUIRED   - Javascript to execute in the browser window : EXAMPLE - "javascript:ahkvar1='It really Does';ahkvar2='!!!';alert('Hey, it Works!');"
; VarNames_to_Return    - OPTIONAL   - Comma delimited list of global javascript variables to return : EXAMPLE - "ahkvar1,ahkvar2"
;
; Return:
; Returns a comma delimited list of the variables contents (In the order passed to the function)
; Calling Example:
; IE_InjectJS(WinExist("A"), "javascript:ahkvar1='It really Does';ahkvar2='!!!';alert('Hey, it Works!');", "ahkvar1,ahkvar2")
;
IE_InjectJS(hWnd_MainWindow, JS_to_Inject, VarNames_to_Return="")
{
   ;Get a list of the hWnd's owned by the window specified
   WinGet, ActiveControlList, ControlListhWnd, ahk_id %hWnd_MainWindow%
   ;Go throught the list 1 at a time to determine if it is the correct control
   ;This will allow the script to find the current tab in IE7
   Loop, Parse, ActiveControlList, `n
      {
      ;Get the classname of the current control
      WinGetClass, ThisWinClass, ahk_id %A_LoopField%
      ;If the classname is correct and it is visible, it is the correct tab
      If (ThisWinClass = "Internet Explorer_Server") and (DllCall("IsWindowVisible", UInt, A_LoopField))
          {
           hIESvr := A_LoopField
           break ;Added by jiawzhang and dedicated for ali wangwang
          }
      }
   ;If a control was not found, give a message and return, doing nothing   
   If !hIESvr
      {
      ;Commented by jiawzhang
      ;MsgBox, Control "Internet Explorer_Server" not found.
      Return
      }
   ;Initialize the COM interface. code modified from SEAN
   IID_IHTMLWindow2 := "{332C4427-26CB-11D0-B483-00C04FD90119}"
   ACC_Init()
   pacc := ACC_AccessibleObjectFromWindow(hIESvr)
   pwin := COM_QueryService(pacc,IID_IHTMLWindow2,IID_IHTMLWindow2)
   COM_Release(pacc)
   ;Execute the Javascript (if there is any). Thanks LEXIKOS.
   If JS_to_Inject
      COM_Invoke(pwin, "execscript", JS_to_Inject)
   ;Get the value of the variables, if any.
   If VarNames_to_Return {
      ;Split the passed variable names into a psuedo array of ahk variables
      StringSplit, Vars_, VarNames_to_Return, `,
      ;Get the value of each javascript variable in the order it was passed
      Loop, %Vars_0%
         Ret .= COM_Invoke(pwin,Vars_%A_Index%) . ","
      ;Remove the trailing comma
      StringTrimRight, Ret, Ret, 1
      }
   ; Cleanup
   COM_Release(pwin)
   ACC_Term()
   ;Return a comma seperated list of variables in the order they were passed
   Return Ret
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

enumerateWangWang()
{
	;Enumerate all the windows which the title is end with "- ghosert" and ahk_class is StandardFrame
	SetTitleMatchMode RegEx
	WinGet,list,list,- ghosert$ ahk_class StandardFrame
	MsgBox %list%
	Loop % list
	{
	   MsgBox % list%A_Index%
	   jiawei := IE_InjectJS(WinExist("ahk_id" . list%A_Index%), "javascript:var html = document.body.innerHTML", "html")
	   ;MsgBox %jiawei%
	}
	SetTitleMatchMode 1
}

""")
ahk.addScript(script)

def sendAlipayPassword(alipayPassword):
    return_value = cast(ahk.ahkFunction(create_string_buffer("sendAlipayPassword"), create_string_buffer(alipayPassword)), c_char_p).value
    # jiawzhang XXX Remember to change code page in windows cmd to 'gbk', otherwise, the line below will throw error.
    return return_value.decode('utf-8')

def sendTaobaoPassword(password):
    return_value = cast(ahk.ahkFunction(create_string_buffer("sendTaobaoPassword"), create_string_buffer(password)), c_char_p).value
    return return_value.decode('utf-8')

def sendMessageToWangwang(wangwangId, wangwangUrl, message):
    "Make sure pass in the unicode based characters"
    # jiawzhang XXX This is the way how I pass in unicode characters.
    return_value = cast(ahk.ahkFunction(create_string_buffer("sendMessageToWangwang"), create_string_buffer(wangwangId.encode('utf-8')), create_string_buffer(wangwangUrl.encode('utf-8')), 
                                        create_string_buffer(message.encode('utf-8'))), c_char_p).value
    return return_value.decode('utf-8')

def enumerateWangWang():
    return_value = cast(ahk.ahkFunction(create_string_buffer("enumerateWangWang")), c_char_p).value
    # jiawzhang XXX Remember to change code page in windows cmd to 'gbk', otherwise, the line below will throw error.
    return return_value.decode('utf-8')

if __name__ == '__main__':
    print sendAlipayPassword('011849')
    sendMessageToWangwang(u"代理梦想家80后", u"http://www.taobao.com/webww/?ver=1&&touid=cntaobao代理梦想家80后&siteid=cntaobao&status=2&portalId=&gid=9190349629&itemsId=", u"有货吗？")
    # jiawzhang TODO: IE_InjectJS function above is not working for python invoking
    enumerateWangWang()

