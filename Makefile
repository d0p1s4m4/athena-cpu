JAVA		= java
CXX			?= g++
RM			= rm -f
MKDIR		= mkdir -p
WGET		= wget

JVMFLAGS	= -Dfile.encoding=UTF-8 -Xms1024m -Xmx1024m -Xss4M 
SBTFLAGS	?=

CACHEDIR	= .cache
TOOLSDIR	= tools

SBT_VERSION	= 1.8.0
SBT_ARCHIVE = sbt-$(SBT_VERSION).tgz
SBT_PKG		= https://github.com/sbt/sbt/releases/download/v$(SBT_VERSION)/$(SBT_ARCHIVE)

# Tools
SBT_JAR		= $(TOOLSDIR)/sbt-launch.jar
TINYAS		= $(TOOLSDIR)/tinyas
TOHEX		= $(TOOLSDIR)/tohex

all: $(SBT_JAR)

.cache/$(SBT_ARCHIVE):
	@ $(MKDIR) $(@D)
	wget "$(SBT_PKG)" -O $@

$(SBT_JAR): .cache/$(SBT_ARCHIVE)
	tar -xf $< -C $(TOOLSDIR) --strip-components=2 sbt/bin/sbt-launch.jar

test:
	$(JAVA) $(JVMFLAGS) -jar $(SBT_JAR) $(SBTFLAGS) test

.PHONY: all test clean re
