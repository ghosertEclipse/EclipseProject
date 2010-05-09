#coding=utf-8
from django.core import template

register = template.Library()

#@register.filter(name='change_gender')
def change_gender(value):
    if value == 'M':
        return '男'
    else:
        return '女'
    
register.filter('change_gender', change_gender)