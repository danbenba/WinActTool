package fr.danbenba.WinActTool;

import java.util.Scanner;

import javafx.scene.Node;

public class Terminal {

    public static Node main(String[] args) {
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
        System.out.println("Terminal started. Type '/restart --admin_override' to reboot the software.");

        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine();

            if ("/admin_override".equalsIgnoreCase(command)) {
                rebootSoftware();
                break;
            } else {
                System.out.println("Unknown command: " + command);
            }
        }
		return null;
    }

    private static void rebootSoftware() {
        System.out.println("Rebooting software...");

        // Example logic for rebooting the software
        try {
            // Simulate some reboot operations
            System.out.println("Closing resources...");
            Thread.sleep(1000); // Simulate some delay

            System.out.println("Restarting application...");
            Thread.sleep(1000); // Simulate application restart

            // In a real application, you might call a method to restart your main application here
            // For example: Application.restart();

            System.out.println("Software rebooted successfully.");
        } catch (InterruptedException e) {
            System.err.println("Reboot interrupted: " + e.getMessage());
        }
        System.exit(0);
    }
}
