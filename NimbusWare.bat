@echo off
title NimbusWare v1.0.0
color 0A
cd /d "%~dp0"

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ❌ Java is not installed or not in PATH!
    echo Please install Java 8 or higher from https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Check if JAR file exists
if not exist "build\libs\workspace-1.0.0.jar" (
    echo.
    echo ❌ JAR file not found! Please compile the project first.
    echo Run: gradlew clean build
    echo.
    pause
    exit /b 1
)

REM Launch NimbusWare
java -Xmx2G -Xms1G -jar "build\libs\workspace-1.0.0.jar"

pause