# Django settings for newtest project.

DEBUG = True
TEMPLATE_DEBUG = DEBUG

ADMINS = (
    # ('Your Name', 'your_email@domain.com'),
)

MANAGERS = ADMINS

DATABASE_ENGINE = '' # 'postgresql', 'mysql', 'sqlite3' or 'ado_mssql'.
DATABASE_NAME = 'd:/project/svn/limodou/django-stepbystep/newtest/data.db'             # Or path to database file if using sqlite3.
DATABASE_USER = ''             # Not used with sqlite3.
DATABASE_PASSWORD = ''         # Not used with sqlite3.
DATABASE_HOST = ''             # Set to empty string for localhost. Not used with sqlite3.
DATABASE_PORT = ''             # Set to empty string for default. Not used with sqlite3.

# Local time zone for this installation. All choices can be found here:
# http://www.postgresql.org/docs/current/static/datetime-keywords.html#DATETIME-TIMEZONE-SET-TABLE
TIME_ZONE = 'CCT'

# Language code for this installation. All choices can be found here:
# http://www.w3.org/TR/REC-html40/struct/dirlang.html#langcodes
# http://blogs.law.harvard.edu/tech/stories/storyReader$15
LANGUAGE_CODE = 'zh-cn'

SITE_ID = 1

# Absolute path to the directory that holds media.
# Example: "/home/media/media.lawrence.com/"
MEDIA_ROOT = ''

# URL that handles the media served from MEDIA_ROOT.
# Example: "http://media.lawrence.com"
MEDIA_URL = ''

# URL prefix for admin media -- CSS, JavaScript and images. Make sure to use a
# trailing slash.
# Examples: "http://foo.com/media/", "/media/".
ADMIN_MEDIA_PREFIX = '/media/'

# Make this unique, and don't share it with anybody.
SECRET_KEY = 'am))0ok3&-2v%!2*=!o"\'w&=v_pkqk4d#=0y35nk=6)ixs+r^_$#'

# List of callables that know how to import templates from various sources.
TEMPLATE_LOADERS = (
    'django.template.loaders.filesystem.load_template_source',
    'django.template.loaders.app_directories.load_template_source',
#     'django.template.loaders.eggs.load_template_source',
)

MIDDLEWARE_CLASSES = (
    'django.middleware.sessions.SessionMiddleware',
    "django.middleware.locale.LocaleMiddleware",
    "django.middleware.common.CommonMiddleware",
    "django.middleware.doc.XViewMiddleware",
)

ROOT_URLCONF = 'newtest.urls'

TEMPLATE_DIRS = (
    # Put strings here, like "/home/html/django_templates".
#    'd:/project/svn/limodou/django-stepbystep/newtest/templates',
    'd:/project/svn/limodou/django-stepbystep/newtest/templates',
#    'C:/Python24/Lib/site-packages/Django-0.90-py2.4.egg/django/contrib/admin/templates',
#    'C:/Python24/Lib/site-packages/Django-0.90-py2.4.egg/django/contrib/admin/templates/admin',
)

INSTALLED_APPS = (
    'newtest.apps.wiki',
    'newtest.apps.address',
    'newtest.apps.ajax',
    'django.contrib.admin', 
)

STATIC_PATH = './media'
UPLOAD_USER = 'limodou'