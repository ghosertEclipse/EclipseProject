from django.db import models

# Create your models here.
class Wiki(models.Model):
    pagename = models.CharField(maxlength=20, unique=True)
    content = models.TextField()
