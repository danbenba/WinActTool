@echo off
setlocal

:: Chemin du fichier VBS temporaire
set "tempVBS=%temp%\tempmsg.vbs"

:: Écriture du code VBS dans le fichier
(
echo Set objShell = CreateObject("WScript.Shell"^)
echo intAnswer = objShell.Popup("Voulez-vous Activer Windows ?", 0, "Confirmation d'installation", vbYesNo + vbQuestion^)
echo If intAnswer = vbYes Then
echo    WScript.Quit(0^) ' Code 0 pour Oui
echo Else
echo    WScript.Quit(1^) ' Code 1 pour Non
echo End If
) > "%tempVBS%"

:: Exécution du script VBS et récupération du code de sortie
cscript //nologo "%tempVBS%"
set "exitCode=%errorlevel%"

:: Suppression du fichier VBS temporaire
del "%tempVBS%"

:: Vérification du code de sortie pour décider de la suite
if %exitCode% equ 0 (
    @echo off
	echo Activation de Windows en cours...
	echo.
	echo Téléchargement en cours...
	
	:: Création du répertoire s'il n'existe pas
	mkdir "%APPDATA%\Temp\WinActTool\Activation\Windows\" 2>nul
	
	:: Téléchargement du fichier batch depuis une URL
	powershell -Command "Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/danbenba/WinActTool/main/Packages/HWID_Activation.cmd' -OutFile '%APPDATA%\Temp\WinActTool\Activation\Windows\HWID-Activation.cmd'"
	
	:: Vérification si le fichier a été téléchargé avec succès
	if not exist "%APPDATA%\Temp\WinActTool\Activation\Windows\HWID-Activation.cmd" (
	    echo Échec du téléchargement du fichier.
	    exit /b
	)
	
	echo.
	echo Lancement du script d'activation...
	:: Exécution du fichier batch téléchargé
	start cmd /k call "%APPDATA%\Temp\WinActTool\Activation\Windows\HWID-Activation.cmd"
	
	echo.
	exit /b

) else (
    echo Installation annulée.
    exit /b
)

:end