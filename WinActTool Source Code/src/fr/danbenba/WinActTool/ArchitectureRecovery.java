package fr.danbenba.WinActTool;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.List;

import javafx.scene.Node;

@SuppressWarnings("unused")
public class ArchitectureRecovery {

	private static void Recovery() {
    	// Récupérer les informations système sans utiliser OSHI
        String osName = System.getProperty("os.name");
        String osBuild = System.getProperty("os.build");
        String osArch = System.getProperty("os.arch");
        String javaVersion = System.getProperty("java.version");
        String systemDetails = "Système d'exploitation: " + osName + "     Architecture: " + osArch + "     Version Java: " + javaVersion;
    }
}
