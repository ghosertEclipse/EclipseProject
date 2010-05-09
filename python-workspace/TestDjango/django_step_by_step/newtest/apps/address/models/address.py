#coding=utf-8
from django.core import meta

# Create your models here.
class Address(meta.Model):
    name = meta.CharField('姓名', maxlength=20, core=True, unique=True)
    gender = meta.CharField('性别', choices=(('M', '男'), ('F', '女')), maxlength=1, core=True, radio_admin=True)
    telphone = meta.CharField('电话', maxlength=20, core=True)
    mobile = meta.CharField('手机', maxlength=11, core=True)
    room = meta.CharField('房间', maxlength=10, core=True)
    
    def __repr__(self):
        return self.name

    class META:
        admin = meta.Admin()
        module_name = 'addresses'
        ordering = ['name']