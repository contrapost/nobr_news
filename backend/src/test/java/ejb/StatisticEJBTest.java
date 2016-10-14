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
import org.omg.CORBA.Object;
import test.DeleterEJB;

import javax.ejb.EJB;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by alexandershipunov on 13/10/2016.
 *
 */
@RunWith(Arquillian.class)
public class StatisticEJBTest {

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
    private StatisticEJB statisticEJB;

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
    public void testStatistic() throws InterruptedException {
        Thread.sleep(11_000);
        List<String> countries = (List<String>) statisticEJB.getFromCache("ListOfAllCountries");
        assertEquals(3, countries.size());

        assertEquals(0L, statisticEJB.getFromCache("NumberOfAllNews"));

        newsEJB.createNews((User)userEJB.getUser("alex@alum.com"), "Text", new Date());

        statisticEJB.getFromCache("NumberOfAllNews");

        Thread.sleep(10_000);

        assertEquals(1L, statisticEJB.getFromCache("NumberOfAllNews"));
    }
}
