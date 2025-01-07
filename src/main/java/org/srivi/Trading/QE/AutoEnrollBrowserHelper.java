package org.srivi.Trading.QE;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

public class AutoEnrollBrowserHelper {
    private WebDriver driver;
    private JTextPane consoleTextPane;
    private StyledDocument doc;

    public AutoEnrollBrowserHelper(JTextPane consoleTextPane) {
        this.consoleTextPane = consoleTextPane;
        this.doc = consoleTextPane.getStyledDocument();
    }

    public boolean launchBrowser(String accountNumber, boolean headless, File autoEnrollFolder) {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
            if (headless) {
                options.addArguments("--headless");
            }
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // 10-second timeout

            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ResourceID.USERNAME)));

            updateConsole("Launch Success", Color.BLACK);
            return true;
        }
        catch (Exception e) {
            String[] lines = e.getMessage().split("\n");
            String firstThreeLines = String.join("\n", Arrays.copyOfRange(lines, 0, Math.min(1, lines.length)));
            updateConsole("Browser launch failed: " + firstThreeLines, Color.RED);
            takeScreenshot("LaunchBrowserFailed", autoEnrollFolder);
            return false;
        }
    }

    private void updateConsole(String message, Color color) {
        Style style = consoleTextPane.addStyle("Style", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), "> " + message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    public boolean performLogin(String username, String password, String accountNumber, File autoEnrollFolder) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // 10-second timeout

            // Wait for the username field to be visible
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ResourceID.USERNAME)));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ResourceID.PASSWORD)));

            //Username Action
            usernameField.click();
            usernameField.sendKeys(username);
            //updateConsole("Entered username", Color.BLACK);

            //Password Action
            passwordField.click();
            passwordField.sendKeys(password);
            //updateConsole("Entered password", Color.BLACK);
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ResourceID.SUBMIT)));

            //Login Button Action
            loginButton.click();
            WebElement PIMPAGE = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ResourceID.ADMIN)));
            updateConsole("LoginSuccess", Color.BLACK);
            return true;
        }
        catch (Exception e) {
            String[] lines = e.getMessage().split("\n");
            String firstThreeLines = String.join("\n", Arrays.copyOfRange(lines, 0, Math.min(1, lines.length)));
            updateConsole("Login operation failed: " + firstThreeLines, Color.RED);

            takeScreenshot("LoginFail", autoEnrollFolder);
            return false;
        }
    }

    // Screenshot Function
    public void takeScreenshot(String accountNumber, File filePath) {
        if (driver instanceof TakesScreenshot) {
            try {
                // Take screenshot and save it to the specified folder
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                // Format the timestamp for the screenshot name
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File screenshotFile = new File(filePath, accountNumber + timestamp + ".png");

                // Create the folder if it does not exist
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }

                // Save the screenshot file
                Files.copy(screenshot.toPath(), screenshotFile.toPath());
                updateConsole("Screenshot saved to: " + screenshotFile.getAbsolutePath(), Color.BLACK);
            }
            catch (IOException e) {
                updateConsole("Failed to save screenshot: " + e.getMessage(), Color.RED);
            }
        } else {
            updateConsole("Driver does not support screenshot capturing", Color.RED);
        }
    }
}