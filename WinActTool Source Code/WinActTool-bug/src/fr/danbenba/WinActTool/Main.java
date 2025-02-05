package fr.danbenba.WinActTool;

import fr.danbenba.WinActTool.Storage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

@SuppressWarnings("unused")
public class Main extends Application {

	// Initialize your UI here
    StackPane root = new StackPane();
	
    
	static final String CURRENT_VERSION = Storage.CURRENT_VERSION;
    static final String CREATOR_LABEL = Storage.CREATOR_LABEL;
    static final String Version_URL = Storage.getVersionCheckUrl();

    @Override
    public void start(Stage primaryStage) {
    	AboutTab.setPrimaryStage(primaryStage); // Ajoutez cette ligne
        VBox aboutTab = AboutTab.createAboutTab();
    	AboutTab.setHostServices(this.getHostServices());
        AboutTab.testHostServices();
    	if (!isRunningAsAdmin()) {
            try {
                relaunchWithAdminRights();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
    	ProtectionClass.checkForLegalCopy();
    	ProtectionClass.checkForLegalCopyForCreator();
        UpdateChecker.checkForUpdates(primaryStage);
    	//ProtectionClass.launchWinActTool();
        AboutTab.checkForLegalCopy();
        }
    }

    private static boolean isRunningAsAdmin() {
        try {
            // Lancer une commande pour vérifier si l'utilisateur actuel a des privilèges d'administrateur
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "net session");
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Lire la sortie de la commande
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains("access is denied")) {
                    return false;
                }
            }

            // Attendre que la commande se termine
            process.waitFor();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void relaunchWithAdminRights() throws IOException {
        String javaBinaryPath = System.getProperty("java.home") + "/bin/java";
        String classpath = System.getProperty("java.class.path");
        String className = Main.class.getName();

        String cmd = "cmd /c runas /noprofile /user:Administrateur \"" + javaBinaryPath + " -cp " + classpath + " " + className + "\"";
        Runtime.getRuntime().exec(cmd);
        System.exit(0);
    }
    
    static void launcherMain(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        Tab tabActWin = new Tab("Windows Activation", WindowsActivatorTab.createCrackerTab()); // Onglet "WA" créé ici
        Tab tabCracker = new Tab("Office Activator", OfficeActivatorTab.createCrackerTab(primaryStage)); // Onglet "Activator" créé ici
        Tab tabUniEdge = new Tab("Other Tools", ToolsTab.CreateToolstab(primaryStage)); // Onglet "Tools" créé ici
        Tab tabAbout = new Tab("About", AboutTab.createAboutTab()); // Onglet "About" créé ici

        // Mettre les onglets non fermables
        tabActWin.setClosable(false);
        tabCracker.setClosable(false);
        tabUniEdge.setClosable(false);
        tabAbout.setClosable(false); // Assurez-vous que l'onglet "About" est non fermable

        // Ajouter l'écouteur pour l'animation à chaque onglet
        for (Tab tab : new Tab[] { tabActWin, tabCracker, tabUniEdge, tabAbout }) {
            tab.setOnSelectionChanged(event -> {
                if (tab.isSelected()) {
                    animateTabChange(tab);
                }
            });
        }

     // Ajouter les onglets au TabPane
        tabPane.getTabs().addAll(tabActWin, tabCracker, tabUniEdge, tabAbout);

        
        
        // Récupérer les informations système sans utiliser OSHI
        String osName = System.getProperty("os.name");
        String osBuild = System.getProperty("os.build");
        String osArch = System.getProperty("os.arch");
        String javaVersion = System.getProperty("java.version");
        String systemDetails = "Système d'exploitation: " + osName + "     Architecture: " + osArch + "     Version Java: " + javaVersion;

        // Créer un Label pour afficher les informations système
        Label systemInfoLabel = new Label(systemDetails);
        systemInfoLabel.setAlignment(Pos.CENTER);

        // Encapsuler le label dans un HBox pour le centrer
        HBox infoBox = new HBox(systemInfoLabel);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setStyle("-fx-padding: 10;");

        infoBox.getStyleClass().add("info-box");
        systemInfoLabel.getStyleClass().add("text-background");
        
        VBox mainLayout = new VBox(tabPane, infoBox); // tabPane est votre TabPane existant
        Scene scene = new Scene(mainLayout, 1200, 660);
        tabActWin.setStyle(" -fx-border-color: transparent; -fx-text-fill: #000000;-fx-focus-color: transparent;fx-faint-focus-color: transparent;");


        // Configuration du Stage (fenêtre)
        primaryStage.setScene(scene);
        primaryStage.setTitle(Storage.TITLE);
        Image icon = new Image("/images/icon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        primaryStage.show();
    }


    private static void animateTabChange(Tab tab) {
        Node tabContent = tab.getContent();
        if (tabContent != null) {
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), tabContent);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
