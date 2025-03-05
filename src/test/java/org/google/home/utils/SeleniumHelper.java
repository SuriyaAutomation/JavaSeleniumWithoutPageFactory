package org.google.home.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

import org.google.home.config.factory.ConfigFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumHelper {

	public static String filePath = System.getProperty("user.dir")+ 
	ConfigFactory.getConfig().defaultDownloadingDirectory().trim();
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Actions actions;
	public static JavascriptExecutor jsExecutor;
	static WebElement element = null;
	static Wait<WebDriver> wdw = null;

	// WebDriverWaits
	public static void implicitWaitNew(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigFactory.getConfig().implicitTimeOut()));
	}

	public static Wait<WebDriver> explicitWait(WebDriver driver, long timeout) {
		return new FluentWait<>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofSeconds(2));
	}

	// Wait for WebElements
	public static WebElement waitAndcheckElementIsPresent(WebDriver driver, By by, long timeout) {
		wdw = explicitWait(driver, timeout);
		return wdw.until(ExpectedConditions.presenceOfElementLocated(by));
	}
	
	public static List<WebElement> waitAndCheckElementsPresence(WebDriver driver, By by, long timeout) {
		wdw = explicitWait(driver, timeout);
		return wdw.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
	}
		
	public static Boolean waitUntillNewpageLoaded(WebDriver driver,String currentUrl) {
		wdw = explicitWait(driver, ConfigFactory.getConfig().explicittimeout());
		return wdw.until((ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl))));
	}
	
	public static WebElement waitAndCheckElementClickable(WebDriver driver, By by, long timeout) {
		wdw = explicitWait(driver, timeout);
		return wdw.until(ExpectedConditions.elementToBeClickable(by));
	}
	

	public static WebElement waitAndCheckElementVisible(WebDriver driver, By by, long timeout) {
		wdw = explicitWait(driver, timeout);
		return wdw.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	
	

	// WebDriver Helper Methods
	public static boolean isPageLoaded(WebDriver driver, By by) {
		try {
			element = waitAndCheckElementVisible(driver, by, getTimeout());
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public static String getPageTitle(WebDriver driver) {
		return driver.getTitle();
	}

	public static void clickOnElement(WebDriver driver, By by) {
		clickOnElement(driver, by, getTimeout());
	}
	
	public static List<WebElement> getElements(WebDriver driver, By by) {
		List<WebElement> elements = null;

		try {			
			elements = waitAndCheckElementsPresence(driver, by,  getTimeout());
			if (elements == null || elements.isEmpty()) {
				throw new Exception("No elements found with the provided XPath.");
			}
		} catch (Exception e) {
			System.out.println("Exception occurred during getting elements: " + e.getMessage());
		}
		return elements;
	}

	public static void clickOnElement(WebDriver driver, By by, long timeout) {
		try {
			try {
				element = waitAndCheckElementClickable(driver, by, timeout);
				jsScrollToElementIfNotInView(driver, element);
				try {
					element.click();
				} catch (StaleElementReferenceException e) {
					System.out.println(
							"StaleElementReferenceException ocurred, so find the element and trying again...!!!");
					element = driver.findElement(by);
					element.click();
				}
			} catch (Exception e) {
				System.out.println(
						"Exception ocurred:: Failed to click the element using Selenium click method, so using Action Class of Selenium...!!!\n"
								+ e.getMessage());
				if (e instanceof ElementNotInteractableException) {
					actions_MoveToElement(driver, element, timeout);
					element.click();
				}
			}
		} catch (Exception e) {
			System.out.println(
					"Exception ocurred during element click:: Trying with JavaScript Executor \n" + e.getMessage());
			jsExecutor_clickOnElement(driver, element);
		}

	}
	
	
	public static String getTextFromElement(WebDriver driver, By by) {
		String text = "";
		try {
			element = waitAndCheckElementVisible(driver, by, getTimeout());
			jsScrollToElementIfNotInView(driver, element);
			text = element.getText();
		} catch (Exception e) {
			if (e instanceof NoSuchElementException) {
				System.out.println(
						"No such element Exception ocurred during getting text from element :: please check the WebElement \n"
								+ e.getMessage());
			}else {
				e.printStackTrace();
			}
		}
		return text;
	}

	public static void enterText(WebDriver driver, By by, String text) {
		try {
			try {
				element = waitAndCheckElementClickable(driver, by, ConfigFactory.getConfig().explicittimeout());
				element.sendKeys(text);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equalsIgnoreCase("ElementClickInterceptedException")) {
					actions_MoveToElement(driver, element, ConfigFactory.getConfig().explicittimeout());
					element.sendKeys(text);
				}
			}
		} catch (Exception e) {
			jsExecutor_SetAttributeValue(driver, by, text);
		}

	}
	
	public static void clearText(WebDriver driver, By by) {
	    try {
	        element = waitAndCheckElementClickable(driver, by, ConfigFactory.getConfig().explicittimeout());
	        element.clear();
	    } catch (Exception e) {	        
	        if (e instanceof ElementClickInterceptedException) {
	            actions_MoveToElement(driver, element, ConfigFactory.getConfig().explicittimeout());
	            element.clear(); 
	        } else {	            
	            jsExecutor_SetAttributeValue(driver, by, "");
	        }
	    }
	}

	// JavaScript Executor Methods
	public static void jsScrollToElementIfNotInView(WebDriver driver, WebElement element) {
		jsExecutor = (JavascriptExecutor) driver;

		// Check if the element is in the viewport
		boolean isElementInView = (Boolean) jsExecutor
				.executeScript(
						"var elem = arguments[0]," + " box = elem.getBoundingClientRect(),"
								+ " cx = window.innerWidth || document.documentElement.clientWidth,"
								+ " cy = window.innerHeight || document.documentElement.clientHeight;"
								+ "return (box.top >= 0 && box.left >= 0 && box.bottom <= cy && box.right <= cx);",
						element);

		// Scroll to the element if it's not in the viewport
		if (!isElementInView) {
			jsExecutor.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", element);
		}
	}

	public static void jsExecutor_clickOnElement(WebDriver driver, WebElement element) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].click()", element);
	}

	public static String jsExecutor_GetAttributeValue(WebDriver driver, By by) {
		jsExecutor = (JavascriptExecutor) driver;
		element = waitAndCheckElementVisible(driver, by, ConfigFactory.getConfig().explicittimeout());
		return (String) jsExecutor.executeScript("return arguments[0].getAttribute('value')", element);

	}
	
	public static String jsExecutor_getPageTitle(WebDriver driver) {
		jsExecutor = (JavascriptExecutor) driver;			
		return (String) jsExecutor.executeScript("return document.title");
	}

	public static void jsExecutor_SetAttributeValue(WebDriver driver, By by, String text) {
		jsExecutor = (JavascriptExecutor) driver;
		element = waitAndCheckElementVisible(driver, by, ConfigFactory.getConfig().explicittimeout());
		jsExecutor.executeScript("arguments[0].setAttribute('value'" + text + ")", element);
	}
	
	public static void insertTextIntoInnerHTML(WebDriver driver, By by, String text) {
		jsExecutor = (JavascriptExecutor) driver;
		String editorId = (String) jsExecutor.executeScript("return Object.keys(CKEDITOR.instances)[0];");
		jsExecutor.executeScript("CKEDITOR.instances[Object.keys(CKEDITOR.instances)[0]].setData('" + text + "');");
    }
	
	public static void downloadPdfFile(WebDriver driver, String pdfUrl) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.open(arguments[0])", pdfUrl);
	}

	// Action class Methods
	public static void actions_MoveToElement(WebDriver driver,WebElement element, long timeout) {
		try {
			actions = new Actions(driver);
			actions.moveToElement(element).perform();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void actions_TripleClick(WebDriver driver, By by) {
		try {
			actions = new Actions(driver);		
			element = waitAndCheckElementClickable(driver, by, ConfigFactory.getConfig().explicittimeout());
			actions.moveToElement(element).click().click().click().perform();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void actions_ClickArrowUp(WebDriver driver,By by,int increaseCount) {
		try {
			actions = new Actions(driver);
			element = waitAndCheckElementClickable(driver, by, ConfigFactory.getConfig().explicittimeout());
			for (int i = 0; i < increaseCount; i++) {
				actions.moveToElement(element).sendKeys(Keys.ARROW_UP).perform();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void actions_ClickArrowDown(WebDriver driver,By by,int decreaseCount) {
		try {
			actions = new Actions(driver);
			element = waitAndCheckElementClickable(driver, by, ConfigFactory.getConfig().explicittimeout());
			for (int i = 0; i < decreaseCount; i++) {
				actions.moveToElement(element).sendKeys(Keys.ARROW_DOWN).perform();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void actions_ClickEnter(WebDriver driver,By by) {
		try {
			actions = new Actions(driver);
			element = waitAndCheckElementClickable(driver, by, ConfigFactory.getConfig().explicittimeout());
			actions.moveToElement(element).sendKeys(Keys.ENTER).perform();				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void actions_ClickEscape(WebDriver driver,By by) {
		try {
			actions = new Actions(driver);
			element = waitAndCheckElementClickable(driver, by, ConfigFactory.getConfig().explicittimeout());
			actions.moveToElement(element).sendKeys(Keys.ESCAPE).perform();				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getTableRecords(WebDriver driver,By by, int rowNum, int colNum) {
		String txtRecord ="";
		WebElement ElementTblDonation = driver.findElement(by);
		List<WebElement> tblRows = ElementTblDonation.findElements(By.tagName("tr"));
		for (int i = 0; i < 1; i++) {			
			List<WebElement> tColCount = tblRows.get(rowNum).findElements(By.tagName("td"));			
			for (int j = 0; j < 1; j++) {
				txtRecord = tColCount.get(colNum).getText();
			}
		}
		return txtRecord;
	}
	
	public static void savePDF(WebDriver driver, String uri,String fileName) {
        String script = 
            "var uri = arguments[0];" +
            "var callback = arguments[1];" +
            "var toBase64 = function(buffer){" +
            "   for(var r, n = new Uint8Array(buffer), t = n.length, a = new Uint8Array(4 * Math.ceil(t / 3)), i = new Uint8Array(64), o = 0, c = 0;" +
            "   64 > c; ++c) i[c] = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/'.charCodeAt(c);" +
            "   for(c = 0; t - t % 3 > c; c += 3, o += 4) r = n[c] << 16 | n[c + 1] << 8 | n[c + 2], a[o] = i[r >> 18], a[o + 1] = i[r >> 12 & 63], a[o + 2] = i[r >> 6 & 63], a[o + 3] = i[63 & r];" +
            "   return t % 3 === 1 ? (r = n[t - 1], a[o] = i[r >> 2], a[o + 1] = i[r << 4 & 63], a[o + 2] = 61, a[o + 3] = 61) : t % 3 === 2 && (r = (n[t - 2] << 8) + n[t - 1], a[o] = i[r >> 10], a[o + 1] = i[r >> 4 & 63], a[o + 2] = i[r << 2 & 63], a[o + 3] = 61), new TextDecoder('ascii').decode(a);" +
            "};" +
            "var xhr = new XMLHttpRequest();" +
            "xhr.responseType = 'arraybuffer';" +
            "xhr.onload = function() { callback(toBase64(xhr.response)) }; " +
            "xhr.onerror = function() { callback(xhr.status) }; " +
            "xhr.open('GET', uri); " +
            "xhr.send();";

        String base64Content = (String) ((JavascriptExecutor) driver).executeAsyncScript(script, uri);
        File file = new File(filePath + File.separator + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
            fos.write(decodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	// Get Timeout from configuration
	public static long getTimeout() {
		return ConfigFactory.getConfig().explicittimeout();
	}
	
	public static String getAttributeFromElement(WebDriver driver, By by, String attributeName) {
		String attributeValue = "";
		try {
			element = waitAndCheckElementVisible(driver, by, getTimeout());
			jsScrollToElementIfNotInView(driver, element);
	        attributeValue = element.getAttribute(attributeName);
		} catch (Exception e) {
			if (e instanceof NoSuchElementException) {
				System.out.println(
						"No such element Exception ocurred during getting attribute from element :: please check the WebElement \n"
								+ e.getMessage());
			}else {
				e.printStackTrace();
			}
		}
		return attributeValue;
	}

}
