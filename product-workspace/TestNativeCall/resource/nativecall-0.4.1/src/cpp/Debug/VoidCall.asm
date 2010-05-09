	TITLE	C:\Documents and Settings\Administrator\My Documents\Software\NativeCall\src\cpp\VoidCall.cpp
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
;	COMDAT ??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@
CONST	SEGMENT DWORD USE32 PUBLIC 'CONST'
CONST	ENDS
;	COMDAT ?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?ThrowNew@JNIEnv_@@QAEJPAV_jclass@@PBD@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?SetObjectField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@0@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetArrayLength@JNIEnv_@@QAEJPAV_jarray@@@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetObjectArrayElement@JNIEnv_@@QAEPAV_jobject@@PAV_jobjectArray@@J@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?GetPrimitiveArrayCritical@JNIEnv_@@QAEPAXPAV_jarray@@PAE@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ?ReleasePrimitiveArrayCritical@JNIEnv_@@QAEXPAV_jarray@@PAXJ@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT ??8@YAHABU_GUID@@0@Z
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT _Java_com_eaio_nativecall_VoidCall_executeCall@8
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
;	COMDAT _Java_com_eaio_nativecall_VoidCall_executeCall0@12
_TEXT	SEGMENT PARA USE32 PUBLIC 'CODE'
_TEXT	ENDS
FLAT	GROUP _DATA, CONST, _BSS
	ASSUME	CS: FLAT, DS: FLAT, SS: FLAT
endif
PUBLIC	?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z ; JNIEnv_::GetIntField
PUBLIC	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField
PUBLIC	_Java_com_eaio_nativecall_VoidCall_executeCall@8
EXTRN	__imp__GetLastError@0:NEAR
EXTRN	?fieldFunctionHandle@@3PAU_jfieldID@@A:DWORD	; fieldFunctionHandle
EXTRN	?fieldLastErrorCode@@3PAU_jfieldID@@A:DWORD	; fieldLastErrorCode
EXTRN	__chkesp:NEAR
;	COMDAT _Java_com_eaio_nativecall_VoidCall_executeCall@8
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_functionHandle$ = -4
_Java_com_eaio_nativecall_VoidCall_executeCall@8 PROC NEAR ; COMDAT

; 79   : (JNIEnv *env, jobject obj) {

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

; 80   : 
; 81   :  jint functionHandle = env->GetIntField(obj, fieldFunctionHandle);

	mov	eax, DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A ; fieldFunctionHandle
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z ; JNIEnv_::GetIntField
	mov	DWORD PTR _functionHandle$[ebp], eax

; 82   : 
; 83   : #ifdef _WINDOWS
; 84   : #ifdef _X86_
; 85   : 
; 86   :  __asm {
; 87   : 
; 88   :   call functionHandle

	call	DWORD PTR _functionHandle$[ebp]

; 89   : 
; 90   :  }
; 91   : 
; 92   : #endif
; 93   : #endif
; 94   : 
; 95   :   env->SetIntField(obj, fieldLastErrorCode, GetLastError());

	mov	esi, esp
	call	DWORD PTR __imp__GetLastError@0
	cmp	esi, esp
	call	__chkesp
	push	eax
	mov	edx, DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	edx
	mov	eax, DWORD PTR _obj$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetIntField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@J@Z ; JNIEnv_::SetIntField

; 96   : 
; 97   : }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
_Java_com_eaio_nativecall_VoidCall_executeCall@8 ENDP
_TEXT	ENDS
PUBLIC	?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z ; JNIEnv_::GetObjectField
PUBLIC	?SetObjectField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@0@Z ; JNIEnv_::SetObjectField
PUBLIC	_Java_com_eaio_nativecall_VoidCall_executeCall0@12
PUBLIC	?GetArrayLength@JNIEnv_@@QAEJPAV_jarray@@@Z	; JNIEnv_::GetArrayLength
PUBLIC	?GetObjectArrayElement@JNIEnv_@@QAEPAV_jobject@@PAV_jobjectArray@@J@Z ; JNIEnv_::GetObjectArrayElement
PUBLIC	??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@ ; `string'
PUBLIC	?GetPrimitiveArrayCritical@JNIEnv_@@QAEPAXPAV_jarray@@PAE@Z ; JNIEnv_::GetPrimitiveArrayCritical
PUBLIC	?ReleasePrimitiveArrayCritical@JNIEnv_@@QAEXPAV_jarray@@PAXJ@Z ; JNIEnv_::ReleasePrimitiveArrayCritical
PUBLIC	?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z	; JNIEnv_::FindClass
PUBLIC	?ThrowNew@JNIEnv_@@QAEJPAV_jclass@@PBD@Z	; JNIEnv_::ThrowNew
PUBLIC	?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ ; JNIEnv_::NewObject
PUBLIC	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
PUBLIC	?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallBooleanMethod
PUBLIC	?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallIntMethod
EXTRN	??2@YAPAXI@Z:NEAR				; operator new
EXTRN	??3@YAXPAX@Z:NEAR				; operator delete
EXTRN	?fieldHolderO@@3PAU_jfieldID@@A:DWORD		; fieldHolderO
EXTRN	?classBoolean@@3PAV_jclass@@A:DWORD		; classBoolean
EXTRN	?classInteger@@3PAV_jclass@@A:DWORD		; classInteger
EXTRN	?classByteArray@@3PAV_jclass@@A:DWORD		; classByteArray
EXTRN	?classCharArray@@3PAV_jclass@@A:DWORD		; classCharArray
EXTRN	?classHolder@@3PAV_jclass@@A:DWORD		; classHolder
EXTRN	?methodBooleanValue@@3PAU_jmethodID@@A:DWORD	; methodBooleanValue
EXTRN	?methodIntValue@@3PAU_jmethodID@@A:DWORD	; methodIntValue
EXTRN	?newIntegerInt@@3PAU_jmethodID@@A:DWORD		; newIntegerInt
EXTRN	?newBooleanBoolean@@3PAU_jmethodID@@A:DWORD	; newBooleanBoolean
EXTRN	_memset:NEAR
;	COMDAT ??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@
; File c:\documents and settings\administrator\my documents\software\nativecall\src\cpp\voidcall.cpp
CONST	SEGMENT
??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@ DB 'java/lang/OutOfMemor'
	DB	'yError', 00H				; `string'
CONST	ENDS
;	COMDAT _Java_com_eaio_nativecall_VoidCall_executeCall0@12
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_params$ = 16
_len$ = -4
_arrays$ = -8
_param$ = -12
_i$ = -16
_intArg$56816 = -20
_byteArrayArg$56819 = -24
_charArrayArg$56825 = -28
_booleanArg$56831 = -32
_tempArg$56832 = -36
_o$56835 = -40
_intPtr$56837 = -44
_byteArrayArg$56843 = -48
_charArrayArg$56849 = -52
_booleanArg$56855 = -56
_tempPtr$56856 = -60
_functionHandle$ = -64
_j$ = -68
_o$56873 = -72
_out$56875 = -76
_out$56885 = -80
$T56914 = -84
$T56915 = -88
$T56916 = -92
$T56917 = -96
$T56918 = -100
$T56919 = -104
_Java_com_eaio_nativecall_VoidCall_executeCall0@12 PROC NEAR ; COMDAT

; 105  : (JNIEnv *env, jobject obj, jobjectArray params) {

	push	ebp
	mov	ebp, esp
	sub	esp, 168				; 000000a8H
	push	ebx
	push	esi
	push	edi
	lea	edi, DWORD PTR [ebp-168]
	mov	ecx, 42					; 0000002aH
	mov	eax, -858993460				; ccccccccH
	rep stosd

; 106  :    
; 107  :  const int len = env->GetArrayLength(params);

	mov	eax, DWORD PTR _params$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetArrayLength@JNIEnv_@@QAEJPAV_jarray@@@Z ; JNIEnv_::GetArrayLength
	mov	DWORD PTR _len$[ebp], eax

; 108  :  
; 109  :  int* arrays = NULL;

	mov	DWORD PTR _arrays$[ebp], 0

; 110  :  if (!(arrays = new int[len])) {

	mov	ecx, DWORD PTR _len$[ebp]
	shl	ecx, 2
	push	ecx
	call	??2@YAPAXI@Z				; operator new
	add	esp, 4
	mov	DWORD PTR $T56914[ebp], eax
	mov	edx, DWORD PTR $T56914[ebp]
	mov	DWORD PTR _arrays$[ebp], edx
	cmp	DWORD PTR _arrays$[ebp], 0
	jne	SHORT $L56805

; 111  :   env->ThrowNew(env->FindClass("java/lang/OutOfMemoryError"), NULL);

	push	0
	push	OFFSET FLAT:??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@ ; `string'
	mov	ecx, DWORD PTR _env$[ebp]
	call	?FindClass@JNIEnv_@@QAEPAV_jclass@@PBD@Z ; JNIEnv_::FindClass
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?ThrowNew@JNIEnv_@@QAEJPAV_jclass@@PBD@Z ; JNIEnv_::ThrowNew

; 112  :   return;

	jmp	$L56800
$L56805:

; 114  :  memset(arrays, 0, (sizeof(int) * len));

	mov	eax, DWORD PTR _len$[ebp]
	shl	eax, 2
	push	eax
	push	0
	mov	ecx, DWORD PTR _arrays$[ebp]
	push	ecx
	call	_memset
	add	esp, 12					; 0000000cH

; 115  : 
; 116  :  jobject param;
; 117  : 
; 118  :  for (int i = len - 1; i >= 0; i--) {  

	mov	edx, DWORD PTR _len$[ebp]
	sub	edx, 1
	mov	DWORD PTR _i$[ebp], edx
	jmp	SHORT $L56810
$L56811:
	mov	eax, DWORD PTR _i$[ebp]
	sub	eax, 1
	mov	DWORD PTR _i$[ebp], eax
$L56810:
	cmp	DWORD PTR _i$[ebp], 0
	jl	$L56812

; 119  :   
; 120  :   param = env->GetObjectArrayElement(params, i);

	mov	ecx, DWORD PTR _i$[ebp]
	push	ecx
	mov	edx, DWORD PTR _params$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetObjectArrayElement@JNIEnv_@@QAEPAV_jobject@@PAV_jobjectArray@@J@Z ; JNIEnv_::GetObjectArrayElement
	mov	DWORD PTR _param$[ebp], eax

; 121  :   
; 122  :   if (param == NULL) {

	cmp	DWORD PTR _param$[ebp], 0
	jne	SHORT $L56813

; 123  :    _push(0);

	push	0

; 125  :   else if (env->IsInstanceOf(param, classInteger)) {

	jmp	$L56854
$L56813:
	mov	eax, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	push	eax
	mov	ecx, DWORD PTR _param$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56815

; 126  :    int intArg = env->CallIntMethod(param, methodIntValue);

	mov	edx, DWORD PTR ?methodIntValue@@3PAU_jmethodID@@A ; methodIntValue
	push	edx
	mov	eax, DWORD PTR _param$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	push	ecx
	call	?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallIntMethod
	add	esp, 12					; 0000000cH
	mov	DWORD PTR _intArg$56816[ebp], eax

; 127  :    _push(intArg)

	push	DWORD PTR _intArg$56816[ebp]

; 129  :   else if (env->IsInstanceOf(param, classByteArray)) {

	jmp	$L56854
$L56815:
	mov	edx, DWORD PTR ?classByteArray@@3PAV_jclass@@A ; classByteArray
	push	edx
	mov	eax, DWORD PTR _param$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56818

; 130  :    char* byteArrayArg = (char*) env->GetPrimitiveArrayCritical((jarray) param, 0);

	push	0
	mov	ecx, DWORD PTR _param$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetPrimitiveArrayCritical@JNIEnv_@@QAEPAXPAV_jarray@@PAE@Z ; JNIEnv_::GetPrimitiveArrayCritical
	mov	DWORD PTR _byteArrayArg$56819[ebp], eax

; 131  :    arrays[i] = (int) &byteArrayArg;

	mov	edx, DWORD PTR _i$[ebp]
	mov	eax, DWORD PTR _arrays$[ebp]
	lea	ecx, DWORD PTR _byteArrayArg$56819[ebp]
	mov	DWORD PTR [eax+edx*4], ecx

; 132  :    _push(byteArrayArg)

	push	DWORD PTR _byteArrayArg$56819[ebp]

; 134  :   else if (env->IsInstanceOf(param, classCharArray)) {

	jmp	$L56854
$L56818:
	mov	edx, DWORD PTR ?classCharArray@@3PAV_jclass@@A ; classCharArray
	push	edx
	mov	eax, DWORD PTR _param$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56824

; 135  :    unsigned short* charArrayArg = (unsigned short*) env->GetPrimitiveArrayCritical(
; 136  :     (jarray) param, 0);

	push	0
	mov	ecx, DWORD PTR _param$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetPrimitiveArrayCritical@JNIEnv_@@QAEPAXPAV_jarray@@PAE@Z ; JNIEnv_::GetPrimitiveArrayCritical
	mov	DWORD PTR _charArrayArg$56825[ebp], eax

; 137  :    arrays[i] = (int) &charArrayArg;

	mov	edx, DWORD PTR _i$[ebp]
	mov	eax, DWORD PTR _arrays$[ebp]
	lea	ecx, DWORD PTR _charArrayArg$56825[ebp]
	mov	DWORD PTR [eax+edx*4], ecx

; 138  :    _push(charArrayArg)

	push	DWORD PTR _charArrayArg$56825[ebp]

; 140  :   else if (env->IsInstanceOf(param, classBoolean)) {

	jmp	$L56854
$L56824:
	mov	edx, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	push	edx
	mov	eax, DWORD PTR _param$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56830

; 141  :    jboolean booleanArg = env->CallBooleanMethod(param, methodBooleanValue);

	mov	ecx, DWORD PTR ?methodBooleanValue@@3PAU_jmethodID@@A ; methodBooleanValue
	push	ecx
	mov	edx, DWORD PTR _param$[ebp]
	push	edx
	mov	eax, DWORD PTR _env$[ebp]
	push	eax
	call	?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallBooleanMethod
	add	esp, 12					; 0000000cH
	mov	BYTE PTR _booleanArg$56831[ebp], al

; 142  :    int tempArg = (booleanArg == JNI_FALSE ? 0 : 1);

	mov	ecx, DWORD PTR _booleanArg$56831[ebp]
	and	ecx, 255				; 000000ffH
	neg	ecx
	sbb	ecx, ecx
	neg	ecx
	mov	DWORD PTR _tempArg$56832[ebp], ecx

; 143  :    _push(tempArg)

	push	DWORD PTR _tempArg$56832[ebp]

; 145  :   else if (env->IsInstanceOf(param, classHolder)) {

	jmp	$L56854
$L56830:
	mov	edx, DWORD PTR ?classHolder@@3PAV_jclass@@A ; classHolder
	push	edx
	mov	eax, DWORD PTR _param$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	$L56854

; 146  :    
; 147  :    /* Holder */
; 148  :    
; 149  :    jobject o = env->GetObjectField(param, fieldHolderO);

	mov	ecx, DWORD PTR ?fieldHolderO@@3PAU_jfieldID@@A ; fieldHolderO
	push	ecx
	mov	edx, DWORD PTR _param$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z ; JNIEnv_::GetObjectField
	mov	DWORD PTR _o$56835[ebp], eax

; 150  :    
; 151  :    if (env->IsInstanceOf(o, classInteger)) {

	mov	eax, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	push	eax
	mov	ecx, DWORD PTR _o$56835[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56836

; 152  :     int *intPtr = new int;

	push	4
	call	??2@YAPAXI@Z				; operator new
	add	esp, 4
	mov	DWORD PTR $T56915[ebp], eax
	mov	edx, DWORD PTR $T56915[ebp]
	mov	DWORD PTR _intPtr$56837[ebp], edx

; 153  :     *intPtr = env->CallIntMethod(o, methodIntValue);

	mov	eax, DWORD PTR ?methodIntValue@@3PAU_jmethodID@@A ; methodIntValue
	push	eax
	mov	ecx, DWORD PTR _o$56835[ebp]
	push	ecx
	mov	edx, DWORD PTR _env$[ebp]
	push	edx
	call	?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallIntMethod
	add	esp, 12					; 0000000cH
	mov	ecx, DWORD PTR _intPtr$56837[ebp]
	mov	DWORD PTR [ecx], eax

; 154  :     arrays[i] = (int) intPtr;

	mov	edx, DWORD PTR _i$[ebp]
	mov	eax, DWORD PTR _arrays$[ebp]
	mov	ecx, DWORD PTR _intPtr$56837[ebp]
	mov	DWORD PTR [eax+edx*4], ecx

; 155  :     _push(intPtr);

	push	DWORD PTR _intPtr$56837[ebp]

; 157  :    else if (env->IsInstanceOf(o, classByteArray)) {

	jmp	$L56854
$L56836:
	mov	edx, DWORD PTR ?classByteArray@@3PAV_jclass@@A ; classByteArray
	push	edx
	mov	eax, DWORD PTR _o$56835[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56842

; 158  :     char* byteArrayArg = (char*) env->GetPrimitiveArrayCritical((jarray) o, 0);

	push	0
	mov	ecx, DWORD PTR _o$56835[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetPrimitiveArrayCritical@JNIEnv_@@QAEPAXPAV_jarray@@PAE@Z ; JNIEnv_::GetPrimitiveArrayCritical
	mov	DWORD PTR _byteArrayArg$56843[ebp], eax

; 159  :     arrays[i] = (int) &byteArrayArg;

	mov	edx, DWORD PTR _i$[ebp]
	mov	eax, DWORD PTR _arrays$[ebp]
	lea	ecx, DWORD PTR _byteArrayArg$56843[ebp]
	mov	DWORD PTR [eax+edx*4], ecx

; 160  :     _push(byteArrayArg)

	push	DWORD PTR _byteArrayArg$56843[ebp]

; 162  :    else if (env->IsInstanceOf(o, classCharArray)) {

	jmp	$L56854
$L56842:
	mov	edx, DWORD PTR ?classCharArray@@3PAV_jclass@@A ; classCharArray
	push	edx
	mov	eax, DWORD PTR _o$56835[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56848

; 163  :     unsigned short* charArrayArg = (unsigned short*) env->GetPrimitiveArrayCritical(
; 164  :      (jarray) o, 0);

	push	0
	mov	ecx, DWORD PTR _o$56835[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetPrimitiveArrayCritical@JNIEnv_@@QAEPAXPAV_jarray@@PAE@Z ; JNIEnv_::GetPrimitiveArrayCritical
	mov	DWORD PTR _charArrayArg$56849[ebp], eax

; 165  :     arrays[i] = (int) &charArrayArg;

	mov	edx, DWORD PTR _i$[ebp]
	mov	eax, DWORD PTR _arrays$[ebp]
	lea	ecx, DWORD PTR _charArrayArg$56849[ebp]
	mov	DWORD PTR [eax+edx*4], ecx

; 166  :     _push(charArrayArg)

	push	DWORD PTR _charArrayArg$56849[ebp]

; 168  :    else if (env->IsInstanceOf(o, classBoolean)) {

	jmp	SHORT $L56854
$L56848:
	mov	edx, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	push	edx
	mov	eax, DWORD PTR _o$56835[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56854

; 169  :     jboolean booleanArg = env->CallBooleanMethod(o, methodBooleanValue);

	mov	ecx, DWORD PTR ?methodBooleanValue@@3PAU_jmethodID@@A ; methodBooleanValue
	push	ecx
	mov	edx, DWORD PTR _o$56835[ebp]
	push	edx
	mov	eax, DWORD PTR _env$[ebp]
	push	eax
	call	?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallBooleanMethod
	add	esp, 12					; 0000000cH
	mov	BYTE PTR _booleanArg$56855[ebp], al

; 170  :     int *tempPtr = new int;

	push	4
	call	??2@YAPAXI@Z				; operator new
	add	esp, 4
	mov	DWORD PTR $T56916[ebp], eax
	mov	ecx, DWORD PTR $T56916[ebp]
	mov	DWORD PTR _tempPtr$56856[ebp], ecx

; 171  :     *tempPtr = (booleanArg == JNI_FALSE ? 0 : 1);

	mov	edx, DWORD PTR _booleanArg$56855[ebp]
	and	edx, 255				; 000000ffH
	neg	edx
	sbb	edx, edx
	neg	edx
	mov	eax, DWORD PTR _tempPtr$56856[ebp]
	mov	DWORD PTR [eax], edx

; 172  :     arrays[i] = (int) tempPtr;

	mov	ecx, DWORD PTR _i$[ebp]
	mov	edx, DWORD PTR _arrays$[ebp]
	mov	eax, DWORD PTR _tempPtr$56856[ebp]
	mov	DWORD PTR [edx+ecx*4], eax

; 173  :     _push(tempPtr);

	push	DWORD PTR _tempPtr$56856[ebp]
$L56854:

; 179  :   
; 180  :  }

	jmp	$L56811
$L56812:

; 181  : 
; 182  :  jint functionHandle = env->GetIntField(obj, fieldFunctionHandle);

	mov	ecx, DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A ; fieldFunctionHandle
	push	ecx
	mov	edx, DWORD PTR _obj$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetIntField@JNIEnv_@@QAEJPAV_jobject@@PAU_jfieldID@@@Z ; JNIEnv_::GetIntField
	mov	DWORD PTR _functionHandle$[ebp], eax

; 183  :  
; 184  : #ifdef _WINDOWS
; 185  : #ifdef _X86_
; 186  :  
; 187  :  __asm {
; 188  :   
; 189  :   call functionHandle

	call	DWORD PTR _functionHandle$[ebp]

; 190  :    
; 191  :  }
; 192  :  
; 193  : #endif
; 194  : #endif
; 195  :  
; 196  :  for (int j = 0; j < len; ++j) {

	mov	DWORD PTR _j$[ebp], 0
	jmp	SHORT $L56862
$L56863:
	mov	eax, DWORD PTR _j$[ebp]
	add	eax, 1
	mov	DWORD PTR _j$[ebp], eax
$L56862:
	mov	ecx, DWORD PTR _j$[ebp]
	cmp	ecx, DWORD PTR _len$[ebp]
	jge	$L56864

; 197  :   param = env->GetObjectArrayElement(params, j);

	mov	edx, DWORD PTR _j$[ebp]
	push	edx
	mov	eax, DWORD PTR _params$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetObjectArrayElement@JNIEnv_@@QAEPAV_jobject@@PAV_jobjectArray@@J@Z ; JNIEnv_::GetObjectArrayElement
	mov	DWORD PTR _param$[ebp], eax

; 198  :   if (param == NULL) {}

	cmp	DWORD PTR _param$[ebp], 0
	jne	SHORT $L56865

; 199  :   else if (env->IsInstanceOf(param, classByteArray) || env->IsInstanceOf(param, classCharArray)) {

	jmp	$L56884
$L56865:
	mov	ecx, DWORD PTR ?classByteArray@@3PAV_jclass@@A ; classByteArray
	push	ecx
	mov	edx, DWORD PTR _param$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	jne	SHORT $L56868
	mov	eax, DWORD PTR ?classCharArray@@3PAV_jclass@@A ; classCharArray
	push	eax
	mov	ecx, DWORD PTR _param$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56867
$L56868:

; 200  :    env->ReleasePrimitiveArrayCritical((jarray) param, (void*) arrays[j], 0);

	push	0
	mov	edx, DWORD PTR _j$[ebp]
	mov	eax, DWORD PTR _arrays$[ebp]
	mov	ecx, DWORD PTR [eax+edx*4]
	push	ecx
	mov	edx, DWORD PTR _param$[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?ReleasePrimitiveArrayCritical@JNIEnv_@@QAEXPAV_jarray@@PAXJ@Z ; JNIEnv_::ReleasePrimitiveArrayCritical

; 202  :   else if (env->IsInstanceOf(param, classHolder)) {

	jmp	$L56884
$L56867:
	mov	eax, DWORD PTR ?classHolder@@3PAV_jclass@@A ; classHolder
	push	eax
	mov	ecx, DWORD PTR _param$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	$L56884

; 203  :    
; 204  :    jobject o = env->GetObjectField(param, fieldHolderO);

	mov	edx, DWORD PTR ?fieldHolderO@@3PAU_jfieldID@@A ; fieldHolderO
	push	edx
	mov	eax, DWORD PTR _param$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?GetObjectField@JNIEnv_@@QAEPAV_jobject@@PAV2@PAU_jfieldID@@@Z ; JNIEnv_::GetObjectField
	mov	DWORD PTR _o$56873[ebp], eax

; 205  : 
; 206  :    if (env->IsInstanceOf(o, classInteger)) {

	mov	ecx, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	push	ecx
	mov	edx, DWORD PTR _o$56873[ebp]
	push	edx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56874

; 207  :     int* out = (int*) arrays[j];

	mov	eax, DWORD PTR _j$[ebp]
	mov	ecx, DWORD PTR _arrays$[ebp]
	mov	edx, DWORD PTR [ecx+eax*4]
	mov	DWORD PTR _out$56875[ebp], edx

; 208  :     env->SetObjectField(param, fieldHolderO, env->NewObject(classInteger, newIntegerInt, *out));

	mov	eax, DWORD PTR _out$56875[ebp]
	mov	ecx, DWORD PTR [eax]
	push	ecx
	mov	edx, DWORD PTR ?newIntegerInt@@3PAU_jmethodID@@A ; newIntegerInt
	push	edx
	mov	eax, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	push	ecx
	call	?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ ; JNIEnv_::NewObject
	add	esp, 16					; 00000010H
	push	eax
	mov	edx, DWORD PTR ?fieldHolderO@@3PAU_jfieldID@@A ; fieldHolderO
	push	edx
	mov	eax, DWORD PTR _param$[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetObjectField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@0@Z ; JNIEnv_::SetObjectField

; 209  :     delete out;

	mov	ecx, DWORD PTR _out$56875[ebp]
	mov	DWORD PTR $T56917[ebp], ecx
	mov	edx, DWORD PTR $T56917[ebp]
	push	edx
	call	??3@YAXPAX@Z				; operator delete
	add	esp, 4

; 211  :    else if (env->IsInstanceOf(o, classByteArray) || env->IsInstanceOf(o, classCharArray)) {

	jmp	$L56884
$L56874:
	mov	eax, DWORD PTR ?classByteArray@@3PAV_jclass@@A ; classByteArray
	push	eax
	mov	ecx, DWORD PTR _o$56873[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	jne	SHORT $L56880
	mov	edx, DWORD PTR ?classCharArray@@3PAV_jclass@@A ; classCharArray
	push	edx
	mov	eax, DWORD PTR _o$56873[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56879
$L56880:

; 212  :     env->ReleasePrimitiveArrayCritical((jarray) o, (void*) arrays[j], 0);

	push	0
	mov	ecx, DWORD PTR _j$[ebp]
	mov	edx, DWORD PTR _arrays$[ebp]
	mov	eax, DWORD PTR [edx+ecx*4]
	push	eax
	mov	ecx, DWORD PTR _o$56873[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?ReleasePrimitiveArrayCritical@JNIEnv_@@QAEXPAV_jarray@@PAXJ@Z ; JNIEnv_::ReleasePrimitiveArrayCritical

; 214  :    else if (env->IsInstanceOf(o, classBoolean)) {

	jmp	SHORT $L56884
$L56879:
	mov	edx, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	push	edx
	mov	eax, DWORD PTR _o$56873[ebp]
	push	eax
	mov	ecx, DWORD PTR _env$[ebp]
	call	?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ; JNIEnv_::IsInstanceOf
	and	eax, 255				; 000000ffH
	test	eax, eax
	je	SHORT $L56884

; 215  :     int* out = (int*) arrays[j];

	mov	ecx, DWORD PTR _j$[ebp]
	mov	edx, DWORD PTR _arrays$[ebp]
	mov	eax, DWORD PTR [edx+ecx*4]
	mov	DWORD PTR _out$56885[ebp], eax

; 216  :     env->SetObjectField(param, fieldHolderO, env->NewObject(classBoolean, newBooleanBoolean, (*out == 0 ? JNI_FALSE : JNI_TRUE)));

	mov	ecx, DWORD PTR _out$56885[ebp]
	xor	edx, edx
	cmp	DWORD PTR [ecx], 0
	setne	dl
	push	edx
	mov	eax, DWORD PTR ?newBooleanBoolean@@3PAU_jmethodID@@A ; newBooleanBoolean
	push	eax
	mov	ecx, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	push	ecx
	mov	edx, DWORD PTR _env$[ebp]
	push	edx
	call	?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ ; JNIEnv_::NewObject
	add	esp, 16					; 00000010H
	push	eax
	mov	eax, DWORD PTR ?fieldHolderO@@3PAU_jfieldID@@A ; fieldHolderO
	push	eax
	mov	ecx, DWORD PTR _param$[ebp]
	push	ecx
	mov	ecx, DWORD PTR _env$[ebp]
	call	?SetObjectField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@0@Z ; JNIEnv_::SetObjectField

; 217  :     delete out;

	mov	edx, DWORD PTR _out$56885[ebp]
	mov	DWORD PTR $T56918[ebp], edx
	mov	eax, DWORD PTR $T56918[ebp]
	push	eax
	call	??3@YAXPAX@Z				; operator delete
	add	esp, 4
$L56884:

; 221  : 
; 222  :  }

	jmp	$L56863
$L56864:

; 223  :  
; 224  :  delete [] arrays;

	mov	ecx, DWORD PTR _arrays$[ebp]
	mov	DWORD PTR $T56919[ebp], ecx
	mov	edx, DWORD PTR $T56919[ebp]
	push	edx
	call	??3@YAXPAX@Z				; operator delete
	add	esp, 4

; 225  :  
; 226  :  env->SetIntField(obj, fieldLastErrorCode, GetLastError());

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
$L56800:

; 227  :  
; 228  : }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 168				; 000000a8H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	12					; 0000000cH
_Java_com_eaio_nativecall_VoidCall_executeCall0@12 ENDP
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
;	COMDAT ?ThrowNew@JNIEnv_@@QAEJPAV_jclass@@PBD@Z
_TEXT	SEGMENT
_this$ = -4
_clazz$ = 8
_msg$ = 12
?ThrowNew@JNIEnv_@@QAEJPAV_jclass@@PBD@Z PROC NEAR	; JNIEnv_::ThrowNew, COMDAT

; 787  :     jint ThrowNew(jclass clazz, const char *msg) {

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

; 788  :         return functions->ThrowNew(this, clazz, msg);

	mov	esi, esp
	mov	eax, DWORD PTR _msg$[ebp]
	push	eax
	mov	ecx, DWORD PTR _clazz$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	mov	ecx, DWORD PTR [eax]
	call	DWORD PTR [ecx+56]
	cmp	esi, esp
	call	__chkesp

; 789  :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
?ThrowNew@JNIEnv_@@QAEJPAV_jclass@@PBD@Z ENDP		; JNIEnv_::ThrowNew
_TEXT	ENDS
;	COMDAT ?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ
_TEXT	SEGMENT
_this$ = 8
_args$ = -4
_result$ = -8
_clazz$ = 12
_methodID$ = 16
?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ PROC NEAR ; JNIEnv_::NewObject, COMDAT

; 834  :     jobject NewObject(jclass clazz, jmethodID methodID, ...) {

	push	ebp
	mov	ebp, esp
	sub	esp, 72					; 00000048H
	push	ebx
	push	esi
	push	edi
	lea	edi, DWORD PTR [ebp-72]
	mov	ecx, 18					; 00000012H
	mov	eax, -858993460				; ccccccccH
	rep stosd

; 835  :         va_list args;
; 836  :         jobject result;
; 837  :         va_start(args, methodID);

	lea	eax, DWORD PTR _methodID$[ebp+4]
	mov	DWORD PTR _args$[ebp], eax

; 838  :         result = functions->NewObjectV(this,clazz,methodID,args);

	mov	esi, esp
	mov	ecx, DWORD PTR _args$[ebp]
	push	ecx
	mov	edx, DWORD PTR _methodID$[ebp]
	push	edx
	mov	eax, DWORD PTR _clazz$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	mov	eax, DWORD PTR [edx]
	call	DWORD PTR [eax+116]
	cmp	esi, esp
	call	__chkesp
	mov	DWORD PTR _result$[ebp], eax

; 839  :         va_end(args);

	mov	DWORD PTR _args$[ebp], 0

; 840  :         return result;

	mov	eax, DWORD PTR _result$[ebp]

; 841  :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 72					; 00000048H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	0
?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ ENDP ; JNIEnv_::NewObject
_TEXT	ENDS
;	COMDAT ?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z
_TEXT	SEGMENT
_this$ = -4
_obj$ = 8
_clazz$ = 12
?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z PROC NEAR ; JNIEnv_::IsInstanceOf, COMDAT

; 854  :     jboolean IsInstanceOf(jobject obj, jclass clazz) {

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

; 855  :         return functions->IsInstanceOf(this,obj,clazz);

	mov	esi, esp
	mov	eax, DWORD PTR _clazz$[ebp]
	push	eax
	mov	ecx, DWORD PTR _obj$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	mov	ecx, DWORD PTR [eax]
	call	DWORD PTR [ecx+128]
	cmp	esi, esp
	call	__chkesp

; 856  :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
?IsInstanceOf@JNIEnv_@@QAEEPAV_jobject@@PAV_jclass@@@Z ENDP ; JNIEnv_::IsInstanceOf
_TEXT	ENDS
;	COMDAT ?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ
_TEXT	SEGMENT
_this$ = 8
_args$ = -4
_result$ = -8
_obj$ = 12
_methodID$ = 16
?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ PROC NEAR ; JNIEnv_::CallBooleanMethod, COMDAT

; 881  :                                jmethodID methodID, ...) {

	push	ebp
	mov	ebp, esp
	sub	esp, 72					; 00000048H
	push	ebx
	push	esi
	push	edi
	lea	edi, DWORD PTR [ebp-72]
	mov	ecx, 18					; 00000012H
	mov	eax, -858993460				; ccccccccH
	rep stosd

; 882  :         va_list args;
; 883  :         jboolean result;
; 884  :         va_start(args,methodID);

	lea	eax, DWORD PTR _methodID$[ebp+4]
	mov	DWORD PTR _args$[ebp], eax

; 885  :         result = functions->CallBooleanMethodV(this,obj,methodID,args);

	mov	esi, esp
	mov	ecx, DWORD PTR _args$[ebp]
	push	ecx
	mov	edx, DWORD PTR _methodID$[ebp]
	push	edx
	mov	eax, DWORD PTR _obj$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	mov	eax, DWORD PTR [edx]
	call	DWORD PTR [eax+152]
	cmp	esi, esp
	call	__chkesp
	mov	BYTE PTR _result$[ebp], al

; 886  :         va_end(args);

	mov	DWORD PTR _args$[ebp], 0

; 887  :         return result;

	mov	al, BYTE PTR _result$[ebp]

; 888  :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 72					; 00000048H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	0
?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ ENDP ; JNIEnv_::CallBooleanMethod
_TEXT	ENDS
;	COMDAT ?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ
_TEXT	SEGMENT
_this$ = 8
_args$ = -4
_result$ = -8
_obj$ = 12
_methodID$ = 16
?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ PROC NEAR ; JNIEnv_::CallIntMethod, COMDAT

; 949  :     jint CallIntMethod(jobject obj, jmethodID methodID, ...) {

	push	ebp
	mov	ebp, esp
	sub	esp, 72					; 00000048H
	push	ebx
	push	esi
	push	edi
	lea	edi, DWORD PTR [ebp-72]
	mov	ecx, 18					; 00000012H
	mov	eax, -858993460				; ccccccccH
	rep stosd

; 950  :         va_list args;
; 951  :         jint result;
; 952  :         va_start(args,methodID);

	lea	eax, DWORD PTR _methodID$[ebp+4]
	mov	DWORD PTR _args$[ebp], eax

; 953  :         result = functions->CallIntMethodV(this,obj,methodID,args);

	mov	esi, esp
	mov	ecx, DWORD PTR _args$[ebp]
	push	ecx
	mov	edx, DWORD PTR _methodID$[ebp]
	push	edx
	mov	eax, DWORD PTR _obj$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	mov	eax, DWORD PTR [edx]
	call	DWORD PTR [eax+200]
	cmp	esi, esp
	call	__chkesp
	mov	DWORD PTR _result$[ebp], eax

; 954  :         va_end(args);

	mov	DWORD PTR _args$[ebp], 0

; 955  :         return result;

	mov	eax, DWORD PTR _result$[ebp]

; 956  :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 72					; 00000048H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	0
?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ ENDP ; JNIEnv_::CallIntMethod
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
;	COMDAT ?SetObjectField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@0@Z
_TEXT	SEGMENT
_obj$ = 8
_fieldID$ = 12
_val$ = 16
_this$ = -4
?SetObjectField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@0@Z PROC NEAR ; JNIEnv_::SetObjectField, COMDAT

; 1276 :     void SetObjectField(jobject obj, jfieldID fieldID, jobject val) {

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

; 1277 :         functions->SetObjectField(this,obj,fieldID,val);

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
	call	DWORD PTR [edx+416]
	cmp	esi, esp
	call	__chkesp

; 1278 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	12					; 0000000cH
?SetObjectField@JNIEnv_@@QAEXPAV_jobject@@PAU_jfieldID@@0@Z ENDP ; JNIEnv_::SetObjectField
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
;	COMDAT ?GetArrayLength@JNIEnv_@@QAEJPAV_jarray@@@Z
_TEXT	SEGMENT
_this$ = -4
_array$ = 8
?GetArrayLength@JNIEnv_@@QAEJPAV_jarray@@@Z PROC NEAR	; JNIEnv_::GetArrayLength, COMDAT

; 1589 :     jsize GetArrayLength(jarray array) {

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

; 1590 :         return functions->GetArrayLength(this,array);

	mov	esi, esp
	mov	eax, DWORD PTR _array$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	mov	eax, DWORD PTR [edx]
	call	DWORD PTR [eax+684]
	cmp	esi, esp
	call	__chkesp

; 1591 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	4
?GetArrayLength@JNIEnv_@@QAEJPAV_jarray@@@Z ENDP	; JNIEnv_::GetArrayLength
_TEXT	ENDS
;	COMDAT ?GetObjectArrayElement@JNIEnv_@@QAEPAV_jobject@@PAV_jobjectArray@@J@Z
_TEXT	SEGMENT
_this$ = -4
_array$ = 8
_index$ = 12
?GetObjectArrayElement@JNIEnv_@@QAEPAV_jobject@@PAV_jobjectArray@@J@Z PROC NEAR ; JNIEnv_::GetObjectArrayElement, COMDAT

; 1597 :     jobject GetObjectArrayElement(jobjectArray array, jsize index) {

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

; 1598 :         return functions->GetObjectArrayElement(this,array,index);

	mov	esi, esp
	mov	eax, DWORD PTR _index$[ebp]
	push	eax
	mov	ecx, DWORD PTR _array$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	mov	ecx, DWORD PTR [eax]
	call	DWORD PTR [ecx+692]
	cmp	esi, esp
	call	__chkesp

; 1599 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
?GetObjectArrayElement@JNIEnv_@@QAEPAV_jobject@@PAV_jobjectArray@@J@Z ENDP ; JNIEnv_::GetObjectArrayElement
_TEXT	ENDS
;	COMDAT ?GetPrimitiveArrayCritical@JNIEnv_@@QAEPAXPAV_jarray@@PAE@Z
_TEXT	SEGMENT
_this$ = -4
_array$ = 8
_isCopy$ = 12
?GetPrimitiveArrayCritical@JNIEnv_@@QAEPAXPAV_jarray@@PAE@Z PROC NEAR ; JNIEnv_::GetPrimitiveArrayCritical, COMDAT

; 1788 :     void * GetPrimitiveArrayCritical(jarray array, jboolean *isCopy) {

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

; 1789 :         return functions->GetPrimitiveArrayCritical(this,array,isCopy);

	mov	esi, esp
	mov	eax, DWORD PTR _isCopy$[ebp]
	push	eax
	mov	ecx, DWORD PTR _array$[ebp]
	push	ecx
	mov	edx, DWORD PTR _this$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	mov	ecx, DWORD PTR [eax]
	call	DWORD PTR [ecx+888]
	cmp	esi, esp
	call	__chkesp

; 1790 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	8
?GetPrimitiveArrayCritical@JNIEnv_@@QAEPAXPAV_jarray@@PAE@Z ENDP ; JNIEnv_::GetPrimitiveArrayCritical
_TEXT	ENDS
;	COMDAT ?ReleasePrimitiveArrayCritical@JNIEnv_@@QAEXPAV_jarray@@PAXJ@Z
_TEXT	SEGMENT
_this$ = -4
_array$ = 8
_carray$ = 12
_mode$ = 16
?ReleasePrimitiveArrayCritical@JNIEnv_@@QAEXPAV_jarray@@PAXJ@Z PROC NEAR ; JNIEnv_::ReleasePrimitiveArrayCritical, COMDAT

; 1791 :     void ReleasePrimitiveArrayCritical(jarray array, void *carray, jint mode) {

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

; 1792 :         functions->ReleasePrimitiveArrayCritical(this,array,carray,mode);

	mov	esi, esp
	mov	eax, DWORD PTR _mode$[ebp]
	push	eax
	mov	ecx, DWORD PTR _carray$[ebp]
	push	ecx
	mov	edx, DWORD PTR _array$[ebp]
	push	edx
	mov	eax, DWORD PTR _this$[ebp]
	push	eax
	mov	ecx, DWORD PTR _this$[ebp]
	mov	edx, DWORD PTR [ecx]
	call	DWORD PTR [edx+892]
	cmp	esi, esp
	call	__chkesp

; 1793 :     }

	pop	edi
	pop	esi
	pop	ebx
	add	esp, 68					; 00000044H
	cmp	ebp, esp
	call	__chkesp
	mov	esp, ebp
	pop	ebp
	ret	12					; 0000000cH
?ReleasePrimitiveArrayCritical@JNIEnv_@@QAEXPAV_jarray@@PAXJ@Z ENDP ; JNIEnv_::ReleasePrimitiveArrayCritical
_TEXT	ENDS
END
