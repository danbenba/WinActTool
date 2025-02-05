package fr.danbenba.WinActTool;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SplashScreen {

    public void splashScreen(Stage primaryStage) {
        // Copier l'image dans le dossier temporaire
        Path tempImagePath = copyImageToTemp();

        // Affichage de l'écran de démarrage
        if (tempImagePath != null) {
            showSplashScreen(primaryStage, tempImagePath.toString());
        }
    }

    private Path copyImageToTemp() {
        try {
            // Chemin de l'image dans les ressources
            String resourcePath = "/images/splashscreen.png";

            // Chemin du dossier temporaire
            String tempDirectory = System.getProperty("java.io.tmpdir");
            Path tempImagePath = Paths.get(tempDirectory, "WinActTool", "resources", "splashscreen.png");

            // Créer les dossiers s'ils n'existent pas
            Files.createDirectories(tempImagePath.getParent());

            // Copier l'image
            try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                if (is == null) {
                    throw new IllegalArgumentException("Fichier non trouvé : " + resourcePath);
                }
                Files.copy(is, tempImagePath, StandardCopyOption.REPLACE_EXISTING);
                return tempImagePath;
            }
        } catch (FileSystemException e) {
            // Affichage du message d'erreur en rouge si le fichier est utilisé par un autre processus
            String red = "\033[31m"; // Code ANSI pour le texte en rouge
            String reset = "\033[0m"; // Réinitialiser la couleur
            System.out.println(red + "[WinActTool] ERROR : WinActTool est déjà ouvert." + reset);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showSplashScreen(Stage primaryStage, String imagePath) {
        ImageView splashImage;
        try {
            splashImage = new ImageView(new Image(new FileInputStream(imagePath), 600, 400, true, true));
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de chargement");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de charger l'image de démarrage.");
            alert.showAndWait();
            Platform.exit();
            return;
        }

        StackPane root = new StackPane(splashImage);
        root.setStyle("-fx-background-color: TRANSPARENT;");
        Scene scene = new Scene(root, 1400, 1200);
        scene.setFill(null);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
        manageTransitions(primaryStage);
    }

    private void manageTransitions(Stage primaryStage) {
        // Première PauseTransition
        PauseTransition delayForLoadingMenu = new PauseTransition(Duration.seconds(2));
        delayForLoadingMenu.setOnFinished(e -> {
            LoadingMenu.main(null);
            ProtectionClass.checkForLegalCopy();
            ProtectionClass.checkForLegalCopyForCreator();

        });

        // Deuxième PauseTransition
        PauseTransition delayForMainAndUpdates = new PauseTransition(Duration.seconds(4));
        delayForMainAndUpdates.setOnFinished(e -> {
            primaryStage.close();
            ActivationKeyManager.checkAndHandleActivationKey(primaryStage);
            //UpdateChecker.checkForUpdates(primaryStage);
            main.launcherMain(new Stage());
        });

        // Démarrage des transitions
        delayForLoadingMenu.play();
        delayForMainAndUpdates.play();
    }
}
