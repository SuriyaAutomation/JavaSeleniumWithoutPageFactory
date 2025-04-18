package org.google.home.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Dummy {

    // Wait for Angular to finish HTTP requests
    public static void waitForAngularLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;

        ExpectedCondition<Boolean> angularLoad = webDriver -> {
            try {
                return (Boolean) jsExec.executeScript(
                    "return (typeof angular !== 'undefined') && " +
                    "angular.element(document).injector() !== undefined && " +
                    "angular.element(document).injector().get('$http').pendingRequests.length === 0");
            } catch (Exception e) {
                return true; // Angular not present, continue
            }
        };

        wait.until(angularLoad);
    }

    // Wait for element to be clickable
    public static WebElement waitAndCheckElementClickable(WebDriver driver, By by, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    // Scroll to element if not in view
    public static void jsScrollToElementIfNotInView(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
    }

    // JS click
    public static void jsExecutor_clickOnElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    // Actions class click (move to element)
    public static void actions_MoveToElement(WebDriver driver, WebElement element, long timeout) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).pause(Duration.ofSeconds(timeout)).perform();
    }

    // Main reusable method
    public static void clickOnElement(WebDriver driver, By by, long timeout) {
        WebElement element = null;
        try {
            waitForAngularLoad(driver); // wait for Angular rendering

            element = waitAndCheckElementClickable(driver, by, timeout);
            jsScrollToElementIfNotInView(driver, element);

            try {
                element.click();
            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException occurred, retrying click.");
                element = driver.findElement(by);
                element.click();
            }

        } catch (Exception e1) {
            System.out.println("Standard click failed: " + e1.getMessage());

            try {
                if (element != null) {
                    actions_MoveToElement(driver, element, timeout);
                    element.click();
                }
            } catch (Exception e2) {
                System.out.println("Actions click failed, falling back to JS click: " + e2.getMessage());
                if (element != null) {
                    jsExecutor_clickOnElement(driver, element);
                }
            }
        }
    }
}
