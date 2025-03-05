package org.google.home.utils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

import org.google.home.config.factory.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BaseTest {

	public WebDriver driver;

	public WebDriver WebDriverManager() throws IOException {
		String downloadDir = System.getProperty("user.dir") + ConfigFactory.getConfig().defaultDownloadingDirectory().trim();
		File downloadDirectory = new File(downloadDir);
		if (!downloadDirectory.exists()) {
		    downloadDirectory.mkdirs();  // Create the directory if it doesn't exist
		}
		if (driver == null) {
			if (ConfigFactory.getConfig().browser().equalsIgnoreCase("chrome")) {
				HashMap<String, Object> chromePreferences = new HashMap<>();
				chromePreferences.put("download.default_directory", downloadDir);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", chromePreferences);				
				driver = new ChromeDriver(options);
				driver.manage().deleteAllCookies();
			} else if (ConfigFactory.getConfig().browser().equalsIgnoreCase("firefox")) {
				FirefoxOptions options = new FirefoxOptions();
				driver = new FirefoxDriver(options);
			} else if (ConfigFactory.getConfig().browser().equalsIgnoreCase("edge")) {
				EdgeOptions options = new EdgeOptions();
				driver = new EdgeDriver(options);
			} else {
				System.out.println("No Browser is provided");
			}			
			launchApplication();
		}
		return driver;
	}

	public void launchApplication() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigFactory.getConfig().implicitTimeOut()));		
		driver.get(getApplicationURL().trim());	
		driver.manage().window().maximize();
	}
	
	public static String getApplicationURL() {
		String appURL = "";		
		appURL = ConfigFactory.getConfig().appURL();		
		return appURL;
	}
	
}