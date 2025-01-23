package org.srivi.Trading;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BrowserHelper {
    private WebDriver driver;
    private static final Logger logger = Logger.getLogger(BrowserHelper.class.getName());
    static {
        // Configure the global logging level for Selenium
        System.setProperty("java.util.logging.ConsoleHandler.level", "FINE");
        System.setProperty("org.openqa.selenium.remote.tracing.opentelemetry.logging.level", "FINE");

        // Configure the logger for your application
        Logger seleniumLogger = Logger.getLogger("org.openqa.selenium");
        seleniumLogger.setLevel(Level.FINE);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        seleniumLogger.addHandler(handler);
    }

    // Enum to represent browser types
    public enum BrowserType {
        CHROME,
        SAFARI
    }

    // Constructor to initialize the WebDriver based on the browser type
    public BrowserHelper(BrowserType browserType) {
        //logger.info("Initializing WebDriver for browser type: " + browserType);
        switch (browserType) {
            case CHROME:
                ChromeOptions options = new ChromeOptions();
                options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
                System.out.println("OS Architecture: " + System.getProperty("os.arch"));// Path to ARM64 Chrome
                driver = new ChromeDriver(options);
              //  driver = new ChromeDriver();  // Selenium Manager will handle driver setup automatically
                break;
            case SAFARI:
                driver = new SafariDriver();  // No driver setup needed for Safari
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type");
        }
        driver.manage().window().maximize();
       // logger.info("WebDriver initialized and window maximized.");
    }

    // Method to launch a website
public boolean launchWebsite(String url) {
    if (driver != null) {
        try {
            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name ='email']"))); // Adjust locator as needed);
            return true; // Return true if the website is launched successfully
        } catch (Exception e) {
               return false; // Return false if there is an exception
        }
    } else {
        // logger.warning("WebDriver is not initialized.");
        return false; // Return false if the WebDriver is not initialized
    }
}

    public void inputCredentials(String username, String password) {
        try {
            //logger.info("Entering credentials.");
            long startTime = System.currentTimeMillis();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name ='email']"))); // Adjust locator as needed);
           // WebElement usernameField = driver.findElement(By.xpath("//input[@name ='emaill']")); // Adjust locator as needed
            WebElement passwordField = driver.findElement(By.xpath("//input[@name ='pass']")); // Adjust locator as needed
            usernameField.click();
            usernameField.sendKeys(username);
            passwordField.click();
            passwordField.sendKeys(password);
            long endTime = System.currentTimeMillis();
           // logger.info("Credentials entered in " + (endTime - startTime) + " ms");
        } catch (Exception e) {
            //logger.log(Level.SEVERE, "Error entering credentials: ", e);
        }
    }

    // Method to quit the browser
    public void closeBrowser() {
        if (driver != null) {
            logger.info("Closing browser.");
            long startTime = System.currentTimeMillis();
            driver.quit();
            long endTime = System.currentTimeMillis();
            logger.info("Browser closed in " + (endTime - startTime) + " ms");
        } else {
            logger.warning("WebDriver is not initialized.");
        }
    }
}