#!/bin/bash

sed -i~ "/<servers>/ a\
<server>\
  <id>central</id>\
  <username>${MVN_CENTRAL_TOKEN_USERNAME}</username>\
  <password>${MVN_CENTRAL_TOKEN_PASSWORD}</password>\
</server>" /usr/share/maven/conf/settings.xml
