#coding=utf-8
# Create your views here.
from django.models.address import addresses
from django.utils.httpwrappers import HttpResponse, HttpResponseRedirect
from django.core.extensions import render_to_response
from django.core.template import loader, Context
from django.contrib.admin.views.decorators import staff_member_required
from django.conf.settings import UPLOAD_USER

@staff_member_required
def upload(request):
    if request.user.username != UPLOAD_USER:
        return render_to_response('address/error', {'message':'你需要使用 %s 来登录！' % UPLOAD_USER})
    file_obj = request.FILES.get('file', None)
    if file_obj:
        import csv
        import StringIO
        buf = StringIO.StringIO(file_obj['content'])
        try:
            reader = csv.reader(buf)
        except:
            return render_to_response('address/error', {'message':'你需要上传一个csv格式的文件！'})
        for row in reader:
            objs = addresses.get_list(name__exact=row[0])
            if not objs:
                obj = addresses.Address(name=row[0], gender=row[1], telphone=row[2], mobile=row[3], room=row[4])
            else:
                obj = objs[0]
                obj.gender = row[1]
                obj.telphone = row[2]
                obj.mobile = row[3]
                obj.room = row[4]
            obj.save()
            
        return HttpResponseRedirect('/address/')
    else:
        return render_to_response('address/error', {'message':'你需要上传一个文件！'})
    
def output(request):
    response = HttpResponse(mimetype='text/csv')
    response['Content-Disposition'] = 'attachment; filename=%s' % 'address.csv'
    t = loader.get_template('csv')
    objs = addresses.get_list()
    d = []
    for o in objs:
        d.append((o.name, o.gender, o.telphone, o.mobile, o.room))
    c = Context({
        'data': d,
    })
    response.write(t.render(c))
    return response

from django.views.generic.list_detail import object_list
def search(request):
    name = request.REQUEST['search']
    if name:
        extra_lookup_kwargs = {'name__icontains':name}
        extra_context = {'searchvalue':name}
        return object_list(request, 'address', 'addresses', paginate_by=10, extra_context=extra_context, extra_lookup_kwargs=extra_lookup_kwargs)
    else:
        return HttpResponseRedirect('/address/')
