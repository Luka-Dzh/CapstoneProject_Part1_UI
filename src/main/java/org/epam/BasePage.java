package org.epam;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public abstract class BasePage {
    protected WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }
    public WebElement waitForElements(WebElement element){
        new WebDriverWait(driver, Duration.ofSeconds(4)).until(ExpectedConditions.visibilityOf(element));
        return element;
    }
    public WebElement getSongFromList(String trackName) {
        List<WebElement> songsList = driver.findElements(By.xpath("//div[@data-testid=\"tracklist-row\"]//div[@dir=\"auto\"]"));
        return waitForElements(songsList.stream().filter(p -> p.getText().equals(trackName)).findFirst().get());
    }
}
