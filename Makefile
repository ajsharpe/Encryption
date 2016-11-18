all:
	javac *.java
	javah Encryption
	gcc -std=c99 -I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/linux -shared -fpic encryption.c -o libencryption.so -Wl,-rpath,. -L.
	export LD_LIBRARY_PATH=$(LD_LIBRARY_PATH):$(shell pwd)
	LD_LIBRARY_PATH=$(LD_LIBRARY_PATH):$(shell pwd)
	@echo $(LD_LIBRARY_PATH)
	@echo "***Setting LD_LIBRARY_PATH in the makefile is not working. It must be set manually in the terminal before running the application."
	@echo
clean:
	rm -f *.class *.so Encryption.h


