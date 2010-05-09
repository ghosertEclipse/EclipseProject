#coding=utf-8
# Create your views here.
from django.utils.httpwrappers import HttpResponse
from django.core.extensions import render_to_response

def input(request):
    input = request.REQUEST["input"]
    return HttpResponse(_('<p>You input is "%(input)s"</p>') % {'input':input})

def json(request):
    a = {'head':(unicode(_('Name'), 'utf-8'), unicode(_('Telphone'), 'utf-8')), 'body':[(u'张三', '1111'), (u'李四', '2222')]}
    import simplejson
    return HttpResponse(simplejson.dumps(a))