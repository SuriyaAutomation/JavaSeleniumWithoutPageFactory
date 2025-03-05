package org.google.home.pageObjects;

import org.google.home.utils.SeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

	public WebDriver driver;
	String currentUrl;

	By txtHomePageValidation = By.xpath("//div[text()='Contributions Manager']");	
	By btnHome = By.xpath("//div[@id='root']//a[@id='homeList__button']");
	By btnLogoutIndividual = By.xpath("//div[@id='root']//div[@id='logoutList__text']/span");
	By btnDonateIndividual = By.xpath("//div[@id='root']//div[@id='donateList__text']/span");
	By btnYearEndStatementIndividual = By.xpath("//div[@id='root']//div[@id='reportsList__text']/span");
	By btnTaxLetterDashboard = By.xpath("//div[@id='root']//a[@id='taxLetterDashboard__button']");
	By btnLogoutCaptain = By.xpath("//div[@id='root']//ul[@id='myTaxRecordsList']/div[@id='logoutList__button']");
	By btnConfirmPopup_Logout = By.xpath("//a[@id='modalLogout__button']");
	By lblNewDonation = By.xpath("//div[@id='addDonation__card']");
	By lblExistingDonation = By.id("myDonations__donation");
	By txtNewDonationWelcomeMsg =  By.id("addDonation__message");
	By txtExistingDonationLatestMsg = By.xpath("//div[@id='myDonations__donation']/p[@id='myDonations__dollar']");
	By btnAddNewDonation = By.id("addDonation__modalButton");
	By btnEditMyDonation = By.id("myDonations__button");
	By txtLatestDonationAmount =  By.id("myDonations__amount");
	By lblCaptainMsg = By.xpath("//h3[text()='SINCE YOU ARE A CAPTAIN, HOW WILL YOU BE DONATING TODAY?']");
	By btnSearch = By.xpath("//div[@id='root']//div[@id='searchList__text']/span[text()='Search']");
	By lblCaptainDonation = By.xpath("//div[@id='root']//div[@id='donateList__text']");
	By btnDonateAsMySelf = By.xpath("//button[text()='DONATE AS MYSELF']");
	
	

	
	public HomePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getLatestDonationAmount() {
		String actualPageTitle =  SeleniumHelper.getTextFromElement(driver, txtLatestDonationAmount);
		return actualPageTitle;
	}
	
	public void homebtn_Individual() {
		SeleniumHelper.clickOnElement(driver, btnHome);		
	}
	
	public void taxLetterDashboard() {
		SeleniumHelper.clickOnElement(driver, btnTaxLetterDashboard);		
	}
	
	public void search() {
		SeleniumHelper.clickOnElement(driver, btnSearch);		
	}
	
	public void addNewDonation_Individual() {
		SeleniumHelper.clickOnElement(driver, btnAddNewDonation);		
	}
	
	public void makeDonation_Individual() {
		SeleniumHelper.clickOnElement(driver, btnDonateIndividual);		
	}

	public String getHomePageTitle() {
		String actualPageTitle = SeleniumHelper.getPageTitle(driver).trim();
		return actualPageTitle;
	}

	public String getLogoutBtn() {
		String text_LogoutBtn = SeleniumHelper.getTextFromElement(driver, btnLogoutIndividual);
		return text_LogoutBtn;
	}

	public String getDonateBtn() {
		String text_DonateBtn = SeleniumHelper.getTextFromElement(driver, btnDonateIndividual);
		return text_DonateBtn;
	}

	public String getYearEndStatementBtn() {
		String text_YearEndStatementBtn = SeleniumHelper.getTextFromElement(driver, btnYearEndStatementIndividual);
		return text_YearEndStatementBtn;
	}

	public String getNewDonationWelcomeMsg() {
		String txtNewDonation = SeleniumHelper.getTextFromElement(driver, txtNewDonationWelcomeMsg);
		return txtNewDonation;
	}

	public String getExistingDonationLatestMsg() {
		String txt_latestDonation = SeleniumHelper.getTextFromElement(driver, txtExistingDonationLatestMsg);
		return txt_latestDonation;
	}

	public boolean getNewUserDonation() {
		boolean NewUserDonation = false;
		NewUserDonation = driver.findElement(btnAddNewDonation).isDisplayed();
		return NewUserDonation;
	}

	public boolean getEditExistingDonation() {
		boolean EditExistingDonation = false;
		EditExistingDonation = driver.findElement(btnEditMyDonation).isDisplayed();
		return EditExistingDonation;
	}

	public boolean isHomePage(String role) {
		boolean pageLoaded = false;
		if (role.equalsIgnoreCase("Individual")|| role.equalsIgnoreCase("Admin")) {
			pageLoaded = SeleniumHelper.isPageLoaded(driver, txtHomePageValidation);
		} else if (role.equalsIgnoreCase("Captain")) {
			pageLoaded = SeleniumHelper.isPageLoaded(driver, btnLogoutCaptain);
		}
		return pageLoaded;

	}
	
	public String  getCurrentPageTitle() {
		return SeleniumHelper.getPageTitle(driver);
	}

	public void logOut(String role) {
		currentUrl = driver.getCurrentUrl();
		if (role.equalsIgnoreCase("Individual")) {
			SeleniumHelper.clickOnElement(driver, btnLogoutIndividual);
			SeleniumHelper.clickOnElement(driver, btnConfirmPopup_Logout);
		} else if (role.equalsIgnoreCase("Captain")||role.equalsIgnoreCase("Admin")) {
			SeleniumHelper.clickOnElement(driver, btnLogoutCaptain);
			SeleniumHelper.clickOnElement(driver, btnConfirmPopup_Logout);
		}						
	}
	
	public void waitUntilNavToNxtPage( ) {		
		SeleniumHelper.waitUntillNewpageLoaded(driver,currentUrl);		
	}
	
	public String getCaptainMessage() {
		String actualMessage = SeleniumHelper.getTextFromElement(driver,lblCaptainMsg);
		return actualMessage;
	}
	
	public String getSearchBtn() {
		String text_SearchBtn = SeleniumHelper.getTextFromElement(driver, btnSearch);
		return text_SearchBtn;
	}
	
	public String getCaptainDonationLabel() {
		String actualCaptainDonationLabelName = SeleniumHelper.getTextFromElement(driver,lblCaptainDonation);
		return actualCaptainDonationLabelName;
	}
	
	public String getCaptainDonationAsSelf() {
		String actualCaptainDonationAsSelf = SeleniumHelper.getTextFromElement(driver,btnDonateAsMySelf);
		return actualCaptainDonationAsSelf;
	}
	
	public void yearEndStatement() {
		SeleniumHelper.clickOnElement(driver, btnYearEndStatementIndividual);		
	}
	
}
