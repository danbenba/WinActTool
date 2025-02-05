package fr.danbenba.WinActTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OfficeActivatorTab {

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
	            System.out.println("[WinActTool] ERROR : Chemin de l'icône introuvable. Chemin: " + path);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("[WinActTool] ERROR : Chargement de l'icône : " + e.getMessage());
	    }
	    return iconView;
	}

	static {
        // Charger la police Ubuntu depuis les ressources
        Font.loadFont(OfficeActivatorTab.class.getResourceAsStream("/font/Ubuntu.ttf"), 14);
        Font.loadFont(OfficeActivatorTab.class.getResourceAsStream("/font/Ubuntu-Bold.ttf"), 14);
    }
	
	public static VBox createCrackerTab(Stage primaryStage) {
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

        ImageView iconView = loadIcon("/images/officeicon.png");

        Button crackerButton = new Button("Launch Activator");
        Label buttonDescription = new Label("L'outil d'activation officiel pour Microsoft Office 2021 utilisant la méthode KMS.");

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

        // Créer une VBox pour contenir tous les éléments, y compris le rectangle d'arrière-plan
        VBox mainLayout = new VBox(10, iconView, buttonDescription, crackerButton, terminalPreview, textFlow);
        mainLayout.setAlignment(Pos.CENTER);

        // Ajouter le rectangle en arrière-plan
        mainLayout.getChildren().add(0, backgroundRectangle);

        terminalPreview.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(terminalPreview, Priority.ALWAYS);

        // Créer une VBox pour le terminalPreview
        VBox terminalBox = new VBox(10, mainLayout, terminalPreview);
        terminalBox.setAlignment(Pos.BOTTOM_CENTER);

        crackerButton.setOnAction(e -> executeAndDisplayCracker(buttonDescription, terminalBox, terminalPreview, textFlow, crackerButton, mainLayout));

        return terminalBox;
    }

	
	private static void appendCenteredText(TextArea textArea, String text) {
        final double charWidth = 6.6; // Estimate the width of a character in the TextArea

        double textAreaWidth = textArea.getWidth();
        int maxCharsInLine = (int) (textAreaWidth / charWidth);
        int paddingSize = (maxCharsInLine - text.length()) / 2;

        StringBuilder padding = new StringBuilder();
        for (int i = 0; i < paddingSize; i++) {
            padding.append("  ");
        }

        textArea.appendText(padding.toString() + text + "\n");
    }
	
	private static boolean checkIfOffice2021IsInstalled() {
	    // Chemin du dossier où Office 2021 est généralement installé
	    String officeDirectoryPath = "C:\\Program Files\\Microsoft Office\\root";

	    // Créer une instance de fichier pour représenter le chemin
	    File officeDirectory = new File(officeDirectoryPath);

	    // Vérifier si le dossier existe et est un répertoire
	    return officeDirectory.exists() && officeDirectory.isDirectory();
	}
	
	private static void executeAndDisplayCracker(Label ofatitleLabel, VBox hidbox, TextArea terminalPreview, Node textFlow, Button crackerButton, VBox mainLayout) {
		Platform.runLater(() -> {
            Text verif = new Text("Verification");
            verif.setFill(Color.BLACK);
            verif.setFont(Font.font("Ubuntu", FontWeight.NORMAL, 20)); // Change la police en Verdana et la taille en 18

            TextFlow successTextFlow = new TextFlow(verif);
            successTextFlow.setTextAlignment(TextAlignment.CENTER);
            successTextFlow.setLayoutX((mainLayout.getWidth() - successTextFlow.prefWidth(-1)) / 2);
            successTextFlow.setLayoutY((mainLayout.getHeight() - successTextFlow.prefHeight(-1)) / 2);
            successTextFlow.setId("successTextFlow");

            // Ajout d'une marge (padding) de 3 pixels
            successTextFlow.setPadding(new Insets(20, 20, 20, 20));
            
            mainLayout.getChildren().remove(crackerButton);
            mainLayout.getChildren().add(successTextFlow);

            // Créer une Timeline pour l'animation des points
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(1), 
                    ae -> {
                        if (verif.getText().length() < "Verification...".length()) {
                            verif.setText(verif.getText() + ".");
                        } else {
                            verif.setText("Activation");
                        }
                    }));

            timeline.setCycleCount(Animation.INDEFINITE); // Faire tourner l'animation indéfiniment
            timeline.play();
        });
		
    	appendCenteredText(terminalPreview, "---------LOGS---------");
        terminalPreview.appendText(" \n");
        
        new Thread(() -> {
        	try {
                Thread.sleep(3000);
        		Platform.runLater(() -> {
            	appendCenteredText(terminalPreview, "---------LOGS---------");
                terminalPreview.appendText(" \n");
        		terminalPreview.appendText("[WinActTool] INFO : Verification de l'activator \n");
                terminalPreview.clear();
        		});
                // Étape 1 : Lire le fichier activator.bat depuis les ressources
                InputStream inputStream = OfficeActivatorTab.class.getResourceAsStream("/files/activator.bat");
                if (inputStream == null) {
                	appendCenteredText(terminalPreview, "---------LOGS---------");
            		Platform.runLater(() -> terminalPreview.appendText(" \n"));
                    Platform.runLater(() -> terminalPreview.appendText("[WinActTool] ERROR : Fichier activator introuvable dans les ressources.\n"));
                    return;
                }
                Platform.runLater(() -> {
            		appendCenteredText(terminalPreview, "---------LOGS---------");
            		terminalPreview.appendText("[WinActTool] INFO : Verification de l'activator OK\n");
                    terminalPreview.appendText(" \n");
            		});
                
                Platform.runLater(() -> {
            		appendCenteredText(terminalPreview, "---------LOGS---------\n");
                	terminalPreview.appendText("[WinActTool] INFO : Verification de l'activator OK\n");
                    terminalPreview.appendText(" \n");
                    terminalPreview.appendText("[WinActTool] INFO : Verification de l'installation d'office \n");
                    terminalPreview.clear();
            		});
                
             // Étape 2 : Vérifier si Office 2021 est installé
                boolean isOffice2021Installed = checkIfOffice2021IsInstalled();
                if (!isOffice2021Installed) {
                	Platform.runLater(() -> terminalPreview.appendText(" \n"));
            		appendCenteredText(terminalPreview, "---------LOGS---------");
            		Platform.runLater(() -> terminalPreview.appendText(" \n"));
                    Platform.runLater(() -> terminalPreview.appendText("[WinActTool] ERROR : Office 2021 n'est pas installé ou introuvable.\n"));
                    return;
                }
                Platform.runLater(() -> {
            	appendCenteredText(terminalPreview, "---------LOGS---------");
            	terminalPreview.appendText("Verifications :\n");
            	terminalPreview.appendText("\n");
                terminalPreview.appendText("[WinActTool] INFO : Verification de l'activator OK\n");
                terminalPreview.appendText("[WinActTool] INFO : Verification de l'installation d'office. OK\n");
                terminalPreview.appendText(" \n");
                terminalPreview.appendText("Activation :\n");
            	terminalPreview.appendText("\n");
                });
                Platform.runLater(() -> {
                	mainLayout.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("successTextFlow"));
                    Text act = new Text("Activation");
                    act.setFill(Color.BLACK);
                    act.setFont(Font.font("Ubuntu", FontWeight.NORMAL, 20)); // Change la police en Verdana et la taille en 18

                    
                    
                    TextFlow successTextFlow = new TextFlow(act);
                    successTextFlow.setTextAlignment(TextAlignment.CENTER);
                    successTextFlow.setLayoutX((mainLayout.getWidth() - successTextFlow.prefWidth(-1)) / 2);
                    successTextFlow.setLayoutY((mainLayout.getHeight() - successTextFlow.prefHeight(-1)) / 2);
                    successTextFlow.setId("successTextFlow");
                    
                    // Ajout d'une marge (padding) de 3 pixels
                    successTextFlow.setPadding(new Insets(20, 20, 20, 20));
                    
                    mainLayout.getChildren().remove(crackerButton);
                    mainLayout.getChildren().add(successTextFlow);

                    // Créer une Timeline pour l'animation des points
                    Timeline timeline = new Timeline(new KeyFrame(
                            Duration.seconds(1), 
                            ae -> {
                                if (act.getText().length() < "Activation...".length()) {
                                    act.setText(act.getText() + ".");
                                } else {
                                    act.setText("Activation");
                                }
                            }));

                    timeline.setCycleCount(Animation.INDEFINITE); // Faire tourner l'animation indéfiniment
                    timeline.play();
                });
        		
                Platform.runLater(() -> {
                terminalPreview.appendText("[WinActTool] INFO : Activating Office in 3 seconds...\n");
                });
                Thread.sleep(3000);
                // Étape 3: Copier le fichier dans le dossier temporaire
                File tempDir = new File(System.getProperty("java.io.tmpdir"));
                File tempFile = new File(tempDir, "activator.bat");
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Étape 4 : Exécuter le fichier batch depuis le répertoire temporaire
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
                	mainLayout.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("successTextFlow"));
                    Text successText = new Text("Office has been successfully activated.\n");
                    successText.setFont(Font.font("Ubuntu", FontWeight.NORMAL, 18)); // Change la police en Verdana et la taille en 20
                    successText.setFill(Color.GREEN);
                    TextFlow successTextFlow = new TextFlow(successText);
                    successTextFlow.setTextAlignment(TextAlignment.CENTER);
                    successTextFlow.setLayoutX((mainLayout.getWidth() - successTextFlow.prefWidth(-1)) / 2);
                    successTextFlow.setLayoutY((mainLayout.getHeight() - successTextFlow.prefHeight(-1)) / 2);
                    // Ajout d'une marge (padding) de 3 pixels
                    successTextFlow.setPadding(new Insets(10, 10, 10, 10));
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
