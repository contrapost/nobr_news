import org.junit.Before;
import org.junit.Test;
import pageObjects.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assume.assumeTrue;

/**
 * Created by alexandershipunov on 16/10/2016.
 */
public class WebPageIT extends WebTestBase {
    @Before
    public void startFromInitialPage() {

        assumeTrue(JBossUtil.isJBossUpAndRunning());

        home = new HomePageObject(getDriver());
        home.toStartingPage();
        home.logout();
        assertTrue(home.isOnPage());
        assertFalse(home.isLoggedIn());
    }

    @Test
    public void testHomePage(){
        home.toStartingPage();
        assertTrue(home.isOnPage());
    }


    @Test
    public void testLoginLink(){
        LoginPageObject login = home.toLogin();
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLoginWrongUser(){
        LoginPageObject login = home.toLogin();
        HomePageObject home = login.clickLogin(getUniqueUserEmail(),"foo");
        assertNull(home);
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLogin(){
        String userEmail = getUniqueUserEmail();
        createAndLogNewUser(userEmail, "Joe", "Black", "United States");
        home.logout();

        assertFalse(home.isLoggedIn());
        LoginPageObject login = home.toLogin();
        home = login.clickLogin(userEmail, "foobar");

        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userEmail));
    }

    @Test
    public void testCreateValidUser(){
        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();
        assertTrue(create.isOnPage());

        String userEmail = getUniqueUserEmail();

        HomePageObject home = create.createUser(userEmail,"foobar", "foobar", "Foo", "Bar", "street", "zipCode", "city", "Norway");
        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userEmail));

        home.logout();
        assertFalse(home.isLoggedIn());
    }

    @Test
    public void testCreateUserFailDueToPasswordMismatch(){
        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();

        HomePageObject home = create.createUser(getUniqueUserEmail(),"foobar","differentPassword","Foo", "Bar", "street", "zipCode", "city", "Norway");
        assertNull(home);
        assertTrue(create.isOnPage());
    }


    @Test
    public void testCreateOneNews(){

        String userEmail = getUniqueUserEmail();
        createAndLogNewUser(userEmail, "Foo", "Bar", "Norway");

        int n = home.getNumberOfDisplayedNews();
        String text = getUniqueText();

        CreateNewsPageObject create = home.toCreateNews();
        home = create.createNews(text);

        assertNotNull(home);
        int x = home.getNumberOfDisplayedNews();
        assertEquals(n+1, x);
        assertTrue(getPageSource().contains(text));

        int rating = home.getRating(text);
        assertEquals(0, rating);
    }

    @Test
    public void testNewsIsAvailableForNonRegisteredUsers() {
        String userEmail = getUniqueUserEmail();
        String text = getUniqueText();
        int numberOfDisplayedNews = home.getNumberOfDisplayedNews();

        createAndLogNewUser(userEmail, "Tom", "Sawyer", "USA");
        CreateNewsPageObject create = home.toCreateNews();
        home = create.createNews(text);

        assertEquals(++numberOfDisplayedNews, home.getNumberOfDisplayedNews());

        home.logout();

        assertTrue(home.isTextOnPage(text));
    }

    @Test
    public void testStatisticsAvailableBothForRegisteredAndNonRegisteredUsers() {
        String userEmail = getUniqueUserEmail();
        createAndLogNewUser(userEmail, "Homer", "Simpson", "Ecuador");

        home.clickStatistics();

        assertTrue(home.isTextOnPage("Top 10 users:"));

        home.logout();

        home.clickStatistics();

        assertTrue(home.isTextOnPage("Top 10 users:"));
    }
}
