package fr.danbenba.WinActTool;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
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
public class PackageInstallerTab {

	private static ImageView loadIcon(String path) {
	    ImageView iconView = null;
	    try {
	        // Utilisation de CrackerTab.class pour obtenir un InputStream
	        InputStream iconStream = PackageInstallerTab.class.getResourceAsStream(path);
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
        Font.loadFont(PackageInstallerTab.class.getResourceAsStream("/font/Ubuntu.ttf"), 14);
        Font.loadFont(PackageInstallerTab.class.getResourceAsStream("/font/Ubuntu-Bold.ttf"), 14);
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
	
	private static void downloadAndRunExe(String url, String fileName, ProgressBar progressBar, Label progressLabel) {
	    // Show a confirmation dialog
	    int userChoice = JOptionPane.showConfirmDialog(null, 
	        "Do you want to download and run " + fileName + "?", 
	        "Confirm Download", 
	        JOptionPane.YES_NO_OPTION);

	    if (userChoice == JOptionPane.YES_OPTION) {
	        // User confirmed the download, proceed with the download
	        try {
	        URL downloadUrl = new URL(url);
	        URLConnection connection = downloadUrl.openConnection();

	        try (InputStream in = connection.getInputStream();
	             ReadableByteChannel rbc = Channels.newChannel(in);
	             FileOutputStream fos = new FileOutputStream(fileName)) {
	            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

	            Platform.runLater(() -> {
	                progressBar.setProgress(1.0);
	                progressLabel.setText("100%");
	            });

	            waitForFileAccess(fileName);

	            if (Desktop.isDesktopSupported()) {
	                Desktop.getDesktop().open(new File(fileName));
	            } else {
	                System.out.println("Desktop is not supported, unable to open the file.");
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Error during file download or execution: " + e.getMessage());
	    }
	}
	}
	
	private static void executeExeFile(String filePath) {
	    try {
	        ProcessBuilder processBuilder = new ProcessBuilder(filePath);
	        processBuilder.start();
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Error executing the file: " + e.getMessage());
	    }
	}

	private static boolean isFileLocked(File file) {
	    try (@SuppressWarnings("resource")
		FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel()) {
	        // Try acquiring the lock without blocking
	        FileLock lock = fileChannel.tryLock();
	        if (lock == null) {
	            return true; // File is locked
	        }
	        lock.release(); // Release the lock immediately
	        return false; // File is not locked
	    } catch (IOException e) {
	        return true; // Assume locked if an IOException occurred
	    }
	}

	
	private static void waitForFileAccess(String fileName) {
	    final int MAX_ATTEMPTS = 5;
	    final long WAIT_TIME_MS = 2000; // 2 seconds

	    for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
	        File file = new File(fileName);

	        // Check if the file can be renamed (a way to check if it is in use)
	        File tempFile = new File(fileName + ".tmp");
	        if (file.renameTo(tempFile)) {
	            // Rename it back and break the loop as file is not in use
	            tempFile.renameTo(file);
	            break;
	        }

	        // Wait for a while before trying again
	        try {
	            Thread.sleep(WAIT_TIME_MS);
	        } catch (InterruptedException ex) {
	            Thread.currentThread().interrupt();
	            throw new RuntimeException("Interrupted while waiting for file access", ex);
	        }
	    }
	}


	public static VBox createpkiTab(Stage primaryStage) {
		TextArea terminalPreview = new TextArea();
	    terminalPreview.setEditable(false);
	    terminalPreview.setVisible(false); // Initially set to invisible
	    terminalPreview.setStyle("-fx-control-inner-background: black; -fx-text-fill: green;");
	    terminalPreview.setPrefWidth(200); // Set the preferred width
	    terminalPreview.setPrefHeight(307.5);

        // Créer un rectangle grand pour l'arrière-plan
        Rectangle backgroundRectangle = new Rectangle();
        backgroundRectangle.setFill(Color.LIGHTGRAY); // Couleur de l'arrière-plan

        Label ofatitleLabel = new Label("Package Installer");
        ofatitleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #333333;");
        ofatitleLabel.setTextAlignment(TextAlignment.CENTER);

        ImageView iconView = loadIcon("/images/packageInstaller.png");

        Label buttonDescription = new Label("\r\n"
        		+ "La méthode la plus rapide et efficace pour installer des applications en seulement deux clics, c'est maintenant réalisable grâce à cet outil innovant qui n'est actuellement pas disponible.");
        Label logsinfo = new Label("InfoConsole");

        // Liste des applications
        ListView<String> appsList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(
            "Installer l'extention" // Ajoutez d'autres éléments ici si nécessaire
        );
        appsList.setItems(items);

        appsList.setOnMouseClicked(event -> {
            Stage downloadStage = new Stage();
            downloadStage.setTitle("Téléchargement de fichier");
            downloadStage.setAlwaysOnTop(true);

            ProgressBar progressBar = new ProgressBar(0);
            Label progressLabel = new Label("0%");

            VBox downloadLayout = new VBox(20);
            downloadLayout.setAlignment(Pos.CENTER);
            downloadLayout.getChildren().addAll(progressBar, progressLabel);

            Scene downloadScene = new Scene(downloadLayout, 300, 100);
            downloadStage.setScene(downloadScene);
            downloadStage.show();

            // Exemple d'URL et de nom de fichier (à remplacer par les vrais)
            String downloadUrl = "https://github.com/marticliment/UniGetUI/releases/download/3.1.0/UniGetUI.Installer.exe";
            String fileName = "uigetui.exe";

            // Lancer le téléchargement et la mise à jour de la barre de progression dans un nouveau Thread
            new Thread(() -> {
            	downloadAndRunExe(downloadUrl, fileName, progressBar, progressLabel);
            }).start();
        });
	
        


        // Déclaration de la variable textFlow
        Node textFlow = new TextFlow(); // Assurez-vous que cette ligne est bien présente

        
        // Animation d'agrandissement
        ScaleTransition stGrow = new ScaleTransition(Duration.millis(200));
        stGrow.setToX(1.1);
        stGrow.setToY(1.1);

        // Animation de rétrécissement
        ScaleTransition stShrink = new ScaleTransition(Duration.millis(200));
        stShrink.setToX(1.0);
        stShrink.setToY(1.0);
        

        // Créer une VBox pour contenir tous les éléments, y compris le rectangle d'arrière-plan
        VBox mainLayout = new VBox(10, iconView, ofatitleLabel, buttonDescription, appsList, terminalPreview, textFlow);
        mainLayout.setAlignment(Pos.CENTER);

        // Ajouter le rectangle en arrière-plan
        mainLayout.getChildren().add(0, backgroundRectangle);

        terminalPreview.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(terminalPreview, Priority.ALWAYS);

        // Créer une VBox pour le terminalPreview
        VBox terminalBox = new VBox(10, mainLayout, terminalPreview, appsList);
        terminalBox.setAlignment(Pos.BOTTOM_CENTER);
        
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
	

}
