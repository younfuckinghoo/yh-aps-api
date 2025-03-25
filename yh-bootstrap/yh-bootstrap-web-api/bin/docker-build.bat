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

cd bin
pause