from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response

def login(request):
    username = request.POST.get('username', None)
    if username:
        request.session['username'] = username
    username = request.session.get('username', None)
    if username:
        return render_to_response('login.html', {'username':username})
    else:
        return render_to_response('login.html')
    
def logout(request):
    try:
        del request.session['username']
    except KeyError:
        pass
    return HttpResponseRedirect("/login/")    