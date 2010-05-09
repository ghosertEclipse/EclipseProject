from django.contrib import admin
from newtest.address.models import Address

class AddressAdmin(admin.ModelAdmin):
    pass 

admin.site.register(Address, AddressAdmin)

