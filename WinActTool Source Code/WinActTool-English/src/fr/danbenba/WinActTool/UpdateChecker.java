package fr.danbenba.WinActTool;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.scene.Scene;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Optional;
import java.util.Scanner;

import javax.swing.UIManager;

public class UpdateChecker {

	private static final String CURRENT_VERSION = Storage.CURRENT_VERSION;
	static final String Updater_Version = Storage.Updater_Version;
    private static final String DOWNLOAD_URL = Storage.getDownloadUrl();
    private static final String VERSION_CHECK_URL = Storage.getVersionCheckUrl();

    private static void showProgressStage(String initialMessage) {
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle("Update");
        
        
        
        // Charger et définir l'icône
        Image icon = new Image(UpdateChecker.class.getResourceAsStream("/images/updateicon.png"));
        progressStage.getIcons().add(icon);
        
        // Désactiver la redimensionnabilité de la fenêtre
        progressStage.setResizable(false);


        Text message = new Text(initialMessage);
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        Text progressPercentage = new Text("0%");
        VBox vBox = new VBox(message, progressBar, progressPercentage); // Ajout du Text à VBox
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        Scene scene = new Scene(vBox, 300, 100);
        progressStage.setScene(scene);
        
        
        // Centrer la fenêtre sur l'écran
        double centerXPosition = Screen.getPrimary().getBounds().getMinX() + Screen.getPrimary().getBounds().getWidth() / 2 - 150; // 150 est la moitié de la largeur de la fenêtre
        double centerYPosition = Screen.getPrimary().getBounds().getMinY() + Screen.getPrimary().getBounds().getHeight() / 2 - 50; // 50 est la moitié de la hauteur de la fenêtre
        progressStage.setX(centerXPosition);
        progressStage.setY(centerYPosition);

        progressStage.show();
    }
    
    @SuppressWarnings("unused")
	private static void downloadNewVersion(String urlString, String savePath, Stage progressStage, ProgressBar progressBar, Text progressText, Text progressPercentage) throws IOException {
        // Commencez le téléchargement
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        long fileSize = httpURLConnection.getContentLengthLong();
        BufferedInputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        long totalBytesRead = 0;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
            totalBytesRead += bytesRead;
            final double progress = totalBytesRead / (double) fileSize;
            Platform.runLater(() -> {
                progressPercentage.setText(String.format("", progress * 100));
            });
         // Création d'une Timeline pour simuler la progression
            Timeline timeline = new Timeline();

            for (int i = 0; i <= 9; i++) {
                final int second = i;
                KeyFrame frame = new KeyFrame(Duration.seconds(i), ae -> {
                    double simulatedProgress = second / 9.0;
                });
                timeline.getKeyFrames().add(frame);
            }

            // Lancement de la Timeline
            timeline.play();

            timeline.setOnFinished(e -> {
                try {
                    replaceOldVersion(savePath, "/WinActTool.jar");
                    Platform.exit();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                progressStage.close();
            });

        }
        in.close();
        fileOutputStream.close();
        

        // Créez un Timeline pour simuler le délai de la barre de progression
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(5),
            ae -> {
                progressText.setText("Launching...");
                try {
                    replaceOldVersion(savePath, "/WinActTool.jar");
                    Platform.exit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                progressStage.close();
            }));

        timeline.play();
    }

    private static void replaceOldVersion(String newPath, String oldPath) throws IOException {
        Files.deleteIfExists(Paths.get(oldPath));
        Runtime.getRuntime().exec("java -jar " + newPath);
    }

    public static void checkForUpdates(Stage primaryStage) {
        try {
            URL url = new URL(VERSION_CHECK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            Scanner scanner = new Scanner(url.openStream());
            String onlineVersion = scanner.nextLine();
            scanner.close();

            if (!CURRENT_VERSION.equals(onlineVersion)) {
                Platform.runLater(() -> {
            
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("WinActTool - AutoUpdate");
                    alert.setHeaderText(null);
                    alert.setContentText("An update is available. The download will begin.");

                    ButtonType buttonYes = new ButtonType("Ok");
                    alert.getButtonTypes().setAll(buttonYes);

                    Button okButton = (Button) alert.getDialogPane().lookupButton(buttonYes);
                    okButton.setStyle("-fx-font-size: 14px; -fx-background-color: lightblue;");
                    
                    // Écouteur pour quand la souris entre sur le bouton
                    okButton.setOnMouseEntered(e -> okButton.setStyle("-fx-font-size: 14px; -fx-background-color: darkgray;"));

                    // Écouteur pour quand la souris quitte le bouton
                    okButton.setOnMouseExited(e -> okButton.setStyle("-fx-font-size: 14px; -fx-background-color: lightblue;"));
                    
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();

                    // Ajout d'un écouteur pour l'événement de fermeture de la fenêtre
                    alertStage.setOnCloseRequest(e -> {
                    	// Exécuter System.exit quand la fenêtre est fermée
                    	System.exit(0);
                    });

                    alertStage.getIcons().add(new Image("/images/updateicon.png"));

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == buttonYes) {
                        try {
                            String saveDir = System.getProperty("user.dir") + "/";
                            new File(saveDir).mkdirs();
                            String savePath = saveDir + "WinActTool.jar";
                            
                            Stage progressStage = new Stage();
                            Text progressText = new Text("Downloading\n Do not close this window.");
                            showProgressStage("Downloading...");
                            
                         // Ajout d'un écouteur pour l'événement de fermeture de la fenêtre
                            progressStage.setOnCloseRequest(e -> {
                            	// Exécuter System.exit quand la fenêtre est fermée
                            	System.exit(0);
                            });
                            
							downloadNewVersion(DOWNLOAD_URL, savePath, progressStage, null, progressText, progressText);
                        } catch (Exception e) {
                        	System.exit(0);
                            e.printStackTrace();
                        }
                    }
                });
            } else {
            	
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
    