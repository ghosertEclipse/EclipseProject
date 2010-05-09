import time
import calendar
import datetime

st = time.gmtime()
print "get current utc struct_time: " + str(st)
print
seconds = calendar.timegm(st)
print "struct_time to utc seconds: " + str(seconds)
print
st = time.gmtime(seconds)
print "seconds to utc struct_time: " + str(st)
print

st = time.localtime()
print "get current local struct_time: " + str(st)
print
seconds = time.mktime(st)
print "struct_time to local seconds: " + str(seconds)
print
st = time.localtime(seconds)
print "seconds to local struct_time: " + str(st)
print

string = time.strftime("%Y %m %d %H:%M:%S", st)
print "struct_time to string: " + string
print
st = time.strptime(string, "%Y %m %d %H:%M:%S")
print "string to struct_time: " + str(st)
print

def utcToLocalString(format, st):
    seconds = calendar.timegm(st)
    st = time.localtime(seconds)
    return time.strftime(format, st)

print "convert utc struct_time to local format string: " + utcToLocalString("%Y %m %d %H:%M:%S", time.gmtime())
print

print "get current local string time: " + time.ctime()
