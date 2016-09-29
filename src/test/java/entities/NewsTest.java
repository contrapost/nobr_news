package entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;

import static entities.DataProcessor.updateInATransaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by alex on 10.09.16.
 *
 */
public class NewsTest {

    private News newsA;
    private News newsB;
    private News newsC;
    private News newsD;

    private EntityManagerFactory factory;
    private EntityManager em;
    private User userA;
    private User userB;
    private User userC;
    private User userD;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
        setNewses();
        setUsers();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    private void setNewses(){
        newsA = new News();
        newsB = new News();
        newsC = new News();
        newsD = new News();
    }

    private void setUsers() {
        userA = new User();
        userA.setName("A");
        userA.getAddress().setCity("Oslo");
        userA.getAddress().setCountry("Norway");

        userB = new User();
        userB.setName("B");
        userB.getAddress().setCity("Madrid");
        userB.getAddress().setCountry("Spain");

        userC = new User();
        userC.setName("C");
        userC.getAddress().setCity("Bergen");
        userC.getAddress().setCountry("Norway");

        userD = new User();
        userD.setName("D");
        userD.getAddress().setCity("London");
        userD.getAddress().setCountry("England");
    }

    private void setNews(User user, News news){
        user.setNewses(new ArrayList<>());
        user.getNewses().add(news);
    }

    @Test
    public void testTotalNumberOfNews(){
        assertTrue(updateInATransaction(Operations.PERSIST, em, newsA, newsB, newsC));

        Query query = em.createNamedQuery(News.TOTAL_NUMBER_OF_NEWS);

        long numberOfNews = (long) query.getSingleResult();

        assertEquals(3, numberOfNews);
    }

    @Test
    public void testTotalNumberOfNewsFromNorway(){
        setNews(userA, newsA);

        userB.setNewses(new ArrayList<>());
        userB.getNewses().add(newsB);

        userC.setNewses(new ArrayList<>());
        userC.getNewses().add(newsC);

        userD.setNewses(new ArrayList<>());
        userD.getNewses().add(newsD);

        newsA.setUser(userA);
        newsB.setUser(userB);
        newsC.setUser(userC);
        newsD.setUser(userD);

        assertTrue(updateInATransaction(Operations.PERSIST, em, newsA, newsB, newsC, newsD, userA, userB, userC, userD));


        Query query = em.createNamedQuery(News.TOTAL_NUMBER_OF_NEWS_FROM_COUNTRY);
        query.setParameter("country", "Norway");

        long numberOfNews = (long) query.getSingleResult();

        assertEquals(2, numberOfNews);
    }
}
