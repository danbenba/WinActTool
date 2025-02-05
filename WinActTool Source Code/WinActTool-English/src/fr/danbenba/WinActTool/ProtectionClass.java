package fr.danbenba.WinActTool;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class ProtectionClass {

	static final String Protection_Version = Storage.Protection_Version;
	static final String CREATOR_LABEL = Storage.CREATOR_LABEL;
	
	public static void checkForLegalCopy() {
	    // Télécharge le fichier .jar
	    try {
	        String fileUrl = "https://github.com/danbenba/WinActTool/raw/main/WinActTool.jar"; // URL du fichier .jar
	        String fileName = "WinActToolOriginal.jar";
	        downloadFile(fileUrl, fileName);
	        
	        // Supprimer le fichier après la comparaison
	        File file = new File(fileName);
	        if(file.delete()) {
	            System.out.println("[WinActTool] LOGS : Verification completed successfully.");
	            @SuppressWarnings("unused")
				class AnimationPoints {
					public void main(String[] args) throws InterruptedException {
	                    System.out.print("[WinActTool] LOGS: Loading files (This operation may take several minutes)");

	                    for (int i = 0; i < 7; i++) {
	                        System.out.print(".");
	                        Thread.sleep(1000); // Délai d'une seconde
	                    }

	                    System.out.println(); // Passer à la ligne suivante une fois la boucle terminée
	                }
	            }

	        } else {
	        	// Code ANSI pour le texte en rouge
	            String red = "\033[31m"; // 31 est le code pour le rouge
	            String reset = "\033[0m"; // Réinitialiser la couleur
	            
	            System.out.println(red + "[WinActTool] ERROR : Vérification échouée." + reset);
	            System.exit(0);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        showIllegalCopyAlert();
	        System.exit(0);
	    }
	}
	


    private static void downloadFile(String fileUrl, String localFilename) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(localFilename)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }
	
	public static void checkForLegalCopyForCreator() {
        if (!CREATOR_LABEL.equals("danbenba")) {
            showIllegalCopyAlert();
            System.exit(0);
        }
    }
	
	
	private static void showIllegalCopyAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Unable to launch");
        alert.setHeaderText(null);
        alert.setContentText("                         Failed to launch,"
        + "\n"
        + "\n"
        + " "
        + "Illegal Copy.");

        alert.showAndWait();
    }
	
}
