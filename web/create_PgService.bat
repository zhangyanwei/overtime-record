rem pg_ctl register [-N servicename] [-U username] [-P password] [-D datadir] [-S a[uto] | d[emand] ] [-w] [-t seconds] [-s] [-o options]
rem pg_ctl unregister [-N servicename]

call env.bat

if "%1" == "--install" goto install
if "%1" == "--remove" (goto remove) else (goto errorexit)

:install
"%PG_HOME%\bin\pg_ctl.exe" register -N %PGSQL_SERVICE_NAME% -D "%PG_HOME%\data" -S auto
if not %errorlevel% == 0 goto errorexit
net start %PGSQL_SERVICE_NAME%
goto exit

:remove
"%PG_HOME%\bin\pg_ctl.exe" unregister -N %PGSQL_SERVICE_NAME%
if not %errorlevel% == 0 goto errorexit
goto exit

:errorexit
exit 1

:exit
exit 0