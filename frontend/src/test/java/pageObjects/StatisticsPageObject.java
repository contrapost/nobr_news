package pageObjects;

import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

/**
 * Created by alexandershipunov on 16/10/2016.
 */
public class StatisticsPageObject extends PageObject {

    public StatisticsPageObject(WebDriver driver) {
        super(driver);

        assertEquals("NOBR_NEWS statistics", driver.getTitle());
    }

    public boolean isOnPage(){
        return driver.getTitle().equals("NOBR_NEWS statistics");
    }
}
