	TITLE	C:\Documents and Settings\Administrator\My Documents\Software\NativeCall\src\cpp\NativeCall.cpp
	.386P
include listing.inc
if @Version gt 510
.model FLAT
else
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
_BSS	SEGMENT DWORD USE32 PUBLIC 'BSS'
_BSS	ENDS
$$SYMBOLS	SEGMENT BYTE USE32 'DEBSYM'
$$SYMBOLS	ENDS
$$TYPES	SEGMENT BYTE USE32 'DEBTYP'
$$TYPES	ENDS
_TLS	SEGMENT DWORD USE32 PUBLIC 'TLS'
_TLS	ENDS
;	COMDAT ??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_08JJOG@function?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_06CODG@module?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_01FLOP@I?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0P@LKIL@functionHandle?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0N@EFAA@moduleHandle?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0O@OMHL@lastErrorCode?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0BL@DALH@com?1eaio?1nativecall?1Holder?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0BD@GEDI@Ljava?1lang?1Object?$DL?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_01PGHN@o?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0BC@IGNJ@java?1lang?1Boolean?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0BC@FBKL@java?1lang?1Integer?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0BB@LLFN@java?1lang?1String?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_02LOEJ@?$FLB?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_02BENO@?$FLC?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_03KJOK@?$CI?$CJZ?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_0N@KEBP@booleanValue?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_03PPCD@?$CI?$CJI?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_08JCMA@intValue?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_04ECLF@?$CII?$CJV?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_06KILP@?$DMinit?$DO?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ??_C@_04JFOE@?$CIZ?$CJV?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetMethodID@JNIEnv_@@QAEPAU_jmethodID@@PAV_jclass@@PBD1@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?NewStringUTF@JNIEnv_@@QAEPAV_jstring@@PBD@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetStringUTFChars@JNIEnv_@@QAEPBDPAV_jstring@@PAE@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?ReleaseStringUTFChars@JNIEnv_@@QAEXPAV_jstring@@PBD@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ??8@YAHABU_GUID@@0@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT _Java_com_eaio_nativecall_NativeCall_initIDs@8
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT _Java_com_eaio_nativecall_NativeCall_initHandles@8
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT _Java_com_eaio_nativecall_NativeCall_getLastError@8
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT _Java_com_eaio_nativecall_NativeCall_destroy@8
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
FLAT	GROUP _DATA, CONST, _BSS
	ASSUME	CS: FLAT, DS: FLAT, SS: FLAT
endif
PUBLIC	?fieldFunction@@3PAU_jfieldID@@A		; fieldFunction
PUBLIC	?fieldModule@@3PAU_jfieldID@@A			; fieldModule
PUBLIC	?fieldFunctionHandle@@3PAU_jfieldID@@A		; fieldFunctionHandle
PUBLIC	?fieldModuleHandle@@3PAU_jfieldID@@A		; fieldModuleHandle
PUBLIC	?fieldLastErrorCode@@3PAU_jfieldID@@A		; fieldLastErrorCode
PUBLIC	?fieldHolderO@@3PAU_jfieldID@@A			; fieldHolderO
PUBLIC	?classBoolean@@3PAV_jclass@@A			; classBoolean
PUBLIC	?classInteger@@3PAV_jclass@@A			; classInteger
PUBLIC	?classString@@3PAV_jclass@@A			; classString
PUBLIC	?classByteArray@@3PAV_jclass@@A			; classByteArray
PUBLIC	?classCharArray@@3PAV_jclass@@A			; classCharArray
PUBLIC	?classHolder@@3PAV_jclass@@A			; classHolder
PUBLIC	?methodBooleanValue@@3PAU_jmethodID@@A		; methodBooleanValue
PUBLIC	?methodIntValue@@3PAU_jmethodID@@A		; methodIntValue
PUBLIC	?newIntegerInt@@3PAU_jmethodID@@A		; newIntegerInt
PUBLIC	?newBooleanBoolean@@3PAU_jmethodID@@A		; newBooleanBoolean
_BSS	SEGMENT
?fieldFunction@@3PAU_jfieldID@@A DD 01H DUP (?)		; fieldFunction
?fieldModule@@3PAU_jfieldID@@A DD 01H DUP (?)		; fieldModule
?fieldFunctionHandle@@3PAU_jfieldID@@A DD 01H DUP (?)	; fieldFunctionHandle
?fieldModuleHandle@@3PAU_jfieldID@@A DD 01H DUP (?)	; fieldModuleHandle
?fieldLastErrorCode@@3PAU_jfieldID@@A DD 01H DUP (?)	; fieldLastErrorCode
?fieldHolderO@@3PAU_jfieldID@@A DD 01H DUP (?)		; fieldHolderO
?classBoolean@@3PAV_jclass@@A DD 01H DUP (?)		; classBoolean
?classInteger@@3PAV_jclass@@A DD 01H DUP (?)		; classInteger
?classString@@3PAV_jclass@@A DD 01H DUP (?)		; classString
?classByteArray@@3PAV_jclass@@A DD 01H DUP (?)		; classByteArray
?classCharArray@@3PAV_jclass@@A DD 01H DUP (?)		; classCharArray
?classHolder@@3PAV_jclass@@A DD 01H DUP (?)		; classHolder
?methodBooleanValue@@3PAU_jmethodID@@A DD 01H DUP (?)	; methodBooleanValue
?methodIntValue@@3PAU_jmethodID@@A DD 01H DUP (?)	; methodIntValue
?newIntegerInt@@3PAU_jmethodID@@A DD 01H DUP (?)	; newIntegerInt
?newBooleanBoolean@@3PAU_jmethodID@@A DD 01H DUP (?)	; newBooleanBoolean
_BSS	ENDS
PUBLIC	?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetFieldID
PUBLIC	_Java_com_eaio_nativecall_NativeCall_initIDs@8
PUBLIC	??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@	; `string'
PUBLIC	??_C@_08JJOG@function?$AA@			; `string'
PUBLIC	??_C@_06CODG@module?$AA@			; `string'
PUBLIC	??_C@_01FLOP@I?$AA@				; `string'
PUBLIC	??_C@_0P@LKIL@functionHandle?$AA@		; `string'
PUBLIC	??_C@_0N@EFAA@moduleHandle?$AA@			; `string'
PUBLIC	??_C@_0O@OMHL@lastErrorCode?$AA@		; `string'
PUBLIC	??_C@_0BL@DALH@com?1eaio?1nativecall?1Holder?$AA@ ; `string'
PUBLIC	??_C@_0BD@GEDI@Ljava?1lang?1Object?$DL?$AA@	; `string'
PUBLIC	??_C@_01PGHN@o?$AA@				; `string'
PUBLIC	??_C@_0BC@IGNJ@java?1lang?1Boolean?$AA@		; `string'
PUBLIC	??_C@_0BC@FBKL@java?1lang?1Integer?$AA@		; `string'
PUBLIC	??_C@_0BB@LLFN@java?1lang?1String?$AA@		; `string'
PUBLIC	??_C@_02LOEJ@?$FLB?$AA@				; `string'
PUBLIC	??_C@_02BENO@?$FLC?$AA@				; `string'
PUBLIC	??_C@_03KJOK@?$CI?$CJZ?$AA@			; `string'
PUBLIC	??_C@_0N@KEBP@booleanValue?$AA@			; `string'
PUBLIC	??_C@_03PPCD@?$CI?$CJI?$AA@			; `string'
PUBLIC	??_C@_08JCMA@intValue?$AA@			; `string'
PUBLIC	??_C@_04ECLF@?$CII?$CJV?$AA@			; `string'
PUBLIC	??_C@_06KILP@?$DMinit?$DO?$AA@			; `string'
PUBLIC	??_C@_04JFOE@?$CIZ?$CJV?$AA@			; `string'
PUBLIC	?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z	; JNIEnv_::FindClass
PUBLIC	?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z	; JNIEnv_::NewGlobalRef
PUBLIC	?GetMethodID@JNIEnv_@@QAEPAU_jmethodID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetMethodID
EXTRN	__chkesp:NEAR
;	COMDAT ??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@
; File c:\documents and settings\administrator\my documents\software\nativecall\src\cpp\nativecall.cpp
CONST	SEGMENT
??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@ DB 'Ljava/lang/String;', 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_08JJOG@function?$AA@
CONST	SEGMENT
??_C@_08JJOG@function?$AA@ DB 'function', 00H		; `string'
CONST	ENDS
;	COMDAT ??_C@_06CODG@module?$AA@
CONST	SEGMENT
??_C@_06CODG@module?$AA@ DB 'module', 00H		; `string'
CONST	ENDS
;	COMDAT ??_C@_01FLOP@I?$AA@
CONST	SEGMENT
??_C@_01FLOP@I?$AA@ DB 'I', 00H				; `string'
CONST	ENDS
;	COMDAT ??_C@_0P@LKIL@functionHandle?$AA@
CONST	SEGMENT
??_C@_0P@LKIL@functionHandle?$AA@ DB 'functionHandle', 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_0N@EFAA@moduleHandle?$AA@
CONST	SEGMENT
??_C@_0N@EFAA@moduleHandle?$AA@ DB 'moduleHandle', 00H	; `string'
CONST	ENDS
;	COMDAT ??_C@_0O@OMHL@lastErrorCode?$AA@
CONST	SEGMENT
??_C@_0O@OMHL@lastErrorCode?$AA@ DB 'lastErrorCode', 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_0BL@DALH@com?1eaio?1nativecall?1Holder?$AA@
CONST	SEGMENT
??_C@_0BL@DALH@com?1eaio?1nativecall?1Holder?$AA@ DB 'com/eaio/nativecall'
	DB	'/Holder', 00H				; `string'
CONST	ENDS
;	COMDAT ??_C@_0BD@GEDI@Ljava?1lang?1Object?$DL?$AA@
CONST	SEGMENT
??_C@_0BD@GEDI@Ljava?1lang?1Object?$DL?$AA@ DB 'Ljava/lang/Object;', 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_01PGHN@o?$AA@
CONST	SEGMENT
??_C@_01PGHN@o?$AA@ DB 'o', 00H				; `string'
CONST	ENDS
;	COMDAT ??_C@_0BC@IGNJ@java?1lang?1Boolean?$AA@
CONST	SEGMENT
??_C@_0BC@IGNJ@java?1lang?1Boolean?$AA@ DB 'java/lang/Boolean', 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_0BC@FBKL@java?1lang?1Integer?$AA@
CONST	SEGMENT
??_C@_0BC@FBKL@java?1lang?1Integer?$AA@ DB 'java/lang/Integer', 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_0BB@LLFN@java?1lang?1String?$AA@
CONST	SEGMENT
??_C@_0BB@LLFN@java?1lang?1String?$AA@ DB 'java/lang/String', 00H ; `string'
CONST	ENDS
;	COMDAT ??_C@_02LOEJ@?$FLB?$AA@
CONST	SEGMENT
??_C@_02LOEJ@?$FLB?$AA@ DB '[B', 00H			; `string'
CONST	ENDS
;	COMDAT ??_C@_02BENO@?$FLC?$AA@
CONST	SEGMENT
??_C@_02BENO@?$FLC?$AA@ DB '[C', 00H			; `string'
CONST	ENDS
;	COMDAT ??_C@_03KJOK@?$CI?$CJZ?$AA@
CONST	SEGMENT
??_C@_03KJOK@?$CI?$CJZ?$AA@ DB '()Z', 00H		; `string'
CONST	ENDS
;	COMDAT ??_C@_0N@KEBP@booleanValue?$AA@
CONST	SEGMENT
??_C@_0N@KEBP@booleanValue?$AA@ DB 'booleanValue', 00H	; `string'
CONST	ENDS
;	COMDAT ??_C@_03PPCD@?$CI?$CJI?$AA@
CONST	SEGMENT
??_C@_03PPCD@?$CI?$CJI?$AA@ DB '()I', 00H		; `string'
CONST	ENDS
;	COMDAT ??_C@_08JCMA@intValue?$AA@
CONST	SEGMENT
??_C@_08JCMA@intValue?$AA@ DB 'intValue', 00H		; `string'
CONST	ENDS
;	COMDAT ??_C@_04ECLF@?$CII?$CJV?$AA@
CONST	SEGMENT
??_C@_04ECLF@?$CII?$CJV?$AA@ DB '(I)V', 00H		; `string'
CONST	ENDS
;	COMDAT ??_C@_06KILP@?$DMinit?$DO?$AA@
CONST	SEGMENT
??_C@_06KILP@?$DMinit?$DO?$AA@ DB '<init>', 00H		; `string'
CONST	ENDS
;	COMDAT ??_C@_04JFOE@?$CIZ?$CJV?$AA@
CONST	SEGMENT
??_C@_04JFOE@?$CIZ?$CJV?$AA@ DB '(Z)V', 00H		; `string'
CONST	ENDS
;	COMDAT _Java_com_eaio_nativecall_NativeCall_initIDs@8
_TEXT	SEGMENT
_env$ = 8
_cls$ = 12
_Java_com_eaio_nativecall_NativeCall_initIDs@8 PROC NEAR ; COMDAT

; 71   : (JNIEnv *env, jclass cls) {

	push	ebp
	mov	ebp, esp
	sub	esp, 64					; 00000040H
	push	ebx
	push	esi
	push	edi
	lea	edi, DWORD PTR [ebp-64]
	mov	ecx, 16					; 00000010H
	mov	eax, -858993460				; ccccccccH
	rep stosd

; 72   : 
; 73   :  // NativeCall fields
; 74   : 
; 75   :  fieldFunction = env->GetFieldID(cls, "function", "Ljava/lang/String;");

	push	OFFSET FLAT:??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@ ; `string'
	push	OFFSET FLAT:??_C@_08JJOG@function?$AA@	; `string'
	mov	eax, DWORD PTR _cls$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetFieldID
	mov	DWORD PTR ?fieldFunction@@3PAU_jfieldID@@A, eax ; fieldFunction

; 76   :  fieldModule = env->GetFieldID(cls, "module", "Ljava/lang/String;");

	push	OFFSET FLAT:??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@ ; `string'
	push	OFFSET FLAT:??_C@_06CODG@module?$AA@	; `string'
	mov	ecx, DWORD PTR _cls$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetFieldID
	mov	DWORD PTR ?fieldModule@@3PAU_jfieldID@@A, eax ; fieldModule

; 77   : 
; 78   :  fieldFunctionHandle = env->GetFieldID(cls, "functionHandle", "I");

	push	OFFSET FLAT:??_C@_01FLOP@I?$AA@		; `string'
	push	OFFSET FLAT:??_C@_0P@LKIL@functionHandle?$AA@ ; `string'
	mov	edx, DWORD PTR _cls$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetFieldID
	mov	DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A, eax ; fieldFunctionHandle

; 79   :  fieldModuleHandle = env-> GetFieldID(cls, "moduleHandle", "I");

	push	OFFSET FLAT:??_C@_01FLOP@I?$AA@		; `string'
	push	OFFSET FLAT:??_C@_0N@EFAA@moduleHandle?$AA@ ; `string'
	mov	eax, DWORD PTR _cls$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetFieldID
	mov	DWORD PTR ?fieldModuleHandle@@3PAU_jfieldID@@A, eax ; fieldModuleHandle

; 80   : 
; 81   :  fieldLastErrorCode = env->GetFieldID(cls, "lastErrorCode", "I");

	push	OFFSET FLAT:??_C@_01FLOP@I?$AA@		; `string'
	push	OFFSET FLAT:??_C@_0O@OMHL@lastErrorCode?$AA@ ; `string'
	mov	ecx, DWORD PTR _cls$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetFieldID
	mov	DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A, eax ; fieldLastErrorCode

; 82   : 
; 83   :  // Holder fields
; 84   : 
; 85   :  classHolder = (jclass) env->NewGlobalRef(env->FindClass("com/eaio/nativecall/Holder"));

	push	OFFSET FLAT:??_C@_0BL@DALH@com?1eaio?1nativecall?1Holder?$AA@ ; `string'
	mov	ecx, DWORD PTR _env$[ebp]
	call	?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z ; JNIEnv_::FindClass
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z ; JNIEnv_::NewGlobalRef
	mov	DWORD PTR ?classHolder@@3PAV_jclass@@A, eax ; classHolder

; 86   :  fieldHolderO = env->GetFieldID(classHolder, "o", "Ljava/lang/Object;");

	push	OFFSET FLAT:??_C@_0BD@GEDI@Ljava?1lang?1Object?$DL?$AA@ ; `string'
	push	OFFSET FLAT:??_C@_01PGHN@o?$AA@		; `string'
	mov	edx, DWORD PTR ?classHolder@@3PAV_jclass@@A ; classHolder
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetFieldID
	mov	DWORD PTR ?fieldHolderO@@3PAU_jfieldID@@A, eax ; fieldHolderO

; 87   : 
; 88   :  // Other classes
; 89   : 
; 90   :  classBoolean = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Boolean"));

	push	OFFSET FLAT:??_C@_0BC@IGNJ@java?1lang?1Boolean?$AA@ ; `string'
	mov	ecx, DWORD PTR _env$[ebp]
	call	?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z ; JNIEnv_::FindClass
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z ; JNIEnv_::NewGlobalRef
	mov	DWORD PTR ?classBoolean@@3PAV_jclass@@A, eax ; classBoolean

; 91   :  /*classByte = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Byte"));
; 92   :  classCharacter = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Character"));
; 93   :  classShort = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Short"));*/
; 94   :  classInteger = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Integer"));

	push	OFFSET FLAT:??_C@_0BC@FBKL@java?1lang?1Integer?$AA@ ; `string'
	mov	ecx, DWORD PTR _env$[ebp]
	call	?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z ; JNIEnv_::FindClass
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z ; JNIEnv_::NewGlobalRef
	mov	DWORD PTR ?classInteger@@3PAV_jclass@@A, eax ; classInteger

; 95   :  /*classLong = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Long"));
; 96   :  classFloat = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Float"));
; 97   :  classDouble = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Double"));*/
; 98   :  classString = (jclass) env->NewGlobalRef(env->FindClass("java/lang/String"));

	push	OFFSET FLAT:??_C@_0BB@LLFN@java?1lang?1String?$AA@ ; `string'
	mov	ecx, DWORD PTR _env$[ebp]
	call	?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z ; JNIEnv_::FindClass
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z ; JNIEnv_::NewGlobalRef
	mov	DWORD PTR ?classString@@3PAV_jclass@@A, eax ; classString

; 99   :  classByteArray = (jclass) env->NewGlobalRef(env->FindClass("[B"));

	push	OFFSET FLAT:??_C@_02LOEJ@?$FLB?$AA@	; `string'
	mov	ecx, DWORD PTR _env$[ebp]
	call	?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z ; JNIEnv_::FindClass
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z ; JNIEnv_::NewGlobalRef
	mov	DWORD PTR ?classByteArray@@3PAV_jclass@@A, eax ; classByteArray

; 100  :  classCharArray = (jclass) env->NewGlobalRef(env->FindClass("[C"));

	push	OFFSET FLAT:??_C@_02BENO@?$FLC?$AA@	; `string'
	mov	ecx, DWORD PTR _env$[ebp]
	call	?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z ; JNIEnv_::FindClass
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z ; JNIEnv_::NewGlobalRef
	mov	DWORD PTR ?classCharArray@@3PAV_jclass@@A, eax ; classCharArray

; 101  :  /*classIntArray = (jclass) env->NewGlobalRef(env->FindClass("[I"));*/
; 102  : 
; 103  :  // Wrapper class methods
; 104  : 
; 105  :  methodBooleanValue = env->GetMethodID(classBoolean,
; 106  :   "booleanValue", "()Z");

	push	OFFSET FLAT:??_C@_03KJOK@?$CI?$CJZ?$AA@	; `string'
	push	OFFSET FLAT:??_C@_0N@KEBP@booleanValue?$AA@ ; `string'
	mov	eax, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetMethodID@JNIEnv_@@QAEPAU_jmethodID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetMethodID
	mov	DWORD PTR ?methodBooleanValue@@3PAU_jmethodID@@A, eax ; methodBooleanValue

; 107  :  /*methodByteValue = env->GetMethodID(classByte,
; 108  :   "byteValue", "()B");
; 109  :  methodCharValue = env->GetMethodID(classCharacter,
; 110  :   "charValue", "()C");
; 111  :  methodShortValue = env->GetMethodID(classShort,
; 112  :   "shortValue", "()S");*/
; 113  :  methodIntValue = env->GetMethodID(classInteger,
; 114  :   "intValue", "()I");

	push	OFFSET FLAT:??_C@_03PPCD@?$CI?$CJI?$AA@	; `string'
	push	OFFSET FLAT:??_C@_08JCMA@intValue?$AA@	; `string'
	mov	ecx, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetMethodID@JNIEnv_@@QAEPAU_jmethodID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetMethodID
	mov	DWORD PTR ?methodIntValue@@3PAU_jmethodID@@A, eax ; methodIntValue

; 115  :  /*methodLongValue = env->GetMethodID(classLong,
; 116  :   "longValue", "()J");
; 117  :  methodFloatValue = env->GetMethodID(classFloat,
; 118  :   "floatValue", "()F");
; 119  :  methodDoubleValue = env->GetMethodID(classDouble,
; 120  :   "doubleValue", "()D");*/
; 121  : 
; 122  :  // Constructors
; 123  : 
; 124  :  newIntegerInt = env->GetMethodID(classInteger, "<init>", "(I)V");

	push	OFFSET FLAT:??_C@_04ECLF@?$CII?$CJV?$AA@ ; `string'
	push	OFFSET FLAT:??_C@_06KILP@?$DMinit?$DO?$AA@ ; `string'
	mov	edx, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetMethodID@JNIEnv_@@QAEPAU_jmethodID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetMethodID
	mov	DWORD PTR ?newIntegerInt@@3PAU_jmethodID@@A, eax ; newIntegerInt

; 125  :  newBooleanBoolean = env->GetMethodID(classBoolean, "<init>", "(Z)V");

	push	OFFSET FLAT:??_C@_04JFOE@?$CIZ?$CJV?$AA@ ; `string'
	push	OFFSET FLAT:??_C@_06KILP@?$DMinit?$DO?$AA@ ; `string'
	mov	eax, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetMethodID@JNIEnv_@@QAEPAU_jmethodID@@PAV_jclass@@PBD1@Z ; JNIEnv_::GetMethodID
	mov	DWORD PTR ?newBooleanBoolean@@3PAU_jmethodID@@A, eax ; newBooleanBoolean

; 126  :   
; 127  : }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 64					; 00000040H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
_Java_com_eaio_nativecall_NativeCall_initIDs@8 ENDP
_TEXT	ENDS
;	COMDAT ?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z
_TEXT	SEGMENT
_this$ = -4
_name$ = 8
?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z PROC NEAR	; JNIEnv_::FindClass, COMDAT

; 759  :     jclass FindClass(const char *name) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 760  :         return functions->FindClass(this, name);

	mov	esi, esp
	mov	eax, DWORD PTR _name$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	mov	eax, DWORD PTR [edx]
	call	DWORD PTR [eax+24]
	cmp	esi, esp
	call	__chkesp

; 761  :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	4
?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z ENDP		; JNIEnv_::FindClass
_TEXT	ENDS
PUBLIC	?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z ; JNIEnv_::GetObjectField
PUBLIC	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField
PUBLIC	_Java_com_eaio_nativecall_NativeCall_initHandles@8
PUBLIC	?GetStringUTFChars@JNIEnv_@@QAEPBDPAV_jstring@@PAE@Z ; JNIEnv_::GetStringUTFChars
PUBLIC	?ReleaseStringUTFChars@JNIEnv_@@QAEXPAV_jstring@@PBD@Z ; JNIEnv_::ReleaseStringUTFChars
EXTRN	__imp__LoadLibraryA@4:NEAR
EXTRN	__imp__GetLastError@0:NEAR
EXTRN	__imp__FreeLibrary@4:NEAR
EXTRN	__imp__GetProcAddress@8:NEAR
;	COMDAT _Java_com_eaio_nativecall_NativeCall_initHandles@8
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_out$ = -4
_moduleNameS$ = -8
_functionNameS$ = -12
_moduleName$ = -16
_functionName$ = -20
_mod$ = -24
_addr$56841 = -28
_Java_com_eaio_nativecall_NativeCall_initHandles@8 PROC NEAR ; COMDAT

; 135  : (JNIEnv *env, jobject obj) {

	push	ebp
	mov	ebp, esp
	sub	esp, 92					; 0000005cH
	push	ebx
	push	esi
	push	edi
	lea	edi, DWORD PTR [ebp-92]
	mov	ecx, 23					; 00000017H
	mov	eax, -858993460				; ccccccccH
	rep stosd

; 136  :   
; 137  :  bool out = JNI_TRUE;

	mov	BYTE PTR _out$[ebp], 1

; 138  :  
; 139  :  jstring moduleNameS = (jstring) env->GetObjectField(obj, fieldModule);

	mov	eax, DWORD PTR ?fieldModule@@3PAU_jfieldID@@A ; fieldModule
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z ; JNIEnv_::GetObjectField
	mov	DWORD PTR _moduleNameS$[ebp], eax

; 140  :  jstring functionNameS = (jstring) env->GetObjectField(obj, fieldFunction);

	mov	edx, DWORD PTR ?fieldFunction@@3PAU_jfieldID@@A ; fieldFunction
	push	edx
	mov	eax, DWORD PTR _obj$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z ; JNIEnv_::GetObjectField
	mov	DWORD PTR _functionNameS$[ebp], eax

; 141  :  
; 142  :  const char* moduleName = env->GetStringUTFChars(moduleNameS, 0);

	push	0
	mov	ecx, DWORD PTR _moduleNameS$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetStringUTFChars@JNIEnv_@@QAEPBDPAV_jstring@@PAE@Z ; JNIEnv_::GetStringUTFChars
	mov	DWORD PTR _moduleName$[ebp], eax

; 143  :  const char* functionName = env->GetStringUTFChars(functionNameS, 0);

	push	0
	mov	edx, DWORD PTR _functionNameS$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetStringUTFChars@JNIEnv_@@QAEPBDPAV_jstring@@PAE@Z ; JNIEnv_::GetStringUTFChars
	mov	DWORD PTR _functionName$[ebp], eax

; 144  :  
; 145  : #ifdef _WINDOWS
; 146  :  
; 147  :  HMODULE mod = LoadLibrary(moduleName);

	mov	esi, esp
	mov	eax, DWORD PTR _moduleName$[ebp]
	push	eax
	call	DWORD PTR __imp__LoadLibraryA@4
	cmp	esi, esp
	call	__chkesp
	mov	DWORD PTR _mod$[ebp], eax

; 148  :  
; 149  :  if (mod == NULL) {

	cmp	DWORD PTR _mod$[ebp], 0
	jne	SHORT $L56839

; 150  :   /* no such module or DllMain returned FALSE */
; 151  :   env->SetIntField(obj, fieldLastErrorCode, GetLastError());

	mov	esi, esp
	call	DWORD PTR __imp__GetLastError@0
	cmp	esi, esp
	call	__chkesp
	push	eax
	mov	ecx, DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	ecx
	mov	edx, DWORD PTR _obj$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField

; 152  :   out = JNI_FALSE;

	mov	BYTE PTR _out$[ebp], 0

; 154  :  else {

	jmp	$L56843
$L56839:

; 155  :   FARPROC addr = GetProcAddress(mod, functionName);

	mov	esi, esp
	mov	eax, DWORD PTR _functionName$[ebp]
	push	eax
	mov	ecx, DWORD PTR _mod$[ebp]
	push	ecx
	call	DWORD PTR __imp__GetProcAddress@8
	cmp	esi, esp
	call	__chkesp
	mov	DWORD PTR _addr$56841[ebp], eax

; 156  :   if (addr == NULL) {

	cmp	DWORD PTR _addr$56841[ebp], 0
	jne	SHORT $L56842

; 157  :    /* function not found */
; 158  :    FreeLibrary(mod);

	mov	esi, esp
	mov	edx, DWORD PTR _mod$[ebp]
	push	edx
	call	DWORD PTR __imp__FreeLibrary@4
	cmp	esi, esp
	call	__chkesp

; 159  :    env->SetIntField(obj, fieldLastErrorCode, GetLastError());

	mov	esi, esp
	call	DWORD PTR __imp__GetLastError@0
	cmp	esi, esp
	call	__chkesp
	push	eax
	mov	eax, DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField

; 160  :    out = JNI_FALSE;

	mov	BYTE PTR _out$[ebp], 0

; 162  :   else {

	jmp	SHORT $L56843
$L56842:

; 163  :    /* all ok */ 
; 164  :    env->SetIntField(obj, fieldModuleHandle, (jint) mod);

	mov	edx, DWORD PTR _mod$[ebp]
	push	edx
	mov	eax, DWORD PTR ?fieldModuleHandle@@3PAU_jfieldID@@A ; fieldModuleHandle
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField

; 165  :    env->SetIntField(obj, fieldFunctionHandle, (jint) addr);

	mov	edx, DWORD PTR _addr$56841[ebp]
	push	edx
	mov	eax, DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A ; fieldFunctionHandle
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField
$L56843:

; 168  :  
; 169  : #endif
; 170  :  
; 171  :  env->ReleaseStringUTFChars(moduleNameS, moduleName);

	mov	edx, DWORD PTR _moduleName$[ebp]
	push	edx
	mov	eax, DWORD PTR _moduleNameS$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?ReleaseStringUTFChars@JNIEnv_@@QAEXPAV_jstring@@PBD@Z ; JNIEnv_::ReleaseStringUTFChars

; 172  :  env->ReleaseStringUTFChars(functionNameS, functionName);

	mov	ecx, DWORD PTR _functionName$[ebp]
	push	ecx
	mov	edx, DWORD PTR _functionNameS$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?ReleaseStringUTFChars@JNIEnv_@@QAEXPAV_jstring@@PBD@Z ; JNIEnv_::ReleaseStringUTFChars

; 173  :  
; 174  :  return out;

	mov	al, BYTE PTR _out$[ebp]

; 175  :  
; 176  : }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 92					; 0000005cH
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
_Java_com_eaio_nativecall_NativeCall_initHandles@8 ENDP
_TEXT	ENDS
PUBLIC	?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z ; JNIEnv_::GetIntField
PUBLIC	_Java_com_eaio_nativecall_NativeCall_getLastError@8
PUBLIC	?NewStringUTF@JNIEnv_@@QAEPAV_jstring@@PBD@Z	; JNIEnv_::NewStringUTF
EXTRN	__imp__FormatMessageA@28:NEAR
EXTRN	__imp__LocalFree@4:NEAR
;	COMDAT _Java_com_eaio_nativecall_NativeCall_getLastError@8
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_lastError$ = -4
_out$ = -8
_msgBufPtr$ = -12
_Java_com_eaio_nativecall_NativeCall_getLastError@8 PROC NEAR ; COMDAT

; 184  : (JNIEnv *env, jobject obj) {

	push	ebp
	mov	ebp, esp
	sub	esp, 76					; 0000004cH
	push	ebx
	push	esi
	push	edi
	lea	edi, DWORD PTR [ebp-76]
	mov	ecx, 19					; 00000013H
	mov	eax, -858993460				; ccccccccH
	rep stosd

; 185  :  
; 186  :  jint lastError = env->GetIntField(obj, fieldLastErrorCode);

	mov	eax, DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z ; JNIEnv_::GetIntField
	mov	DWORD PTR _lastError$[ebp], eax

; 187  :  
; 188  :  if (lastError == 0) return NULL;

	cmp	DWORD PTR _lastError$[ebp], 0
	jne	SHORT $L56851
	xor	eax, eax
	jmp	SHORT $L56849
$L56851:

; 189  :  
; 190  :  jstring out = NULL;

	mov	DWORD PTR _out$[ebp], 0

; 191  :  
; 192  : #ifdef _WINDOWS
; 193  :  
; 194  :  LPVOID msgBufPtr = NULL;

	mov	DWORD PTR _msgBufPtr$[ebp], 0

; 195  :  FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER | 
; 196  :   FORMAT_MESSAGE_FROM_SYSTEM, NULL, lastError, 0,
; 197  :   (LPSTR) &msgBufPtr, 0, NULL);

	mov	esi, esp
	push	0
	push	0
	lea	edx, DWORD PTR _msgBufPtr$[ebp]
	push	edx
	push	0
	mov	eax, DWORD PTR _lastError$[ebp]
	push	eax
	push	0
	push	4352					; 00001100H
	call	DWORD PTR __imp__FormatMessageA@28
	cmp	esi, esp
	call	__chkesp

; 198  :  
; 199  :  out = env->NewStringUTF((char*) msgBufPtr);

	mov	ecx, DWORD PTR _msgBufPtr$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?NewStringUTF@JNIEnv_@@QAEPAV_jstring@@PBD@Z ; JNIEnv_::NewStringUTF
	mov	DWORD PTR _out$[ebp], eax

; 200  :  
; 201  :  LocalFree(msgBufPtr);

	mov	esi, esp
	mov	edx, DWORD PTR _msgBufPtr$[ebp]
	push	edx
	call	DWORD PTR __imp__LocalFree@4
	cmp	esi, esp
	call	__chkesp

; 202  :  
; 203  : #endif
; 204  :  
; 205  :  return out;

	mov	eax, DWORD PTR _out$[ebp]
$L56849:

; 206  :  
; 207  : }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 76					; 0000004cH
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
_Java_com_eaio_nativecall_NativeCall_getLastError@8 ENDP
_TEXT	ENDS
PUBLIC	_Java_com_eaio_nativecall_NativeCall_destroy@8
;	COMDAT _Java_com_eaio_nativecall_NativeCall_destroy@8
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_module$ = -4
_Java_com_eaio_nativecall_NativeCall_destroy@8 PROC NEAR ; COMDAT

; 215  : (JNIEnv *env, jobject obj) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd

; 216  :  
; 217  :  jint module = env->GetIntField(obj, fieldModuleHandle);

	mov	eax, DWORD PTR ?fieldModuleHandle@@3PAU_jfieldID@@A ; fieldModuleHandle
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z ; JNIEnv_::GetIntField
	mov	DWORD PTR _module$[ebp], eax

; 218  : 
; 219  :  if (module == 0) return;

	cmp	DWORD PTR _module$[ebp], 0
	jne	SHORT $L56861
	jmp	SHORT $L56859
$L56861:

; 220  :  
; 221  : #ifdef _WINDOWS
; 222  :   
; 223  :  if (FreeLibrary((HMODULE) module) == 0) {

	mov	esi, esp
	mov	edx, DWORD PTR _module$[ebp]
	push	edx
	call	DWORD PTR __imp__FreeLibrary@4
	cmp	esi, esp
	call	__chkesp
	test	eax, eax
	jne	SHORT $L56863

; 224  :   env->SetIntField(obj, fieldLastErrorCode, GetLastError());

	mov	esi, esp
	call	DWORD PTR __imp__GetLastError@0
	cmp	esi, esp
	call	__chkesp
	push	eax
	mov	eax, DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField
$L56863:

; 226  :   
; 227  : #endif
; 228  :  
; 229  :  env->SetIntField(obj, fieldModuleHandle, 0);

	push	0
	mov	edx, DWORD PTR ?fieldModuleHandle@@3PAU_jfieldID@@A ; fieldModuleHandle
	push	edx
	mov	eax, DWORD PTR _obj$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField

; 230  :  env->SetIntField(obj, fieldFunctionHandle, 0);

	push	0
	mov	ecx, DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A ; fieldFunctionHandle
	push	ecx
	mov	edx, DWORD PTR _obj$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField
$L56859:

; 231  :  
; 232  :  return;
; 233  :  
; 234  : }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
_Java_com_eaio_nativecall_NativeCall_destroy@8 ENDP
_TEXT	ENDS
;	COMDAT ?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z
_TEXT	SEGMENT
_this$ = -4
_lobj$ = 8
?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z PROC NEAR ; JNIEnv_::NewGlobalRef, COMDAT

; 810  :     jobject NewGlobalRef(jobject lobj) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 811  :         return functions->NewGlobalRef(this,lobj);

	mov	esi, esp
	mov	eax, DWORD PTR _lobj$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	mov	eax, DWORD PTR [edx]
	call	DWORD PTR [eax+84]
	cmp	esi, esp
	call	__chkesp

; 812  :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	4
?NewGlobalRef@JNIEnv_@@QAEPAV_jobject@@PAV2@@Z ENDP	; JNIEnv_::NewGlobalRef
_TEXT	ENDS
;	COMDAT ?GetMethodID@JNIEnv_@@QAEPAU_jmethodID@@PAV_jclass@@PBD1@Z
_TEXT	SEGMENT
_this$ = -4
_clazz$ = 8
_name$ = 12
_sig$ = 16
?GetMethodID@JNIEnv_@@QAEPAU_jmethodID@@PAV_jclass@@PBD1@Z PROC NEAR ; JNIEnv_::GetMethodID, COMDAT

; 859  :                           const char *sig) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 860  :         return functions->GetMethodID(this,clazz,name,sig);

	mov	esi, esp
	mov	eax, DWORD PTR _sig$[ebp]
	push	eax
	mov	ecx, DWORD PTR _name$[ebp]
	push	ecx
	mov	edx, DWORD PTR _clazz$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	mov	edx, DWORD PTR [ecx]
	call	DWORD PTR [edx+132]
	cmp	esi, esp
	call	__chkesp

; 861  :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	12					; 0000000cH
?GetMethodID@JNIEnv_@@QAEPAU_jmethodID@@PAV_jclass@@PBD1@Z ENDP ; JNIEnv_::GetMethodID
_TEXT	ENDS
;	COMDAT ?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z
_TEXT	SEGMENT
_clazz$ = 8
_name$ = 12
_sig$ = 16
_this$ = -4
?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z PROC NEAR ; JNIEnv_::GetFieldID, COMDAT

; 1244 :                         const char *sig) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 1245 :         return functions->GetFieldID(this,clazz,name,sig);

	mov	esi, esp
	mov	eax, DWORD PTR _sig$[ebp]
	push	eax
	mov	ecx, DWORD PTR _name$[ebp]
	push	ecx
	mov	edx, DWORD PTR _clazz$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	mov	edx, DWORD PTR [ecx]
	call	DWORD PTR [edx+376]
	cmp	esi, esp
	call	__chkesp

; 1246 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	12					; 0000000cH
?GetFieldID@JNIEnv_@@QAEPAU_jfieldID@@PAV_jclass@@PBD1@Z ENDP ; JNIEnv_::GetFieldID
_TEXT	ENDS
;	COMDAT ?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z
_TEXT	SEGMENT
_obj$ = 8
_fieldID$ = 12
_this$ = -4
?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z PROC NEAR ; JNIEnv_::GetObjectField, COMDAT

; 1248 :     jobject GetObjectField(jobject obj, jfieldID fieldID) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 1249 :         return functions->GetObjectField(this,obj,fieldID);

	mov	esi, esp
	mov	eax, DWORD PTR _fieldID$[ebp]
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	mov	ecx, DWORD PTR [eax]
	call	DWORD PTR [ecx+380]
	cmp	esi, esp
	call	__chkesp

; 1250 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z ENDP ; JNIEnv_::GetObjectField
_TEXT	ENDS
;	COMDAT ?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z
_TEXT	SEGMENT
_obj$ = 8
_fieldID$ = 12
_this$ = -4
?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z PROC NEAR ; JNIEnv_::GetIntField, COMDAT

; 1263 :     jint GetIntField(jobject obj, jfieldID fieldID) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 1264 :         return functions->GetIntField(this,obj,fieldID);

	mov	esi, esp
	mov	eax, DWORD PTR _fieldID$[ebp]
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	mov	ecx, DWORD PTR [eax]
	call	DWORD PTR [ecx+400]
	cmp	esi, esp
	call	__chkesp

; 1265 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z ENDP ; JNIEnv_::GetIntField
_TEXT	ENDS
;	COMDAT ?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z
_TEXT	SEGMENT
_obj$ = 8
_fieldID$ = 12
_val$ = 16
_this$ = -4
?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z PROC NEAR ; JNIEnv_::SetIntField, COMDAT

; 1296 :                      jint val) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 1297 :         functions->SetIntField(this,obj,fieldID,val);

	mov	esi, esp
	mov	eax, DWORD PTR _val$[ebp]
	push	eax
	mov	ecx, DWORD PTR _fieldID$[ebp]
	push	ecx
	mov	edx, DWORD PTR _obj$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	mov	edx, DWORD PTR [ecx]
	call	DWORD PTR [edx+436]
	cmp	esi, esp
	call	__chkesp

; 1298 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	12					; 0000000cH
?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ENDP ; JNIEnv_::SetIntField
_TEXT	ENDS
;	COMDAT ?NewStringUTF@JNIEnv_@@QAEPAV_jstring@@PBD@Z
_TEXT	SEGMENT
_this$ = -4
_utf$ = 8
?NewStringUTF@JNIEnv_@@QAEPAV_jstring@@PBD@Z PROC NEAR	; JNIEnv_::NewStringUTF, COMDAT

; 1576 :     jstring NewStringUTF(const char *utf) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 1577 :         return functions->NewStringUTF(this,utf);

	mov	esi, esp
	mov	eax, DWORD PTR _utf$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	mov	eax, DWORD PTR [edx]
	call	DWORD PTR [eax+668]
	cmp	esi, esp
	call	__chkesp

; 1578 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	4
?NewStringUTF@JNIEnv_@@QAEPAV_jstring@@PBD@Z ENDP	; JNIEnv_::NewStringUTF
_TEXT	ENDS
;	COMDAT ?GetStringUTFChars@JNIEnv_@@QAEPBDPAV_jstring@@PAE@Z
_TEXT	SEGMENT
_this$ = -4
_str$ = 8
_isCopy$ = 12
?GetStringUTFChars@JNIEnv_@@QAEPBDPAV_jstring@@PAE@Z PROC NEAR ; JNIEnv_::GetStringUTFChars, COMDAT

; 1582 :     const char* GetStringUTFChars(jstring str, jboolean *isCopy) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 1583 :         return functions->GetStringUTFChars(this,str,isCopy);

	mov	esi, esp
	mov	eax, DWORD PTR _isCopy$[ebp]
	push	eax
	mov	ecx, DWORD PTR _str$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	mov	ecx, DWORD PTR [eax]
	call	DWORD PTR [ecx+676]
	cmp	esi, esp
	call	__chkesp

; 1584 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
?GetStringUTFChars@JNIEnv_@@QAEPBDPAV_jstring@@PAE@Z ENDP ; JNIEnv_::GetStringUTFChars
_TEXT	ENDS
;	COMDAT ?ReleaseStringUTFChars@JNIEnv_@@QAEXPAV_jstring@@PBD@Z
_TEXT	SEGMENT
_this$ = -4
_str$ = 8
_chars$ = 12
?ReleaseStringUTFChars@JNIEnv_@@QAEXPAV_jstring@@PBD@Z PROC NEAR ; JNIEnv_::ReleaseStringUTFChars, COMDAT

; 1585 :     void ReleaseStringUTFChars(jstring str, const char* chars) {

	push	ebp
	mov	ebp, esp
	sub	esp, 68					; 00000044H
	push	ebx
	push	esi
	push	edi
	push	ecx
	lea	edi, DWORD PTR [ebp-68]
	mov	ecx, 17					; 00000011H
	mov	eax, -858993460				; ccccccccH
	rep stosd
	pop	ecx
	mov	DWORD PTR _this$[ebp], ecx

; 1586 :         functions->ReleaseStringUTFChars(this,str,chars);

	mov	esi, esp
	mov	eax, DWORD PTR _chars$[ebp]
	push	eax
	mov	ecx, DWORD PTR _str$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	mov	ecx, DWORD PTR [eax]
	call	DWORD PTR [ecx+680]
	cmp	esi, esp
	call	__chkesp

; 1587 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
?ReleaseStringUTFChars@JNIEnv_@@QAEXPAV_jstring@@PBD@Z ENDP ; JNIEnv_::ReleaseStringUTFChars
_TEXT	ENDS
END
