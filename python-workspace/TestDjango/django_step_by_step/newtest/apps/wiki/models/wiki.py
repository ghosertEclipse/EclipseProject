from django.core import meta

# Create your models here.
class Wiki(meta.Model):
    pagename = meta.CharField(maxlength=20, unique=True)
    content = meta.TextField()

