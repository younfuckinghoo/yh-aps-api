@echo off
rem /**
rem  * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: Winhai@163.com
rem  */
echo.
echo [��Ϣ] ���Web���̣�������Web���̡�
echo.

%~d0
cd %~dp0

rem ���Web���̣���ʼ��
cd ..
call mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U
rem ���Web���̣�������

rem ��������
cd target
call java -jar web.jar

pause