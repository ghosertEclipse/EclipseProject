from django.conf.urls.defaults import *
from newtest.address.models import Address

info_dict = {
#    'model': Address,
    'queryset': Address.objects.all(),
}
urlpatterns = patterns('',
    # No pagation
    # (r'^/?$', 'django.views.generic.list_detail.object_list', info_dict),
    # Pagation support
    (r'^/?$', 'django.views.generic.list_detail.object_list', dict(paginate_by=3, **info_dict)),
    (r'^upload/$', 'address.views.upload'),
)

