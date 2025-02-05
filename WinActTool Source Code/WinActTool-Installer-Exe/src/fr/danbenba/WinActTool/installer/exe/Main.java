package fr.danbenba.WinActTool.installer.exe;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.io.FileUtils;

@SuppressWarnings("serial")
public class Main extends JFrame {
  final String website = "https://morpheuslauncher.it";
  
  private final JProgressBar progressBar;
  
  private final JLabel statusLabel;
  
  @SuppressWarnings("incomplete-switch")
public Main() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    switch (OSUtils.getPlatform()) {
      case windows:
      case macos:
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        break;
    } 
    setTitle("Morpheus Installer/Updater");
    setDefaultCloseOperation(3);
    setLayout(new FlowLayout());
    this.progressBar = new JProgressBar(0, 100);
    this.progressBar.setPreferredSize(new Dimension(280, 25));
    add(this.progressBar);
    this.statusLabel = new JLabel();
    add(this.statusLabel);
    setSize(320, 100);
    setLocationRelativeTo((Component)null);
  }
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
          Main downloader;
          try {
            downloader = new Main();
          } catch (UnsupportedLookAndFeelException|ClassNotFoundException|InstantiationException|IllegalAccessException e) {
            throw new RuntimeException(e);
          } 
          downloader.setVisible(true);
          downloader.downloadFiles();
        });
  }
  
  private static void downloadFile(String url, String destinationPath) throws IOException {
    URL fileUrl = new URL(url);
    try(BufferedInputStream in = new BufferedInputStream(fileUrl.openStream()); FileOutputStream fos = new FileOutputStream(destinationPath)) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1)
        fos.write(buffer, 0, bytesRead); 
    } 
  }
  
  private static void extractZip(String zipFilePath, String destinationFolder) throws IOException {
    try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
      ZipEntry entry = zipIn.getNextEntry();
      while (entry != null) {
        String filePath = destinationFolder + File.separator + entry.getName();
        if (!entry.isDirectory()) {
          Files.createDirectories(Paths.get(filePath, new String[0]).getParent(), (FileAttribute<?>[])new FileAttribute[0]);
          try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = zipIn.read(buffer)) != -1)
              out.write(buffer, 0, bytesRead); 
          } 
        } else {
          Files.createDirectories(Paths.get(filePath, new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
        } 
        zipIn.closeEntry();
        entry = zipIn.getNextEntry();
      } 
    } 
  }
  
  public static ArrayList<String> getNetString(String url) throws IOException {
    ArrayList<String> out = new ArrayList<>();
    BufferedReader buf = new BufferedReader(new InputStreamReader((new URL(url)).openStream(), "UTF-8"));
    String line;
    while ((line = buf.readLine()) != null)
      out.add(line); 
    buf.close();
    return out;
  }
  
  private void downloadFiles() {
    SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
        protected Void doInBackground() throws Exception {
          String zipname;
          boolean shouldUpdate = true;
          switch (Main.OSUtils.getPlatform()) {
            case windows:
              zipname = "morpheus_win.zip";
              break;
            case macos:
              zipname = "morpheus_osx.dmg";
              break;
            case linux:
              zipname = "morpheus_tux.zip";
              break;
            default:
              zipname = "morpheus_other.zip";
              break;
          } 
          String zipUrl = String.format("%s/downloads/%s", new Object[] { "https://morpheuslauncher.it", zipname });
          String jarUrl = String.format("%s/downloads/Launcher.jar", new Object[] { "https://morpheuslauncher.it" });
          String verUrl = String.format("%s/version.txt", new Object[] { "https://morpheuslauncher.it" });
          String destinationFolder = Main.OSUtils.getWorkingDirectory("morpheus").getPath();
          Main.this.statusLabel.setText("looking for updates...");
          Main.this.progressBar.setValue(5);
          File versionfile = new File(String.format("%s/%s", new Object[] { destinationFolder, "version.txt" }));
          if (versionfile.exists())
            try (BufferedReader br = new BufferedReader(new FileReader(versionfile))) {
              String riga;
              while ((riga = br.readLine()) != null) {
                if (riga.equals(Main.getNetString(verUrl).get(0)))
                  shouldUpdate = false; 
              } 
            } catch (IOException e) {
              e.printStackTrace();
              Main.this.statusLabel.setText("failed to check for updates");
              Main.this.progressBar.setValue(10);
            }  
          try {
            Files.createDirectories(Paths.get(destinationFolder, new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            Main.this.statusLabel.setText("creating morpheus directory.");
            Main.this.progressBar.setValue(20);
            if (shouldUpdate) {
              String zip_dest = String.format("%s/%s", new Object[] { destinationFolder, zipname });
              Main.downloadFile(zipUrl, zip_dest);
              Main.this.statusLabel.setText("downloading gui..");
              Main.this.progressBar.setValue(30);
              if (Main.OSUtils.getPlatform().equals(Main.OSUtils.OS.macos)) {
                String[] command = { "hdiutil", "attach", zip_dest };
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                  if (line.contains("morpheus_osx"))
                    System.out.println(line); 
                } 
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                  Main.this.statusLabel.setText("mounting dmg..");
                  Main.this.progressBar.setValue(40);
                  File f1 = new File("/Volumes/morpheus_osx/morpheus_launcher_gui.app");
                  File f2 = new File(String.format("%s/morpheus_launcher_gui.app", new Object[] { destinationFolder }));
                  FileUtils.copyDirectory(f1, f2);
                  Main.this.statusLabel.setText("Copying files..");
                  Main.this.progressBar.setValue(50);
                  Runtime.getRuntime().exec(new String[] { "umount", "/Volumes/morpheus_osx" });
                  Main.this.statusLabel.setText("Ejecting dmg..");
                  Main.this.progressBar.setValue(65);
                } else {
                  Main.this.statusLabel.setText("Error while mounting dmg.");
                } 
              } else {
                Main.extractZip(String.format("%s/%s", new Object[] { destinationFolder, zipname }), destinationFolder);
                Main.this.statusLabel.setText("Copying files..");
                Main.this.progressBar.setValue(50);
              } 
              Main.this.progressBar.setValue(70);
              Main.downloadFile(jarUrl, String.format("%s/%s", new Object[] { destinationFolder, "Launcher.jar" }));
              Main.this.statusLabel.setText("downloading launcher..");
              Main.this.progressBar.setValue(80);
              Files.deleteIfExists(Paths.get(zip_dest, new String[0]));
              Main.this.statusLabel.setText("Cleaning up..");
              Main.this.progressBar.setValue(90);
              Main.downloadFile(verUrl, versionfile.getPath());
            } 
            Main.this.statusLabel.setText("Done. starting application..");
            Main.this.progressBar.setValue(100);
            switch (Main.OSUtils.getPlatform()) {
              case windows:
                Runtime.getRuntime().exec(String.format("%s/morpheus_launcher_gui.exe", new Object[] { destinationFolder }));
                break;
              case macos:
                Runtime.getRuntime().exec(new String[] { "open", String.format("%s/morpheus_launcher_gui.app", new Object[] { destinationFolder }) });
                break;
              case linux:
                Runtime.getRuntime().exec(String.format("chmod +x %s/morpheus_launcher_gui", new Object[] { destinationFolder }));
                Runtime.getRuntime().exec(String.format("%s/morpheus_launcher_gui", new Object[] { destinationFolder }));
                break;
              default:
                Main.this.statusLabel.setText("What OS is this?, ask help on https://discord.gg/aerXnBe");
                Thread.sleep(5000L);
                break;
            } 
            System.exit(0);
          } catch (IOException e) {
            e.printStackTrace();
            Main.this.statusLabel.setText("Error while downloading/extracting files.");
            Thread.sleep(1000L);
            System.exit(0);
          } 
          return null;
        }
        
        protected void process(List<Integer> chunks) {
          int progress = ((Integer)chunks.get(chunks.size() - 1)).intValue();
          Main.this.progressBar.setValue(progress);
        }
        
        protected void done() {
          Main.this.progressBar.setValue(100);
        }
      };
    worker.execute();
  }
  
  public static class OSUtils {
    public static File getWorkingDirectory(String applicationName) {
      File workingDirectory;
      String applicationData, userHome = System.getProperty("user.home", ".");
      switch (getPlatform().ordinal()) {
        case 0:
        case 1:
          workingDirectory = new File(userHome, '.' + applicationName + '/');
          break;
        case 2:
          applicationData = System.getenv("APPDATA");
          if (applicationData != null) {
            workingDirectory = new File(applicationData, "." + applicationName + '/');
            break;
          } 
          workingDirectory = new File(userHome, '.' + applicationName + '/');
          break;
        case 3:
          workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
          break;
        default:
          workingDirectory = new File(userHome, applicationName + '/');
          break;
      } 
      if (!workingDirectory.exists() && !workingDirectory.mkdirs())
        throw new RuntimeException("The working directory could not be created: " + workingDirectory); 
      return workingDirectory;
    }
    
    public static OS getPlatform() {
      String osName = System.getProperty("os.name").toLowerCase();
      if (osName.contains("win"))
        return OS.windows; 
      if (osName.contains("mac"))
        return OS.macos; 
      if (osName.contains("solaris"))
        return OS.solaris; 
      if (osName.contains("sunos"))
        return OS.solaris; 
      if (osName.contains("linux"))
        return OS.linux; 
      return osName.contains("unix") ? OS.linux : OS.unknown;
    }
    
    public enum OS {
      linux, solaris, windows, macos, unknown;
    }
  }
  
  public enum OS {
    linux, solaris, windows, macos, unknown;
  }
}
