package fr.danbenba.WinActTool.protection.installator;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import javax.swing.JProgressBar;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings({ "serial", "unused" })
public class Main extends JFrame {
	static final String Updater_Version = "1.6";
    private JLabel loadingLabel;
    private JProgressBar progressBar;
    private JButton cancelButton;

    public Main() {
        setTitle("WinActTool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 100);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null); // Center the window

        loadingLabel = new JLabel("Téléchargement d'une libraire...");
        add(loadingLabel);
          
        this.progressBar = new JProgressBar(0, 100);
        this.progressBar.setPreferredSize(new Dimension(280, 25));
        add(this.progressBar);

        this.progressBar.setBorderPainted(false); // Désactiver le dessin de la bordure
        add(this.progressBar);

        cancelButton = new JButton("Hide");
        cancelButton.setPreferredSize(new Dimension(70, 20));
        //add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        setVisible(true);
    }

    public void updateProgress(int value) {
        if (value == 0) {
            progressBar.setValue(30); // Commencez directement à 30%
            loadingLabel.setText("Loading...");
        } else {
            progressBar.setValue(value);
            if (value >= 60) {
                loadingLabel.setText("Loading...");
            } else if (value >= 30) {
                loadingLabel.setText("Loading...");
            }
        }
    }

    private static int calculateProgress(int totalDataRead, int fileSize) {
        double percent = totalDataRead * 100.0 / fileSize;
        return (int) Math.min(percent, 100);
    }

    public static void main(String[] args) {
        // Set the look and feel to the system's native look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Main window = new Main();

            new Thread(() -> {
                try {
                    String urlString = "https://github.com/danbenba/WinActTool/releases/download/lastedversion/WinActTool.jar";
                    URL url = new URL(urlString);
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
                        SwingUtilities.invokeLater(() -> window.updateProgress(progress));
                    }

                    bout.close();
                    in.close();

                    window.dispose(); // Close the loading window

                    Runtime.getRuntime().exec("java -jar WinActTool.jar");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
