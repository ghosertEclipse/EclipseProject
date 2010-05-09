/*
 * NativeCall.cpp
 * 
 * Created on 11.09.2004.
 *
 * $Id: NativeCall.cpp,v 1.4 2006/04/19 20:54:55 grnull Exp $
 *
 * eaio: NativeCall - calling operating system methods from Java
 * Copyright (c) 2004-2006 Johann Burkard (<mailto:jb@eaio.com>)
 * <http://eaio.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

#include <jni.h>
#include "com_eaio_nativecall_NativeCall.h"

#ifdef _WINDOWS
#include <windows.h>
#include <winbase.h>
#endif

// NativeCall fields

jfieldID fieldFunction, fieldModule, fieldFunctionHandle,
 fieldModuleHandle, fieldLastErrorCode;

// Holder fields

jfieldID fieldHolderO;

// Classes

jclass classBoolean,/* classByte, classCharacter, classShort,*/
 classInteger, /* classLong, classFloat, classDouble,*/ classString,
 classByteArray, classCharArray, /*classIntArray,*/ classHolder;

// Wrapper class methods

jmethodID methodBooleanValue,/* methodByteValue, methodCharValue,
methodShortValue,*/ methodIntValue/*, methodLongValue, methodFloatValue,
methodDoubleValue*/;

// Constructors

jmethodID newIntegerInt, newBooleanBoolean;

/*
 * Class:     com_eaio_nativecall_NativeCall
 * Method:    initIDs
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_eaio_nativecall_NativeCall_initIDs
(JNIEnv *env, jclass cls) {

 // NativeCall fields

 fieldFunction = env->GetFieldID(cls, "function", "Ljava/lang/String;");
 fieldModule = env->GetFieldID(cls, "module", "Ljava/lang/String;");

 fieldFunctionHandle = env->GetFieldID(cls, "functionHandle", "I");
 fieldModuleHandle = env-> GetFieldID(cls, "moduleHandle", "I");

 fieldLastErrorCode = env->GetFieldID(cls, "lastErrorCode", "I");

 // Holder fields

 classHolder = (jclass) env->NewGlobalRef(env->FindClass("com/eaio/nativecall/Holder"));
 fieldHolderO = env->GetFieldID(classHolder, "o", "Ljava/lang/Object;");

 // Other classes

 classBoolean = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Boolean"));
 /*classByte = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Byte"));
 classCharacter = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Character"));
 classShort = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Short"));*/
 classInteger = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Integer"));
 /*classLong = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Long"));
 classFloat = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Float"));
 classDouble = (jclass) env->NewGlobalRef(env->FindClass("java/lang/Double"));*/
 classString = (jclass) env->NewGlobalRef(env->FindClass("java/lang/String"));
 classByteArray = (jclass) env->NewGlobalRef(env->FindClass("[B"));
 classCharArray = (jclass) env->NewGlobalRef(env->FindClass("[C"));
 /*classIntArray = (jclass) env->NewGlobalRef(env->FindClass("[I"));*/

 // Wrapper class methods

 methodBooleanValue = env->GetMethodID(classBoolean,
  "booleanValue", "()Z");
 /*methodByteValue = env->GetMethodID(classByte,
  "byteValue", "()B");
 methodCharValue = env->GetMethodID(classCharacter,
  "charValue", "()C");
 methodShortValue = env->GetMethodID(classShort,
  "shortValue", "()S");*/
 methodIntValue = env->GetMethodID(classInteger,
  "intValue", "()I");
 /*methodLongValue = env->GetMethodID(classLong,
  "longValue", "()J");
 methodFloatValue = env->GetMethodID(classFloat,
  "floatValue", "()F");
 methodDoubleValue = env->GetMethodID(classDouble,
  "doubleValue", "()D");*/

 // Constructors

 newIntegerInt = env->GetMethodID(classInteger, "<init>", "(I)V");
 newBooleanBoolean = env->GetMethodID(classBoolean, "<init>", "(Z)V");
  
}

/*
 * Class:     com_eaio_nativecall_NativeCall
 * Method:    initHandles
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_eaio_nativecall_NativeCall_initHandles
(JNIEnv *env, jobject obj) {
  
 bool out = JNI_TRUE;
 
 jstring moduleNameS = (jstring) env->GetObjectField(obj, fieldModule);
 jstring functionNameS = (jstring) env->GetObjectField(obj, fieldFunction);
 
 const char* moduleName = env->GetStringUTFChars(moduleNameS, 0);
 const char* functionName = env->GetStringUTFChars(functionNameS, 0);
 
#ifdef _WINDOWS
 
 HMODULE mod = LoadLibrary(moduleName);
 
 if (mod == NULL) {
  /* no such module or DllMain returned FALSE */
  env->SetIntField(obj, fieldLastErrorCode, GetLastError());
  out = JNI_FALSE;
 }
 else {
  FARPROC addr = GetProcAddress(mod, functionName);
  if (addr == NULL) {
   /* function not found */
   FreeLibrary(mod);
   env->SetIntField(obj, fieldLastErrorCode, GetLastError());
   out = JNI_FALSE;
  }
  else {
   /* all ok */ 
   env->SetIntField(obj, fieldModuleHandle, (jint) mod);
   env->SetIntField(obj, fieldFunctionHandle, (jint) addr);
  }
 }
 
#endif
 
 env->ReleaseStringUTFChars(moduleNameS, moduleName);
 env->ReleaseStringUTFChars(functionNameS, functionName);
 
 return out;
 
}

/*
 * Class:     com_eaio_nativecall_NativeCall
 * Method:    getLastError
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_eaio_nativecall_NativeCall_getLastError
(JNIEnv *env, jobject obj) {
 
 jint lastError = env->GetIntField(obj, fieldLastErrorCode);
 
 if (lastError == 0) return NULL;
 
 jstring out = NULL;
 
#ifdef _WINDOWS
 
 LPVOID msgBufPtr = NULL;
 FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER | 
  FORMAT_MESSAGE_FROM_SYSTEM, NULL, lastError, 0,
  (LPSTR) &msgBufPtr, 0, NULL);
 
 out = env->NewStringUTF((char*) msgBufPtr);
 
 LocalFree(msgBufPtr);
 
#endif
 
 return out;
 
}

/*
 * Class:     com_eaio_nativecall_NativeCall
 * Method:    destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_eaio_nativecall_NativeCall_destroy
(JNIEnv *env, jobject obj) {
 
 jint module = env->GetIntField(obj, fieldModuleHandle);

 if (module == 0) return;
 
#ifdef _WINDOWS
  
 if (FreeLibrary((HMODULE) module) == 0) {
  env->SetIntField(obj, fieldLastErrorCode, GetLastError());
 }
  
#endif
 
 env->SetIntField(obj, fieldModuleHandle, 0);
 env->SetIntField(obj, fieldFunctionHandle, 0);
 
 return;
 
}