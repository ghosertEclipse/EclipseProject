# $Id: readme.txt,v 1.1 2006/01/05 19:55:58 grnull Exp $

Hi,

this is the readme file for people who want to build the native libraries for
NativeCall themselves.

Compiling the native libraries requires some header files (after all, it's
just C++).

Search for

	jni.h

which is required. You will most likey also need jni_md.h and
jniproto_md.h. Copy these files to this directory.

The library has been compiled and is set up for libctiny.lib to make the
binaries even smaller. You can download libctiny.lib from

	<http://msdn.microsoft.com/msdnmag/issues/01/01/hood/default.aspx>

After you downloaded it, extract libctiny.lib to this folder. You also might
have to change the paths for the compiler and the linker.

