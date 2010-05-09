from django.conf.urls.defaults import *

info_dict = {
    'app_label': 'address',
    'module_name': 'addresses',
}
urlpatterns = patterns('',
    (r'^/?$', 'django.views.generic.list_detail.object_list', dict(paginate_by=10, **info_dict)),
    (r'^upload/$', 'newtest.apps.address.views.upload'),
    (r'^output/$', 'newtest.apps.address.views.output'),
    (r'^search/$', 'newtest.apps.address.views.search'),
)
