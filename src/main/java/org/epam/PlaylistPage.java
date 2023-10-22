package org.epam;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class PlaylistPage extends BasePage{
    @FindBy(xpath = "//button[@aria-label='Создать плейлист или папку']")
    private WebElement plusIcon;
    @FindBy(xpath = "//button[@class='wC9sIed7pfp47wZbmU6m']")
    private WebElement createPlaylistButton;
    @FindBy(xpath = "//h1[@class='Type__TypeElement-sc-goli3j-0 dYGhLW']")
    private WebElement playlistName;
    @FindBy(xpath = "//div[@aria-labelledby='listrow-title-spotify:playlist:5RU24ZIZiyQxIoud2UyeNd listrow-subtitle-spotify:playlist:5RU24ZIZiyQxIoud2UyeNd']")
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
    @FindBy(xpath = "//span[@class='Type__TypeElement-sc-goli3j-0 ieTwfQ ellipsis-one-line PDPsYDh4ntfQE3B4duUI'and contains(text(),'Мой')]")
    private WebElement addToExactPlaylist;
    @FindBy(xpath = "//li[@class='ufICQKJq0XJE5iiIsZfj caTDfb6Oj7a5_8jBLUSo aRyoyQFJkzhoSOnf2ERM vOp2HlcPkxOHebo3If32 ETclQEbcAcQdGdSioHaJ qEiVyQ28VnOKb0LeijqL']")
    private WebElement newCreatedPlaylist;
    @FindBy(xpath = "//div[@class='Type__TypeElement-sc-goli3j-0 fZDcWX t_yrXoUO3qGsJS4Y6iXX standalone-ellipsis-one-line']")
    private WebElement addedTrack;

    protected PlaylistPage(WebDriver driver) {
        super(driver);
    }
    public PlaylistPage createPlaylist(){
        waitForElements(plusIcon);
        plusIcon.click();
        createPlaylistButton.click();

        return new PlaylistPage(driver);
    }
    public PlaylistPage assertPlaylistName(String expectedName){
        waitForElements(playlistName);
        String actualName = playlistName.getText();
        Assert.assertEquals(actualName,expectedName,"Incorrect Playlist naming.");
        return new PlaylistPage(driver);
    }
    public PlaylistPage editPlaylistName(){
        waitForElements(createdPlaylist);
        Actions actions = new Actions(driver);
        actions.contextClick(createdPlaylist).perform();
        waitForElements(changeInfoButton);
        changeInfoButton.click();
        waitForElements(nameInput);
        nameInput.clear();
        nameInput.sendKeys("My Favourite Playlist");
        saveButton.click();
        return new PlaylistPage(driver);
    }
    public void assertEditedPlaylistName(String expectedName){
        createdPlaylist.click();
        waitForElements(playlistName);
        String actualName = playlistName.getText();
        Assert.assertEquals(actualName,expectedName,"Incorrect Playlist naming.");
    }
    public PlaylistPage searchAndAddToPlaylist(){
        waitForElements(playlistName);
        searchIcon.click();
        waitForElements(searchBar);
        searchBar.click();
        searchBar.sendKeys("Whitney Elizabeth Houston");
        waitForElements(tracksFilter);
        tracksFilter.click();
        waitForElements(track);
        Actions actions = new Actions(driver);
        actions.contextClick(track).perform();
        addToPlaylist.click();
        addToExactPlaylist.click();

        return new PlaylistPage(driver);
    }
    public void assertTrackAddedToPlaylist(String expectedName){
        newCreatedPlaylist.click();
        waitForElements(addedTrack);
        String actualName = addedTrack.getText();
        Assert.assertEquals(actualName,expectedName,"Track is not added.");
    }
}
