FROM ubuntu:latest

WORKDIR /opt/CodeTester
COPY . /opt/CodeTester


RUN apt-get update
RUN apt-get upgrade -y


# Disable IntelliJ data sharing
RUN set -x \
  && dir=/root/.local/share/JetBrains/consentOptions \
  && mkdir -p "$dir" \
  && echo -n "rsch.send.usage.stat:1.1:0:$(date +%s)000" > "$dir/accepted"


# Accept End User Agreement/privacy policy
RUN set -x \
  && dir="/root/.java/.userPrefs/jetbrains/_!(!!cg\"p!(}!}@\"j!(k!|w\"w!'8!b!\"p!':!e@==" \
  && mkdir -p "$dir" \
  && echo '<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n\
<!DOCTYPE map SYSTEM "http://java.sun.com/dtd/preferences.dtd">\n\
<map MAP_XML_VERSION="1.0">\n\
  <entry key="accepted_version" value="2.1"/>\n\
  <entry key="eua_accepted_version" value="1.1"/>\n\
  <entry key="privacyeap_accepted_version" value="2.1"/>\n\
</map>' > "$dir/prefs.xml" \
  && cat "$dir/prefs.xml"


# Install xcfb
RUN apt-get install -y xvfb


# Install gradle
RUN apt-get install -y wget zip default-jdk
RUN mkdir /opt/gradle
RUN wget "https://services.gradle.org/distributions/gradle-6.7.1-bin.zip"
RUN unzip -d /opt/gradle gradle-6.7.1-bin.zip
RUN rm gradle-6.7.1-bin.zip
ENV PATH=$PATH:/opt/gradle/gradle-6.7.1/bin
