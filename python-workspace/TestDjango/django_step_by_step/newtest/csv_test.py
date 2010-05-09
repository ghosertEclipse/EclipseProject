#coding=utf-8
from django.utils.httpwrappers import HttpResponse
from django.core.template import loader, Context

address = [
    ('张三', '地址一'), 
    ('李四', '地址二')
    ]

def output(request, filename):
    response = HttpResponse(mimetype='text/csv')
    response['Content-Disposition'] = 'attachment; filename=%s.csv' % filename

    t = loader.get_template('csv')
    c = Context({
        'data': address,
    })
    response.write(t.render(c))
    return response