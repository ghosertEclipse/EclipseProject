主要看nGetPortName, Java_com_sun_media_sound_PortMixerProvider_nNewPortMixerInfo这两个函数
把本工程下修改过的PortMixer.c和PortMixerProvider.c两个文件放在
j2sdk-1_4_2-src-scsl\j2se\src\share\native\com\sun\media\sound\ 目录下，替换掉原来的文件
THEN REBUILD JDK AND GET JSOUND.DLL


Java与C之间通过JNI传递中文字符串
简介 
本文提供一个具体的实例,说明Java和C之间通过JNI传递含有中文字符串的参数,解决中文乱码问题（双字节或多字节乱码）。本文给出具体的源代码，对源代码进行了解释，C部分提供了 C和C++两种方式实现的完整代码。并且所有代码均在实验环境下测试通过。本文读者须对Java和C(或C++)有一定的基础知识。
实验环境
Windows XP （或Windows2000）
j2sdk1.4.2/j2re1.4.2
Microsoft VisualC++6.0
安装好j2sdk后需要设置环境变量 
CLASSPATH=.;C:\j2sdk1.4.2\bin;C:\j2sdk1.4.2\lib\dt.jar;C:\j2sdk1.4.2\lib\tools.jar;C:\j2sdk1.4.2\lib\htmlconverter.jar（j2sdk安装在c:\j2sdk1.4.2目录下）
源代码及代码说明
Java代码：
/*
* javactransfer.java
* By dpwu
* e-mail：dpwu_js@sina.com.cn
*/
public class javactransfer
{
  public String hypotenuse(String send_buf,String recv_buf,int errno)
  {
    return hypotenuse0(send_buf,recv_buf,errno);
  }

  private native String  hypotenuse0(String send_buf,String recv_buf,int errno);

  static
  {
    System.loadLibrary("javactransfer");//调用dll
  }
  public static void main( String[] args )
  {

     javactransfer obj= new javactransfer();
     System.out.println("");
     System.out.println("");
     System.out.println("       begin!");
     System.out.println("");
     System.out.println("");
     String javasend="发送中文chinese!";
     System.out.println(" java send:"+"["+javasend+"]");
String javarecv=obj.hypotenuse("teststr",javasend,1);  
//javasend传含有中文字符串给C
//javarecv接受C含有中的文字符串
     System.out.println(" java recv:"+"["+javarecv+"]");
     System.out.println("");
     System.out.println("");
     System.out.println("        end!");
   }
}



C实现代码为：
/*
* javactransfer.c
* By dpwu
* e-mail：dpwu_js@sina.com.cn
*/

#include <windows.h>
#include "javactransfer.h"//通过javah –jni javactransfer 生成
#include <stdio.h>
#include "stdlib.h"
#include "string.h"

char* jstringToWindows( JNIEnv *env, jstring jstr );
jstring WindowsTojstring( JNIEnv* env, char* str );

JNIEXPORT jstring JNICALL 
Java_javactransfer_hypotenuse0(JNIEnv *env,jobject obj,jstring send_buf,jstring recv_buf,jint errno)
{
  char * Buf_Return;

  Buf_Return  =  (char*)malloc(1024); 
  
  const char * recvtest = jstringToWindows( env, recv_buf );
/*上句recvtest接收Java传过来的中文字符串正确,如果改为下句,则出现乱码:
const char * recvtest = (*env)->GetStringUTFChars( env,recv_buf,0);  
*/
printf( " c  recv  :[%s]\n" , recvtest);
  
  sprintf(Buf_Return,"接收中文chinese!");
  printf( "\n\n\n c send   :[%s]\n" , Buf_Return); 
  recv_buf = WindowsTojstring(env ,Buf_Return);
/*上句recv_buf传windows本地C中含有中文字符串给Java正确;如果改为下句出现乱码:
recv_buf = (*env)->NewStringUTF( env, Buf_Return );
  */
  return recv_buf;
}
char* jstringToWindows( JNIEnv  *env, jstring jstr )
{
  int length = (*env)->GetStringLength(env,jstr );
  const jchar* jcstr = (*env)->GetStringChars(env,jstr, 0 );
  char* rtn = (char*)malloc( length*2+1 );
  int size = 0;
  size = WideCharToMultiByte( CP_ACP, 0, (LPCWSTR)jcstr, length, rtn,(length*2+1), NULL, NULL );
  if( size <= 0 )
    return NULL;
  (*env)->ReleaseStringChars(env,jstr, jcstr );
  rtn[size] = 0;
  return rtn;
}

jstring WindowsTojstring( JNIEnv* env, char* str )
{
  jstring rtn = 0;
  int slen = strlen(str);
  unsigned short* buffer = 0;
  if( slen == 0 )
    rtn = (*env)->NewStringUTF(env,str ); 
  else
  {
    int length = MultiByteToWideChar( CP_ACP, 0, (LPCSTR)str, slen, NULL, 0 );
    buffer = malloc( length*2 + 1 );
    if( MultiByteToWideChar( CP_ACP, 0, (LPCSTR)str, slen, (LPWSTR)buffer, length ) >0 )
      rtn = (*env)->NewString( env, (jchar*)buffer, length );
  }
  if( buffer )
  free( buffer );
  return rtn;
}
若javactransfer.java,javactransfer.c均放在d:\javac目录下，
然后执行
(此处图片不能上传）

javac javactransfer.java :编译java源代码;
javah –jni javactransfer :生成头文件;
cl -Ic:\j2sdk1.4.2\include -Ic:\j2sdk1.4.2\include\win32  -LD javactransfer.c -Fejavactransfer.dll
通过Microsoft Visual C++将C源代码生成dll文件，供java调用。
执行结果如下：
（此处图片不能上传）
执行键入：java javactranfer 

C++实现代码为:
/*
* javactransfer.cpp
* By dpwu
* e-mail：dpwu_js@sina.com.cn
*/

#include <windows.h>
#include "javactransfer.h"//通过javah –jni javactransfer 生成
#include <stdio.h>
#include "stdlib.h"
#include "string.h"

char* jstringToWindows( JNIEnv *env, jstring jstr );
jstring WindowsTojstring( JNIEnv* env, char* str );

JNIEXPORT jstring JNICALL 
Java_javactransfer_hypotenuse0(JNIEnv *env,jobject obj,jstring send_buf,jstring recv_buf,jint _tperrno)

{
  char * Buf_Return;
  Buf_Return  =  (char*)malloc(1024); 

  const char * ctest = jstringToWindows( env, recv_buf );
/*
  如果把上句改为下句，C接收Java字符串时将出现乱码：
  const char *ctest = (env)->GetStringUTFChars(  recv_buf , 0);
*/
  printf( " c  recv  :[%s]\n" , ctest);
  
  sprintf(Buf_Return,"接收中文chinese!");
  printf( "\n\n\n c send   :[%s]\n" , Buf_Return); 
  recv_buf = WindowsTojstring(env ,Buf_Return);
/*上句执行正确,如果把上句换成下句,Java在接收C字符串时出现乱码
recv_buf = (env)->NewStringUTF(Buf_Return);
*/
  return recv_buf;
}
char* jstringToWindows( JNIEnv  *env, jstring jstr )
{
  int length = (env)->GetStringLength(jstr );
  const jchar* jcstr = (env)->GetStringChars(jstr, 0 );
  char* rtn = (char*)malloc( length*2+1 );
  int size = 0;
  size = WideCharToMultiByte( CP_ACP, 0, (LPCWSTR)jcstr, length, rtn,(length*2+1), NULL, NULL );
  if( size <= 0 )
    return NULL;
  (env)->ReleaseStringChars(jstr, jcstr );
  rtn[size] = 0;
  return rtn;
}

jstring WindowsTojstring( JNIEnv* env, char* str )
{
  jstring rtn = 0;
  int slen = strlen(str);
  unsigned short * buffer = 0;
  if( slen == 0 )
    rtn = (env)->NewStringUTF(str ); 
  else
  {
    int length = MultiByteToWideChar( CP_ACP, 0, (LPCSTR)str, slen, NULL, 0 );
    buffer = (unsigned short *)malloc( length*2 + 1 );
    if( MultiByteToWideChar( CP_ACP, 0, (LPCSTR)str, slen, (LPWSTR)buffer, length ) >0 )
      rtn = (env)->NewString(  (jchar*)buffer, length );
  }
  if( buffer )
  free( buffer );
  return rtn;
}
当为C++实现时,只需要在编译时改为：
cl -Ic:\j2sdk1.4.2\include -Ic:\j2sdk1.4.2\include\win32 -LD javactransfer.cpp -Fejavactransfer.dll 

后话
本文所述内容可以应用于复杂系统的改造，现有大型生产系统（例如：金融等）很多都是unix+C+tuxedo基于共享内存实现,而这些系统为了满足安全和响应时间要求不可能在短期内完全丢弃,本文中提供的方法,可以和tuxedo客户端通过VC++封装成各种dll,这些dll可以在tuxedo本身很多优良特性的基础上进行基于Java的开发,以用来解决新的基于WEB应用的需求。如有错误之处，请与笔者联系：dpwu_js@sina.com.cn。

参考资料：David Wendt 《NLS strings and JNI》
WebSphere programmer, IBM
