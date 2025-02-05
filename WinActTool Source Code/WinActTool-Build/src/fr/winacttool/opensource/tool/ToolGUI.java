package fr.winacttool.opensource.tool;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.io.*;

public class ToolGUI {
    private static JTextArea previewArea;
    static final String Script_Main = "D:\\Tools\\Java\\WinActTool\\Script\\WinActTool.bat";
    
    public static void main(String[] args) {
        // Set the look and feel to the system's default to mimic Windows style
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("WinActTool OpenSource ToolKit GUI");
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        
        // Bouton 1
        JButton button1 = new JButton("Build");
        button1.setBounds(10, 20, 120, 25);
        button1.addActionListener(e -> executeCommand(Script_Main + " --build"));
        panel.add(button1);
        
        // Bouton 2
        JButton button2 = new JButton("Run Advanced");
        button2.setBounds(150, 20, 140, 25);
        button2.addActionListener(e -> executeCommand(Script_Main + " --run"));
        panel.add(button2);
        
        // Bouton 3
        JButton button3 = new JButton("Clean Debug");
        button3.setBounds(300, 20, 120, 25);
        button3.addActionListener(e -> executeCommand(Script_Main + " --clean-debug"));
        panel.add(button3);
        
        // Bouton 4
        JButton button4 = new JButton("Clean Build");
        button4.setBounds(10, 50, 120, 25);
        button4.addActionListener(e -> executeCommand(Script_Main +" --clean-build"));
        panel.add(button4);

        // Bouton 5
        JButton button5 = new JButton("About");
        button5.setBounds(150, 50, 120, 25);
        button5.addActionListener(e -> executeCommand(Script_Main + " --version"));
        panel.add(button5);
        
        // Bouton 6
        JButton button6 = new JButton("Edit Script");
        button6.setBounds(300, 50, 120, 25);
        button6.addActionListener(e -> executeCommand("start D:\\\\Tools\\\\Java\\\\WinActTool\\\\SublimeText\\\\sublime_text.exe " + Script_Main));
        panel.add(button6);
        
        // Bouton 7
        JButton button7 = new JButton("Open Explorer At Folder of Project");
        button7.setBounds(450, 20, 120, 25);
        button7.addActionListener(e -> executeCommand("echo Opening... && explorer D:\\Desktop\\danbenba\\Software\\Development\\Java\\WinActTool\\WinActTool && echo Opened"));
        panel.add(button7);
        
        // Bouton 8
        JButton button8 = new JButton("Build English");
        button8.setBounds(430, 50, 120, 25);
        button8.addActionListener(e -> executeCommand(Script_Main + " --benus"));
        panel.add(button8);
        

        // Label "Version / by"
        JLabel poweredByLabel = new JLabel("Version 1.0\n "
        		+ "\n"
        		+ "Powered by danbenba");
        poweredByLabel.setForeground(Color.GRAY); // Définir la couleur du texte en gris
        poweredByLabel.setFont(new Font("Arial", Font.PLAIN, 10)); // Définir la police et la taille

        // Positionner le label en bas de la fenêtre
        int labelWidth = 200;
        int labelHeight = 20;
        int xPosition = (700 - labelWidth) / 2; // Centrer horizontalement
        int yPosition = 400 - labelHeight - 10; // 10 pixels au-dessus du bas de la fenêtre
        poweredByLabel.setBounds(xPosition, yPosition, labelWidth, labelHeight);

        panel.add(poweredByLabel);

        // Preview area
        previewArea = new JTextArea();
        previewArea.setBounds(10, 100, 660, 250);
        previewArea.setEditable(false);
        previewArea.setVisible(false);
        JScrollPane scrollPane = new JScrollPane(previewArea);
        scrollPane.setBounds(10, 100, 660, 250);
        panel.add(scrollPane);
    }

    private static void executeCommand(String command) {
        new Thread(() -> {
            try {
            	previewArea.setVisible(true);
                String cmdCommand = "cmd.exe /c " + command;
                Process process = Runtime.getRuntime().exec(cmdCommand);

                // Lecture du flux de sortie standard
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                // Lecture du flux d'erreur standard
                BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                SwingUtilities.invokeLater(() -> previewArea.setText("")); // Effacer le texte précédent

                String inputLine;
                while ((inputLine = stdInput.readLine()) != null) {
                    String finalInputLine = inputLine;
                    SwingUtilities.invokeLater(() -> previewArea.append(finalInputLine + "\n")); // Ajouter la sortie à la zone de prévisualisation
                }

                String errorLine;
                while ((errorLine = stdError.readLine()) != null) {
                    String finalErrorLine = errorLine;
                    SwingUtilities.invokeLater(() -> previewArea.append("ERROR: " + finalErrorLine + "\n")); // Ajouter les erreurs à la zone de prévisualisation
                }

                int exitCode = process.waitFor();
                SwingUtilities.invokeLater(() -> previewArea.append("Exited with code: " + exitCode + "\n"));
            } catch (IOException | InterruptedException e) {
                SwingUtilities.invokeLater(() -> previewArea.append("Error: " + e.getMessage() + "\n")); // Afficher l'erreur dans la zone de prévisualisation
            }
        }).start();
    }
}
