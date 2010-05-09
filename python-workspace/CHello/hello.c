#include <jni.h>
#include "test_Hello.h"
#include <stdio.h>
JNIEXPORT void JNICALL Java_test_Hello_sayHello
(JNIEnv *env, jobject obj)
{
	printf("Hello world !\n");
	return;
}
