package fr.danbenba.WinActTool.Installer;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("serial")
public class Installer extends JFrame {
    private JLabel loadingLabel;
    private JProgressBar progressBar;
    private JButton cancelButton;

    public Installer() {
        setTitle("WinActTool - Installer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 114);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null); // Center the window

        loadingLabel = new JLabel("Téléchargement d'une libraire...");
        add(loadingLabel);

        // Customized progress bar with rounded corners
        progressBar = new JProgressBar(0, 100) {
            @Override
            public void updateUI() {
                super.updateUI();
                setUI(new BasicProgressBarUI() {
                    private final int arcSize = 10;

                    @Override
                    protected void paintDeterminate(Graphics g, JComponent c) {
                        if (!(g instanceof Graphics2D)) {
                            return;
                        }
                        
                        Insets b = progressBar.getInsets(); // Area for border
                        int barRectWidth = progressBar.getWidth() - (b.right + b.left);
                        int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
                        if (barRectWidth <= 0 || barRectHeight <= 0) {
                            return;
                        }
                        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        // Draw the background
                        g2d.setColor(progressBar.getBackground());
                        g2d.fillRoundRect(b.left, b.top, barRectWidth, barRectHeight, arcSize, arcSize);

                        // Draw the progress bar
                        g2d.setColor(progressBar.getForeground());
                        g2d.fillRoundRect(b.left, b.top, amountFull, barRectHeight, arcSize, arcSize);

                        if (progressBar.isStringPainted()) {
                            paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
                        }
                    }
                });
            }
        };
        progressBar.setPreferredSize(new Dimension(280, 20));
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 110, 0)); // Green color
        progressBar.setBackground(Color.WHITE);
        add(progressBar);

        cancelButton = new JButton("Hide");
        cancelButton.setPreferredSize(new Dimension(70, 20));
        add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        setVisible(true);
    }

    public void updateProgress(int value) {
        progressBar.setValue(value);
        if (value >= 60) {
            loadingLabel.setText("Lancement en cours...");
        } else if (value >= 30) {
            loadingLabel.setText("Vérification du téléchargement...");
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
            Installer window = new Installer();

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
