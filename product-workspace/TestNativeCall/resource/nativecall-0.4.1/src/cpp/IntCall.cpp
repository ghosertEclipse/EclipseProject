/*
 * IntCall.cpp
 * 
 * Created on 11.09.2004.
 *
 * $Id: IntCall.cpp,v 1.3 2006/04/19 20:54:55 grnull Exp $
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
#include "com_eaio_nativecall_IntCall.h"

#ifdef _WINDOWS
#include <windows.h>
#include <winbase.h>
#endif

#ifdef _WINDOWS
#ifdef _X86_

#define _push(x) __asm { push x };

#endif
#endif

// NativeCall fields

extern jfieldID fieldFunction, fieldModule, fieldFunctionHandle,
 fieldModuleHandle, fieldLastErrorCode;

// Holder fields

extern jfieldID fieldHolderO;

// Classes

extern jclass classBoolean,/* classByte, classCharacter, classShort,*/
 classInteger, /* classLong, classFloat, classDouble,*/ classString,
 classByteArray, classCharArray, /*classIntArray,*/ classHolder;

// Wrapper class methods

extern jmethodID methodBooleanValue,/* methodByteValue, methodCharValue,
methodShortValue,*/ methodIntValue/*, methodLongValue, methodFloatValue,
methodDoubleValue*/;

// Constructors

extern jmethodID newIntegerInt, newBooleanBoolean;

/*
 * Class:     com_eaio_nativecall_IntCall
 * Method:    executeCall
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_eaio_nativecall_IntCall_executeCall
(JNIEnv *env, jobject obj) {

 jint functionHandle = env->GetIntField(obj, fieldFunctionHandle);
 jint outVal;

#ifdef _WINDOWS
#ifdef _X86_

 __asm {

  call functionHandle
  mov outVal, eax

 }

#endif
#endif

 env->SetIntField(obj, fieldLastErrorCode, GetLastError());

 return outVal;

}

/*
 * Class:     com_eaio_nativecall_IntCall
 * Method:    executeCall0
 * Signature: ([Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_com_eaio_nativecall_IntCall_executeCall0
(JNIEnv *env, jobject obj, jobjectArray params) {
  
 const int len = env->GetArrayLength(params);
 
 int* arrays = NULL;
 if (!(arrays = new int[len])) {
  env->ThrowNew(env->FindClass("java/lang/OutOfMemoryError"), NULL);
  return 0;
 }
 memset(arrays, 0, (sizeof(int) * len));

 jobject param;

 for (int i = len - 1; i >= 0; i--) {  
  
  param = env->GetObjectArrayElement(params, i);
  
  if (param == NULL) {
   _push(0);
  }
  else if (env->IsInstanceOf(param, classInteger)) {
   int intArg = env->CallIntMethod(param, methodIntValue);
   _push(intArg)
  }
  else if (env->IsInstanceOf(param, classByteArray)) {
   char* byteArrayArg = (char*) env->GetPrimitiveArrayCritical((jarray) param, 0);
   arrays[i] = (int) &byteArrayArg;
   _push(byteArrayArg)
  }
  else if (env->IsInstanceOf(param, classCharArray)) {
   unsigned short* charArrayArg = (unsigned short*) env->GetPrimitiveArrayCritical(
    (jarray) param, 0);
   arrays[i] = (int) &charArrayArg;
   _push(charArrayArg)
  }
  else if (env->IsInstanceOf(param, classBoolean)) {
   jboolean booleanArg = env->CallBooleanMethod(param, methodBooleanValue);
   int tempArg = (booleanArg == JNI_FALSE ? 0 : 1);
   _push(tempArg)
  }
  else if (env->IsInstanceOf(param, classHolder)) {
   
   /* Holder */
   
   jobject o = env->GetObjectField(param, fieldHolderO);
   
   if (env->IsInstanceOf(o, classInteger)) {
    int *intPtr = new int;
    *intPtr = env->CallIntMethod(o, methodIntValue);
    arrays[i] = (int) intPtr;
    _push(intPtr);
   }
   else if (env->IsInstanceOf(o, classByteArray)) {
    char* byteArrayArg = (char*) env->GetPrimitiveArrayCritical((jarray) o, 0);
    arrays[i] = (int) &byteArrayArg;
    _push(byteArrayArg)
   }
   else if (env->IsInstanceOf(o, classCharArray)) {
    unsigned short* charArrayArg = (unsigned short*) env->GetPrimitiveArrayCritical(
     (jarray) o, 0);
    arrays[i] = (int) &charArrayArg;
    _push(charArrayArg)
   }
   else if (env->IsInstanceOf(o, classBoolean)) {
    jboolean booleanArg = env->CallBooleanMethod(o, methodBooleanValue);
    int *tempPtr = new int;
    *tempPtr = (booleanArg == JNI_FALSE ? 0 : 1);
    arrays[i] = (int) tempPtr;
    _push(tempPtr);
   }
   
   /* end Holder */
   
  }
  
 }

 jint functionHandle = env->GetIntField(obj, fieldFunctionHandle);
 jint outVal;
 
#ifdef _WINDOWS
#ifdef _X86_
 
 __asm {
  
  call functionHandle
  mov outVal, eax
   
 }
 
#endif
#endif
 
 for (int j = 0; j < len; ++j) {
  param = env->GetObjectArrayElement(params, j);
  if (param == NULL) {}
  else if (env->IsInstanceOf(param, classByteArray) || env->IsInstanceOf(param, classCharArray)) {
   env->ReleasePrimitiveArrayCritical((jarray) param, (void*) arrays[j], 0);
  }
  else if (env->IsInstanceOf(param, classHolder)) {
   
   jobject o = env->GetObjectField(param, fieldHolderO);

   if (env->IsInstanceOf(o, classInteger)) {
    int* out = (int*) arrays[j];
    env->SetObjectField(param, fieldHolderO, env->NewObject(classInteger, newIntegerInt, *out));
    delete out;
   }
   else if (env->IsInstanceOf(o, classByteArray) || env->IsInstanceOf(o, classCharArray)) {
    env->ReleasePrimitiveArrayCritical((jarray) o, (void*) arrays[j], 0);
   }
   else if (env->IsInstanceOf(o, classBoolean)) {
    int* out = (int*) arrays[j];
    env->SetObjectField(param, fieldHolderO, env->NewObject(classBoolean, newBooleanBoolean, (*out == 0 ? JNI_FALSE : JNI_TRUE)));
    delete out;
   }
   
  }

 }
 
 delete [] arrays;
 
 env->SetIntField(obj, fieldLastErrorCode, GetLastError());
 
 return outVal;
 
}
