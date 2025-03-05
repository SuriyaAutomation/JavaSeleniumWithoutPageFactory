package org.google.home.definitions;

import java.io.IOException;

import org.google.home.pageObjects.HomePage;
import org.google.home.utils.BaseTest;
import org.google.home.utils.TestSetUp;

import io.cucumber.java.en.Given;

public class CommonDefinitions extends BaseTest {

	TestSetUp setUp;
	public HomePage homePage;

	public CommonDefinitions(TestSetUp setUp) throws IOException {
		this.setUp = setUp;
		this.homePage = setUp.pageObjectManager.getHomePage();

	}

	@Given("Open Browser and Launch Application")
	public void open_Browser_and_Launch_Application() throws IOException {
		setUp.baseTest.WebDriverManager();	
	}
		

}
