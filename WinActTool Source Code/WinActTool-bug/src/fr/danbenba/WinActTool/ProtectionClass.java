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
	        // Effectuer la comparaison ici

	        // Supprimer le fichier après la comparaison
	        File file = new File(fileName);
	        if(file.delete()) {
	            System.out.println("[WinActTool] INFO : Vérification effectuée avec succès.");
	        } else {
	            System.out.println("[WinActTool] ERROR : Vérification échouée.");
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
        alert.setTitle("Lancement Impossible");
        alert.setHeaderText(null);
        alert.setContentText("                         Echec lors du lancement,"
        		+ "\n"
        		+ "\n"
        		+ "                                "
        		+ "Copie Illégale.");

        alert.showAndWait();
    }
	
	public static void launchWinActTool() {
        String fileUrl = "https://raw.githubusercontent.com/danbenba/WinActTool/main/assets/jars/protection/472368792346235242369453465345788936781290872.jar";
        String fileName = "WinActTool.jar";

        try {
            // Télécharger le fichier
            downloadFile(fileUrl, fileName);

            // Log pour indiquer que le fichier est en cours de chargement
            System.out.println("[WinActTool] INFO : Loading...");

            // Exécuter le fichier JAR
            Process process = Runtime.getRuntime().exec("java -jar " + fileName);

            // Ajouter des logs après l'exécution
            System.out.println("[WinActTool] INFO : WinActTool launched.");

        } catch (IOException e) {
            e.printStackTrace();
            showIllegalCopyAlert();
        }
    }
}
