#coding=utf-8
from django.core.extensions import render_to_response

address = [
    {'name':'张三', 'address':'地址一'}, 
    {'name':'李四', 'address':'地址二'}
    ]

def index(request):
    return render_to_response('list', {'address': address})
