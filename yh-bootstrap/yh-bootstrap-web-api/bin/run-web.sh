#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: Winhai@163.com
#  * 
#  */
echo ""
echo "[信息] 打包Web工程，并运行Web工程。"
echo ""

# 打包Web工程（开始）
cd ..
mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U
cd target
# 打包Web工程（结束）


# 根据情况修改 web.jar 为您的 jar 包名称
mkdir app
cp web.war ./app
cd app
jar -xvf web.war
rm web.war
cd WEB-INF
sh ./startup.sh