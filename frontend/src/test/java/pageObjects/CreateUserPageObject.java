package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;

/**
 * Created by alexandershipunov on 16/10/2016.
 */
public class CreateUserPageObject extends PageObject {

    public CreateUserPageObject(WebDriver driver) {
        super(driver);

        assertEquals("Create User", driver.getTitle());
    }

    public boolean isOnPage(){
        return driver.getTitle().equals("Create User");
    }

    public HomePageObject createUser(String userEmail, String password, String confirmPassword,
                                     String userName, String userSurname, String street, String zip, String city, String country){

        setText("createUserForm:userEmail",userEmail);
        setText("createUserForm:password",password);
        setText("createUserForm:confirmPassword",confirmPassword);
        setText("createUserForm:userName",userName);
        setText("createUserForm:userSurname",userSurname);
        setText("createUserForm:street",street);
        setText("createUserForm:zip",zip);
        setText("createUserForm:city",city);
        setText("createUserForm:country",country);

        driver.findElement(By.id("createUserForm:createButton")).click();
        waitForPageToLoad();

        if(isOnPage()){
            return null;
        } else {
            return new HomePageObject(driver);
        }
    }

}
