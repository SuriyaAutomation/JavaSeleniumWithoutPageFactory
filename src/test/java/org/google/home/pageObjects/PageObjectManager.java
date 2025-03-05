package org.google.home.pageObjects;

import org.openqa.selenium.WebDriver;

public class PageObjectManager {
	    
    public WebDriver driver;
    public HomePage homePage;

    public PageObjectManager(WebDriver driver)
    {
        this.driver = driver;
    }
    
      
    public HomePage getHomePage()
    { 
    	homePage= new HomePage(driver);
        return homePage;
    }  
    
    
    
}
