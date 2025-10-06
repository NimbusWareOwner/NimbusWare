@echo off
title NimbusWare v1.0.0
color 0A

echo.
echo  ███╗   ██╗██╗███╗   ███╗██████╗ ██╗   ██╗███████╗    ██╗    ██╗ █████╗ ██████╗ ███████╗
echo  ████╗  ██║██║████╗ ████║██╔══██╗██║   ██║██╔════╝    ██║    ██║██╔══██╗██╔══██╗██╔════╝
echo  ██╔██╗ ██║██║██╔████╔██║██████╔╝██║   ██║███████╗    ██║    ██║███████║██████╔╝█████╗  
echo  ██║╚██╗██║██║██║╚██╔╝██║██╔══██╗██║   ██║╚════██║    ██║    ██║██╔══██║██╔══██╗██╔══╝  
echo  ██║ ╚████║██║██║ ╚═╝ ██║██████╔╝╚██████╔╝███████║    ██║    ██║██║  ██║██║  ██║███████╗
echo  ╚═╝  ╚═══╝╚═╝╚═╝     ╚═╝╚═════╝  ╚═════╝ ╚══════╝    ╚═╝    ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
echo.
echo  ═══════════════════════════════════════════════════════════════════════════════════════
echo  🎮 NimbusWare v1.0.0 - Advanced Minecraft Cheat Client
echo  ═══════════════════════════════════════════════════════════════════════════════════════
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed or not in PATH!
    echo Please install Java 8 or higher from https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Check if JAR file exists
if not exist "build\libs\workspace-1.0.0.jar" (
    echo ❌ JAR file not found! Please compile the project first.
    echo Run: gradlew clean build
    echo.
    pause
    exit /b 1
)

echo ✅ Java found
echo ✅ JAR file found
echo.
echo 🚀 Starting NimbusWare...
echo.

REM Set JVM options for better performance
set JAVA_OPTS=-Xmx2G -Xms1G -XX:+UseG1GC -XX:+UseStringDeduplication

REM Launch NimbusWare
java %JAVA_OPTS% -jar "build\libs\workspace-1.0.0.jar"

REM If the program exits, show message
echo.
echo NimbusWare has stopped.
pause