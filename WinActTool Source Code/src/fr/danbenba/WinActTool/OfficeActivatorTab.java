package fr.danbenba.WinActTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import javax.swing.JOptionPane;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
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

@SuppressWarnings("unused")
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
	        	// Code ANSI pour le texte en rouge
	            String red = "\033[31m"; // 31 est le code pour le rouge
	            String reset = "\033[0m"; // Réinitialiser la couleur
	            
	            System.out.println(red + "[WinActTool] ERROR : Chemin de l'icône introuvable. Chemin: " + path + reset);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
        	// Code ANSI pour le texte en rouge
            String red = "\033[31m"; // 31 est le code pour le rouge
            String reset = "\033[0m"; // Réinitialiser la couleur
            
	        System.out.println(red + "[WinActTool] ERROR : Chargement de l'icône : " + e.getMessage() + reset);
	    }
	    return iconView;
	}

	static {
        // Charger la police Ubuntu depuis les ressources
        Font.loadFont(OfficeActivatorTab.class.getResourceAsStream("/font/Ubuntu.ttf"), 14);
        Font.loadFont(OfficeActivatorTab.class.getResourceAsStream("/font/Ubuntu-Bold.ttf"), 14);
    }
	
	@SuppressWarnings("unused")
	private static void adjustLayout(VBox mainLayout, TextArea terminalPreview, Node button, Node logo, Node description) {
	    if (terminalPreview.isVisible()) {
	        // Top Layout - Align button, logo, and description at the top
	        mainLayout.setAlignment(Pos.TOP_CENTER);
	        mainLayout.getChildren().clear();
	        mainLayout.getChildren().addAll(logo, description, button, terminalPreview);
	    } else {
	        // Centered Layout - Center button, logo, and description
	        mainLayout.setAlignment(Pos.CENTER);
	        mainLayout.getChildren().clear();
	        mainLayout.getChildren().addAll(logo, description, button);
	    }
	}
	
	@SuppressWarnings("unused")
	public static VBox createCrackerTab(Stage primaryStage) {
		TextArea terminalPreview = new TextArea();
	    terminalPreview.setEditable(false);
	    terminalPreview.setVisible(false); // Initially set to invisible
	    terminalPreview.setStyle("-fx-control-inner-background: black; -fx-text-fill: green;");
	    terminalPreview.setPrefWidth(200); // Set the preferred width
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
        Label logsinfo = new Label("InfoConsole");

        

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
	    if (officeDirectory.exists() && officeDirectory.isDirectory()) {
	    	return officeDirectory.exists() && officeDirectory.isDirectory();
	    }


	    String alternatePath = "C:\\Program Files (x86)\\Microsoft Office\\root";
	    File alternateDirectory = new File(alternatePath);
	    if (alternateDirectory.exists() && alternateDirectory.isDirectory()) {
	    	return alternateDirectory.exists() && alternateDirectory.isDirectory();
	    }
		return false;
	}
	
	private static void executeAndDisplayCracker(Label ofatitleLabel, VBox hidbox, TextArea terminalPreview, Node textFlow, Button crackerButton, VBox mainLayout) {
		// Vérifier si le programme est exécuté avec des privilèges administratifs
		if (!isRunAsAdmin()) {
		    // Afficher une boîte de dialogue d'erreur
			Platform.runLater(() -> {
			    Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Erreur d'exécution");
			    alert.setHeaderText("Privileges insuffisants");
			    alert.setContentText("Le script doit être exécuté en tant qu'administrateur.");

			    ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
			    alert.getButtonTypes().setAll(okButton);

			    // Définir le propriétaire de la boîte de dialogue
			    Stage primaryStage = (Stage) mainLayout.getScene().getWindow();
			    alert.initOwner(primaryStage);

			    // Attendre que la boîte de dialogue soit prête avant de modifier le style du bouton
			    alert.setOnShown(e -> {
			        Button okButtonInstance = (Button) alert.getDialogPane().lookupButton(okButton);
			        okButtonInstance.setStyle("-fx-background-color: #0078D7; " + // Couleur de fond bleue
			                                  "-fx-text-fill: white; " + // Texte blanc
			                                  "-fx-font-size: 20px; " + // Taille de texte à 10px
			                                  "-fx-min-height: 18px; " + // Hauteur minimale à 18px
                    						  "-fx-pref-height: 50px; " + // Hauteur préférée à 18px
                    						  "-fx-max-height: 50px;" + // Hauteur maximale à 18px 
			                                  "-fx-background-radius: 5px; " + // Coins arrondis
			                                  "-fx-padding: 10px 20px; " + // Espacement interne
			                                  "-fx-border-radius: 5px; " + // Rayon de bordure pour les coins arrondis
			                                  "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 5, 0, 0, 3);"); // Ombre subtile
			    });

			    Optional<ButtonType> result = alert.showAndWait();
			    if (result.isPresent() && result.get() == okButton) {
			        System.exit(0);
			    }
			});
		    return;
		}
	    
		Platform.runLater(() -> {
			terminalPreview.setVisible(true);
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
        	    Platform.runLater(() -> {
            	appendCenteredText(terminalPreview, "---------LOGS---------");
                terminalPreview.appendText(" \n");
        		terminalPreview.appendText("[WinActTool] LOGS : Verification de l'activator \n");
                terminalPreview.clear();
        		});
                // Étape 1 : Lire le fichier activator.bat depuis les ressources
                InputStream inputStream = OfficeActivatorTab.class.getResourceAsStream("/files/Office-Activator.bat");
                if (inputStream == null) {
                	appendCenteredText(terminalPreview, "---------LOGS---------");
            		Platform.runLater(() -> terminalPreview.appendText(" \n"));
                	// Code ANSI pour le texte en rouge
                    String red = "\033[31m"; // 31 est le code pour le rouge
                    String reset = "\033[0m"; // Réinitialiser la couleur
                    Platform.runLater(() -> terminalPreview.appendText(red +"[WinActTool] ERROR : Fichier activator introuvable dans les ressources.\n" + reset));
                    return;
                }
                Platform.runLater(() -> {
            		appendCenteredText(terminalPreview, "---------LOGS---------");
            		terminalPreview.appendText("[WinActTool] LOGS : Verification de l'activator OK\n");
                    terminalPreview.appendText(" \n");
            		});
                
                Platform.runLater(() -> {
            		appendCenteredText(terminalPreview, "---------LOGS---------\n");
                	terminalPreview.appendText("[WinActTool] LOGS : Verification de l'activator OK\n");
                    terminalPreview.appendText(" \n");
                    terminalPreview.appendText("[WinActTool] LOGS : Verification de l'installation d'office \n");
                    terminalPreview.clear();
            		});
                
             // Étape 2 : Vérifier si Office 2021 est installé
                boolean isOffice2021Installed = checkIfOffice2021IsInstalled();
                if (!isOffice2021Installed) {
                	Platform.runLater(() -> terminalPreview.appendText(" \n"));
            		appendCenteredText(terminalPreview, "---------LOGS---------");
            		Platform.runLater(() -> terminalPreview.appendText(" \n"));
                	// Code ANSI pour le texte en rouge
                    String red = "\033[31m"; // 31 est le code pour le rouge
                    String reset = "\033[0m"; // Réinitialiser la couleur
                    Platform.runLater(() -> terminalPreview.appendText(red + "[WinActTool] ERROR : Office 2021 n'est pas installé ou introuvable.\n" + reset));
                    return;
                }
                Platform.runLater(() -> {
            	appendCenteredText(terminalPreview, "---------LOGS---------");
            	terminalPreview.appendText("Verifications :\n");
            	terminalPreview.appendText("\n");
                terminalPreview.appendText("[WinActTool] LOGS : Verification de l'activator OK\n");
                terminalPreview.appendText("[WinActTool] LOGS : Verification de l'installation d'office. OK\n");
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
                terminalPreview.appendText("[WinActTool] LOGS : Activating Office in 3 seconds...\n");
                });
                Thread.sleep(3000);
                // Étape 3: Copier le fichier dans le dossier temporaire
                File tempDir = new File(System.getProperty("java.io.tmpdir"));
                File tempFile = new File(tempDir, "Office-Activator.bat");
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
                Platform.runLater(() -> {
                	mainLayout.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("successTextFlow"));
              
                    // Afficher un message temporaire ou réaliser une autre action
                    Text successTextFlow = new Text("Préparation de la dernière étape...\n");
                    successTextFlow.setFont(Font.font("Ubuntu", FontWeight.NORMAL, 18));
                    successTextFlow.setFill(Color.BLUE);
                    successTextFlow.setId("successTextFlow");
                    TextFlow tempTextFlow = new TextFlow(successTextFlow);
                    tempTextFlow.setTextAlignment(TextAlignment.CENTER);
                    tempTextFlow.setLayoutX((mainLayout.getWidth() - tempTextFlow.prefWidth(-1)) / 2);
                    tempTextFlow.setLayoutY((mainLayout.getHeight() - tempTextFlow.prefHeight(-1)) / 2);
                    tempTextFlow.setPadding(new Insets(10, 10, 10, 10));
                    tempTextFlow.setId("successTextFlow");
                    mainLayout.getChildren().remove(crackerButton);
                    mainLayout.getChildren().add(tempTextFlow);
                });
                
                try {
                    // Lire le fichier batch supplémentaire depuis les ressources
                    InputStream additionalTaskStream = OfficeActivatorTab.class.getResourceAsStream("/files/patcher.bat");
                    if (additionalTaskStream == null) {
                        Platform.runLater(() -> terminalPreview.appendText("Le fichier additionalTask.bat est introuvable dans les ressources.\n"));
                        return;
                    }

                    // Copier le fichier dans le répertoire temporaire
                    File tempDir1 = new File(System.getProperty("java.io.tmpdir"));
                    File tempAdditionalTaskFile = new File(tempDir1, "office-patcher.bat");
                    Files.copy(additionalTaskStream, tempAdditionalTaskFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    // Ouvrir une nouvelle fenêtre de l'invite de commande pour exécuter le fichier batch
                    ProcessBuilder additionalTaskProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "cmd.exe", "/k", tempAdditionalTaskFile.getAbsolutePath());
                    additionalTaskProcessBuilder.redirectErrorStream(true);
                    Process additionalTaskProcess = additionalTaskProcessBuilder.start();

                    // Lire la sortie de la commande
                    BufferedReader additionalTaskReader = new BufferedReader(new InputStreamReader(additionalTaskProcess.getInputStream(), StandardCharsets.UTF_8));
                    String additionalTaskLine;
                    while ((additionalTaskLine = additionalTaskReader.readLine()) != null) {
                        final String finalAdditionalTaskLine = additionalTaskLine + "\n";
                        Platform.runLater(() -> terminalPreview.appendText(finalAdditionalTaskLine));
                    }

                    // Attendre la fin du processus
                    additionalTaskProcess.waitFor();

                    // Fermer la fenêtre de commande
                    Runtime.getRuntime().exec("taskkill /F /IM cmd.exe");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
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
	URL url = main.class.getResource("/style.css");
	
	private static boolean isRunAsAdmin() {
        File testPrivilege = new File("C:\\testPrivilege.txt");
        try {
            if (testPrivilege.createNewFile()) {
                testPrivilege.delete();
                return true;
            }
        } catch (IOException e) {
            // L'exception indique un manque de privilèges
        }
        return false;
    }
	

}
