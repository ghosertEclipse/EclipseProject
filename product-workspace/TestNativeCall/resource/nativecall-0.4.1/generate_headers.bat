@echo off
rem $Id: generate_headers.bat,v 1.1 2006/01/05 19:59:53 grnull Exp $
rem Generates headers from the NativeCall classes
cd bin
javah com.eaio.nativecall.IntCall com.eaio.nativecall.NativeCall com.eaio.nativecall.VoidCall
