package fr.danbenba.WinActTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;


public class WindowsActivatorTab {

	private static ImageView loadIcon(String path) {
	    ImageView iconView = null;
	    try {
	        // Utilisation de CrackerTab.class pour obtenir un InputStream
	        InputStream iconStream = OfficeActivatorTab.class.getResourceAsStream(path);
	        if (iconStream != null) {
	            Image icon = new Image(iconStream);
	            iconView = new ImageView(icon);
	            iconView.setFitHeight(128); // Hauteur de l'icône
	            iconView.setFitWidth(128);  // Largeur de l'icône
	        } else {
	            System.out.println("Erreur: Chemin de l'icône introuvable. Chemin: " + path);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Erreur de chargement de l'icône : " + e.getMessage());
	    }
	    return iconView;
	}

	// Méthode executeAndDisplayCrackerBasedOnSelection ajustée
	private static void executeAndDisplayCrackerBasedOnSelection(Label buttonDescription, VBox terminalBox, TextArea terminalPreview, Node textFlow, Button crackerButton, ComboBox<String> windowsVersionComboBox) {
    String selectedVersion = windowsVersionComboBox.getValue();
    
 // Logique pour choisir le fichier batch en fonction de la version de Windows sélectionnée
    @SuppressWarnings("unused")
	String batchFileName;
    switch (selectedVersion) {
        case "Windows 11":
            batchFileName = "/activator.bat";
            break;
        case "Windows 10":
            batchFileName = "/activator_win11.bat";
            break;
        case "Windows 8.1/8":
            batchFileName = "/activator_win8.bat";
            break;
        case "Windows 7":
            batchFileName = "/activator_win7.bat";
            break;
        default:
            batchFileName = "/activator.bat"; // Fichier par défaut
            break;
    }

}
	
	public static VBox createCrackerTab() {
        TextArea terminalPreview = new TextArea();
        terminalPreview.setEditable(false);
        terminalPreview.setStyle("-fx-control-inner-background: black; -fx-text-fill: green;");
        terminalPreview.setPrefWidth(200); // Définir la largeur préférée sur 200 pixels
        terminalPreview.setPrefHeight(307.5);

        // Créer un rectangle grand pour l'arrière-plan
        Rectangle backgroundRectangle = new Rectangle();
        backgroundRectangle.setFill(Color.LIGHTGRAY); // Couleur de l'arrière-plan

        //Label ofatitleLabel = new Label("Office Activator");
        //ofatitleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #333333;");
        //ofatitleLabel.setTextAlignment(TextAlignment.CENTER);

        ImageView iconView = loadIcon("/images/windowsicon.png");

        Button crackerButton = new Button("Launch Activator");
        Label buttonDescription = new Label("L'outil d'activation officiel pour Windows 7-11. Cette outil est indisponible.");

        // Déclaration de la variable textFlow
        Node textFlow = new TextFlow(); // Assurez-vous que cette ligne est bien présente

        
    
        // Ajoutez des propriétés de transformation pour les animations de survol
        crackerButton.setScaleX(1); // Taille horizontale initiale
        crackerButton.setScaleY(1); // Taille verticale initiale

        crackerButton.setStyle("-fx-font-size: 25px;"
                + "-fx-font-weight: 500;"
                + "-fx-text-fill: #007bff;"
                + "-fx-background-color: #fff;"
                + "-fx-padding: 15px 30px;"
                + "-fx-border-color: transparent;"
                + "-fx-background-radius: 5px;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);");

        // Animation d'agrandissement
        ScaleTransition stGrow = new ScaleTransition(Duration.millis(200), crackerButton);
        stGrow.setToX(1.1);
        stGrow.setToY(1.1);

        // Animation de rétrécissement
        ScaleTransition stShrink = new ScaleTransition(Duration.millis(200), crackerButton);
        stShrink.setToX(1.0);
        stShrink.setToY(1.0);
        
        

        // Animation de changement de couleur de fond au survol
        crackerButton.setOnMouseEntered(e -> {
            stGrow.playFromStart();
            crackerButton.setStyle("-fx-font-size: 25px;"
                    + "-fx-font-weight: 500;"
                    + "-fx-text-fill: #fff;" // Texte blanc au survol
                    + "-fx-background-color: #007bff;" // Fond bleu au survol
                    + "-fx-padding: 15px 30px;"
                    + "-fx-border-color: transparent;"
                    + "-fx-background-radius: 5px;"
                    + "-fx-cursor: hand;"
                    + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);");
        });

        // Retour au style initial lorsque la souris quitte le bouton
        crackerButton.setOnMouseExited(e -> {
            stShrink.playFromStart();
            crackerButton.setStyle("-fx-font-size: 25px;"
                    + "-fx-font-weight: 500;"
                    + "-fx-text-fill: #007bff;" // Texte bleu initial
                    + "-fx-background-color: #fff;" // Fond blanc initial
                    + "-fx-padding: 15px 30px;"
                    + "-fx-border-color: transparent;"
                    + "-fx-background-radius: 5px;"
                    + "-fx-cursor: hand;"
                    + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);");
        });

       
     // Création de la ComboBox
        ComboBox<String> windowsVersionComboBox = new ComboBox<>();
        windowsVersionComboBox.getItems().addAll("Windows 11", "Windows 10", "Windows 8.1/8", "Windows 7");
        windowsVersionComboBox.getSelectionModel().selectFirst();

        // Configuration du VBox mainLayout
        VBox mainLayout = new VBox(6);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().add(backgroundRectangle);
        mainLayout.getChildren().add(iconView);
        mainLayout.getChildren().add(buttonDescription);
        mainLayout.getChildren().add(windowsVersionComboBox);
        mainLayout.getChildren().add(crackerButton);
        mainLayout.getChildren().add(terminalPreview);
        mainLayout.getChildren().add(textFlow);

        // Configuration de l'action du bouton
        crackerButton.setOnAction(e -> executeAndDisplayCrackerBasedOnSelection(buttonDescription, mainLayout, terminalPreview, textFlow, crackerButton, windowsVersionComboBox));

        // Configuration et retour du VBox terminalBox
        VBox terminalBox = new VBox(10, mainLayout);
        terminalBox.setAlignment(Pos.BOTTOM_CENTER);

        return terminalBox;
    }

    	

	
	private static void appendCenteredText(TextArea textArea, String text) {
        final double charWidth = 6.2; // Estimate the width of a character in the TextArea

        double textAreaWidth = textArea.getWidth();
        int maxCharsInLine = (int) (textAreaWidth / charWidth);
        int paddingSize = (maxCharsInLine - text.length()) / 2;

        StringBuilder padding = new StringBuilder();
        for (int i = 0; i < paddingSize; i++) {
            padding.append("  ");
        }

        textArea.appendText(padding.toString() + text + "\n");
    }
	
	@SuppressWarnings("unused")
	private static void executeAndDisplayCracker(Label ofatitleLabel, VBox hidbox, TextArea terminalPreview, Node textFlow, Button crackerButton, VBox mainLayout) {
    	appendCenteredText(terminalPreview, "---------LOGS---------");
        terminalPreview.appendText(" \n");
        terminalPreview.appendText("Activating Windows in 3 seconds...\n");

        new Thread(() -> {
            try {
            	Thread.sleep(3000);
                // Étape 1 : Lire le fichier cracker.bat depuis les ressources
            	InputStream inputStream = OfficeActivatorTab.class.getResourceAsStream("/activator.bat");
                if (inputStream == null) {
                    Platform.runLater(() -> terminalPreview.appendText("Fichier de l'activator introuvable dans les ressources.\n"));
                    return;
                }

                // Étape 2 : Copier le fichier dans le dossier temporaire
                File tempDir = new File(System.getProperty("java.io.tmpdir"));
                File tempFile = new File(tempDir, "activator.bat");
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Étape 3 : Exécuter le fichier batch depuis le répertoire temporaire
                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", tempFile.getAbsolutePath());
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    final String finalLine = line + "\n";
                    Platform.runLater(() -> terminalPreview.appendText(finalLine));
                }

                process.waitFor();
                Platform.runLater(() -> {
                    Text successText = new Text("Office has been successfully activated.\n");
                    successText.setFill(Color.GREEN);
                    TextFlow successTextFlow = new TextFlow(successText);
                    successTextFlow.setTextAlignment(TextAlignment.CENTER);
                    successTextFlow.setLayoutX((mainLayout.getWidth() - successTextFlow.prefWidth(-1)) / 2);
                    successTextFlow.setLayoutY((mainLayout.getHeight() - successTextFlow.prefHeight(-1)) / 2);
                    mainLayout.getChildren().remove(crackerButton);
                    mainLayout.getChildren().add(successTextFlow);
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> terminalPreview.appendText("Erreur : " + e.getMessage() + "\n"));
            }
        }).start();
    }
	URL url = Main.class.getResource("/style.css");
}
