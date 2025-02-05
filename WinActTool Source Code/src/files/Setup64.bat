@echo off
cls
title Office 2021 x64 Setup
echo					    	   Office Setup
echo.
echo				  Version 2021 x64 Pro, fait par danbenba
echo.
echo.
echo.
color B
pushd "%~dp0"
%temp%\Oconfig\configuration\bin\main-setup.exe /configure "configuration/configuration-x64.xml"
