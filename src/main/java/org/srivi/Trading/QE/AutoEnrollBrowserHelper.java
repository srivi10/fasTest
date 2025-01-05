package org.srivi.Trading.QE;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.*;
import java.awt.*;

public class AutoEnrollBrowserHelper {
    private WebDriver driver;
    private JTextArea consoleTextArea;

    public AutoEnrollBrowserHelper(JTextArea consoleTextArea) {
        this.consoleTextArea = consoleTextArea;
    }

    public void launchBrowser(boolean headless) {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
            if (headless) {
                options.addArguments("--headless");
            }
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            updateConsole("Launch Success", Color.BLACK);
        } catch (Exception e) {
            updateConsole("Browser launch Fail", Color.RED);
        }
    }

    private void updateConsole(String message, Color color) {
        consoleTextArea.setForeground(color);
        consoleTextArea.setText(message);
    }

    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}