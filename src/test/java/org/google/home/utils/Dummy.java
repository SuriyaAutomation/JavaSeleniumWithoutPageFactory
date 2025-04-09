package org.google.home.utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class Dummy {


	    public String switchToNewTabAndGetTitle(WebElement elementToClick , WebDriver driver) {
	        String originalWindow = driver.getWindowHandle();
	        elementToClick.click();

	        Set<String> allWindows = driver.getWindowHandles();
	        while (allWindows.size() <= 1) {
	            allWindows = driver.getWindowHandles();
	        }
	        List<String> windowList = new ArrayList<>(allWindows);
	        for (String window : windowList) {
	            if (!window.equals(originalWindow)) {
	                driver.switchTo().window(window);
	                break;
	            }
	        }
	        String newTabTitle = driver.getTitle();
	        driver.close();
	        driver.switchTo().window(originalWindow);

	        return newTabTitle;
	    }
	}




