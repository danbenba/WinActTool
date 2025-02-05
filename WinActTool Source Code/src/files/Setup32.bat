@echo off
cls
title Office 2021 x32 Setup
echo					            Office Setup
echo.
echo				  Version 2021 x32 Pro, fait par danbenba
echo.
echo.
echo.
color B
pushd "%~dp0"
configuration\bin\main-setup.exe /configure "configuration/configuration-x32.xml"
