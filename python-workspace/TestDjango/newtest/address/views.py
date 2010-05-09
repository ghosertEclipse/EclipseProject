#coding=utf-8
# Create your views here.
from newtest.address.models import Address
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response
from django.conf import settings
from django.contrib.admin.views.decorators import staff_member_required

@staff_member_required
def upload(request):
    if request.user.username != settings.UPLOAD_USER:
        return render_to_response('address/error.html',
            {'message':'你需要使用 %s 来登录！' % settings.UPLOAD_USER})

    file_obj = request.FILES.get('file', None)
    if file_obj:
        import csv
        import StringIO
        content = ''
        for chunk in file_obj.chunks():
            content = content + chunk
        buf = StringIO.StringIO(content)
        try:
            reader = csv.reader(buf)
        except:
            return render_to_response('address/error.html',
                {'message':'你需要上传一个csv格式的文件！'})
        for row in reader:
#            objs = Address.objects.get_list(name__exact=row[0])
            objs = Address.objects.filter(name=row[0])
            if not objs:
                obj = Address(name=row[0], gender=row[1],
                    telphone=row[2], mobile=row[3], room=row[4])
            else:
                obj = objs[0]
                obj.gender = row[1]
                obj.telphone = row[2]
                obj.mobile = row[3]
                obj.room = row[4]
            obj.save()

        return HttpResponseRedirect('/address/')
    else:
        return render_to_response('address/error.html',
            {'message':'你需要上传一个文件！'})

