��Ҫ��nGetPortName, Java_com_sun_media_sound_PortMixerProvider_nNewPortMixerInfo����������
�ѱ��������޸Ĺ���PortMixer.c��PortMixerProvider.c�����ļ�����
j2sdk-1_4_2-src-scsl\j2se\src\share\native\com\sun\media\sound\ Ŀ¼�£��滻��ԭ�����ļ�
THEN REBUILD JDK AND GET JSOUND.DLL


Java��C֮��ͨ��JNI���������ַ���
��� 
�����ṩһ�������ʵ��,˵��Java��C֮��ͨ��JNI���ݺ��������ַ����Ĳ���,��������������⣨˫�ֽڻ���ֽ����룩�����ĸ��������Դ���룬��Դ��������˽��ͣ�C�����ṩ�� C��C++���ַ�ʽʵ�ֵ��������롣�������д������ʵ�黷���²���ͨ�������Ķ������Java��C(��C++)��һ���Ļ���֪ʶ��
ʵ�黷��
Windows XP ����Windows2000��
j2sdk1.4.2/j2re1.4.2
Microsoft VisualC++6.0
��װ��j2sdk����Ҫ���û������� 
CLASSPATH=.;C:\j2sdk1.4.2\bin;C:\j2sdk1.4.2\lib\dt.jar;C:\j2sdk1.4.2\lib\tools.jar;C:\j2sdk1.4.2\lib\htmlconverter.jar��j2sdk��װ��c:\j2sdk1.4.2Ŀ¼�£�
Դ���뼰����˵��
Java���룺
/*
* javactransfer.java
* By dpwu
* e-mail��dpwu_js@sina.com.cn
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
    System.loadLibrary("javactransfer");//����dll
  }
  public static void main( String[] args )
  {

     javactransfer obj= new javactransfer();
     System.out.println("");
     System.out.println("");
     System.out.println("       begin!");
     System.out.println("");
     System.out.println("");
     String javasend="��������chinese!";
     System.out.println(" java send:"+"["+javasend+"]");
String javarecv=obj.hypotenuse("teststr",javasend,1);  
//javasend�����������ַ�����C
//javarecv����C�����е����ַ���
     System.out.println(" java recv:"+"["+javarecv+"]");
     System.out.println("");
     System.out.println("");
     System.out.println("        end!");
   }
}



Cʵ�ִ���Ϊ��
/*
* javactransfer.c
* By dpwu
* e-mail��dpwu_js@sina.com.cn
*/

#include <windows.h>
#include "javactransfer.h"//ͨ��javah �Cjni javactransfer ����
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
/*�Ͼ�recvtest����Java�������������ַ�����ȷ,�����Ϊ�¾�,���������:
const char * recvtest = (*env)->GetStringUTFChars( env,recv_buf,0);  
*/
printf( " c  recv  :[%s]\n" , recvtest);
  
  sprintf(Buf_Return,"��������chinese!");
  printf( "\n\n\n c send   :[%s]\n" , Buf_Return); 
  recv_buf = WindowsTojstring(env ,Buf_Return);
/*�Ͼ�recv_buf��windows����C�к��������ַ�����Java��ȷ;�����Ϊ�¾��������:
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
��javactransfer.java,javactransfer.c������d:\javacĿ¼�£�
Ȼ��ִ��
(�˴�ͼƬ�����ϴ���

javac javactransfer.java :����javaԴ����;
javah �Cjni javactransfer :����ͷ�ļ�;
cl -Ic:\j2sdk1.4.2\include -Ic:\j2sdk1.4.2\include\win32  -LD javactransfer.c -Fejavactransfer.dll
ͨ��Microsoft Visual C++��CԴ��������dll�ļ�����java���á�
ִ�н�����£�
���˴�ͼƬ�����ϴ���
ִ�м��룺java javactranfer 

C++ʵ�ִ���Ϊ:
/*
* javactransfer.cpp
* By dpwu
* e-mail��dpwu_js@sina.com.cn
*/

#include <windows.h>
#include "javactransfer.h"//ͨ��javah �Cjni javactransfer ����
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
  ������Ͼ��Ϊ�¾䣬C����Java�ַ���ʱ���������룺
  const char *ctest = (env)->GetStringUTFChars(  recv_buf , 0);
*/
  printf( " c  recv  :[%s]\n" , ctest);
  
  sprintf(Buf_Return,"��������chinese!");
  printf( "\n\n\n c send   :[%s]\n" , Buf_Return); 
  recv_buf = WindowsTojstring(env ,Buf_Return);
/*�Ͼ�ִ����ȷ,������Ͼ任���¾�,Java�ڽ���C�ַ���ʱ��������
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
��ΪC++ʵ��ʱ,ֻ��Ҫ�ڱ���ʱ��Ϊ��
cl -Ic:\j2sdk1.4.2\include -Ic:\j2sdk1.4.2\include\win32 -LD javactransfer.cpp -Fejavactransfer.dll 

��
�����������ݿ���Ӧ���ڸ���ϵͳ�ĸ��죬���д�������ϵͳ�����磺���ڵȣ��ܶ඼��unix+C+tuxedo���ڹ����ڴ�ʵ��,����ЩϵͳΪ�����㰲ȫ����Ӧʱ��Ҫ�󲻿����ڶ�������ȫ����,�������ṩ�ķ���,���Ժ�tuxedo�ͻ���ͨ��VC++��װ�ɸ���dll,��Щdll������tuxedo����ܶ��������ԵĻ����Ͻ��л���Java�Ŀ���,����������µĻ���WEBӦ�õ��������д���֮�������������ϵ��dpwu_js@sina.com.cn��

�ο����ϣ�David Wendt ��NLS strings and JNI��
WebSphere programmer, IBM
