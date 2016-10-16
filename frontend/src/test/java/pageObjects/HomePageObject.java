package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by alexandershipunov on 16/10/2016.
 */
public class HomePageObject extends PageObject {

    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    public HomePageObject toStartingPage() {
        String context = "/nobr_news";
        driver.get("localhost:8080" + context + "/home.jsf");
        waitForPageToLoad();

        return this;
    }

    public boolean isOnPage() {
        return driver.getTitle().equals("NOBR_NEWS HOME PAGE");
    }

    public boolean isTextOnPage(String text) {
        List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"));
        return list.size() > 0;
    }

    public StatisticsPageObject clickStatistics(){
        driver.findElement(By.id("getstatisticsBtn")).click();
        waitForPageToLoad();
        return new StatisticsPageObject(driver);
    }

    public LoginPageObject toLogin() {
        if (isLoggedIn()) {
            logout();
        }

        driver.findElement(By.id("login")).click();
        waitForPageToLoad();
        return new LoginPageObject(driver);
    }


    public CreateNewsPageObject toCreateNews() {
        if (!isLoggedIn()) {
            return null;
        }

        driver.findElement(By.id("createNews")).click();
        waitForPageToLoad();
        return new CreateNewsPageObject(driver);
    }

    public int getNumberOfDisplayedNews() {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='newsTable']//tbody//tr[string-length(text()) > 0]"));

        return elements.size();
    }

    public int getRating(String text) {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='newsTable']//tbody//tr[contains(td[3], '" + text + "')]/td[6]")
        );
        if (elements.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(elements.get(0).getText());
    }
}
