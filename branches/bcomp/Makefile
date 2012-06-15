PKG=ru/ifmo/it
CLI=$(PKG)/bcomp/ui/CLI
JAR=dist/bcomp.jar

ALL:
	cd src && javac -d ../build/classes $(CLI).java
	cd build/classes && jar cf ../../$(JAR) ru

run:
	java -classpath $(JAR) ru.ifmo.it.bcomp.ui.CLI

upload:
	scp -P 2222 $(JAR) kot.spb.ru:~www/data/bcomp
	scp $(JAR) 192.168.10.10:java/bcomp
