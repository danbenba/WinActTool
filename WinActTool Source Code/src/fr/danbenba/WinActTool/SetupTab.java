package fr.danbenba.WinActTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.util.Arrays;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SetupTab {

    public static VBox createSetupTab() {
        ComboBox<String> versionSelection = new ComboBox<>();
        versionSelection.getItems().addAll("Version Basic", "Version Pro");

        ComboBox<String> architectureSelection = new ComboBox<>();
        architectureSelection.getItems().addAll("Architecture 32-bit", "Architecture 64-bit");

        Button launchButton = new Button("Installer");

        launchButton.setOnAction(e -> {
            String version = versionSelection.getValue();
            String architecture = architectureSelection.getValue();

            if (version == null || architecture == null) {
                showAlert("Veuillez sélectionner la version et l'architecture avant de continuer.");
                return;
            }

            Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de l'installation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Voulez-vous vraiment installer " + version + " pour " + architecture + " ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Path installDir = Paths.get(System.getProperty("java.io.tmpdir"), "Oconfig");
                    LoadingMenu loadingMenu = new LoadingMenu();

                    if (!Files.exists(installDir)) {
                        new Thread(() -> {
                            try {
                                downloadAndExtractFile(loadingMenu);
                                Platform.runLater(() -> showAlert("Téléchargement terminé. Veuillez patienter pendant l'installation."));
                                launchBatchFile(version, architecture);
                                Platform.runLater(() -> showAlert("Installation terminée avec succès."));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                Platform.runLater(() -> showAlert("Une erreur est survenue pendant l'installation."));
                            } finally {
                                loadingMenu.dispose();
                            }
                        }).start();
                    } else {
                        showAlert("L'installation est déjà effectuée. Lancement du fichier batch...");
                        new Thread(() -> {
                            try {
                                launchBatchFile(version, architecture);
                                Platform.runLater(() -> showAlert("Installation terminée avec succès."));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                Platform.runLater(() -> showAlert("Une erreur est survenue pendant l'installation."));
                            }
                        }).start();
                    }
                } else {
                    showAlert("Installation annulée.");
                }
            });
        });

        VBox setupLayout = new VBox(10, versionSelection, architectureSelection, launchButton);
        setupLayout.setAlignment(Pos.CENTER);
        return setupLayout;
    }

    private static void downloadAndExtractFile(LoadingMenu loadingMenu) throws IOException {
        URL downloadUrl = new URL("https://github.com/danbenba/WinActTool/raw/refs/heads/main/Packages/Oconfig.zip");
        Path tempZipFile = Files.createTempFile("Oconfig", ".zip");
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "Oconfig");

        if (Files.exists(tempDir)) {
            return;
        }

        Files.createDirectories(tempDir);

        try (InputStream inputStream = downloadUrl.openStream()) {
            long totalBytes = downloadUrl.openConnection().getContentLengthLong();
            byte[] buffer = new byte[8192];
            int bytesRead;
            long bytesDownloaded = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                Files.write(tempZipFile, Arrays.copyOf(buffer, bytesRead), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                bytesDownloaded += bytesRead;
                int progress = (int) ((bytesDownloaded * 100) / totalBytes);
                SwingUtilities.invokeLater(() -> loadingMenu.updateProgress(progress));
            }
        }

        // Extract the ZIP file
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(tempZipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path extractedFile = tempDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(extractedFile);
                } else {
                    Files.createDirectories(extractedFile.getParent()); // Ensure parent directories are created
                    Files.copy(zipInputStream, extractedFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } finally {
            Files.deleteIfExists(tempZipFile);
        }
    }

    private static void launchBatchFile(String version, String architecture) throws IOException {
        String batchFileName = getBatchFileName(version, architecture);
        // Chemin vers le fichier batch dans les ressources
        URL resourceUrl = SetupTab.class.getResource("/files/" + batchFileName);

        if (resourceUrl == null) {
            throw new IllegalArgumentException("Le fichier batch correspondant n'a pas été trouvé : " + batchFileName);
        }

        // Créer un fichier temporaire pour exécuter le batch
        Path tempBatchFile = Files.createTempFile(null, ".bat");
        try (InputStream inputStream = resourceUrl.openStream()) {
            Files.copy(inputStream, tempBatchFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // Construire et démarrer le processus pour exécuter le batch
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", tempBatchFile.toString());
        processBuilder.inheritIO();
        Process process = processBuilder.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Le processus batch a été interrompu", e);
        } finally {
            Files.delete(tempBatchFile); // Nettoyer le fichier temporaire
        }
    }

    private static String getBatchFileName(String version, String architecture) {
        // Déterminer le nom du fichier batch en fonction de la version et de l'architecture
        if ("Version Basic".equals(version)) {
            if ("Architecture 32-bit".equals(architecture)) {
                return "Setup32Basic.bat";
            } else if ("Architecture 64-bit".equals(architecture)) {
                return "Setup64Basic.bat";
            }
        } else if ("Version Pro".equals(version)) {
            if ("Architecture 32-bit".equals(architecture)) {
                return "Setup32.bat";
            } else if ("Architecture 64-bit".equals(architecture)) {
                return "Setup64.bat";
            }
        }
        throw new IllegalArgumentException("Combinaison de version et d'architecture non valide : " + version + ", " + architecture);
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    URL url = SetupTab.class.getResource("/style.css");
}

@SuppressWarnings("serial")
class LoadingMenuSetup extends JFrame {
    private JLabel loadingLabel;
    private JProgressBar progressBar;

    public LoadingMenuSetup() {
        setTitle("Osetup - Downloading Files...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 100);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null); // Centre la fenêtre

        loadingLabel = new JLabel("Downloading files...");
        add(loadingLabel);

        setAlwaysOnTop(true); // Ajoutez cette ligne pour garder la fenêtre au premier plan

        // Initialisation de la barre de progression
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(279, 24));
        progressBar.setBorderPainted(false);
        add(progressBar);

        setVisible(true);
    }

    public void updateProgress(int value) {
        progressBar.setValue(value);
        if (value >= 60) {
            loadingLabel.setText("Extracting...");
        } else if (value >= 30) {
            loadingLabel.setText("Extracting...");
        }
    }
}
