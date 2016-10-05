package ejb;

import entities.Address;
import entities.Comment;
import entities.News;
import entities.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 03.10.16.
 *
 */
@RunWith(Arquillian.class)
public class NewsEJBTest {

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
    public void setUp(){
        emptyDatabase();
        setUserEJBs();
    }

    @After
    public void tearDown(){
        emptyDatabase();
    }

    private void emptyDatabase() {
        deleterEJB.deleteEntities(News.class);
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(User.class);
    }

    private void setUserEJBs() {
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

        userEJB.createNewUser("Alex", "Alum", "alex@alum.com", "12we34ty", addressAlex);
        userEJB.createNewUser("Bart", "Blum", "bart@blum.com", "12we34ty", addressBart);
        userEJB.createNewUser("Conney", "Clum", "conney@clum.com", "12we34ty", addressConney);
        userEJB.createNewUser("Den", "Dlum", "den@dlum.com", "12we34ty", addressDen);
    }

    @Test
    public void testCreateNews() {
        Address address = new Address();
        address.setStreet("Street");
        address.setZipCode("1234");
        address.setCity("City");
        address.setCountry("Country");

        userEJB.createNewUser("name", "surname", "name@surname.com", "12we34ty", address);

        newsEJB.createNews(userEJB.getUser("name@surname.com"), "Text", new Date());

        assertEquals(1, newsEJB.getNumberOfAllNewses());
    }

    @Test
    public void testGetNumberOfNewsFromCountry() {
        commentEJB.createComment(userEJB.getUser("alex@alum.com"),
                newsEJB.createNews(userEJB.getUser("alex@alum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment(userEJB.getUser("alex@alum.com"),
                newsEJB.createNews(userEJB.getUser("alex@alum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment(userEJB.getUser("alex@alum.com"),
                newsEJB.createNews(userEJB.getUser("bart@blum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment(userEJB.getUser("alex@alum.com"),
                newsEJB.createNews(userEJB.getUser("conney@clum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment(userEJB.getUser("conney@clum.com"),
                newsEJB.createNews(userEJB.getUser("conney@clum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());
        commentEJB.createComment(userEJB.getUser("conney@clum.com"),
                newsEJB.createNews(userEJB.getUser("den@dlum.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());

        //3 newses from Norway (Alex has 2 and Bart has 1)

        assertEquals(3, newsEJB.gerNumberOfNewsFromCountry("Norway"));
    }
}
