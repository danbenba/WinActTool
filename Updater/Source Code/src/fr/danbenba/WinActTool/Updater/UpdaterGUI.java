package fr.danbenba.WinActTool.Updater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings({"serial", "unused"})
public class UpdaterGUI extends JFrame {
    static final String Updater_Version = "1.6";
    private JLabel loadingLabel;
    private JProgressBar progressBar;
    private JButton cancelButton;
    private static String downloadUrl; // Variable globale pour l'URL de téléchargement

    public UpdaterGUI() {
        languageSelection(); // Appeler la méthode de sélection de langue
    }

    private void initDownloadUI() {
        setTitle("WinActTool - Updater");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 120);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null); // Centrer la fenêtre

        loadingLabel = new JLabel("Downloading Updates...");
        add(loadingLabel);

        // Barre de progression standard
        this.progressBar = new JProgressBar(0, 100);
        this.progressBar.setPreferredSize(new Dimension(279, 24));
        this.progressBar.setForeground(new Color(6, 176, 37)); // Couleur verte
        this.progressBar.setStringPainted(true);
        this.progressBar.setBorderPainted(false);
        add(this.progressBar);

        cancelButton = new JButton("Hide");
        cancelButton.setPreferredSize(new Dimension(70, 20));
        add(cancelButton);

        cancelButton.addActionListener(e -> dispose()); // Fermer la fenêtre

        setVisible(true);
    }

    private void languageSelection() {
        JFrame languageFrame = new JFrame("Select Language");
        languageFrame.setSize(320, 120);
        languageFrame.setLayout(new FlowLayout());
        languageFrame.setLocationRelativeTo(null); // Centrer la fenêtre

        JLabel languageLabel = new JLabel("Choose a language:");
        languageFrame.add(languageLabel);

        // Liste déroulante pour la sélection de la langue
        String[] languages = {"Français", "English"};
        JComboBox<String> languageList = new JComboBox<>(languages);
        languageFrame.add(languageList);

        JButton nextButton = new JButton("Next");
        languageFrame.add(nextButton);

        nextButton.addActionListener(e -> {
            // Définir l'URL en fonction de la langue sélectionnée
            downloadUrl = languageList.getSelectedItem().equals("Français") ?
                "https://github.com/danbenba/WinActTool/releases/download/lastedversion/WinActTool-fr-FR.jar" :
                "https://github.com/danbenba/WinActTool/releases/download/lastedversion/WinActTool-en-US.jar";

            languageFrame.dispose(); // Fermer la fenêtre de sélection
            initDownloadUI(); // Initialiser l'interface de téléchargement
            startDownload(); // Commencer le téléchargement
        });

        languageFrame.setVisible(true);
    }

    private void startDownload() {
        new Thread(() -> {
            try {
                URL url = new URL(downloadUrl);
                URLConnection connection = url.openConnection();
                int fileSize = connection.getContentLength();

                BufferedInputStream in = new BufferedInputStream(url.openStream());
                FileOutputStream fos = new FileOutputStream("WinActTool.jar");
                BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

                byte[] data = new byte[1024];
                int totalDataRead = 0;
                int i;
                while ((i = in.read(data, 0, 1024)) >= 0) {
                    totalDataRead += i;
                    bout.write(data, 0, i);
                    int progress = calculateProgress(totalDataRead, fileSize);
                    SwingUtilities.invokeLater(() -> updateProgress(progress));
                }

                bout.close();
                in.close();

                dispose(); // Fermer la fenêtre de chargement

                Runtime.getRuntime().exec("java -jar WinActTool.jar");
            } catch (FileNotFoundException e) {
                SwingUtilities.invokeLater(() -> {
                    loadingLabel.setText("Erreur de téléchargement : Fichier non trouvé");
                    progressBar.setVisible(false); // Masquer la barre de progression
                    cancelButton.setText("Fermer");
                });
                e.printStackTrace();
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    loadingLabel.setText("Erreur de téléchargement : Erreur d'entrée/sortie");
                    progressBar.setVisible(false); // Masquer la barre de progression
                    cancelButton.setText("Fermer");
                });
                e.printStackTrace();
            }
        }).start();
    }


    public void updateProgress(int value) {
        progressBar.setValue(value);
        if (value >= 90) {
            setTitle("Launching...");
            loadingLabel.setText("Launching...");
        } else if (value >= 40) {
            setTitle("Extracting...");
            loadingLabel.setText("Extracting...");
        }
    }

    private static int calculateProgress(int totalDataRead, int fileSize) {
        double percent = totalDataRead * 100.0 / fileSize;
        return (int) Math.min(percent, 100);
    }

    public static void main(String[] args) {
        // Définir l'apparence et la convivialité sur celle native du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new UpdaterGUI()); // Créer et afficher la fenêtre de mise à jour
    }
}
