from threading import Lock, Thread

class Operation:
    def __init__(self):
        self.count = 0
    
    def increment(self, amount):
        with lock:
	        self.count = self.count + amount
	        print self.count

class MyThread(Thread):
    def run(self):
        for i in range(1000):
            operation.increment(1)

def main():
    global lock
    lock = Lock()
    global operation
    operation = Operation()
    MyThread().start()
    MyThread().start()
    MyThread().start()
    MyThread().start()
    MyThread().start()

if __name__ == '__main__':
    main()