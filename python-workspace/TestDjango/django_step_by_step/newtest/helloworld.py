from django.utils.httpwrappers import HttpResponse

def index(request):
    return HttpResponse("Hello, django!")