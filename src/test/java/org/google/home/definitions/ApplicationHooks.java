package org.google.home.definitions;

import java.io.IOException;

import org.google.home.utils.TestSetUp;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;

public class ApplicationHooks {

	public TestSetUp setUp;

	public ApplicationHooks(TestSetUp setUp) {
		this.setUp = setUp;
	}

	@After
	public void tearDown() throws IOException {
		setUp.baseTest.WebDriverManager().quit();
	}

	@AfterStep
	public void addScreenshot(Scenario scenario) throws IOException {
		WebDriver driver = setUp.baseTest.WebDriverManager();
		if (scenario.isFailed()) {
			final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			scenario.attach(screenshot, "image/png", "FailScreenshot");
		}
	}
}
