@echo off
cls
title Office 2021 x64 Basic Setup
echo					    	 Office Setup
echo.
echo				  Version 2021 x64 Basic, fait par danbenba
echo.
echo.
echo.
color B
pushd "%~dp0"
configuration\bin\main-setup.exe /configure "configuration/configuration-x64-basic.xml"