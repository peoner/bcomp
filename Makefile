PKG=ru/ifmo/it/bcomp/ui
JAR=dist/bcomp.jar

ALL:
	cd src && javac -d ../build/classes $(PKG)/MPDecoder.java
	cd build/classes && jar cf ../../$(JAR) ru

run:
	java -classpath $(JAR) ru.ifmo.it.bcomp.ui.CLI

decode:
	java -classpath $(JAR) ru.ifmo.it.bcomp.ui.MPDecoder

upload:
	scp -P 2222 $(JAR) kot.spb.ru:~www/data/bcomp
	scp $(JAR) 192.168.10.10:java/bcomp

clean:
	rm -rf build/classes/ru dist/bcomp.jar
