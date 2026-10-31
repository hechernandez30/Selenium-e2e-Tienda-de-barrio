@echo off
setlocal
cd /d "%~dp0..\..\.."
if not exist "pom.xml" (
  echo No se encontro e2e\pom.xml. Ejecuta este archivo desde el repositorio.
  pause
  exit /b 1
)
where mvn >nul 2>&1
if errorlevel 1 (
  echo Maven no esta en el PATH. Instala Maven y vuelve a intentar.
  pause
  exit /b 1
)
echo.
echo Ejecutando pruebas: Caja  (grupo cash)
echo Datatable: %~dp0
echo Informe HTML: e2e\reports\
echo.
call mvn test -Dgroups=cash
set EXITCODE=%ERRORLEVEL%
echo.
echo Codigo de salida: %EXITCODE%
pause
exit /b %EXITCODE%
