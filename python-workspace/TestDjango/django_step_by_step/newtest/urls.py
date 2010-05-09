from django.conf.urls.defaults import *
#from django.conf.settings import STATIC_PATH

urlpatterns = patterns('',
    # Example:
#    (r'^helloworld/', 'newtest.apps.helloworld.views.index'),
    (r'^$', 'newtest.helloworld.index'),
    (r'^add/$', 'newtest.add.index'),
    (r'^list/$', 'newtest.list.index'),
    (r'^csv/(?P<filename>\w+)/$', 'newtest.csv_test.output'),
    (r'^login/$', 'newtest.login.login'),
    (r'^logout/$', 'newtest.login.logout'),
    (r'^wiki/$', 'newtest.apps.wiki.views.index'),
    (r'^wiki/(?P<pagename>\w+)/$', 'newtest.apps.wiki.views.index'),
    (r'^wiki/(?P<pagename>\w+)/edit/$', 'newtest.apps.wiki.views.edit'),
    (r'^wiki/(?P<pagename>\w+)/save/$', 'newtest.apps.wiki.views.save'),
    (r'^address/', include('newtest.apps.address.urls')),
    (r'^site_media/(?P<path>.*)$', 'django.views.static.serve', {'document_root': STATIC_PATH}),
    (r'^ajax/$', 'django.views.generic.simple.direct_to_template', {'template': 'ajax/ajax'}),
    (r'^ajax/input/$', 'newtest.apps.ajax.views.input'),
    (r'^ajax/json/$', 'newtest.apps.ajax.views.json'),
    (r'^captcha/$', 'django.views.generic.simple.direct_to_template', {'template': 'captcha/index'}),
    (r'^captcha/input/$', 'newtest.apps.captcha.views.input'),
    (r'^captcha/image/$', 'newtest.apps.captcha.views.image'),
    # Uncomment this for admin:
    (r'^admin/', include('django.contrib.admin.urls.admin')),
)
