from django import template
import HTMLCalendar

register = template.Library()

class CalendarNode(template.Node):
    def __init__(self, year, mon):
        self.year = int(year)
        self.mon = int(mon)

    def render(self, context):
        return HTMLCalendar.MonthCal().render(self.year, self.mon)

def do_calendar(parser, token):
    try:
        tag_name, arg = token.contents.split(None, 1)
    except ValueError:
        #if no args then using current date
        import datetime
        today = datetime.date.today()
        year, mon = today.year, today.mon
    else:
        try:
            year, mon = arg.split(None, 1)
        except ValueError:
            raise template.TemplateSyntaxError, "%r tag requires year and mon arguments" % tag_name
            
    return CalendarNode(year, mon)

register.tag('calendar', do_calendar)
