package org.epam;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
public class BaseTest {
    protected final WebDriver driver = new ChromeDriver();

    protected void setUpWebDriver() {
        WebDriverManager.chromedriver().setup();
        driver.manage().window().maximize();
    }

    protected void quit() {
        driver.quit();
    }
}
