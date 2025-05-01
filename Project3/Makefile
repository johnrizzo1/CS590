# Makefile for JumpingJim Java and C++ examples

CXX = g++
CXXFLAGS = -std=c++17 -O2 -Wall
BOOST_LIBS = -lboost_system -lboost_filesystem

JAVA_FILES = JumpingJim.java BoardDFS.java
JAVA_CLASSES = JumpingJim.class BoardDFS.class

all: java cpp

java: $(JAVA_CLASSES)

%.class: %.java
	javac $<

cpp: JumpingJim

JumpingJim: JumpingJim.cpp
	$(CXX) $(CXXFLAGS) JumpingJim.cpp -o JumpingJim $(BOOST_LIBS)

run-cpp: JumpingJim
	./JumpingJim

run-java: java
	java JumpingJim

run: run-java

clean:
	rm -f $(JAVA_CLASSES) JumpingJim

.PHONY: all java cpp run-cpp clean