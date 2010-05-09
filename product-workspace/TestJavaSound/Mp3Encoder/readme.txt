MP3 encoding support for Java Sound
===================================

Version: 2001-04-25

Overview
--------
This package contains a plug-in for Java, which enables 
mp3 encoding using the Java Sound API. Included are
the compiled native libraries for Windows and Linux/i386 (PC).

All files are part of Tritonus, an open source implementation
of the Java Sound API. Tritonus is under the GNU Library 
General Public License. The source code of the plug-in is 
contained in the tritonus distribution.


Features
--------
The encoder creates MPEG1 layer III, MPEG2 layer III
or MPEG2.5 layer III files and chooses automatically
the right encoding, based on sample rate and chosen
bit rate. Bit rates can be chosen from 8KBit/s to 
320KBit/s. VBR (variable bit rate) is supported with
LAME. Different quality levels may be choosen which affect
- encoding speed for CBR (constant bit rate)
- mp3 file size for VBR.
To see how these parameters are set, see the included
Mp3Encoder.java demo program.


Requirements
------------
- A Java 2 platform. It should work with JDK1.2.x (then
  you need a full installation of tritonus), but
  JDK1.3 or JRE1.3 is recommended. You can download a 
  JDK or JRE from http://www.javasoft.com . This documentation 
  assumes a Sun JDK1.3 installation.
- The libraries tritonus_mp3.jar and tritonus_share.jar.
  You can download them from http://www.tritonus.org/plugins.html
- On Windows:
  An mp3 encoder in a bladenc-compatible dll. There are
  many different encoders, but we highly recommend LAME.
  You can download compiled LAME dll's from
  http://dkutsanov.chat.ru/
  http://www.jthz.com/~lame/
  More on http://www.sulaco.org/mp3/links.html (at bottom).
  Choose a package which includes lame_enc.dll for Windows.
  Any version 3.70 and above should work fine.
  Note: 3.88 versions do not work for mono files
  LAME Homepage: http://www.sulaco.org/mp3/
- On Linux/i386:
  LAME in a recent version (i.e. after April 2001). Best is
  to download it directly from CVS as described below.


Installation of the mp3 encoder plugin on Windows
-------------------------------------------------
1. Copy tritonus_mp3.jar, and tritonus_share.jar in 
   the Extension Directory (see below).
2. Copy lametritonus.dll in the Binary Extension
   Directory or in Windows System.
3. If your encoder dll is not named "lame_enc.dll" 
   rename it to that name. Put it either in Windows System
   Directory or in the folder where your Java program runs.

Windows directories
-------------------
Extension Directory:
  For JDK: jre\lib\ext in JDK home,
    e.g. C:\JDK1.3\jre\lib\ext
  For JRE: lib\ext in JRE home, 
    e.g. C:\Program Files\JavaSoft\JRE\1.3.0_02\lib\ext
  Alternatively, you may also place the jars in the classpath.
  Then you can place them where ever you want:
  set CLASSPATH=C:\path\to\tritonus_share.jar;C:\path\to\tritonus_mp3.jar;%CLASSPATH%
Binary Extension Directory:
  For JDK: jre\bin in JDK home,
    e.g. C:\JDK1.3\jre\bin
  For JRE: bin in JRE home, 
    e.g. C:\Program Files\JavaSoft\JRE\1.3.0_02\bin
Windows System Directory:
  On Windows 95/98/ME:
    C:\Windows\System
  On NT4/2000:
    C:\Winnt\System32

The JRE paths are the place where to put the files for the 
Java plugin for browsers.
  

Installation of the mp3 encoder plugin on Linux/i386
----------------------------------------------------
1. Copy tritonus_mp3.jar, and tritonus_share.jar in 
   the Extension Directory (see below).
2. Copy liblametritonus.so in the Binary Extension
   Directory.
3. Download LAME via CVS:
   cvs -d:pserver:anonymous@cvs.lame.sourceforge.net:/cvsroot/lame login
   (Password empty)
   cvs -z3 -d:pserver:anonymous@cvs.lame.sourceforge.net:/cvsroot/lame co lame
   This creates the directory "lame".
4. Installation of LAME:
   Go in the "lame" directory and issue "./configure --enable-shared" 
   and "make". 
   As root, invoke "make install". This copies the lame library to 
   /usr/local/lib. You may need to include /usr/local/lib to 
   /etc/ld.so.conf and then run "ldconfig"

Linux directories
-----------------
You can find out the installation directory of the JDK by issuing
"which java". I get the follwing result:
/usr/local/java2/bin/java 
which means that JDK home is /usr/local/java2. The existence of
the "jre" directory in JDK home usually means that you have installed
the JDK (rather than JRE).

Extension Directory:
  For JDK: jre/lib/ext in JDK home,
    e.g. /usr/local/java2/jre/lib/ext
  For JRE: lib/ext in JRE home, 
    e.g. /usr/local/jre1.3.0_02/lib/ext
  Alternatively, you may also place the jars in the classpath.
  Then you can place them where ever you want:
  export CLASSPATH=tritonus_share.jar:tritonus_mp3.jar:$CLASSPATH
Binary Extension Directory:
  For JDK: jre/lib/i386 in JDK home,
    e.g. ls /usr/local/java2/jre/lib/i386
  For JRE: bin in JRE home, 
    e.g. ls /usr/local/jre1.3.0_02/lib/i386


Test
----
You can test whether your installation has succeeded by
running the included program Mp3Encoder.java.
1. compile it with
     javac Mp3Encoder.java
   If this succeeds, the jars are found.
2. run it with a 44KHz wave file:
     java Mp3Encoder <file>.wav
   It should output a file called <file>.mp3.
   For error checking, use this:
     java Mp3Encoder -v -e -t <file>.wav
   To see all parameters of Mp3Encoder, call it like this:
     java Mp3Encoder -h

Note that the linux version may become incompatible (when the
LAME developers change the LAME API). If you encounter that 
problem, please notify me at florian@tritonus.org.


FAQ: Why don't you distribute LAME with this plug-in ?
------------------------------------------------------
LAME may violate patents on the binary mp3 file format.
These patents are held by Fraunhofer/Thomson. They do not
apply to all countries. Therefore, distributing a compiled 
version of LAME would therefore result in legal actions 
against us.

FAQ: Why LAME (rather than e.g. BladEnc) ?
------------------------------------------
1. I made tests where I compared about 10 different encoders 
   by ear (free and commercial ones). BladEnc was worst of all. 
   There were very audible noises.
2. The German magazine c't agreed with my test results. They 
   found Fraunhofer's encoder the best, closely followed by 
   LAME. All other encoders don't compare.
3. BladEnc is very illegal. LAME is less illegal: BladEnc uses 
   copyrighted ISO code and its patented psycho model. LAME 
   is a clean-room implementation with an own psycho-model. 
   Still, the mp3 file format is patented, which no free encoder 
   can overcome...
4. BladEnc features some errors which are in the ISO code 
   (e.g. CRC calculation)
5. LAME is under heavy development. It gets better every day :)

FAQ: Can I use this plug-in in an applet ?
------------------------------------------
Yes, but you can only use it in an applet if the mp3 (i.e. the dll
or the .so) plug-in is installed on the client computer.
You cannot deploy the native library to the user for security 
reasons. Also, in recent JDK's, it has become impossible to 
install a Service Provider (which is this plug-in) in an 
applet. 


Contact
-------
When you encounter problems or want to tell me that it works,
feel free to contact me at
florian@tritonus.org. You can also subscribe to the Tritonus
mailing list at
http://lists.sourceforge.net/lists/listinfo/tritonus-user
and write your email to the list.


Download
--------
The latest version of this plug-in can be downloaded from
http://www.tritonus.org
in the plugins section.


Copyright
---------
This program is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published
by the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

The GNU Library General Public License is included in the file "LGPL".

(c) 2001 by Florian Bomers and the Tritonus team
