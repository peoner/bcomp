PKG=ru/ifmo/it/bcomp/ui
JAR=dist/bcomp.jar
BIN=build/classes

ALL:
	sed s/%REV%/`svnversion`/g manifest.mf.template > $(BIN)/manifest.mf
	cd src && javac -d ../$(BIN) $(PKG)/MPDecoder.java
	cd build/classes && jar cfm ../../$(JAR) manifest.mf ru

run:
	java -jar $(JAR)

run-o:
	java -jar $(JAR) -o

decode:
	java -classpath $(JAR) ru.ifmo.it.bcomp.ui.MPDecoder

decode-o:
	java -classpath $(JAR) ru.ifmo.it.bcomp.ui.MPDecoder -o

upload:
	scp -P 2222 $(JAR) kot.spb.ru:~www/data/bcomp
	scp $(JAR) 192.168.10.10:java/bcomp

clean:
	rm -rf $(BIN)/* $(JAR)
