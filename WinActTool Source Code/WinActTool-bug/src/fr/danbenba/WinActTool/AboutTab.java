package fr.danbenba.WinActTool;

import java.net.URL;
import javafx.application.HostServices;
import fr.danbenba.WinActTool.Storage;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import java.io.File;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.awt.Desktop;
import java.awt.Desktop;
import java.net.URI;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class AboutTab {
    
    private static final String CREATOR_LABEL = Storage.CREATOR_LABEL;
    
    
    private static HostServices hostServices;
    private static Label updateStatusLabel; // Label pour afficher le statut des mises à jour
    private static Stage primaryStage; // Ajoutez cette ligne

    
    public static void testHostServices() {
        if (hostServices == null) {
            System.out.println("[WinActTool] ERROR : HostServices is still null when tested.");
        } else {
            System.out.println("[WinActTool] INFO : HostServices is available when tested.");
            // You can add a test call to hostServices here, if appropriate
        }

    }
    
    public static void setHostServices(HostServices hs) {
        hostServices = hs;
        if (hostServices == null) {
            System.out.println("[WinActTool] ERROR : HostServices is null after setting.");
        } else {
            System.out.println("[WinActTool] INFO : HostServices successfully set.");
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

        // Créer la description
        Label descriptionLabel = new Label("Tout le monde connaît Microsoft Toolkit, mais voici une nouvelle version considérablement plus stable, compatible avec les dernières versions de Windows et d'Office. Elle propose des mises à jour régulières et offre un accès anticipé aux nouveautés, le tout gratuitement.");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-font-family: 'Ubuntu'; -fx-font-size: 16;");
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);

        // Lien vers le site web
        Hyperlink linkToWebsite = new Hyperlink("Visitez notre site web");
        linkToWebsite.setOnAction(e -> {
            if (hostServices != null) {
                hostServices.showDocument("http://winacttool.rf.gd");
                System.out.println("Ouverture du site web : http://winacttool.rf.gd");
            } else {
                System.out.println("[WinActTool] ERROR : hostServices est null");
            }
        });

        // Lien vers le GitHub officiel
        Hyperlink linkToGitHub = new Hyperlink("Voir le GitHub officiel");
        linkToGitHub.setOnAction(e -> {
            if (hostServices != null) {
                hostServices.showDocument("https://github.com/danbenba/WinActTool");
                System.out.println("Ouverture du GitHub : https://github.com/danbenba/winacttool");
            } else {
                System.out.println("[WinActTool] ERROR : hostServices est null");
            }
        });

        // Lien vers le site du créateur
        Hyperlink linkToCreatorSite = new Hyperlink("Site du créateur");
        linkToCreatorSite.setOnAction(e -> {
            if (hostServices != null) {
                hostServices.showDocument("http://algoforge.rf.gd/");
                System.out.println("Ouverture du site du créateur : http://algoforge.rf.gd/");
            } else {
                System.out.println("[WinActTool] ERROR : hostServices est null");
            }
            
        });
        
        // Espacer la description des liens et autres éléments
        Label spacerLabel2 = new Label("");
        spacerLabel2.setMinHeight(20); // Taille de l'espace

        // Création du bouton "Mode Développeur"
        Button devModeButton = new Button("Mode Développeur");
        devModeButton.setOnAction(e -> promptForPassword());

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

        // Créer le label de la version beta
        //Label versionLabel = new Label("\n \n \n \n \n \n \n \n Version " + Storage.CURRENT_VERSION_For_ABOUT + " Bêta"
        		//+ " ");
        //versionLabel.setTextAlignment(TextAlignment.CENTER);
       
        
        // Créer le label de la version
        Label versionLabel = new Label("\n \n Version " + Storage.CURRENT_VERSION
        		+ " ");
        versionLabel.setTextAlignment(TextAlignment.CENTER);

        // Créer le label du créateur
        Label creatorLabel = new Label("Powered by " + CREATOR_LABEL
        		+ " ");
        creatorLabel.setTextAlignment(TextAlignment.CENTER);

        // Créer le copyright
        Label copyrightLabel = new Label("Copyright © 2020-2023 AlgoForge"
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
     
    
	private static void promptForPassword() {
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Mode Développeur");
        passwordDialog.setHeaderText("Accès Mode Développeur");
        passwordDialog.setContentText("Entrez le mot de passe :");

        Optional<String> result = passwordDialog.showAndWait();
        result.ifPresent(password -> {
            if (password.equals("VotreMotDePasse")) { // Remplacez par le mot de passe réel
                System.out.println("Mode développeur activé");
                downloadAndLaunchJar();
            } else {
                System.out.println("Mot de passe incorrect");
            }
        });
    }

    private static void downloadAndLaunchJar() {
        try {
            URL website = new URL("https://github.com/danbenba/WinActTool/raw/main/WinActTool.jar");
            String fileName = "WinActTool.jar";
            File jarFile = new File(fileName);

            // Télécharge le fichier .jar
            try (var in = website.openStream()) {
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
        public static boolean checkForUpdates(Object param) {
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
    
    URL url = Main.class.getResource("/style.css");
}
