main:
	gcc -c -o hello.o hello.c
	gcc -shared -Wl,--add-stdcall-alias -o hello.dll hello.o
clean:
	del hello.o hello.dll
all:
    