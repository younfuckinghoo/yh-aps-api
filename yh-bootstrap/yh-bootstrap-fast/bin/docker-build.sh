#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: Winhai@163.com
#  * 
#  */
echo ""
echo "[信息] 打包Web工程，编译Docker镜像。"
echo ""

cd ..
mvn clean package docker:remove docker:build -Dmaven.test.skip=true -U

echo ""
echo ""
echo "参考下面的脚本，拷贝到 Docker 服务器上运行："
echo ""
echo "docker run --name jeesite-web -p 8980:8980 -d --restart unless-stopped -v ~/:/data winhai/jeesite-web && docker logs -f jeesite-web"
echo ""
echo "启动完成后，访问项目：http://127.0.0.1:8980/js/a/login   用户名：system   密码：admin"
echo ""

cd bin