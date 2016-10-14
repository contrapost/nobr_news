package entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Date;

import static entities.TestUtil.updateInATransaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by alex on 10.09.16.
 */
public class NewsTest {

    private News newsA;
    private News newsB;
    private News newsC;
    private News newsD;

    private User userA;
    private User userB;
    private User userC;
    private User userD;

    private EntityManagerFactory factory;
    private EntityManager em;


    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DBManual");
        em = factory.createEntityManager();
        setUsers();
        setNewses();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    private void setNewses() {
        newsA = new News();
        newsB = new News();
        newsC = new News();
        newsD = new News();

        newsA.setText("NewsA");
        newsB.setText("NewsB");
        newsC.setText("NewsC");
        newsD.setText("NewsD");

        newsA.setDate(new Date());
        newsB.setDate(new Date());
        newsC.setDate(new Date());
        newsD.setDate(new Date());

        newsA.setAuthor(userA);
        newsB.setAuthor(userB);
        newsC.setAuthor(userC);
        newsD.setAuthor(userD);
    }

    private void setUsers() {
        userA = new User();
        userA.setName("Alex");
        userA.setSurname("Aurum");
        userA.getAddress().setZipCode("1234");
        userA.getAddress().setStreet("Street");
        userA.getAddress().setCity("Oslo");
        userA.getAddress().setCountry("Norway");
        userA.setEmail("alex@aurum.com");
        userA.setPassword("er78dfe");
        userA.setHash("hash");
        userA.setSalt("salt");

        userB = new User();
        userB.setName("Bob");
        userB.setSurname("Brom");
        userB.getAddress().setZipCode("1234");
        userB.getAddress().setStreet("Street");
        userB.getAddress().setCity("Madrid");
        userB.getAddress().setCountry("Spain");
        userB.setEmail("bob@brom.com");
        userB.setPassword("er78dfe");
        userB.setHash("hash");
        userB.setSalt("salt");

        userC = new User();
        userC.setName("Chris");
        userC.setSurname("Chlor");
        userC.getAddress().setZipCode("1234");
        userC.getAddress().setStreet("Street");
        userC.getAddress().setCity("Bergen");
        userC.getAddress().setCountry("Norway");
        userC.setEmail("chris@chlor.com");
        userC.setPassword("er78dfe");
        userC.setHash("hash");
        userC.setSalt("salt");

        userD = new User();
        userD.setName("David");
        userD.setSurname("Doom");
        userD.getAddress().setZipCode("1234");
        userD.getAddress().setStreet("Street");
        userD.getAddress().setCity("London");
        userD.getAddress().setCountry("England");
        userD.setEmail("david@doom.com");
        userD.setPassword("er78dfe");
        userD.setHash("hash");
        userD.setSalt("salt");
    }

    private void setNews(User user, News news) {
        user.getNewses().add(news);
    }

    @Test
    public void testTotalNumberOfNews() {


        assertTrue(updateInATransaction(Operations.PERSIST, em, userA, userB, userC, newsA, newsB, newsC));

        Query query = em.createNamedQuery(News.TOTAL_NUMBER_OF_NEWS);

        long numberOfNews = (long) query.getSingleResult();

        assertEquals(3, numberOfNews);
    }

    @Test
    public void testTotalNumberOfNewsFromNorway() {
        setNews(userA, newsA);
        setNews(userB, newsB);
        setNews(userC, newsC);
        setNews(userD, newsD);

        assertTrue(updateInATransaction(Operations.PERSIST, em, userA, userB, userC, userD, newsA, newsB, newsC, newsD));


        Query query = em.createNamedQuery(News.TOTAL_NUMBER_OF_NEWS_FROM_COUNTRY);
        query.setParameter("country", "Norway");

        long numberOfNews = (long) query.getSingleResult();

        assertEquals(2, numberOfNews);
    }
}
