.PHONY clean

train: train.cpp
	g++ -O3 -std=c++17 -o train train.cpp -lFJML

run.o: run.cpp
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -O3 -std=c++17 -o run.o run.cpp -lFJML

ocr.so: run.o
	g++ -shared -fPIC -o libocr.so run.o -lFJML

clean:
	rm -f train run.o ocr.so
