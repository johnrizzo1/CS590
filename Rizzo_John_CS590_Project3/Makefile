# Makefile for JumpingJim Java and C++ examples

# Print help if no arguments are given
.DEFAULT_GOAL := help

help:
	@echo "Usage: make [target]"
	@echo ""
	@echo "Targets:"
	@echo "  all        Build both Java and C++ versions"
	@echo "  java       Build Java classes"
	@echo "  cpp        Build C++ executable"
	@echo "  run-java   Run Java version"
	@echo "  run-cpp    Run C++ version"
	@echo "  run        Run Java version (default)"
	@echo "  clean      Remove build artifacts"

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

.PHONY: all java cpp run-cpp clean help