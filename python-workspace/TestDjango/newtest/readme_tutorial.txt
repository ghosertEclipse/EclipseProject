3.1   安装
python setup.py install
为了更好地使用 django-admin.py ，建议将 Python 的 Scripts 目录加入到 PATH 环境变量中去。
django-admin.py startproject newtest
启动 web server
manage.py runserver 0.0.0.0:80

<form method="post" action="/add/">
请注意 action 为 /add/ ，在 Django 中链接后面一般都要有 '/' ，不然有可能得不到 POST 数据

修改urls.py
(r'^add/$', 'newtest.add.index'),

(r'^csv/(?P<filename>\w+)/$', 'newtest.csv_test.output'),
指：http://localhost/csv/address/
其中的address会作为filename='address'传递给newtest.csv_test.output(request, filename):

创建 list.py
#coding=utf-8
from django.shortcuts import render_to_response

address = [
    {'name':'张三', 'address':'地址一'},
    {'name':'李四', 'address':'地址二'}
    ]

def index(request):
    return render_to_response('list.html', {'address': address})

修改 settings.py
TEMPLATE_DIRS = (
    # Put strings here, like "/home/html/django_templates".
    # Always use forward slashes, even on Windows.
    './templates',
)

创建 templates/list.html
<h2>通讯录</h2>
<table border="1">
  <tr><th>姓名</th><th>地址</th></tr>
  {% for user in address %}
  <tr>
  <td>{{ user.name }}</td>
  <td>{{ user.address }}</td>
  </tr>
{% endfor %}
</table>

SESSION & DATABASE
session 是为了实现页面间的数据交换而产生的东西，一般有一个 session_id ，它会保存在浏览器的 cookie 中，因此如果你的浏览器禁止了 cookie ，下面的试验是做不了的。
request.session['username'] = username
username = request.session.get('username', None)
try:
	del request.session['username']
except KeyError:
	pass

{% if not username %}
<form method="post" action="/login/">
    用户名：<input type="text" name="username" value=""><br/>
    <input type="submit" value="登录">
</form>
{% else %}
你已经登录了！{{ username }}<br/>
<form method="post" action="/logout/">
    <input type="submit" value="注销">
</form>
{% endif %}

因为在 django 中 session 是存放在数据库中的。所以在这里要进行数据库的初始化了
修改 settings.py
主要修改以下地方:
DATABASE_ENGINE = 'sqlite3'
DATABASE_NAME = './data.db'
初始化数据库
改了配置还不够，还要执行相应的建库、建表的操作，使用 django-admin.py 或 manage.py 都可以:
manage.py syncdb

创建 wiki app
manage.py startapp wiki

编辑 wiki/models.py
from django.db import models

# Create your models here.
class Wiki(models.Model):
    pagename = models.CharField(maxlength=20, unique=True)
    content = models.TextField()

修改 settings.py, 安装 app
虽然我们的其它工作没有做完，但我还是想先安装一下 app 吧。每个一 app 都需要安装一下。安装一般有两步：

a.修改settings.py
INSTALLED_APPS = (
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.sites',
    'newtest.wiki',
)
这个在文件的最后，前4个是缺省定义的。给出指定 wiki 包的引用名来。这一步是为了以后方便地导入所必须的。因为我们的目录都是包的形式，因此这里就是与目录相对应的。

b.执行(在newtest目录下)
manage.py syncdb

Save data to database:
进入 newtest 目录，然后:

manage.py shell
进入 python

>>> from newtest.wiki.models import Wiki
>>> page = Wiki(pagename='FrontPage', content='Welcome to Easy Wiki')
>>> page.save()
>>> Wiki.objects.all()
[<Wiki object>]
>>> p = Wiki.objects.all()[0]

修改表结构
manage.py sqlreset address|sqlite3 data.db
另外 django-amdin.py 还提供了更为简单的命令 manage.py reset address ，效果同上面是一样的。
修改 settings.py
把 LANGUAGE_CODE 由 'en' 改为 'zh-cn' ， TIME_ZONE 建议改为 'CCT'

Django 已经为我们想到了，这就是 Generic views 所做的。它把最常见的显示列表，显示详细信息，增加，修改，删除对象这些处理都已经做好了一个通用的方法，一旦有类似的处理，可以直接使用，不用再重新开发了。但在配置上有特殊的要求。具体的可以看 Generic views 文档
对于目前我这个简单的应用来说，我只需要一个简单的列表显示功能即可，好在联系人的信息并不多可以在一行显示下。因此我要使用 django.views.generic.list_detail 模块来处理。
增加 address/urls.py
对，我们为 address 应用增加了自已的 urls.py。

from django.conf.urls.defaults import *
from newtest.address.models import Address

info_dict = {
#    'model': Address,
    'queryset': Address.objects.all(),
}
urlpatterns = patterns('',
    (r'^/?$', 'django.views.generic.list_detail.object_list', info_dict),
)
info_dict 存放着 object_list 需要的参数，它是一个字典。不同的 generic view 方法需要不同的 info_dict 字典(这个变量你可以随便起名)。对于我们要调用的 object_list 它只要一个 queryset 值即可。但这个值需要是一个 queryset 对象。因此在第二句我们从 newtest.address.models 中导入了 Address 。并且使用 Address.objects.all() 来得到一个全部记录的 queryset 。

前面已经谈到：使用 generic view 只是减少了 view 的代码量，但对于模板仍然是必不可少的。因此要创建符合 generic view 要求的模板。主要是模板存放的位置和模板文件的名字。

使用 object_list() 需要的模板文件名为： app_label/model_name_list.html ，这是缺省要查找的模板名。

15   创建 templates/address 目录
16   创建 templates/address/address_list.html
<h1>通讯录</h1>
<hr>
<table border="1">
<tr>
  <th>姓名</th>
  <th>性别</th>
  <th>电话</th>
  <th>手机</th>
  <th>房间</th>
</tr>
{% for person in object_list %}
<tr>
  <td>{{ person.name }}</td>
  <td>{{ person.gender }}</td>
  <td>{{ person.telphone }}</td>
  <td>{{ person.mobile }}</td>
  <td>{{ person.room }}</td>
</tr>
{% endfor %}
</table>
17   修改 urls.py
将我们的应用的 urls.py include 进去。

from django.conf.urls.defaults import *

urlpatterns = patterns('',
    # Example:
    # (r'^testit/', include('newtest.apps.foo.urls.foo')),
    (r'^$', 'newtest.helloworld.index'),
    (r'^add/$', 'newtest.add.index'),
    (r'^list/$', 'newtest.list.index'),
    (r'^csv/(?P<filename>\w+)/$', 'newtest.csv_test.output'),
    (r'^login/$', 'newtest.login.login'),
    (r'^logout/$', 'newtest.login.logout'),
    (r'^wiki/$', 'newtest.wiki.views.index'),
    (r'^wiki/(?P<pagename>\w+)/$', 'newtest.wiki.views.index'),
    (r'^wiki/(?P<pagename>\w+)/edit/$', 'newtest.wiki.views.edit'),
    (r'^wiki/(?P<pagename>\w+)/save/$', 'newtest.wiki.views.save'),
    (r'^address/', include('newtest.address.urls')),

    # Uncomment this for admin:
     (r'^admin/', include('django.contrib.admin.urls')),
)
可以看到 r'^address/' 没有使用 $ ，因为它只匹配前部分，后面的留给 address 中的 urls.py 来处理。

