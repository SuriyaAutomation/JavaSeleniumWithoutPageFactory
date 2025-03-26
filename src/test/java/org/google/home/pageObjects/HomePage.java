package org.google.home.pageObjects;

import org.google.home.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

	public WebDriver driver;
	String currentUrl;

	By txtHomePageValidation = By.xpath("//div[text()='Google']");	

	//dynamic elements 
	private static final By tblDonation = By.xpath("//main[@id='main-container']//table/tbody");
	private static final String txtAssUserId = "//input[@id='ldap_input%s']";
	private static final String popUpRecurringDonation = "//p[text()='%s' and contains(text(),'donations') ]";
		
	public HomePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public static String prepareXPathString(String xpath, String value) {
		return String.format(xpath, value);
	}

	public String getpopUpRecurringDonationMsg() {
		String txtRecurringDonation = SeleniumHelper.getTextFromElement(driver, By.xpath(prepareXPathString(popUpRecurringDonation,String.valueOf("WelcomeHome"))));
		return txtRecurringDonation;
	}
	
	public void clickpopUpRecurringDonationMsg() {
		SeleniumHelper.clickOnElement(driver, By.xpath(prepareXPathString(popUpRecurringDonation,String.valueOf("WelcomeHome"))));		
	}
	
	public String getHomePageTitle() {
		String actualPageTitle = SeleniumHelper.getPageTitle(driver).trim();
		return actualPageTitle;
	}

	
	public String  getCurrentPageTitle() {
		return SeleniumHelper.getPageTitle(driver);
	}

	
}
