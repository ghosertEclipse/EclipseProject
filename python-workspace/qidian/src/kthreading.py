import threading
import sys

class KThread(threading.Thread):
    """A subclass of threading.Thread, with a kill()
    method.
    
    Come from:
    Kill a thread in Python: 
    http://mail.python.org/pipermail/python-list/2004-May/260937.html
    """
    def __init__(self, *args, **kwargs):
        threading.Thread.__init__(self, *args, **kwargs)
        self.killed = False

    def start(self):
        """Start the thread."""
        self.__run_backup = self.run
        self.run = self.__run      # Force the Thread to install our trace.
        threading.Thread.start(self)

    def __run(self):
        """Hacked run function, which installs the
        trace."""
        sys.settrace(self.globaltrace)
        self.__run_backup()
        self.run = self.__run_backup

    def globaltrace(self, frame, why, arg):
        if why == 'call':
          return self.localtrace
        else:
          return None

    def localtrace(self, frame, why, arg):
        if self.killed:
          if why == 'line':
            raise SystemExit()
        return self.localtrace

    def kill(self):
        self.killed = True

if __name__ == '__main__':
    import time
    def function():
        condition = threading.Condition()
        condition.acquire()
        condition.wait()
        condition.release()
        print 'jiawei'

    t2 = KThread(target=function)
    t2.start()
    t2.join(5)
    isAlive = t2.isAlive()
    # Uncomment the line below to see the different result.
    t2.kill()
    if isAlive:
        print 'Time is out.'
    else:
        print 'thread finished.'
