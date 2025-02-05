package fr.danbenba.WinActTool;

import java.net.URL;
import javafx.application.HostServices;
import javafx.application.Platform;
import fr.danbenba.WinActTool.Storage;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.geometry.Insets;
import java.nio.file.StandardCopyOption;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ProgressBar;

import java.util.Optional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.awt.Desktop;
import java.awt.Desktop;
import java.net.HttpURLConnection;
import java.net.SocketOption;
import java.net.URI;

@SuppressWarnings("unused")
public class AboutTab {
    
    private static final String CREATOR_LABEL = Storage.CREATOR_LABEL;
    private static final String TYPE_VERSION_ID = Storage.TYPE_VERSION_ID;
    
    
    private static HostServices hostServices;
    private static Label updateStatusLabel; // Label pour afficher le statut des mises à jour
    private static Stage primaryStage; // Ajoutez cette ligne

    
    public static void testHostServices() {
        if (hostServices == null) {
        	// Code ANSI pour le texte en rouge
            String red = "\033[31m"; // 31 est le code pour le rouge
            String reset = "\033[0m"; // Réinitialiser la couleur
            System.out.println(red + "[WinActTool] ERROR : HostServices is still null when tested." + reset);
        } else {
            System.out.println("[WinActTool] LOGS : HostServices is available when tested.");
            // You can add a test call to hostServices here, if appropriate
        }

    }
    
    public static void setHostServices(HostServices hs) {
        hostServices = hs;
        if (hostServices == null) {
        	// Code ANSI pour le texte en rouge
            String red = "\033[31m"; // 31 est le code pour le rouge
            String reset = "\033[0m"; // Réinitialiser la couleur
            System.out.println(red + "[WinActTool] ERROR : HostServices is null after setting." + reset);
        } else {
            System.out.println("[WinActTool] LOGS : HostServices successfully set.");
        }
    }

     
    public static void checkForLegalCopy() {
        if (!CREATOR_LABEL.equals("danbenba")) {
            showIllegalCopyAlert();
            System.exit(0);
        }
    }
    
    static {
        // Charger la police Ubuntu depuis les ressources
        Font.loadFont(AboutTab.class.getResourceAsStream("/font/Ubuntu.ttf"), 14);
        Font.loadFont(AboutTab.class.getResourceAsStream("/font/Ubuntu-Bold.ttf"), 14);
    }
    
    public static VBox createAboutTab() {
    	VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
         
        // Charger l'image
        ImageView imageView = new ImageView(new Image("/images/icon.png"));
        imageView.setFitHeight(128);
        imageView.setFitWidth(128);

        // Créer le titre
        Label titleLabel = new Label("WinActTool");
        titleLabel.setStyle("-fx-font-family: 'Ubuntu'; -fx-font-size: 16; -fx-font-weight: bold;");
        titleLabel.setTextAlignment(TextAlignment.CENTER);

        // Create the description
        Label descriptionLabel = new Label("Everyone knows Microsoft Toolkit, but here is a new version that is considerably more stable, compatible with the latest versions of Windows and Office. It offers regular updates and offers early access to new features, all for free.");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-font-family: 'Ubuntu'; -fx-font-size: 16;");
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);

        // Link to website
        Hyperlink linkToWebsite = new Hyperlink("Visit our website");
        linkToWebsite.setOnAction(e -> {
            if (hostServices != null) {
                hostServices.showDocument("http://winacttool.rf.gd");
                System.out.println("Opening the website: http://winacttool.rf.gd");
            } else {
                System.out.println("[WinActTool] ERROR: hostServices is null");
            }
        });

        // Link to the official GitHub
        Hyperlink linkToGitHub = new Hyperlink("See the official GitHub");
        linkToGitHub.setOnAction(e -> {
            if (hostServices != null) {
                hostServices.showDocument("https://github.com/danbenba/WinActTool");
                System.out.println("Opening GitHub: https://github.com/danbenba/winacttool");
            } else {
                System.out.println("[WinActTool] ERROR: hostServices is null");
            }
        });

        // Link to the creator's website
        Hyperlink linkToCreatorSite = new Hyperlink("Creator Site");
        linkToCreatorSite.setOnAction(e -> {
            if (hostServices != null) {
                hostServices.showDocument("http://algoforge.rf.gd/");
                System.out.println("Opening the creator's site: http://algoforge.rf.gd/");
            } else {
                System.out.println("[WinActTool] ERROR: hostServices is null");
            }
           
        });
       
        // Space out the description of links and other elements
        Label spacerLabel2 = new Label("");
        spacerLabel2.setMinHeight(20); // Space size

        // Changing the button function
        Button devModeButton = new Button("Change language");
        devModeButton.setOnAction(e -> showLanguageChoice());

        // Création du bouton "Check for Updates"
        Button checkForUpdatesButton = new Button("Check for Updates");
        checkForUpdatesButton.setOnAction(e -> checkForUpdates());

        // Positionner les boutons dans une ligne horizontale
        HBox buttonsBox = new HBox(10, devModeButton, checkForUpdatesButton);
        buttonsBox.setAlignment(Pos.CENTER);

        // Initialisation du Label pour le statut des mises à jour
        updateStatusLabel = new Label("");
        updateStatusLabel.setTextFill(Color.GREEN); // Couleur verte pour le texte

        
     // Espacer la description des liens et autres éléments
        Label spacerLabel = new Label("");
        spacerLabel.setMinHeight(20); // Taille de l'espace

        // Créer le label de la version beta (Disabled)
        
        /**Label versionLabel = new Label("\n \n \n \n \n \n \n \n Version " + Storage.CURRENT_VERSION_For_ABOUT + " Bêta"
        		+ " ");
        versionLabel.setTextAlignment(TextAlignment.CENTER);**/
       
        
        // Créer le label de la version
        Label versionLabel = new Label("\n \nSnapshot " + Storage.CURRENT_VERSION + " (" + TYPE_VERSION_ID + ")"
        		+ " ");
        versionLabel.setTextAlignment(TextAlignment.CENTER);

        // Créer le label du créateur
        Label creatorLabel = new Label("Created by " + CREATOR_LABEL
        		+ " ");
        creatorLabel.setTextAlignment(TextAlignment.CENTER);

        // Créer le copyright
        Label copyrightLabel = new Label("Copyright © 2024 danbenba"
        		+ " ");
        copyrightLabel.setTextAlignment(TextAlignment.CENTER);

        // Positionner les liens dans une ligne horizontale
        HBox linksBox = new HBox(10, linkToWebsite, linkToGitHub, linkToCreatorSite);
        linksBox.setAlignment(Pos.CENTER);

        // Ajouter les éléments au layout
        VBox layout1 = new VBox(10, imageView, titleLabel, descriptionLabel, spacerLabel, linksBox, spacerLabel2, devModeButton, versionLabel, creatorLabel, copyrightLabel);
        layout1.setAlignment(Pos.CENTER);
        layout1.setPadding(new Insets(10));

        return layout1;
    }
    
    private static Object checkForUpdates() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
     
	
	private static void showLanguageChoice() {
	    List<String> languages = Arrays.asList("Français", "English");
	    ChoiceDialog<String> dialog = new ChoiceDialog<>("Français", languages);
	    dialog.setTitle("Choix de la Langue");
	    dialog.setHeaderText("Sélectionnez une langue");
	    dialog.setContentText("Langue :");

	    Optional<String> result = dialog.showAndWait();
	    result.ifPresent(language -> {
	        String url = "";
	        switch (language) {
	            case "English":
	                url = "https://github.com/danbenba/WinActTool/releases/download/lastedversion/WinActTool-fr.jar";
	                break;
	            case "Français":
	                url = "https://example.com/WinActTool_en.jar";
	                break;
	            default:
	                showMessage("Langue non supportée", Color.RED);
	                return;
	        }
	        downloadAndLaunchJar();
	    });
	}

	private static void showDownloadButton() {
	    // Créer un bouton pour télécharger
	    Button downloadButton = new Button("Télécharger et Lancer");
	    downloadButton.setOnAction(e -> downloadAndLaunchJar());
	    
	    // Afficher ce bouton dans une nouvelle fenêtre ou un nouveau dialogue (à déterminer en fonction de vos besoins)
	    
	    // Par exemple, afficher le bouton dans un dialogue d'alerte
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("Télécharger");
	    alert.setHeaderText(null);
	    alert.setContentText("Cliquez pour télécharger et lancer l'application.");
	    
	    // Créer un conteneur pour le bouton
	    VBox content = new VBox(downloadButton);
	    content.setSpacing(10);
	    alert.getDialogPane().setContent(content);
	    
	    alert.showAndWait();
	}
	
	private static void downloadAndLaunchJar(String urlString, ProgressBar progressBar, Label progressLabel, Stage progressStage) {
	    try {
	        URL website = new URL(urlString);
	        String fileName = "WinActTool.jar";
	        File jarFile = new File(fileName);

	        try (InputStream in = website.openStream()) {
	            try (FileOutputStream fos = new FileOutputStream(jarFile)) {
	                byte[] buffer = new byte[1024];
	                int bytesRead;
	                long totalBytesRead = 0;
	                long fileSize = getFileSize(website);

	                while ((bytesRead = in.read(buffer)) != -1) {
	                    fos.write(buffer, 0, bytesRead);
	                    totalBytesRead += bytesRead;
	                    final double progress = (double) totalBytesRead / fileSize;
	                    Platform.runLater(() -> {
	                        progressBar.setProgress(progress);
	                        progressLabel.setText(String.format("Téléchargement en cours... %.2f%%", progress * 100));
	                    });
	                }
	            }

	            Platform.runLater(() -> {
	                progressLabel.setText("Téléchargement terminé. Lancement en cours...");
	                progressBar.setProgress(1);
	                progressStage.close();
	                launchJar(jarFile);
	            });

	        } catch (IOException e) {
	            Platform.runLater(() -> {
	                progressLabel.setText("Erreur lors du téléchargement.");
	                e.printStackTrace();
	            });
	        }

	    } catch (Exception e) {
	        Platform.runLater(() -> {
	            progressLabel.setText("Erreur lors de l'initialisation du téléchargement.");
	            e.printStackTrace();
	        });
	    }
	}

	private static long getFileSize(URL url) {
	    HttpURLConnection conn = null;
	    try {
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("HEAD");
	        conn.getInputStream();
	        return conn.getContentLengthLong();
	    } catch (IOException e) {
	        return -1;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
	}

	private static void launchJar(File jarFile) {
	    try {
	        ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarFile.getAbsolutePath());
	        pb.inheritIO();
	        Process p = pb.start();
	    } catch (IOException e) {
	        e.printStackTrace();
	        // Gérer l'erreur de lancement ici
	    }
	}



    
	private static void promptForPassword() {
	    TextInputDialog passwordDialog = new TextInputDialog();
	    passwordDialog.setTitle("Mode Développeur");
	    passwordDialog.setHeaderText("Accès Mode Développeur");
	    passwordDialog.setContentText("Entrez le mot de passe :");

	    Image customImage = new Image("file:images/mot-de-passe.png");
	    ImageView customImageView = new ImageView(customImage);
	    passwordDialog.setGraphic(customImageView);
	    
	    // Center the header text
	    DialogPane dialogPane = passwordDialog.getDialogPane();
	    dialogPane.setHeaderText("Accès Mode Développeur\n\n Nom d'utilisateur : danbenba");
	    Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
	    if (headerLabel != null) {
	        headerLabel.setAlignment(Pos.CENTER);
	        headerLabel.setTextAlignment(TextAlignment.CENTER);
	        headerLabel.setMaxWidth(Double.MAX_VALUE);
	        headerLabel.setMaxHeight(Double.MAX_VALUE);
	        AnchorPane.setLeftAnchor(headerLabel, 0.0);
	        AnchorPane.setRightAnchor(headerLabel, 0.0);
	    }

	    Optional<String> result = passwordDialog.showAndWait();
	    result.ifPresent(password -> {
	        if (password.equals("WinActTool")) { // Remplacez par le mot de passe réel
	            showMessage("Mot de passe correct. Contact moi sur mon email : danbenba@proton.me en mettant en nom\n de code au debut du message [IIIISSSS].\n Si tu voie ce message. C'est que tu as reussi.", Color.GREEN);
	            downloadAndLaunchJar();
	        } else {
	            showMessage("Mots de passe incorrect", Color.RED);
	        }
	    });
	}



    private static void showMessage(String message, Color color) {
        Alert alert = new Alert(AlertType.NONE);
        alert.setTitle("Message");
        alert.setHeaderText(null);

        Text text = new Text(message);
        text.setFill(color);
        text.setTextAlignment(TextAlignment.CENTER);
        alert.getDialogPane().setContent(text);
        alert.showAndWait();
    }

    private static String getResourceAsStream(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void downloadAndLaunchJar() {
        try {
            URL website = new URL("https://github.com/danbenba/WinActTool/raw/main/WinActTool.jar");
            String fileName = "WinActTool.jar";
            File jarFile = new File(fileName);

            // Télécharge le fichier .jar
            try (InputStream in = website.openStream()) {
                Files.copy(in, Paths.get(jarFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
            }

            // Lance le fichier .jar
            Desktop.getDesktop().open(jarFile);

        } catch (Exception e) {
            // Affichez une popup si le fichier .jar est introuvable ou le lien invalide
            showBetaUnavailablePopup();
            e.printStackTrace();
        }
    }

    private static void showBetaUnavailablePopup() {
        // Implémentez la logique pour afficher la popup "Aucune version beta disponible"
        System.out.println("Aucune version beta disponible");
    }


    public static class UpdateChecker {
    	public static boolean checkForUpdates(Stage primaryStage) {
    	        boolean updatesAvailable = UpdateChecker.checkForUpdates(primaryStage);
        if (updatesAvailable) {
            // Afficher un popup et fermer la fenêtre
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Mise à jour disponible");
            alert.setHeaderText(null);
            alert.setContentText("Une mise à jour est disponible. L'application va maintenant se fermer.");
            alert.showAndWait();

            primaryStage.close(); // Ferme la fenêtre principale
        } else {
            updateStatusLabel.setText("Pas de mise à jour disponible");
        }
		return updatesAvailable;
    }
    }
    
    private static void showIllegalCopyAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Lancement Impossible");
        alert.setHeaderText(null);
        alert.setContentText("Echec lors du lancement, Copie Illégale.");
        alert.showAndWait();
    }
    
    URL url = main.class.getResource("/style.css");
}
