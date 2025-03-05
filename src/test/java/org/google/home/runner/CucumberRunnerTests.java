package org.google.home.runner;

import org.google.home.config.factory.ConfigFactory;
import org.google.home.utils.Email;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = "@all", features = "src/test/resources/features", glue = "org.google.home.definitions", plugin = {
		"pretty", "json:target/cucumber.json", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" })

public class CucumberRunnerTests extends AbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		System.setProperty("dataproviderthreadcount", System.getenv("THREADCOUNT"));
		return super.scenarios();
	}

	@AfterSuite
	public void testngAfterSuite() {
		if (ConfigFactory.getConfig().emailTestReport().equalsIgnoreCase("y")) {
			try {
				Email.sendEmailNotification();
			} catch (Exception e) {
				System.out.println("Exception sending email:: " + e.getStackTrace());
			}
		} else {
			System.out.println("Automation Test Report Email not sent due to environment variable set to N...!");
		}
	}
}
