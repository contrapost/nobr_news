package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

/**
 * Created by alexandershipunov on 16/10/2016.
 */
public class CreateNewsPageObject extends PageObject {

    public CreateNewsPageObject(WebDriver driver) {
        super(driver);

        assertEquals("Create News", driver.getTitle());
    }

    public boolean isOnPage(){
        return driver.getTitle().equals("Create News");
    }

    public HomePageObject createNews(String text){
        setText("createNewsForm:text",text);

        driver.findElement(By.id("createNewsForm:createNewsBtn")).click();
        waitForPageToLoad();

        if(isOnPage()){
            return null;
        } else {
            return new HomePageObject(driver);
        }
    }

}
