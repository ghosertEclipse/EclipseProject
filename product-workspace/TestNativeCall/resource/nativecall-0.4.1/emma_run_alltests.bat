@echo off
rem $Id: emma_run_alltests.bat,v 1.1 2006/01/05 19:59:53 grnull Exp $
rem Instrument classes, run the test suite and produce HTMLified results
rem Requires emma.jar (from EMMA -- <http://emma.sourceforge.net>) in this
rem directory.
java -cp emma.jar emma instr -m overwrite -cp bin
java -Djava.library.path=src\cpp\Release -cp emma.jar;bin;"C:\Program Files\eclipse\plugins\org.junit_3.8.1\junit.jar";nativeloader-200505172341.jar com.eaio.nativecall.AllTests
java -cp emma.jar;bin emma report -r html -sp src\java;srctest\java -in coverage.em,coverage.ec
