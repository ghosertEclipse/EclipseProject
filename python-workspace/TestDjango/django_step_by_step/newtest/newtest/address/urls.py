from django.conf.urls.defaults import *
from newtest.address.models import Address

info_dict = {
#    'model': Address,
    'queryset': Address.objects.all(),
}
urlpatterns = patterns('',
    (r'^/?$', 'django.views.generic.list_detail.object_list', 
        dict(paginate_by=10, **info_dict)),
    (r'^upload/$', 'address.views.upload'),
    (r'^output/$', 'address.views.output'),
    (r'^search/$', 'address.views.search'),
)
