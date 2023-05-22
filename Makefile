.PHONY: clean all

all: OCR_server/train OCR_server/run.o libocr.so

OCR_server/train: OCR_server/train.cpp
	g++ -O3 -std=c++17 -o OCR_server/train OCR_server/train.cpp -lFJML

OCR_server/run.o: OCR_server/run.cpp
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -O3 -std=c++17 -o OCR_server/run.o OCR_server/run.cpp -lFJML

libocr.so: OCR_server/run.o
	g++ -shared -fPIC -o libocr.so OCR_server/run.o -lFJML

athleteocr: libocr.so OCR_server/AthleteOCR.java
	javac -cp OCR_server:opencv-470.jar:json.jar -d OCR_server/ OCR_server/ImageProcessing.java
	javac -cp OCR_server:opencv-470.jar:json.jar -d OCR_server/ OCR_server/AthleteOCR.java
	java -cp OCR_server:opencv-470.jar:json.jar -Djava.library.path=. OCR_server/AthleteOCR

clean:
	rm -f OCR_server/train OCR_server/run.o libocr.so
