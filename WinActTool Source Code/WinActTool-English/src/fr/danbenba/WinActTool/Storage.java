package fr.danbenba.WinActTool;

public class Storage {
	private static final String DOWNLOAD_URL = "https://github.com/danbenba/WinActTool/raw/main/WinActTool.jar";
    private static final String VERSION_CHECK_URL = "https://raw.githubusercontent.com/danbenba/WinActTool/main/version.txt";
	static final String CURRENT_VERSION = "3.1b";
	static final String Language = "English";
	static final String TITLE = "WinActTool - " + Language + " Version";
	static final String TYPE_VERSION = "Snapshot";
	static final String TYPE_VERSION_ID = "03w01b";
    static final String CREATOR_LABEL = "danbenba";
    static final String Protection_Version = "2.0";
    static final String Updater_Version = "1.8";
    
    
    
	public static String getDownloadUrl() {
		return DOWNLOAD_URL;
	}
	public static String getVersionCheckUrl() {
		return VERSION_CHECK_URL;
	}
    
}
