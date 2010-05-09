from django.core.extensions import render_to_response
from django.utils.httpwrappers import HttpResponseRedirect

def login(request):
    username = request.POST.get('username', None)
    if username:
        request.session['username'] = username
    username = request.session.get('username', None)
    if username:
        return render_to_response('login', {'username':username})
    else:
        return render_to_response('login')
    
def logout(request):
    try:
        del request.session['username']
    except KeyError:
        pass
    return HttpResponseRedirect("/login/")    