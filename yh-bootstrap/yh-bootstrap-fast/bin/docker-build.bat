@echo off
rem /**
rem  * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: Winhai@163.com
rem  */
echo.
echo [��Ϣ] ���Web���̣�����Docker����
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package docker:remove docker:build -Dmaven.test.skip=true -U

echo.
echo.
echo �ο�����Ľű��������� Docker �����������У�
echo.
echo "docker run --name jeesite-web -p 8980:8980 -d --restart unless-stopped -v ~:/data winhai/jeesite-web && docker logs -f jeesite-web"
echo.
echo ������ɺ󣬷�����Ŀ��http://127.0.0.1:8980/js/a/login   �û�����system   ���룺admin
echo.

cd bin
pause