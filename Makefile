PKG=ru/ifmo/it
CLI=$(PKG)/bcomp/ui/cli

ALL:
	cd src && javac -d ../bin $(CLI).java
