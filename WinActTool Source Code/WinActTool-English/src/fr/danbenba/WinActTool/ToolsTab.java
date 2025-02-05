package fr.danbenba.WinActTool;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ToolsTab {

    public static VBox CreateToolstab(Stage primaryStage) {
        Label titleLabel = new Label("         Other Tools          ");
        titleLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold; -fx-padding: 10px; -fx-alignment: center;");

        VBox leftContainer = new VBox();
        VBox rightContainer = new VBox();

        VBox button1 = createButton("Uninstall Edge", "Removing Application", "edgerv.exe");
        VBox button2 = createButton("Uninstall WD", "Tools for Windows", "DefenderRemover.exe");
        VBox button3 = createButton("Enable Details", "Registry Tool", "verbose.reg");
        VBox button4 = createButton("AutoRefresh", "Lazy Tool", "AutoRefresh.exe");

        leftContainer.getChildren().addAll(button1, button3);
        rightContainer.getChildren().addAll(button2, button4);

        leftContainer.setStyle("-fx-alignment: top-left; -fx-spacing: 10px;");
        rightContainer.setStyle("-fx-alignment: top-right; -fx-spacing: 10px;");

        HBox mainContainer = new HBox(leftContainer, titleLabel, rightContainer);
        mainContainer.setStyle("-fx-alignment: center; -fx-spacing: 20px; -fx-padding: 20px;");

        VBox root = new VBox(mainContainer);
        root.setStyle("-fx-alignment: center; -fx-spacing: 20px; -fx-padding: 20px;");

        return root;
    }

    private static VBox createButton(String buttonText, String description, String scriptName) {
        Button button = new Button(buttonText);
        button.setStyle("-fx-font-size: 14;");
        button.setOnAction(event -> runScript(scriptName));

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-size: 12; -fx-alignment: center;");

        Rectangle square = new Rectangle(256, 128);
        square.setStroke(Color.BLACK);
        square.setStrokeWidth(4);
        square.setFill(Color.TRANSPARENT);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(square, button);

        VBox buttonInfoContainer = new VBox(stackPane, descriptionLabel);
        buttonInfoContainer.setStyle("-fx-alignment: center; -fx-spacing: 10px;");

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
