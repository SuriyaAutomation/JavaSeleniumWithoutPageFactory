package org.google.home.utils;

import java.io.IOException;

import org.google.home.pageObjects.PageObjectManager;
import org.openqa.selenium.WebElement;

public class TestSetUp {

	public WebElement errorMessage;
    public WebElement homePageUserName;
    public PageObjectManager pageObjectManager;
    public BaseTest baseTest;

    public TestSetUp() throws IOException {

        baseTest = new BaseTest();
        pageObjectManager = new PageObjectManager(baseTest.WebDriverManager());

    }
}
