package fr.danbenba.WinActTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class LoadingMenu extends JFrame {
    private JLabel loadingLabel;
    private JProgressBar progressBar;

    public LoadingMenu() {
        setTitle("WinActTool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 100);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null); // Centre la fenêtre

        loadingLabel = new JLabel("Extracting...");
        add(loadingLabel);
        
        setAlwaysOnTop(true); // Ajoutez cette ligne pour garder la fenêtre au premier plan
        
        // Initialisation de la barre de progression
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(279, 24));
        progressBar.setBorderPainted(false);
        add(progressBar);

        // Timer pour mettre à jour la barre de progression
        Timer timer = new Timer(0010, new ActionListener() {
            private int progress = 0;
            private boolean paused = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paused) {
                    if (progress < 30) {
                        progress += 2; 
                    } else {
                        paused = true;
                        try {
                            Thread.sleep(3); // Pause pendant 3 millisecondes
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        progress = 90; // Saute directement à 90
                    }
                    updateProgress(progress);
                }

                if (progress >= 90) {
                    progress += 10; // Avance de 90 à 100
                }

                if (progress > 100) {
                    ((Timer)e.getSource()).stop();
                    dispose(); // Ferme la fenêtre une fois la progression terminée
                }
            }
        });
        timer.start();

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

    public static void main(String[] args) {
        // Appliquer le look and feel du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoadingMenu();
            }
        });
    }
}
