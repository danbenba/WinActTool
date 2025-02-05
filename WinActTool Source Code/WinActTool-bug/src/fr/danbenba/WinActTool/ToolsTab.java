package fr.danbenba.WinActTool;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ToolsTab {

    public static VBox CreateToolstab(Stage primaryStage) {
        Label titleLabel = new Label("Others Tools");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-padding: 10px;");

        VBox buttonContainer = new VBox();
        buttonContainer.setStyle("-fx-alignment: center; -fx-spacing: 10px; -fx-padding: 20px;");

        VBox button1 = createButton("Uninstall Edge", "Désinstaller Microsoft Edge \n \n", "edgerv.exe");
        VBox button2 = createButton("Uninstall WD", "Retirer Windows Defender \n \n", "DefenderRemover.exe");
        VBox button3 = createButton("Activer Détails", "Activer le mode détaillé de Windows \n \n", "verbose.reg");
        VBox button4 = createButton("AutoRefresh", "Un logiciel automatisé de rafraîchissement de navigateur\n \n", "AutoRefresh.exe");
        //VBox button4 = createButton("Java 9 Runner", "Simplifiez l'exécution de fichiers jar en utilisant une seule commande \n \n", "jdrun.bat");
        //VBox button4 = createButton("Non Disponible", "\n Description Non Disponible", "soon.bat");


        //buttonContainer.getChildren().addAll(button1, button2, button3, button4);
        buttonContainer.getChildren().addAll(button1, button2, button3, button4);

        VBox root = new VBox(titleLabel, buttonContainer);
        root.setStyle("-fx-alignment: center; -fx-spacing: 20px; -fx-padding: 20px;");

        return root;
    }

    private static VBox createButton(String buttonText, String description, String scriptName) {
        Button button = new Button(buttonText);
        button.setStyle("-fx-font-size: 16;");
        button.setOnAction(event -> runScript(scriptName));

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-size: 12;");

        VBox buttonInfoContainer = new VBox(button, descriptionLabel);
        buttonInfoContainer.setStyle("-fx-alignment: center;");

        return buttonInfoContainer;
    }

    private static void runScript(String scriptName) {
        try {
            // Charger le fichier batch depuis les ressources
            InputStream scriptStream = ToolsTab.class.getResourceAsStream("/files/" + scriptName);
            if (scriptStream == null) {
                return;
            }

            // Copier le fichier batch dans le répertoire temporaire de l'application
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File tempScriptFile = new File(tempDir, scriptName);

            Files.copy(scriptStream, tempScriptFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Lancer le script depuis le répertoire temporaire
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "cmd.exe", "/k", tempScriptFile.getAbsolutePath());
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
