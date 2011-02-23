;#NoTrayIcon

#Include acc.ahk
#Include com.ahk

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


; The star '*' below will cause a standard output, and then you can capture it from python side.
; FileAppend, This method is sample for console windows, *
isContinue := true
while (isContinue)
{
	Sleep, 1000
	Loop, %0%  ; For each parameter:
	{
		param := %A_Index%  ; Fetch the contents of the variable whose name is contained in A_Index.
		;Enumerate the windows which the title like "likecider - ghosert" and ahk_class is StandardFrame
		WinGet,id,id,%param% ahk_class StandardFrame
		jiawei := IE_InjectJS(WinExist("ahk_id" . id), "javascript:REPLACE_WITH_ENUM_JS var html = main();", "html")
		if (jiawei == "") {
			isContinue := false
			FileAppend, %param%`tfalse`n, *
		} else {
			MsgBox %jiawei%
			FileAppend, %jiawei%`n, *
		}
	}
}
