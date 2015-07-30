@echo off

call env.bat

if "%1" == "--install" goto install
if "%1" == "--remove" (goto remove) else (goto errorexit)

:install

set CATALINA_BASE=%PRODUCT_HOME%\tomcat

set JVM_DLL=%JAVA_HOME%\jre\bin\server\jvm.dll
if not exist "%JVM_DLL%" (
    set JVM_DLL=%JAVA_HOME%\bin\server\jvm.dll
)

set JVM_OPTS='-Dcatalina.home=%CATALINA_HOME%'
set JVM_OPTS=%JVM_OPTS%;'-Dcatalina.base=%CATALINA_BASE%'
set JVM_OPTS=%JVM_OPTS%;'-Djava.endorsed.dirs=%CATALINA_BASE%\endorsed'
set JVM_OPTS=%JVM_OPTS%;'-Djava.io.tmpdir=%CATALINA_BASE%\temp'
set JVM_OPTS=%JVM_OPTS%;'-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager'
set JVM_OPTS=%JVM_OPTS%;'-Djava.util.logging.config.file=%CATALINA_BASE%\conf\logging.properties'
set JVM_OPTS=%JVM_OPTS%;'-Dtools.ctd.home=%PRODUCT_HOME%'

set CLASS_PATH=%CATALINA_HOME%\bin\bootstrap.jar;%CATALINA_HOME%\bin\tomcat-juli.jar

set TOMCAT7_OPTS=--DisplayName="%SERVICE_NAME%"
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --Description="Apache Tomcat Server for %SERVICE_NAME% - http://tomcat.apache.org/ "
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --Install="%CATALINA_HOME%\bin\Tomcat7.exe"
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --Startup=auto
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --JavaHome="%JAVA_HOME%"
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --Jvm="%JVM_DLL%"
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --JvmOptions="%JVM_OPTS%"
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --Classpath="%CLASS_PATH%"
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --StartMode=jvm
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --StopMode=jvm
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --StartClass=org.apache.catalina.startup.Bootstrap
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --StartParams=start
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --StartPath="%CATALINA_BASE%"
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --StopClass=org.apache.catalina.startup.Bootstrap
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --StopParams=stop
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --StopPath="%CATALINA_BASE%"
set TOMCAT7_OPTS=%TOMCAT7_OPTS% --LogPath="%PRODUCT_HOME%\logs"

mkdir "%PRODUCT_HOME%\bin"
copy "%CATALINA_HOME%\bin\Tomcat7.exe" "%PRODUCT_HOME%\bin\%SERVICE_NAME%.exe"
copy "%CATALINA_HOME%\bin\Tomcat7w.exe" "%PRODUCT_HOME%\bin\%SERVICE_NAME%w.exe"
"%PRODUCT_HOME%\bin\%SERVICE_NAME%.exe" //IS//%SERVICE_NAME% %TOMCAT7_OPTS%
if not %errorlevel% == 0 goto errorexit
net start %SERVICE_NAME%
goto exit

:remove
"%PRODUCT_HOME%\bin\%SERVICE_NAME%.exe" //DS//%SERVICE_NAME%
if not %errorlevel% == 0 goto errorexit
goto exit

:errorexit
exit 1

:exit
exit 0