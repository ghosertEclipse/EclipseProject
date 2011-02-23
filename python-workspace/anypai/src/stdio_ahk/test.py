import subprocess as sub
p = sub.Popen('test.exe',stdout=sub.PIPE,stderr=sub.PIPE)
output, errors = p.communicate()
print 'output'
print output
print 'errors'
print (errors == '')
