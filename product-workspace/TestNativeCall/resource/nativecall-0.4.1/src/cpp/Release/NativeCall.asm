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
_TLS	SEGMENT DWORD USE32 PUBLIC 'TLS'
_TLS	ENDS
;	COMDAT ??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_08JJOG@function?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_06CODG@module?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_01FLOP@I?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_0P@LKIL@functionHandle?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_0N@EFAA@moduleHandle?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_0O@OMHL@lastErrorCode?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_0BL@DALH@com?1eaio?1nativecall?1Holder?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_0BD@GEDI@Ljava?1lang?1Object?$DL?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_01PGHN@o?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_0BC@IGNJ@java?1lang?1Boolean?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_0BC@FBKL@java?1lang?1Integer?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_0BB@LLFN@java?1lang?1String?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_02LOEJ@?$FLB?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_02BENO@?$FLC?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_03KJOK@?$CI?$CJZ?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_0N@KEBP@booleanValue?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_03PPCD@?$CI?$CJI?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_08JCMA@intValue?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_04ECLF@?$CII?$CJV?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_06KILP@?$DMinit?$DO?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
;	COMDAT ??_C@_04JFOE@?$CIZ?$CJV?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
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
PUBLIC	_Java_com_eaio_nativecall_NativeCall_initIDs@8
;	COMDAT ??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@
; File C:\Program Files\IBMJava13\include\jni.h
_DATA	SEGMENT
??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@ DB 'Ljava/lang/String;', 00H ; `string'
_DATA	ENDS
;	COMDAT ??_C@_08JJOG@function?$AA@
_DATA	SEGMENT
??_C@_08JJOG@function?$AA@ DB 'function', 00H		; `string'
_DATA	ENDS
;	COMDAT ??_C@_06CODG@module?$AA@
_DATA	SEGMENT
??_C@_06CODG@module?$AA@ DB 'module', 00H		; `string'
_DATA	ENDS
;	COMDAT ??_C@_01FLOP@I?$AA@
_DATA	SEGMENT
??_C@_01FLOP@I?$AA@ DB 'I', 00H				; `string'
_DATA	ENDS
;	COMDAT ??_C@_0P@LKIL@functionHandle?$AA@
_DATA	SEGMENT
??_C@_0P@LKIL@functionHandle?$AA@ DB 'functionHandle', 00H ; `string'
_DATA	ENDS
;	COMDAT ??_C@_0N@EFAA@moduleHandle?$AA@
_DATA	SEGMENT
??_C@_0N@EFAA@moduleHandle?$AA@ DB 'moduleHandle', 00H	; `string'
_DATA	ENDS
;	COMDAT ??_C@_0O@OMHL@lastErrorCode?$AA@
_DATA	SEGMENT
??_C@_0O@OMHL@lastErrorCode?$AA@ DB 'lastErrorCode', 00H ; `string'
_DATA	ENDS
;	COMDAT ??_C@_0BL@DALH@com?1eaio?1nativecall?1Holder?$AA@
_DATA	SEGMENT
??_C@_0BL@DALH@com?1eaio?1nativecall?1Holder?$AA@ DB 'com/eaio/nativecall'
	DB	'/Holder', 00H				; `string'
_DATA	ENDS
;	COMDAT ??_C@_0BD@GEDI@Ljava?1lang?1Object?$DL?$AA@
_DATA	SEGMENT
??_C@_0BD@GEDI@Ljava?1lang?1Object?$DL?$AA@ DB 'Ljava/lang/Object;', 00H ; `string'
_DATA	ENDS
;	COMDAT ??_C@_01PGHN@o?$AA@
_DATA	SEGMENT
??_C@_01PGHN@o?$AA@ DB 'o', 00H				; `string'
_DATA	ENDS
;	COMDAT ??_C@_0BC@IGNJ@java?1lang?1Boolean?$AA@
_DATA	SEGMENT
??_C@_0BC@IGNJ@java?1lang?1Boolean?$AA@ DB 'java/lang/Boolean', 00H ; `string'
_DATA	ENDS
;	COMDAT ??_C@_0BC@FBKL@java?1lang?1Integer?$AA@
_DATA	SEGMENT
??_C@_0BC@FBKL@java?1lang?1Integer?$AA@ DB 'java/lang/Integer', 00H ; `string'
_DATA	ENDS
;	COMDAT ??_C@_0BB@LLFN@java?1lang?1String?$AA@
_DATA	SEGMENT
??_C@_0BB@LLFN@java?1lang?1String?$AA@ DB 'java/lang/String', 00H ; `string'
_DATA	ENDS
;	COMDAT ??_C@_02LOEJ@?$FLB?$AA@
_DATA	SEGMENT
??_C@_02LOEJ@?$FLB?$AA@ DB '[B', 00H			; `string'
_DATA	ENDS
;	COMDAT ??_C@_02BENO@?$FLC?$AA@
_DATA	SEGMENT
??_C@_02BENO@?$FLC?$AA@ DB '[C', 00H			; `string'
_DATA	ENDS
;	COMDAT ??_C@_03KJOK@?$CI?$CJZ?$AA@
_DATA	SEGMENT
??_C@_03KJOK@?$CI?$CJZ?$AA@ DB '()Z', 00H		; `string'
_DATA	ENDS
;	COMDAT ??_C@_0N@KEBP@booleanValue?$AA@
_DATA	SEGMENT
??_C@_0N@KEBP@booleanValue?$AA@ DB 'booleanValue', 00H	; `string'
_DATA	ENDS
;	COMDAT ??_C@_03PPCD@?$CI?$CJI?$AA@
_DATA	SEGMENT
??_C@_03PPCD@?$CI?$CJI?$AA@ DB '()I', 00H		; `string'
_DATA	ENDS
;	COMDAT ??_C@_08JCMA@intValue?$AA@
_DATA	SEGMENT
??_C@_08JCMA@intValue?$AA@ DB 'intValue', 00H		; `string'
_DATA	ENDS
;	COMDAT ??_C@_04ECLF@?$CII?$CJV?$AA@
_DATA	SEGMENT
??_C@_04ECLF@?$CII?$CJV?$AA@ DB '(I)V', 00H		; `string'
_DATA	ENDS
;	COMDAT ??_C@_06KILP@?$DMinit?$DO?$AA@
_DATA	SEGMENT
??_C@_06KILP@?$DMinit?$DO?$AA@ DB '<init>', 00H		; `string'
_DATA	ENDS
;	COMDAT ??_C@_04JFOE@?$CIZ?$CJV?$AA@
_DATA	SEGMENT
??_C@_04JFOE@?$CIZ?$CJV?$AA@ DB '(Z)V', 00H		; `string'
_DATA	ENDS
_TEXT	SEGMENT
_env$ = 8
_cls$ = 12
_Java_com_eaio_nativecall_NativeCall_initIDs@8 PROC NEAR

; 71   : (JNIEnv *env, jclass cls) {

	push	ebx

; 72   : 
; 73   :  // NativeCall fields
; 74   : 
; 75   :  fieldFunction = env->GetFieldID(cls, "function", "Ljava/lang/String;");

	mov	ebx, DWORD PTR _cls$[esp]
	push	esi
	mov	esi, DWORD PTR _env$[esp+4]
	push	edi
	mov	edi, OFFSET FLAT:??_C@_0BD@DFD@Ljava?1lang?1String?$DL?$AA@ ; `string'
	mov	eax, DWORD PTR [esi]
	push	edi
	push	OFFSET FLAT:??_C@_08JJOG@function?$AA@	; `string'
	push	ebx
	push	esi
	call	DWORD PTR [eax+376]

; 76   :  fieldModule = env->GetFieldID(cls, "module", "Ljava/lang/String;");

	push	edi
	mov	DWORD PTR ?fieldFunction@@3PAU_jfieldID@@A, eax ; fieldFunction
	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_06CODG@module?$AA@	; `string'
	push	ebx
	push	esi
	call	DWORD PTR [eax+376]

; 77   : 
; 78   :  fieldFunctionHandle = env->GetFieldID(cls, "functionHandle", "I");

	mov	edi, OFFSET FLAT:??_C@_01FLOP@I?$AA@	; `string'
	mov	DWORD PTR ?fieldModule@@3PAU_jfieldID@@A, eax ; fieldModule
	mov	eax, DWORD PTR [esi]
	push	edi
	push	OFFSET FLAT:??_C@_0P@LKIL@functionHandle?$AA@ ; `string'
	push	ebx
	push	esi
	call	DWORD PTR [eax+376]

; 79   :  fieldModuleHandle = env-> GetFieldID(cls, "moduleHandle", "I");

	push	edi
	mov	DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A, eax ; fieldFunctionHandle
	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_0N@EFAA@moduleHandle?$AA@ ; `string'
	push	ebx
	push	esi
	call	DWORD PTR [eax+376]

; 80   : 
; 81   :  fieldLastErrorCode = env->GetFieldID(cls, "lastErrorCode", "I");

	push	edi
	mov	DWORD PTR ?fieldModuleHandle@@3PAU_jfieldID@@A, eax ; fieldModuleHandle
	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_0O@OMHL@lastErrorCode?$AA@ ; `string'
	push	ebx
	push	esi
	call	DWORD PTR [eax+376]
	mov	DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A, eax ; fieldLastErrorCode

; 82   : 
; 83   :  // Holder fields
; 84   : 
; 85   :  classHolder = (jclass) env->NewGlobalRef(env->FindClass("com/eaio/nativecall/Holder"));

	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_0BL@DALH@com?1eaio?1nativecall?1Holder?$AA@ ; `string'
	push	esi
	call	DWORD PTR [eax+24]
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	esi
	call	DWORD PTR [ecx+84]

; 86   :  fieldHolderO = env->GetFieldID(classHolder, "o", "Ljava/lang/Object;");

	mov	ecx, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_0BD@GEDI@Ljava?1lang?1Object?$DL?$AA@ ; `string'
	push	OFFSET FLAT:??_C@_01PGHN@o?$AA@		; `string'
	push	eax
	push	esi
	mov	DWORD PTR ?classHolder@@3PAV_jclass@@A, eax ; classHolder
	call	DWORD PTR [ecx+376]
	mov	DWORD PTR ?fieldHolderO@@3PAU_jfieldID@@A, eax ; fieldHolderO

; 87   : 
; 88   :  // Other classes
; 89   : 
; 90   :  classBoolean = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Boolean"));

	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_0BC@IGNJ@java?1lang?1Boolean?$AA@ ; `string'
	push	esi
	call	DWORD PTR [eax+24]
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	esi
	call	DWORD PTR [ecx+84]
	mov	DWORD PTR ?classBoolean@@3PAV_jclass@@A, eax ; classBoolean

; 91   :  /*classByte = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Byte"));
; 92   :  classCharacter = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Character"));
; 93   :  classShort = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Short"));*/
; 94   :  classInteger = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Integer"));

	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_0BC@FBKL@java?1lang?1Integer?$AA@ ; `string'
	push	esi
	call	DWORD PTR [eax+24]
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	esi
	call	DWORD PTR [ecx+84]
	mov	DWORD PTR ?classInteger@@3PAV_jclass@@A, eax ; classInteger

; 95   :  /*classLong = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Long"));
; 96   :  classFloat = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Float"));
; 97   :  classDouble = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Double"));*/
; 98   :  classString = (jclass) env->NewGlobalRef(env->FindClass("java/lang/String"));

	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_0BB@LLFN@java?1lang?1String?$AA@ ; `string'
	push	esi
	call	DWORD PTR [eax+24]
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	esi
	call	DWORD PTR [ecx+84]
	mov	DWORD PTR ?classString@@3PAV_jclass@@A, eax ; classString

; 99   :  classByteArray = (jclass) env->NewGlobalRef(env->FindClass("[B"));

	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_02LOEJ@?$FLB?$AA@	; `string'
	push	esi
	call	DWORD PTR [eax+24]
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	esi
	call	DWORD PTR [ecx+84]
	mov	DWORD PTR ?classByteArray@@3PAV_jclass@@A, eax ; classByteArray

; 100  :  classCharArray = (jclass) env->NewGlobalRef(env->FindClass("[C"));

	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_02BENO@?$FLC?$AA@	; `string'
	push	esi
	call	DWORD PTR [eax+24]
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	esi
	call	DWORD PTR [ecx+84]

; 101  :  /*classIntArray = (jclass) env->NewGlobalRef(env->FindClass("[I"));*/
; 102  : 
; 103  :  // Wrapper class methods
; 104  : 
; 105  :  methodBooleanValue = env->GetMethodID(classBoolean,
; 106  :   "booleanValue", "()Z");

	mov	ecx, DWORD PTR [esi]
	mov	DWORD PTR ?classCharArray@@3PAV_jclass@@A, eax ; classCharArray
	mov	eax, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	push	OFFSET FLAT:??_C@_03KJOK@?$CI?$CJZ?$AA@	; `string'
	push	OFFSET FLAT:??_C@_0N@KEBP@booleanValue?$AA@ ; `string'
	push	eax
	push	esi
	call	DWORD PTR [ecx+132]

; 107  :  /*methodByteValue = env->GetMethodID(classByte,
; 108  :   "byteValue", "()B");
; 109  :  methodCharValue = env->GetMethodID(classCharacter,
; 110  :   "charValue", "()C");
; 111  :  methodShortValue = env->GetMethodID(classShort,
; 112  :   "shortValue", "()S");*/
; 113  :  methodIntValue = env->GetMethodID(classInteger,
; 114  :   "intValue", "()I");

	mov	ecx, DWORD PTR [esi]
	mov	DWORD PTR ?methodBooleanValue@@3PAU_jmethodID@@A, eax ; methodBooleanValue
	mov	eax, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	push	OFFSET FLAT:??_C@_03PPCD@?$CI?$CJI?$AA@	; `string'
	push	OFFSET FLAT:??_C@_08JCMA@intValue?$AA@	; `string'
	push	eax
	push	esi
	call	DWORD PTR [ecx+132]

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

	mov	ecx, DWORD PTR [esi]
	mov	DWORD PTR ?methodIntValue@@3PAU_jmethodID@@A, eax ; methodIntValue
	mov	eax, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	mov	edi, OFFSET FLAT:??_C@_06KILP@?$DMinit?$DO?$AA@ ; `string'
	push	OFFSET FLAT:??_C@_04ECLF@?$CII?$CJV?$AA@ ; `string'
	push	edi
	push	eax
	push	esi
	call	DWORD PTR [ecx+132]

; 125  :  newBooleanBoolean = env->GetMethodID(classBoolean, "<init>", "(Z)V");

	mov	ecx, DWORD PTR [esi]
	mov	DWORD PTR ?newIntegerInt@@3PAU_jmethodID@@A, eax ; newIntegerInt
	mov	eax, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	push	OFFSET FLAT:??_C@_04JFOE@?$CIZ?$CJV?$AA@ ; `string'
	push	edi
	push	eax
	push	esi
	call	DWORD PTR [ecx+132]
	pop	edi
	pop	esi
	mov	DWORD PTR ?newBooleanBoolean@@3PAU_jmethodID@@A, eax ; newBooleanBoolean
	pop	ebx

; 126  :   
; 127  : }

	ret	8
_Java_com_eaio_nativecall_NativeCall_initIDs@8 ENDP
_TEXT	ENDS
PUBLIC	_Java_com_eaio_nativecall_NativeCall_initHandles@8
EXTRN	__imp__LoadLibraryA@4:NEAR
EXTRN	__imp__GetLastError@0:NEAR
EXTRN	__imp__FreeLibrary@4:NEAR
EXTRN	__imp__GetProcAddress@8:NEAR
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_out$ = -1
_moduleNameS$ = 12
_functionNameS$ = -12
_moduleName$ = 8
_functionName$ = -8
_addr$27794 = -16
_Java_com_eaio_nativecall_NativeCall_initHandles@8 PROC NEAR

; 135  : (JNIEnv *env, jobject obj) {

	push	ebp
	mov	ebp, esp
	sub	esp, 16					; 00000010H

; 136  :   
; 137  :  bool out = JNI_TRUE;
; 138  :  
; 139  :  jstring moduleNameS = (jstring) env->GetObjectField(obj, fieldModule);

	mov	eax, DWORD PTR ?fieldModule@@3PAU_jfieldID@@A ; fieldModule
	push	ebx
	push	esi
	mov	esi, DWORD PTR _env$[ebp]
	push	edi
	mov	edi, DWORD PTR _obj$[ebp]
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	edi
	push	esi
	mov	BYTE PTR _out$[ebp], 1
	call	DWORD PTR [ecx+380]

; 140  :  jstring functionNameS = (jstring) env->GetObjectField(obj, fieldFunction);

	mov	ecx, DWORD PTR [esi]
	mov	DWORD PTR _moduleNameS$[ebp], eax
	mov	eax, DWORD PTR ?fieldFunction@@3PAU_jfieldID@@A ; fieldFunction
	push	eax
	push	edi
	push	esi
	call	DWORD PTR [ecx+380]

; 141  :  
; 142  :  const char* moduleName = env->GetStringUTFChars(moduleNameS, 0);

	push	0
	mov	DWORD PTR _functionNameS$[ebp], eax
	push	DWORD PTR _moduleNameS$[ebp]
	mov	eax, DWORD PTR [esi]
	push	esi
	call	DWORD PTR [eax+676]

; 143  :  const char* functionName = env->GetStringUTFChars(functionNameS, 0);

	push	0
	mov	DWORD PTR _moduleName$[ebp], eax
	push	DWORD PTR _functionNameS$[ebp]
	mov	eax, DWORD PTR [esi]
	push	esi
	call	DWORD PTR [eax+676]

; 144  :  
; 145  : #ifdef _WINDOWS
; 146  :  
; 147  :  HMODULE mod = LoadLibrary(moduleName);

	push	DWORD PTR _moduleName$[ebp]
	mov	DWORD PTR _functionName$[ebp], eax
	call	DWORD PTR __imp__LoadLibraryA@4
	mov	ebx, eax

; 148  :  
; 149  :  if (mod == NULL) {

	test	ebx, ebx
	jne	SHORT $L27792
$L27990:

; 150  :   /* no such module or DllMain returned FALSE */
; 151  :   env->SetIntField(obj, fieldLastErrorCode, GetLastError());

	call	DWORD PTR __imp__GetLastError@0
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	edi
	push	esi
	call	DWORD PTR [ecx+436]

; 152  :   out = JNI_FALSE;

	and	BYTE PTR _out$[ebp], 0

; 153  :  }
; 154  :  else {

	jmp	SHORT $L27982
$L27792:

; 155  :   FARPROC addr = GetProcAddress(mod, functionName);

	push	DWORD PTR _functionName$[ebp]
	push	ebx
	call	DWORD PTR __imp__GetProcAddress@8

; 156  :   if (addr == NULL) {

	test	eax, eax
	mov	DWORD PTR _addr$27794[ebp], eax
	jne	SHORT $L27795

; 157  :    /* function not found */
; 158  :    FreeLibrary(mod);

	push	ebx
	call	DWORD PTR __imp__FreeLibrary@4

; 159  :    env->SetIntField(obj, fieldLastErrorCode, GetLastError());
; 160  :    out = JNI_FALSE;
; 161  :   }
; 162  :   else {

	jmp	SHORT $L27990
$L27795:

; 163  :    /* all ok */ 
; 164  :    env->SetIntField(obj, fieldModuleHandle, (jint) mod);

	mov	eax, DWORD PTR [esi]
	push	ebx
	push	DWORD PTR ?fieldModuleHandle@@3PAU_jfieldID@@A ; fieldModuleHandle
	push	edi
	push	esi
	call	DWORD PTR [eax+436]

; 165  :    env->SetIntField(obj, fieldFunctionHandle, (jint) addr);

	push	DWORD PTR _addr$27794[ebp]
	mov	eax, DWORD PTR [esi]
	push	DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A ; fieldFunctionHandle
	push	edi
	push	esi
	call	DWORD PTR [eax+436]
$L27982:

; 166  :   }
; 167  :  }
; 168  :  
; 169  : #endif
; 170  :  
; 171  :  env->ReleaseStringUTFChars(moduleNameS, moduleName);

	push	DWORD PTR _moduleName$[ebp]
	mov	eax, DWORD PTR [esi]
	push	DWORD PTR _moduleNameS$[ebp]
	push	esi
	call	DWORD PTR [eax+680]

; 172  :  env->ReleaseStringUTFChars(functionNameS, functionName);

	push	DWORD PTR _functionName$[ebp]
	mov	eax, DWORD PTR [esi]
	push	DWORD PTR _functionNameS$[ebp]
	push	esi
	call	DWORD PTR [eax+680]

; 173  :  
; 174  :  return out;

	mov	al, BYTE PTR _out$[ebp]
	pop	edi
	pop	esi
	pop	ebx

; 175  :  
; 176  : }

	leave
	ret	8
_Java_com_eaio_nativecall_NativeCall_initHandles@8 ENDP
_TEXT	ENDS
PUBLIC	_Java_com_eaio_nativecall_NativeCall_getLastError@8
EXTRN	__imp__FormatMessageA@28:NEAR
EXTRN	__imp__LocalFree@4:NEAR
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_msgBufPtr$ = 8
_Java_com_eaio_nativecall_NativeCall_getLastError@8 PROC NEAR

; 184  : (JNIEnv *env, jobject obj) {

	push	ebp
	mov	ebp, esp

; 185  :  
; 186  :  jint lastError = env->GetIntField(obj, fieldLastErrorCode);

	mov	eax, DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	esi
	mov	esi, DWORD PTR _env$[ebp]
	push	eax
	push	DWORD PTR _obj$[ebp]
	mov	ecx, DWORD PTR [esi]
	push	esi
	call	DWORD PTR [ecx+400]

; 187  :  
; 188  :  if (lastError == 0) return NULL;

	xor	ecx, ecx
	cmp	eax, ecx
	jne	SHORT $L27804
	xor	eax, eax
	jmp	SHORT $L27802
$L27804:

; 189  :  
; 190  :  jstring out = NULL;
; 191  :  
; 192  : #ifdef _WINDOWS
; 193  :  
; 194  :  LPVOID msgBufPtr = NULL;
; 195  :  FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER | 
; 196  :   FORMAT_MESSAGE_FROM_SYSTEM, NULL, lastError, 0,
; 197  :   (LPSTR) &msgBufPtr, 0, NULL);

	push	ecx
	lea	edx, DWORD PTR _msgBufPtr$[ebp]
	push	ecx
	push	edx
	push	ecx
	push	eax
	push	ecx
	push	4352					; 00001100H
	mov	DWORD PTR _msgBufPtr$[ebp], ecx
	call	DWORD PTR __imp__FormatMessageA@28

; 198  :  
; 199  :  out = env->NewStringUTF((char*) msgBufPtr);

	push	DWORD PTR _msgBufPtr$[ebp]
	mov	eax, DWORD PTR [esi]
	push	esi
	call	DWORD PTR [eax+668]

; 200  :  
; 201  :  LocalFree(msgBufPtr);

	push	DWORD PTR _msgBufPtr$[ebp]
	mov	esi, eax
	call	DWORD PTR __imp__LocalFree@4

; 202  :  
; 203  : #endif
; 204  :  
; 205  :  return out;

	mov	eax, esi
$L27802:
	pop	esi

; 206  :  
; 207  : }

	pop	ebp
	ret	8
_Java_com_eaio_nativecall_NativeCall_getLastError@8 ENDP
_TEXT	ENDS
PUBLIC	_Java_com_eaio_nativecall_NativeCall_destroy@8
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_Java_com_eaio_nativecall_NativeCall_destroy@8 PROC NEAR

; 216  :  
; 217  :  jint module = env->GetIntField(obj, fieldModuleHandle);

	mov	eax, DWORD PTR ?fieldModuleHandle@@3PAU_jfieldID@@A ; fieldModuleHandle
	push	esi
	mov	esi, DWORD PTR _env$[esp]
	push	edi
	mov	edi, DWORD PTR _obj$[esp+4]
	push	eax
	mov	ecx, DWORD PTR [esi]
	push	edi
	push	esi
	call	DWORD PTR [ecx+400]

; 218  : 
; 219  :  if (module == 0) return;

	test	eax, eax
	je	SHORT $L28016

; 220  :  
; 221  : #ifdef _WINDOWS
; 222  :   
; 223  :  if (FreeLibrary((HMODULE) module) == 0) {

	push	eax
	call	DWORD PTR __imp__FreeLibrary@4
	test	eax, eax
	jne	SHORT $L28007

; 224  :   env->SetIntField(obj, fieldLastErrorCode, GetLastError());

	call	DWORD PTR __imp__GetLastError@0
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	edi
	push	esi
	call	DWORD PTR [ecx+436]
$L28007:

; 225  :  }
; 226  :   
; 227  : #endif
; 228  :  
; 229  :  env->SetIntField(obj, fieldModuleHandle, 0);

	mov	eax, DWORD PTR [esi]
	push	0
	push	DWORD PTR ?fieldModuleHandle@@3PAU_jfieldID@@A ; fieldModuleHandle
	push	edi
	push	esi
	call	DWORD PTR [eax+436]

; 230  :  env->SetIntField(obj, fieldFunctionHandle, 0);

	mov	eax, DWORD PTR [esi]
	push	0
	push	DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A ; fieldFunctionHandle
	push	edi
	push	esi
	call	DWORD PTR [eax+436]
$L28016:
	pop	edi
	pop	esi

; 231  :  
; 232  :  return;
; 233  :  
; 234  : }

	ret	8
_Java_com_eaio_nativecall_NativeCall_destroy@8 ENDP
_TEXT	ENDS
END
