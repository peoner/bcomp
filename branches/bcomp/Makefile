PKG=ru.ifmo.cs.bcomp.ui
JAR=dist/bcomp.jar
BIN=build/classes

ALL:
	sed s/%REV%/`svnversion`/g manifest.mf.template > $(BIN)/manifest.mf
	cd src && javac -d ../$(BIN) `echo $(PKG) | tr . /`/MPDecoder.java
	cd src && javac -d ../$(BIN) `echo $(PKG) | tr . /`/GUI.java
	cd build/classes && jar cfm ../../$(JAR) manifest.mf ru

run:
	java -server -jar $(JAR)

run-o:
	java -server -jar $(JAR) -o

decode:
	java -classpath $(JAR) $(PKG).MPDecoder

decode-o:
	java -classpath $(JAR) $(PKG).MPDecoder -o

gui:
	java -classpath $(JAR) $(PKG).GUI

upload:
	scp -P 2222 $(JAR) kot.spb.ru:~www/data/bcomp
	scp $(JAR) 192.168.10.10:java/bcomp

clean:
	rm -rf $(BIN)/* $(JAR)
