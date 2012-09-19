PKG=ru.ifmo.cs.bcomp.ui
DIST=dist
JAR=$(DIST)/bcomp.jar
BIN=build/classes

ALL:
	for i in $(DIST) $(BIN); do [ -d $$i ] || mkdir -p $$i; done
	sed s/%REV%/`svnversion`/g manifest.mf.template > $(BIN)/manifest.mf
	cd src && javac -d ../$(BIN) `echo $(PKG) | tr . /`/BCompApp.java
	cd build/classes && jar cfm ../../$(JAR) manifest.mf ru

gui:
	cd dist && ./bcomp

gui-o:
	cd dist && ./bcomp -o

cli:
	cd dist && ./bcomp -c

cli-o:
	cd dist && ./bcomp -c -o

decode:
	cd dist && ./bcomp -d

decode-o:
	cd dist && ./bcomp -d -o

upload:
	scp -P 2222 $(DIST)/* kot.spb.ru:~www/data/bcomp
	scp $(JAR) 192.168.10.10:java/bcomp

clean:
	rm -rf $(BIN)/* $(JAR)
