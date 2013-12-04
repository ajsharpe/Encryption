all:
	javac *.java
	javah Encryption
	gcc -std=c99 -I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/linux -shared -fpic encryption.c -o libencryption.so -Wl,-rpath,. -L.
	export LD_LIBRARY_PATH=$(LD_LIBRARY_PATH)/afs/ualberta.ca/home/a/j/ajsharpe/Documents/5thYear/ECE422/P2/
	@echo ""
	@echo "***Setting LD_LIBRARY_PATH in the makefile is not working. It must be set manually in the terminal before running the application."

clean:
	rm -f *.class *.so


