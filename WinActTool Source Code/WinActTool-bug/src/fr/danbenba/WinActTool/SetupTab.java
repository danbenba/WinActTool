package fr.danbenba.WinActTool;

import java.net.URL;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class SetupTab {

    public static VBox createSetupTab() {
        ComboBox<String> versionSelection = new ComboBox<>();
        versionSelection.getItems().addAll("Version Basic", "Version Pro");

        ComboBox<String> architectureSelection = new ComboBox<>();
        architectureSelection.getItems().addAll("Architecture 32-bit", "Architecture 64-bit");

        Button launchButton = new Button("Installer");
        launchButton.setOnAction(e -> {
            String version = versionSelection.getValue();
            String architecture = architectureSelection.getValue();
            // Logique pour lancer l'installation basée sur les sélections
            launchBatchFile(version, architecture);
        });

        VBox setupLayout = new VBox(10, versionSelection, architectureSelection, launchButton);
        setupLayout.setAlignment(Pos.CENTER);
        return setupLayout;
    }

    private static void launchBatchFile(String version, String architecture) {
        // Logique pour lancer le fichier batch en fonction de la version et de l'architecture choisies
        // Cette méthode doit être complétée en fonction des besoins spécifiques de l'application
    }
    
    
	URL url = Main.class.getResource("/style.css");
}
