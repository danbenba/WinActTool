package fr.danbenba.WinActTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.UIManager;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

@SuppressWarnings("unused")
public class main extends Application {

	// Initialize your UI here
    StackPane root = new StackPane();
	
	static final String CURRENT_VERSION = Storage.CURRENT_VERSION;
    static final String CREATOR_LABEL = Storage.CREATOR_LABEL;
    static final String Version_URL = Storage.getVersionCheckUrl();

    @Override
    public void start(Stage primaryStage) {
        //AboutTab.setHostServices(this.getHostServices());
        //AboutTab.testHostServices();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        AboutTab.checkForLegalCopy();
    	SplashScreen splashScreen = new SplashScreen();
		splashScreen.splashScreen(primaryStage);
    }
    
    private static boolean isRunAsAdmin() {
        File testPrivilege = new File("C:\\testPrivilege.txt");
        try {
            if (testPrivilege.createNewFile()) {
                testPrivilege.delete();
                return true;
            }
        } catch (IOException e) {
            // L'exception indique un manque de privilèges
        }
        return false;
    }

    
    private static void fadeInPopup(VBox layout) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), layout);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
    
    
    static void launcherMain(Stage primaryStage) {
    	
        TabPane tabPane = new TabPane();
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        boolean isAdmin = isRunAsAdmin();
        String privilegeLevel = isAdmin ? "Administrateur" : "Utilisateur";

        // Créer un Label pour le niveau de privilège
        Label privilegeLabel = new Label("Privilege: " + privilegeLevel + "\n");
        privilegeLabel.setAlignment(Pos.CENTER);

        // Si l'utilisateur est un administrateur, afficher le texte en noir, sinon en rouge
        if (isAdmin) {
            privilegeLabel.setStyle("-fx-text-fill: black;");
        } else {
            privilegeLabel.setStyle("-fx-text-fill: red");
            Tooltip tooltip = new Tooltip("Cette Application exige le privilège administrateur.");
            Tooltip.install(privilegeLabel, tooltip);
            // Forcer l'affichage du tooltip pour le débogage
            privilegeLabel.setOnMouseEntered(event -> tooltip.show(privilegeLabel, event.getScreenX(), event.getScreenY() + 15));
            privilegeLabel.setOnMouseExited(event -> tooltip.hide());
        }

        // Optional: Set up another label to display a different message for an admin
        if (isAdmin) {
            Label adminMessageLabel = new Label("Mode Administrateur Activé");
            adminMessageLabel.setAlignment(Pos.CENTER);
            adminMessageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            // Ajouter adminMessageLabel à votre scène ou layout
        }

        
        Tab tabActWin = new Tab("Windows Activation", WindowsActivatorTab.createCrackerTab()); // Onglet "WA" créé ici
        Tab tabCracker = new Tab("Office Activator", OfficeActivatorTab.createCrackerTab(primaryStage)); // Onglet "Activator" créé ici
        Tab tabOSetup = new Tab("Installer Office", SetupTab.createSetupTab()); // Onglet "Installer office" créé ici
        Tab tabPKI = new Tab("Package Installer", PackageInstallerTab.createpkiTab(primaryStage)); // Onglet "Package Installer" créé ici
        Tab TabTools = new Tab("Other Tools", ToolsTab.CreateToolstab(primaryStage)); // Onglet "Tools" créé ici
        Tab tabAbout = new Tab("About", AboutTab.createAboutTab()); // Onglet "About" créé ici

        // Mettre les onglets non fermables
        tabActWin.setClosable(false);
        tabCracker.setClosable(false);
        tabOSetup.setClosable(false);
        tabPKI.setClosable(false);
        TabTools.setClosable(false);
        tabAbout.setClosable(false);
        
        // Ajouter l'écouteur pour l'animation à chaque onglet
        for (Tab tab : new Tab[] { tabActWin, tabCracker, tabPKI, TabTools, tabAbout, tabOSetup }) {
            tab.setOnSelectionChanged(event -> {
                if (tab.isSelected()) {
                    animateTabChange(tab);
                }
            });
        }
        
        

        // Ajouter les onglets au TabPane
        tabPane.getTabs().addAll(tabActWin, tabCracker, tabPKI, TabTools, tabAbout, tabOSetup);

        
        
     // Récupérer les informations système sans utiliser OSHI
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String javaVersion = System.getProperty("java.version");

        // Construire la chaîne de détails du système sans inclure le niveau de privilège
        String systemDetails = "WinActTool Version: " + CURRENT_VERSION +
                "     Architecture: " + osArch +
                "     Système d'exploitation: " + osName +
                "     Version Java: " + javaVersion;

        // Créer un Label pour afficher les informations système
        Label systemInfoLabel = new Label(systemDetails);
        systemInfoLabel.setAlignment(Pos.CENTER);

        // Encapsuler les labels dans un VBox pour les afficher verticalement
        VBox infoBox = new VBox(10); // sets the spacing between children to 10px
        infoBox.getChildren().addAll(privilegeLabel, systemInfoLabel);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setStyle("-fx-padding: 15;");

        infoBox.getStyleClass().add("info-box");
        systemInfoLabel.getStyleClass().add("text-background");
        
        VBox mainLayout = new VBox(tabPane, infoBox); // tabPane est votre TabPane existant
        Scene scene1 = new Scene(mainLayout, 1250, 700);
        tabActWin.setStyle(" -fx-border-color: transparent; -fx-text-fill: #000000;-fx-focus-color: transparent;fx-faint-focus-color: transparent;");

        mainLayout.setAlignment(Pos.BOTTOM_CENTER); // Align to bottom center

        // Configuration du Stage (fenêtre)
        primaryStage.setScene(scene1);
        primaryStage.setTitle(Storage.TITLE);
        Image icon = new Image("/images/icon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        scene1.getStylesheets().add(main.class.getResource("style.css").toExternalForm());
        primaryStage.show();
    }

    // Annimation de la fenêtre
    private static void animateTabChange(Tab tab) {
        Node tabContent = tab.getContent();
        if (tabContent != null) {
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), tabContent);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
        }
    }
    
    public static class PopupDemo extends Application {

        @Override
        public void start(Stage primaryStage) {
            
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
}
