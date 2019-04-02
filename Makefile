# Makefile for codebase
SOURCE_DIR	:= src
OUTPUT_DIR	:= bin

# Tools

FIND	:= find
RM	:= rm -rf
MKDIR	:= mkdir -p
JAVAC	:= javac

JFLAGS	:= -sourcepath $(SOURCE_DIR) -d $(OUTPUT_DIR) -cp "lib/antlr-4.7-complete.jar"

# the make rules

all: rules

rules:
	$(FIND) $(SOURCE_DIR) -name '*.java' > $@
	$(MKDIR) $(OUTPUT_DIR)
	$(JAVAC) $(JFLAGS) @$@
	$(RM) rules

clean:
	$(RM) rules $(OUTPUT_DIR)

.PHONY: all rules clean


