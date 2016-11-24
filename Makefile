all:
	javac -d ./ src/*.java
	javah -classpath ./src -o src/Encryption.h Encryption 
	gcc -std=c99 -I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/linux -shared -fpic src/encryption.c -o libencryption.so -Wl,-rpath,. -L.
	$(shell export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:`pwd`)
	@echo $(LD_LIBRARY_PATH)
	@echo "[WARN]***Setting LD_LIBRARY_PATH in the Makefile is not working. It must be set manually in the terminal before running the application."
	@echo "You can do this by running the command: export LD_LIBRARY_PATH=`pwd`"
	@echo
clean:
	rm -f *.class *.so src/Encryption.h


