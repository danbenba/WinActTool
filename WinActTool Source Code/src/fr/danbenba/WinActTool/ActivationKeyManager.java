package fr.danbenba.WinActTool;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ActivationKeyManager extends Application {

    private static final String ACTIVATION_KEY_FILE = System.getenv("APPDATA") + "\\WinActTool\\Resource\\KEY";
    private static final String ACTIVATION_KEY_URL = "https://raw.githubusercontent.com/justwinlab/764566537.watkey/refs/heads/main/oem-key";
    private static final int MAX_KEY_LENGTH = 29; // 25 characters + 4 dashes
    
    

    public static void checkAndHandleActivationKey(Stage primaryStage) {
        if (!Files.exists(Paths.get(ACTIVATION_KEY_FILE))) {
            Platform.runLater(() -> {
                Stage activationStage = new Stage();
                activationStage.initModality(Modality.APPLICATION_MODAL);
                activationStage.initOwner(primaryStage);
                new ActivationKeyManager().start(activationStage);
            });

            // Afficher l'écran blanc pendant que l'activation de clé est affichée
            showBlankScreen(primaryStage);
        } else {
            Task<Void> checkKeyTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    String key = readActivationKey();
                    if (isValidKey(key)) {
                        System.out.println("[WinActTool] LOGS : Licenced !");
                    } else {
                        System.out.println("[WinActTool] LOGS : Clé d'activation incorrecte.");
                        Platform.runLater(() -> showErrorAndExit("Clé d'activation incorrecte."));

                    }
                    return null;
                }
            };
            new Thread(checkKeyTask).start();
        }
    }

    private static void showBlankScreen(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 150);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Activation de WinActTool");

        Label keyLabel = new Label("Entrez votre clé d'activation:");
        TextField keyField = new TextField();
        keyField.setPromptText("XXXXX-XXXXX-XXXXX-XXXXX");
        Button submitButton = new Button("Activer");

        // Apply CSS styling
        keyLabel.getStyleClass().add("label");
        keyField.getStyleClass().add("text-field");
        submitButton.getStyleClass().add("button");

        // Listener pour formater et valider la clé avec des tirets
        keyField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > MAX_KEY_LENGTH) {
                keyField.setText(oldText);
                return;
            }
            String formattedText = formatKey(newText.toUpperCase());
            if (!newText.equals(formattedText)) {
                keyField.setText(formattedText);
                keyField.positionCaret(formattedText.length()); // Positionner le curseur à la fin
            }
        });

        submitButton.setOnAction(e -> {
            String key = keyField.getText().trim();
            if (isValidKey(key)) {
                saveActivationKey(key);
                primaryStage.close();
                Platform.runLater(() -> main.launcherMain(new Stage()));
            } else {
                showError("Clé d'activation incorrecte.");
            }
        });

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
        });

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.add(keyLabel, 0, 0);
        gridPane.add(keyField, 1, 0);
        gridPane.add(submitButton, 1, 1);

        Scene scene = new Scene(gridPane, 400, 150);
        primaryStage.initStyle(StageStyle.UTILITY); // Appelez ceci avant d'afficher le stage
        // Load and apply the CSS file
        scene.getStylesheets().add(getClass().getResource("gui-ackey-style.css").toExternalForm());
        primaryStage.initStyle(StageStyle.UTILITY); // Appelez ceci avant d'afficher le stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static String readActivationKey() throws IOException {
        StringBuilder keyBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(ACTIVATION_KEY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                keyBuilder.append(line.trim());
            }
        }
        return keyBuilder.toString();
    }

    private static boolean isValidKey(String key) {
        try {
            URL url = new URL(ACTIVATION_KEY_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().equals(key)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> showErrorAndExit("Erreur de connexion à l'URL de vérification de la clé."));
        }
        return false;
    }

    private static void saveActivationKey(String key) {
        try {
            Path keyPath = Paths.get(ACTIVATION_KEY_FILE);
            Files.createDirectories(keyPath.getParent());
            Files.write(keyPath, key.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> showErrorAndExit("Erreur de sauvegarde de la clé d'activation."));
        }
    }

    private static String formatKey(String text) {
        // Supprimer tous les caractères non alphanumériques
        String cleanText = text.replaceAll("[^A-Z0-9]", "");
        // Limiter la longueur à MAX_KEY_LENGTH
        if (cleanText.length() > MAX_KEY_LENGTH) {
            cleanText = cleanText.substring(0, MAX_KEY_LENGTH);
        }
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < cleanText.length(); i++) {
            if (i > 0 && i % 5 == 0) {
                formatted.append('-');
            }
            formatted.append(cleanText.charAt(i));
        }
        return formatted.toString();
    }

    private static void showErrorAndExit(String message) {
        Platform.runLater(() -> {
            showError(message);
            Platform.exit();
        });
    }

    private static void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
