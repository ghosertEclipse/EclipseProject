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
_TLS	SEGMENT DWORD USE32 PUBLIC 'TLS'
_TLS	ENDS
;	COMDAT ??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@
_DATA	SEGMENT DWORD USE32 PUBLIC 'DATA'
_DATA	ENDS
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
FLAT	GROUP _DATA, CONST, _BSS
	ASSUME	CS: FLAT, DS: FLAT, SS: FLAT
endif
PUBLIC	_Java_com_eaio_nativecall_VoidCall_executeCall@8
EXTRN	?fieldFunctionHandle@@3PAU_jfieldID@@A:DWORD	; fieldFunctionHandle
EXTRN	?fieldLastErrorCode@@3PAU_jfieldID@@A:DWORD	; fieldLastErrorCode
EXTRN	__imp__GetLastError@0:NEAR
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_functionHandle$ = 8
_Java_com_eaio_nativecall_VoidCall_executeCall@8 PROC NEAR

; 79   : (JNIEnv *env, jobject obj) {

	push	ebp
	mov	ebp, esp

; 80   : 
; 81   :  jint functionHandle = env->GetIntField(obj, fieldFunctionHandle);

	mov	eax, DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A ; fieldFunctionHandle
	push	esi
	mov	esi, DWORD PTR _env$[ebp]
	push	eax
	push	DWORD PTR _obj$[ebp]
	mov	ecx, DWORD PTR [esi]
	push	esi
	call	DWORD PTR [ecx+400]
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

	call	DWORD PTR __imp__GetLastError@0
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	DWORD PTR _obj$[ebp]
	push	esi
	call	DWORD PTR [ecx+436]

; 96   : 
; 97   : }

	pop	esi
	pop	ebp
	ret	8
_Java_com_eaio_nativecall_VoidCall_executeCall@8 ENDP
_TEXT	ENDS
PUBLIC	??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@ ; `string'
PUBLIC	_Java_com_eaio_nativecall_VoidCall_executeCall0@12
PUBLIC	?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ ; JNIEnv_::NewObject
PUBLIC	?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallBooleanMethod
PUBLIC	?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallIntMethod
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
EXTRN	??2@YAPAXI@Z:NEAR				; operator new
EXTRN	??3@YAXPAX@Z:NEAR				; operator delete
EXTRN	_memset:NEAR
;	COMDAT ??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@
; File C:\Program Files\IBMJava13\include\jni.h
_DATA	SEGMENT
??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@ DB 'java/lang/OutOfMemor'
	DB	'yError', 00H				; `string'
_DATA	ENDS
_TEXT	SEGMENT
_env$ = 8
_obj$ = 12
_params$ = 16
_len$ = -28
_arrays$ = -20
_param$ = -24
_i$ = 8
_intArg$27769 = -24
_byteArrayArg$27772 = -4
_charArrayArg$27778 = -8
_tempArg$27785 = -24
_intPtr$27790 = -24
_byteArrayArg$27796 = -12
_charArrayArg$27802 = -16
_tempPtr$27809 = -24
_functionHandle$ = 8
_j$ = 8
_Java_com_eaio_nativecall_VoidCall_executeCall0@12 PROC NEAR

; 105  : (JNIEnv *env, jobject obj, jobjectArray params) {

	push	ebp
	mov	ebp, esp
	sub	esp, 28					; 0000001cH
	push	ebx
	push	esi

; 106  :    
; 107  :  const int len = env->GetArrayLength(params);

	mov	esi, DWORD PTR _env$[ebp]
	push	edi
	push	DWORD PTR _params$[ebp]
	mov	eax, DWORD PTR [esi]
	push	esi
	call	DWORD PTR [eax+684]
	mov	ebx, eax

; 108  :  
; 109  :  int* arrays = NULL;
; 110  :  if (!(arrays = new int[len])) {

	mov	edi, ebx
	mov	DWORD PTR _len$[ebp], ebx
	shl	edi, 2
	push	edi
	call	??2@YAPAXI@Z				; operator new
	test	eax, eax
	pop	ecx
	mov	DWORD PTR _arrays$[ebp], eax
	jne	SHORT $L27758

; 111  :   env->ThrowNew(env->FindClass("java/lang/OutOfMemoryError"), NULL);

	mov	eax, DWORD PTR [esi]
	push	OFFSET FLAT:??_C@_0BL@NKOM@java?1lang?1OutOfMemoryError?$AA@ ; `string'
	push	esi
	call	DWORD PTR [eax+24]
	mov	ecx, DWORD PTR [esi]
	push	0
	push	eax
	push	esi
	call	DWORD PTR [ecx+56]

; 112  :   return;

	jmp	$L28021
$L27758:

; 113  :  }
; 114  :  memset(arrays, 0, (sizeof(int) * len));

	push	edi
	push	0
	push	eax
	call	_memset

; 115  : 
; 116  :  jobject param;
; 117  : 
; 118  :  for (int i = len - 1; i >= 0; i--) {  

	lea	eax, DWORD PTR [ebx-1]
	add	esp, 12					; 0000000cH
	test	eax, eax
	mov	DWORD PTR _i$[ebp], eax
	jl	$L27765
	mov	ecx, DWORD PTR _arrays$[ebp]
	lea	edi, DWORD PTR [ecx+eax*4]
	jmp	SHORT $L27763
$L28028:

; 113  :  }
; 114  :  memset(arrays, 0, (sizeof(int) * len));

	mov	eax, DWORD PTR _i$[ebp]
$L27763:

; 119  :   
; 120  :   param = env->GetObjectArrayElement(params, i);

	mov	ecx, DWORD PTR [esi]
	push	eax
	push	DWORD PTR _params$[ebp]
	push	esi
	call	DWORD PTR [ecx+692]
	mov	ebx, eax

; 121  :   
; 122  :   if (param == NULL) {

	test	ebx, ebx
	jne	SHORT $L27766

; 123  :    _push(0);

	push	0

; 124  :   }
; 125  :   else if (env->IsInstanceOf(param, classInteger)) {

	jmp	$L27764
$L27766:
	mov	eax, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27768

; 126  :    int intArg = env->CallIntMethod(param, methodIntValue);

	push	DWORD PTR ?methodIntValue@@3PAU_jmethodID@@A ; methodIntValue
	push	ebx
	push	esi
	call	?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallIntMethod
	add	esp, 12					; 0000000cH
	mov	DWORD PTR _intArg$27769[ebp], eax

; 127  :    _push(intArg)

	push	DWORD PTR _intArg$27769[ebp]

; 128  :   }
; 129  :   else if (env->IsInstanceOf(param, classByteArray)) {

	jmp	$L27764
$L27768:
	mov	eax, DWORD PTR ?classByteArray@@3PAV_jclass@@A ; classByteArray
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27771

; 130  :    char* byteArrayArg = (char*) env->GetPrimitiveArrayCritical((jarray) param, 0);

	mov	eax, DWORD PTR [esi]
	push	0
	push	ebx
	push	esi
	call	DWORD PTR [eax+888]
	mov	DWORD PTR _byteArrayArg$27772[ebp], eax

; 131  :    arrays[i] = (int) &byteArrayArg;

	lea	eax, DWORD PTR _byteArrayArg$27772[ebp]
	mov	DWORD PTR [edi], eax

; 132  :    _push(byteArrayArg)

	push	DWORD PTR _byteArrayArg$27772[ebp]

; 133  :   }
; 134  :   else if (env->IsInstanceOf(param, classCharArray)) {

	jmp	$L27764
$L27771:
	mov	eax, DWORD PTR ?classCharArray@@3PAV_jclass@@A ; classCharArray
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27777

; 135  :    unsigned short* charArrayArg = (unsigned short*) env->GetPrimitiveArrayCritical(
; 136  :     (jarray) param, 0);

	mov	eax, DWORD PTR [esi]
	push	0
	push	ebx
	push	esi
	call	DWORD PTR [eax+888]
	mov	DWORD PTR _charArrayArg$27778[ebp], eax

; 137  :    arrays[i] = (int) &charArrayArg;

	lea	eax, DWORD PTR _charArrayArg$27778[ebp]
	mov	DWORD PTR [edi], eax

; 138  :    _push(charArrayArg)

	push	DWORD PTR _charArrayArg$27778[ebp]

; 139  :   }
; 140  :   else if (env->IsInstanceOf(param, classBoolean)) {

	jmp	$L27764
$L27777:
	mov	eax, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27783

; 141  :    jboolean booleanArg = env->CallBooleanMethod(param, methodBooleanValue);

	push	DWORD PTR ?methodBooleanValue@@3PAU_jmethodID@@A ; methodBooleanValue
	push	ebx
	push	esi
	call	?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallBooleanMethod
	add	esp, 12					; 0000000cH

; 142  :    int tempArg = (booleanArg == JNI_FALSE ? 0 : 1);

	neg	al
	sbb	eax, eax
	neg	eax
	mov	DWORD PTR _tempArg$27785[ebp], eax

; 143  :    _push(tempArg)

	push	DWORD PTR _tempArg$27785[ebp]

; 144  :   }
; 145  :   else if (env->IsInstanceOf(param, classHolder)) {

	jmp	$L27764
$L27783:
	mov	eax, DWORD PTR ?classHolder@@3PAV_jclass@@A ; classHolder
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	$L27764

; 146  :    
; 147  :    /* Holder */
; 148  :    
; 149  :    jobject o = env->GetObjectField(param, fieldHolderO);

	mov	eax, DWORD PTR ?fieldHolderO@@3PAU_jfieldID@@A ; fieldHolderO
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+380]

; 150  :    
; 151  :    if (env->IsInstanceOf(o, classInteger)) {

	mov	ecx, DWORD PTR [esi]
	mov	ebx, eax
	mov	eax, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27789

; 152  :     int *intPtr = new int;

	push	4
	call	??2@YAPAXI@Z				; operator new
	pop	ecx
	mov	DWORD PTR _intPtr$27790[ebp], eax

; 153  :     *intPtr = env->CallIntMethod(o, methodIntValue);

	push	DWORD PTR ?methodIntValue@@3PAU_jmethodID@@A ; methodIntValue
	push	ebx
	push	esi
	call	?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallIntMethod
	mov	ecx, DWORD PTR _intPtr$27790[ebp]
	add	esp, 12					; 0000000cH

; 154  :     arrays[i] = (int) intPtr;

	mov	DWORD PTR [edi], ecx
	mov	DWORD PTR [ecx], eax

; 155  :     _push(intPtr);

	push	DWORD PTR _intPtr$27790[ebp]

; 156  :    }
; 157  :    else if (env->IsInstanceOf(o, classByteArray)) {

	jmp	$L27764
$L27789:
	mov	eax, DWORD PTR ?classByteArray@@3PAV_jclass@@A ; classByteArray
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27795

; 158  :     char* byteArrayArg = (char*) env->GetPrimitiveArrayCritical((jarray) o, 0);

	mov	eax, DWORD PTR [esi]
	push	0
	push	ebx
	push	esi
	call	DWORD PTR [eax+888]
	mov	DWORD PTR _byteArrayArg$27796[ebp], eax

; 159  :     arrays[i] = (int) &byteArrayArg;

	lea	eax, DWORD PTR _byteArrayArg$27796[ebp]
	mov	DWORD PTR [edi], eax

; 160  :     _push(byteArrayArg)

	push	DWORD PTR _byteArrayArg$27796[ebp]

; 161  :    }
; 162  :    else if (env->IsInstanceOf(o, classCharArray)) {

	jmp	SHORT $L27764
$L27795:
	mov	eax, DWORD PTR ?classCharArray@@3PAV_jclass@@A ; classCharArray
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27801

; 163  :     unsigned short* charArrayArg = (unsigned short*) env->GetPrimitiveArrayCritical(
; 164  :      (jarray) o, 0);

	mov	eax, DWORD PTR [esi]
	push	0
	push	ebx
	push	esi
	call	DWORD PTR [eax+888]
	mov	DWORD PTR _charArrayArg$27802[ebp], eax

; 165  :     arrays[i] = (int) &charArrayArg;

	lea	eax, DWORD PTR _charArrayArg$27802[ebp]
	mov	DWORD PTR [edi], eax

; 166  :     _push(charArrayArg)

	push	DWORD PTR _charArrayArg$27802[ebp]

; 167  :    }
; 168  :    else if (env->IsInstanceOf(o, classBoolean)) {

	jmp	SHORT $L27764
$L27801:
	mov	eax, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	ebx
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27764

; 169  :     jboolean booleanArg = env->CallBooleanMethod(o, methodBooleanValue);

	push	DWORD PTR ?methodBooleanValue@@3PAU_jmethodID@@A ; methodBooleanValue
	push	ebx
	push	esi
	call	?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ ; JNIEnv_::CallBooleanMethod

; 170  :     int *tempPtr = new int;

	push	4
	mov	bl, al
	call	??2@YAPAXI@Z				; operator new
	add	esp, 16					; 00000010H

; 171  :     *tempPtr = (booleanArg == JNI_FALSE ? 0 : 1);

	xor	ecx, ecx
	test	bl, bl
	setne	cl
	mov	DWORD PTR _tempPtr$27809[ebp], eax
	mov	DWORD PTR [eax], ecx

; 172  :     arrays[i] = (int) tempPtr;

	mov	DWORD PTR [edi], eax

; 173  :     _push(tempPtr);

	push	DWORD PTR _tempPtr$27809[ebp]

; 172  :     arrays[i] = (int) tempPtr;

$L27764:
	dec	DWORD PTR _i$[ebp]
	sub	edi, 4
	cmp	DWORD PTR _i$[ebp], 0
	jge	$L28028

; 115  : 
; 116  :  jobject param;
; 117  : 
; 118  :  for (int i = len - 1; i >= 0; i--) {  

	mov	ebx, DWORD PTR _len$[ebp]
$L27765:

; 174  :    }
; 175  :    
; 176  :    /* end Holder */
; 177  :    
; 178  :   }
; 179  :   
; 180  :  }
; 181  : 
; 182  :  jint functionHandle = env->GetIntField(obj, fieldFunctionHandle);

	mov	eax, DWORD PTR ?fieldFunctionHandle@@3PAU_jfieldID@@A ; fieldFunctionHandle
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	DWORD PTR _obj$[ebp]
	push	esi
	call	DWORD PTR [ecx+400]
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

	and	DWORD PTR _j$[ebp], 0
	test	ebx, ebx
	jle	$L27817

; 174  :    }
; 175  :    
; 176  :    /* end Holder */
; 177  :    
; 178  :   }
; 179  :   
; 180  :  }
; 181  : 
; 182  :  jint functionHandle = env->GetIntField(obj, fieldFunctionHandle);

	mov	ebx, DWORD PTR _arrays$[ebp]
$L27815:

; 197  :   param = env->GetObjectArrayElement(params, j);

	push	DWORD PTR _j$[ebp]
	mov	eax, DWORD PTR [esi]
	push	DWORD PTR _params$[ebp]
	push	esi
	call	DWORD PTR [eax+692]
	mov	edi, eax

; 198  :   if (param == NULL) {}

	test	edi, edi
	mov	DWORD PTR _param$[ebp], edi
	je	$L27816

; 199  :   else if (env->IsInstanceOf(param, classByteArray) || env->IsInstanceOf(param, classCharArray)) {

	mov	eax, DWORD PTR ?classByteArray@@3PAV_jclass@@A ; classByteArray
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	edi
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	jne	$L27833
	mov	eax, DWORD PTR ?classCharArray@@3PAV_jclass@@A ; classCharArray
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	edi
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	jne	$L27833

; 201  :   }
; 202  :   else if (env->IsInstanceOf(param, classHolder)) {

	mov	eax, DWORD PTR ?classHolder@@3PAV_jclass@@A ; classHolder
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	edi
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	$L27816

; 203  :    
; 204  :    jobject o = env->GetObjectField(param, fieldHolderO);

	mov	eax, DWORD PTR ?fieldHolderO@@3PAU_jfieldID@@A ; fieldHolderO
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	edi
	push	esi
	call	DWORD PTR [ecx+380]

; 205  : 
; 206  :    if (env->IsInstanceOf(o, classInteger)) {

	mov	ecx, DWORD PTR [esi]
	mov	edi, eax
	mov	eax, DWORD PTR ?classInteger@@3PAV_jclass@@A ; classInteger
	push	eax
	push	edi
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27827

; 207  :     int* out = (int*) arrays[j];

	mov	edi, DWORD PTR [ebx]

; 208  :     env->SetObjectField(param, fieldHolderO, env->NewObject(classInteger, newIntegerInt, *out));

	push	DWORD PTR [edi]
	push	DWORD PTR ?newIntegerInt@@3PAU_jmethodID@@A ; newIntegerInt
	push	DWORD PTR ?classInteger@@3PAV_jclass@@A	; classInteger
$L28031:
	push	esi
	call	?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ ; JNIEnv_::NewObject
	add	esp, 16					; 00000010H
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	DWORD PTR ?fieldHolderO@@3PAU_jfieldID@@A ; fieldHolderO
	push	DWORD PTR _param$[ebp]
	push	esi
	call	DWORD PTR [ecx+416]

; 209  :     delete out;

	push	edi
	call	??3@YAXPAX@Z				; operator delete
	pop	ecx

; 210  :    }
; 211  :    else if (env->IsInstanceOf(o, classByteArray) || env->IsInstanceOf(o, classCharArray)) {

	jmp	SHORT $L27816
$L27827:
	mov	eax, DWORD PTR ?classByteArray@@3PAV_jclass@@A ; classByteArray
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	edi
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	jne	SHORT $L27833
	mov	eax, DWORD PTR ?classCharArray@@3PAV_jclass@@A ; classCharArray
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	edi
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	jne	SHORT $L27833

; 212  :     env->ReleasePrimitiveArrayCritical((jarray) o, (void*) arrays[j], 0);
; 213  :    }
; 214  :    else if (env->IsInstanceOf(o, classBoolean)) {

	mov	eax, DWORD PTR ?classBoolean@@3PAV_jclass@@A ; classBoolean
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	edi
	push	esi
	call	DWORD PTR [ecx+128]
	test	al, al
	je	SHORT $L27816

; 215  :     int* out = (int*) arrays[j];

	mov	edi, DWORD PTR [ebx]

; 216  :     env->SetObjectField(param, fieldHolderO, env->NewObject(classBoolean, newBooleanBoolean, (*out == 0 ? JNI_FALSE : JNI_TRUE)));

	xor	eax, eax
	cmp	DWORD PTR [edi], eax
	setne	al
	push	eax
	push	DWORD PTR ?newBooleanBoolean@@3PAU_jmethodID@@A ; newBooleanBoolean
	push	DWORD PTR ?classBoolean@@3PAV_jclass@@A	; classBoolean

; 217  :     delete out;

	jmp	SHORT $L28031
$L27833:

; 200  :    env->ReleasePrimitiveArrayCritical((jarray) param, (void*) arrays[j], 0);

	mov	eax, DWORD PTR [esi]
	push	0
	push	DWORD PTR [ebx]
	push	edi
	push	esi
	call	DWORD PTR [eax+892]
$L27816:
	inc	DWORD PTR _j$[ebp]
	mov	eax, DWORD PTR _j$[ebp]
	add	ebx, 4
	cmp	eax, DWORD PTR _len$[ebp]
	jl	$L27815
$L27817:

; 218  :    }
; 219  :    
; 220  :   }
; 221  : 
; 222  :  }
; 223  :  
; 224  :  delete [] arrays;

	push	DWORD PTR _arrays$[ebp]
	call	??3@YAXPAX@Z				; operator delete
	pop	ecx

; 225  :  
; 226  :  env->SetIntField(obj, fieldLastErrorCode, GetLastError());

	call	DWORD PTR __imp__GetLastError@0
	mov	ecx, DWORD PTR [esi]
	push	eax
	push	DWORD PTR ?fieldLastErrorCode@@3PAU_jfieldID@@A ; fieldLastErrorCode
	push	DWORD PTR _obj$[ebp]
	push	esi
	call	DWORD PTR [ecx+436]
$L28021:

; 227  :  
; 228  : }

	pop	edi
	pop	esi
	pop	ebx
	leave
	ret	12					; 0000000cH
_Java_com_eaio_nativecall_VoidCall_executeCall0@12 ENDP
_TEXT	ENDS
;	COMDAT ?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ
_TEXT	SEGMENT
_this$ = 8
_clazz$ = 12
_methodID$ = 16
?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ PROC NEAR ; JNIEnv_::NewObject, COMDAT

; 835  :         va_list args;
; 836  :         jobject result;
; 837  :         va_start(args, methodID);
; 838  :         result = functions->NewObjectV(this,clazz,methodID,args);

	mov	eax, DWORD PTR _this$[esp-4]
	lea	edx, DWORD PTR _methodID$[esp]
	push	edx
	push	DWORD PTR _methodID$[esp]
	mov	ecx, DWORD PTR [eax]
	push	DWORD PTR _clazz$[esp+4]
	push	eax
	call	DWORD PTR [ecx+116]

; 839  :         va_end(args);
; 840  :         return result;
; 841  :     }

	ret	0
?NewObject@JNIEnv_@@QAAPAV_jobject@@PAV_jclass@@PAU_jmethodID@@ZZ ENDP ; JNIEnv_::NewObject
_TEXT	ENDS
;	COMDAT ?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ
_TEXT	SEGMENT
_this$ = 8
_obj$ = 12
_methodID$ = 16
?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ PROC NEAR ; JNIEnv_::CallBooleanMethod, COMDAT

; 882  :         va_list args;
; 883  :         jboolean result;
; 884  :         va_start(args,methodID);
; 885  :         result = functions->CallBooleanMethodV(this,obj,methodID,args);

	mov	eax, DWORD PTR _this$[esp-4]
	lea	edx, DWORD PTR _methodID$[esp]
	push	edx
	push	DWORD PTR _methodID$[esp]
	mov	ecx, DWORD PTR [eax]
	push	DWORD PTR _obj$[esp+4]
	push	eax
	call	DWORD PTR [ecx+152]

; 886  :         va_end(args);
; 887  :         return result;
; 888  :     }

	ret	0
?CallBooleanMethod@JNIEnv_@@QAAEPAV_jobject@@PAU_jmethodID@@ZZ ENDP ; JNIEnv_::CallBooleanMethod
_TEXT	ENDS
;	COMDAT ?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ
_TEXT	SEGMENT
_this$ = 8
_obj$ = 12
_methodID$ = 16
?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ PROC NEAR ; JNIEnv_::CallIntMethod, COMDAT

; 950  :         va_list args;
; 951  :         jint result;
; 952  :         va_start(args,methodID);
; 953  :         result = functions->CallIntMethodV(this,obj,methodID,args);

	mov	eax, DWORD PTR _this$[esp-4]
	lea	edx, DWORD PTR _methodID$[esp]
	push	edx
	push	DWORD PTR _methodID$[esp]
	mov	ecx, DWORD PTR [eax]
	push	DWORD PTR _obj$[esp+4]
	push	eax
	call	DWORD PTR [ecx+200]

; 954  :         va_end(args);
; 955  :         return result;
; 956  :     }

	ret	0
?CallIntMethod@JNIEnv_@@QAAJPAV_jobject@@PAU_jmethodID@@ZZ ENDP ; JNIEnv_::CallIntMethod
_TEXT	ENDS
END
