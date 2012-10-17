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
	cd $(DIST) && ./bcomp

gui-o:
	cd $(DIST) && ./bcomp -o

cli:
	cd $(DIST) && ./bcomp -c

cli-o:
	cd $(DIST) && ./bcomp -c -o

decode:
	cd $(DIST) && ./bcomp -d

decode-o:
	cd $(DIST) && ./bcomp -d -o

upload:
	scp $(DIST)/* helios.cs.ifmo.ru:java/bcomp

javadoc:
	cd src && javadoc -d ../doc -subpackages ru.ifmo.cs.elements ru.ifmo.cs.io ru.ifmo.cs.bcomp ru.ifmo.cs.bcomp.ui ru.ifmo.cs.bcomp.ui.components ru.ifmo.cs

clean:
	rm -rf $(BIN)/* $(JAR)
