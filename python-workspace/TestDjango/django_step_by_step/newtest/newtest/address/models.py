#coding=utf-8
from django.db import models

# Create your models here.

class Address(models.Model):
    name = models.CharField('姓名', maxlength=20, unique=True)
    gender = models.CharField('性别', choices=(('M', '男'), ('F', '女')), 
        maxlength=1, radio_admin=True)
    telphone = models.CharField('电话', maxlength=20)
    mobile = models.CharField('手机', maxlength=11)
    room = models.CharField('房间', maxlength=10)

    def __repr__(self):
        return self.name

    class Admin: pass
