package ejb;

import entities.Address;
import entities.Comment;
import entities.News;
import entities.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.spi.ArquillianProxyException;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.Throwables;
import org.junit.runner.RunWith;
import test.DeleterEJB;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.validation.ConstraintViolationException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by alexandershipunov on 30/09/16.
 */
@RunWith(Arquillian.class)
public class UserEJBTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "ejb")
                .addClass(DeleterEJB.class)
                .addPackages(true, "entities")
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }


    @EJB
    private UserEJB userEJB;

    @EJB
    private NewsEJB newsEJB;

    @EJB
    private CommentEJB commentEJB;

    @EJB
    private DeleterEJB deleterEJB;

    @Before
    public void setUp() {
        deleterEJB.deleteEntities(News.class);
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(User.class);
        setUserEJBs();
    }

    @After
    public void emptyDatabase() {
        deleterEJB.deleteEntities(News.class);
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(User.class);
    }

    public void setUserEJBs() {
        //Alex's address
        Address addressAlex = new Address();
        addressAlex.setStreet("Street");
        addressAlex.setZipCode("1234");
        addressAlex.setCity("City");
        addressAlex.setCountry("Norway");

        //Bart's address
        Address addressBart = new Address();
        addressBart.setStreet("Street");
        addressBart.setZipCode("1234");
        addressBart.setCity("City");
        addressBart.setCountry("Norway");

        //Conney's address
        Address addressConney = new Address();
        addressConney.setStreet("Street");
        addressConney.setZipCode("1234");
        addressConney.setCity("City");
        addressConney.setCountry("Siberia");

        //Den's address
        Address addressDen = new Address();
        addressDen.setStreet("Street");
        addressDen.setZipCode("1234");
        addressDen.setCity("City");
        addressDen.setCountry("Babel");

        userEJB.createUser("Alex", "Alum", "alex@alum.com", "12we34ty", addressAlex);
        userEJB.createUser("Bart", "Blum", "bart@blum.com", "12we34ty", addressBart);
        userEJB.createUser("Conney", "Clum", "conney@clum.com", "12we34ty", addressConney);
        userEJB.createUser("Den", "Dlum", "den@dlum.com", "12we34ty", addressDen);
    }

    @Test
    public void testCreateUser() {
        Address address = new Address();
        address.setStreet("Street");
        address.setZipCode("1234");
        address.setCity("City");
        address.setCountry("Country");

        userEJB.createUser("name", "surname", "name@surname.com", "12we34ty", address);

        //Four users have been created before in setUp()
        assertEquals(5, userEJB.getNumberOfAllUsers());
    }

    @Test
    public void testGetAllCountries() {
        //Both Alex and Bart are from Norway, so it should be 3 different countries
        assertEquals(3, userEJB.getAllCountries().size());
    }

    @Test
    public void testGetNumberOfAllUsers() {

        assertEquals(4, userEJB.getNumberOfAllUsers());
    }

    @Test
    public void testGetNumberOfUsersFromCountry() {

        assertEquals(2, userEJB.getNumberOfUsersFromCountry("Norway"));
    }

    @Test
    public void testGetXTopUsers() {
        commentEJB.createComment((User)userEJB.getUser("alex@alum.com"),
                newsEJB.createNews((User)userEJB.getUser("alex@alum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment((User)userEJB.getUser("alex@alum.com"),
                newsEJB.createNews((User)userEJB.getUser("alex@alum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment((User)userEJB.getUser("alex@alum.com"),
                newsEJB.createNews((User)userEJB.getUser("bart@blum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment((User)userEJB.getUser("alex@alum.com"),
                newsEJB.createNews((User)userEJB.getUser("conney@clum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment((User)userEJB.getUser("conney@clum.com"),
                newsEJB.createNews((User)userEJB.getUser("conney@clum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment((User)userEJB.getUser("conney@clum.com"),
                newsEJB.createNews((User)userEJB.getUser("den@dlum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());

        //Alex has 2 newses and 4 comments, Conney has 2 newses and 2 comments, Bart has 1 news, Den has 1 news

        assertEquals(2, userEJB.getXTopUsers(2).size());

        assertEquals("alex@alum.com", userEJB.getXTopUsers(2).get(0).getEmail());
        assertEquals("conney@clum.com", userEJB.getXTopUsers(2).get(1).getEmail());
        assertFalse(userEJB.getXTopUsers(2).stream().anyMatch(user -> "den@dlum.com".equals(user.getEmail())));
        assertFalse(userEJB.getXTopUsers(2).stream().anyMatch(user -> "bart@blum.com".equals(user.getEmail())));
    }

    @Test
    public void testUserConstraint() {
        Address address = new Address();
        address.setStreet("Street");
        address.setZipCode("1234");
        address.setCity("City");
        address.setCountry("Country");

        try {
            userEJB.createUser(null, "surname", "name@surname.com", "12we34ty", address);
        } catch (EJBException e) {
//            Throwable cause = com.google.common.base.Throwables.getRootCause(e);
//            assertTrue("Cause: " + cause, cause instanceof ConstraintViolationException);
        }
    }

    @Test
    public void testCreateUsersWithSameEmail() throws InterruptedException {
        //Two threads tries to create two users with the same email address
        Thread threadA = new Thread(this::createUser);

        Thread threadB = new Thread(this::createUser);

        threadA.start();
        threadB.start();

        Thread.sleep(5_000);
        assertEquals(5, userEJB.getNumberOfAllUsers());

        threadA.join();
        threadB.join();
        //Four users have been created before in setUp()
        assertEquals(5, userEJB.getNumberOfAllUsers());

        //Two threads tries to create two users with different email addresses
        Thread threadC = new Thread(() -> {
            userEJB.createUser("name", "surname", "name1@surname.com", "12we34ty", createAddress());
        });

        Thread threadD = new Thread(() -> {
            userEJB.createUser("name", "surname", "name2@surname.com", "12we34ty", createAddress());
        });

        threadC.start();
        threadD.start();

        threadC.join();
        threadD.join();
        assertEquals(7, userEJB.getNumberOfAllUsers());
    }

    private void createUser() {
        userEJB.createUser("name", "surname", "name@surname.com", "12we34ty", createAddress());
    }

    private Address createAddress() {
        Address address = new Address();
        address.setStreet("Street");
        address.setZipCode("1234");
        address.setCity("City");
        address.setCountry("Country");

        return address;
    }
}
