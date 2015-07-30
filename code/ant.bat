@echo off

call env.bat
start "" "%ANT_HOME%\bin\ant.bat" -f build.xml