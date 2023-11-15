package org.epam;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class PlaylistPage extends BasePage{
    @FindBy(xpath = "//button[@aria-label='Создать плейлист или папку']")
    private WebElement plusIcon;
    @FindBy(xpath = "//button[@class='wC9sIed7pfp47wZbmU6m']")
    private WebElement createPlaylistButton;
    @FindBy(xpath = "//h1[@class='Type__TypeElement-sc-goli3j-0 dYGhLW']")
    private WebElement playlistName;
    @FindBy(xpath = "//div[@class='Box__BoxComponent-sc-y4nds-0 MLyQK Box-sc-8t9c76-0 cLAfSn ojrThQm1wxR2gZ6GntJB']")
    private WebElement createdPlaylist;
    @FindBy(xpath = "//span[contains(text(),'Изменение сведений')]")
    private WebElement changeInfoButton;
    @FindBy(xpath = "//input[@data-testid='playlist-edit-details-name-input']")
    private WebElement nameInput;
    @FindBy(xpath = "//span[@class='ButtonInner-sc-14ud5tc-0 bzuYkS encore-inverted-light-set']")
    private WebElement saveButton;
    @FindBy(xpath = "//a[@aria-label='Поиск']")
    private  WebElement searchIcon;
    @FindBy(xpath = "//input[@class='Type__TypeElement-sc-goli3j-0 ieTwfQ QO9loc33XC50mMRUCIvf']")
    private WebElement searchBar;
    @FindBy(xpath = "//span[@class='ChipInner__ChipInnerComponent-sc-1ly6j4j-0 iyAEwU'and contains(text(),'Треки')]")
    private WebElement tracksFilter;
    @FindBy(xpath = "//div[@data-encore-id='type'and contains(text(),'I Will Always Love You')]")
    private WebElement track;
    @FindBy(xpath = "//button[@class='wC9sIed7pfp47wZbmU6m']")
    private WebElement addToPlaylist;
    @FindBy(xpath = "//span[@class='Type__TypeElement-sc-goli3j-0 ieTwfQ ellipsis-one-line PDPsYDh4ntfQE3B4duUI'and contains(text(),'New')]")
    private WebElement addToExactPlaylist;
    @FindBy(xpath = "//div[@class='Box__BoxComponent-sc-y4nds-0 MLyQK Box-sc-8t9c76-0 cLAfSn ojrThQm1wxR2gZ6GntJB']")
    private WebElement newCreatedPlaylist;
    @FindBy(xpath = "//div[@class='Type__TypeElement-sc-goli3j-0 fZDcWX t_yrXoUO3qGsJS4Y6iXX standalone-ellipsis-one-line']")
    private WebElement addedTrack;
    @FindBy(xpath = "//div[@data-encore-id='type'and contains(text(),'Greatest')]")
    private WebElement trackToRemove;
    @FindBy(xpath = "//span[@class='Type__TypeElement-sc-goli3j-0 ieTwfQ ellipsis-one-line PDPsYDh4ntfQE3B4duUI'and contains(text(),'Удалить')]")
    private WebElement deleteFromPlaylistButton;
    @FindBy(xpath = "//span[@class='Type__TypeElement-sc-goli3j-0 ieTwfQ ellipsis-one-line PDPsYDh4ntfQE3B4duUI'and text()='Удалить']")
    private WebElement deletePlaylistButton;
    @FindBy(xpath = "//span[@class='ButtonInner-sc-14ud5tc-0 glYGDr encore-bright-accent-set MIsUJlamzLYuAlvPbmZz'and text()='Удалить']")
    private WebElement confirmationToDelete;
    @FindBy(xpath = "//a[@class='MfVrtIzQJ7iZXfRWg6eM']")
    private WebElement checkDeletedPlaylist;

    protected PlaylistPage(WebDriver driver) {
        super(driver);
    }
    public PlaylistPage createPlaylist(){
        waitForElements(plusIcon);
        plusIcon.click();
        createPlaylistButton.click();

        return new PlaylistPage(driver);
    }
    public PlaylistPage assertPlaylistName(){
        waitForElements(playlistName);
        String actualName = playlistName.getText();
        Assert.assertTrue(actualName.contains("Мой плейлист №"),"Incorrect Playlist naming.");
        return new PlaylistPage(driver);
    }
    public PlaylistPage editPlaylistName(){
        waitForElements(createdPlaylist);
        Actions actions = new Actions(driver);
        actions.contextClick(createdPlaylist).perform();
        waitForElements(changeInfoButton).click();
        waitForElements(nameInput).clear();
        nameInput.sendKeys("My Favourite Playlist");
        saveButton.click();
        return new PlaylistPage(driver);
    }
    public void assertEditedPlaylistName(String expectedName){
        waitForElements(newCreatedPlaylist).click();
        waitForElements(playlistName);
        String actualName = playlistName.getText();
        Assert.assertEquals(actualName,expectedName,"Incorrect Playlist naming.");
    }
    public PlaylistPage searchAndAddToPlaylist(){
        waitForElements(searchIcon);
        searchIcon.click();
        waitForElements(searchBar).click();
        searchBar.sendKeys("Whitney Houston");
        waitForElements(tracksFilter).click();
        waitForElements(track);
        Actions actions = new Actions(driver);
        actions.contextClick(track).perform();
        waitForElements(addToPlaylist).click();
        waitForElements(addToExactPlaylist).click();
        newCreatedPlaylist.click();
        return new PlaylistPage(driver);
    }
    public void assertTrackAddedToPlaylist(String expectedName){
        newCreatedPlaylist.click();
        waitForElements(addedTrack);
        String actualName = addedTrack.getText();
        Assert.assertEquals(actualName,expectedName,"Track is not added.");
    }
    public PlaylistPage removeTrackFromPlaylist(){
        waitForElements(playlistName);
        searchIcon.click();
        waitForElements(searchBar).click();
        searchBar.sendKeys("Whitney Houston");
        waitForElements(tracksFilter).click();
        waitForElements(trackToRemove);
        Actions actions = new Actions(driver);
        actions.contextClick(trackToRemove).perform();
        addToPlaylist.click();
        addToExactPlaylist.click();
        newCreatedPlaylist.click();
        waitForElements(trackToRemove);
        actions.contextClick(trackToRemove).perform();
        deleteFromPlaylistButton.click();
        return new PlaylistPage(driver);
    }
    public void assertTrackIsRemoved(){
        newCreatedPlaylist.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOf(trackToRemove));
        try {
            trackToRemove.click();
            Assert.fail("Track is not deleted.");
        } catch (NoSuchElementException ignored){}
    }
    public PlaylistPage deletePlaylist(){
        waitForElements(playlistName);
        Actions actions = new Actions(driver);
        actions.contextClick(newCreatedPlaylist).perform();
        waitForElements(deletePlaylistButton).click();
        waitForElements(confirmationToDelete).click();
        return new PlaylistPage(driver);
    }
    public void assertPlaylistIsDeleted(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(checkDeletedPlaylist));
        try {
            newCreatedPlaylist.click();
            Assert.fail("Playlist is not deleted.");
        } catch (NoSuchElementException ignored){}
    }
}
