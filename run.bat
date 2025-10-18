@echo off
title Student Management System
echo ===============================================
echo    Student Management System - Setup
echo ===============================================
echo.

echo Setting up Java environment...
set JAVA_HOME=C:\Java\jdk-17.0.2
set PATH=%JAVA_HOME%\bin;%PATH%
set JAVAFX_HOME=C:\Java\javafx-sdk-17.0.2

echo.
echo Compiling Java files...
javac --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls src/StudentManagementSystem.java

if %errorlevel% neq 0 (
    echo.
    echo ❌ Compilation failed! Please check:
    echo 1. Java JDK 17+ is installed
    echo 2. JavaFX SDK 17 is installed at C:\Java\javafx-sdk-17.0.2
    echo 3. File paths are correct
    pause
    exit /b 1
)

echo.
echo ✅ Compilation successful!
echo.
echo Running Student Management System...
java --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls -cp src StudentManagementSystem

pause