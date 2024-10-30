package org.srivi.Trading;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserHelper {
    private WebDriver driver;

    // Enum to represent browser types
    public enum BrowserType {
        CHROME,
        SAFARI
    }

    // Constructor to initialize the WebDriver based on the browser type
    public BrowserHelper(BrowserType browserType) {
        switch (browserType) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case SAFARI:
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type");
        }
        driver.manage().window().maximize();
    }

    // Method to launch a website
    public void launchWebsite(String url) {
        if (driver != null) {
            driver.get(url);
        }
    }

    public void inputCredentials(String username, String password) {
        WebElement usernameField = driver.findElement(By.xpath("//input[@name ='email']")); // Adjust locator as needed
        WebElement passwordField = driver.findElement(By.xpath("//input[@name ='pass']")); // Adjust locator as needed
        usernameField.click();
        usernameField.sendKeys(username);
        passwordField.click();
        passwordField.sendKeys(password);
    }

    // Method to quit the browser
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
