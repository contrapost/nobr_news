package entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static entities.DataProcessor.updateInATransaction;

/**
 * Created by alex on 07.09.16.
 *
 */
public class UserTest {

    private EntityManagerFactory factory;
    private EntityManager em;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    @Test
    public void testEmptyUser(){

        User user = new User();
        assertTrue(updateInATransaction(Operations.PERSIST, em, user));
    }

    @Test
    public void testUserWithAddress(){

        Address address = new Address();

        assertTrue(updateInATransaction(Operations.PERSIST, em, address));

        User user = new User();
        user.setAddress(address);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user));
    }

    @Test
    public void testUserCanCreateNews(){
        User user = new User();

        News news = new News();
        String newsText = "Great news";
        news.setText(newsText);

        user.setNewses(new ArrayList<>());
        user.getNewses().add(news);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));

        assertTrue(em.find(User.class, user.getUserID()).getNewses().stream().anyMatch(n -> newsText.equals(n.getText())));
    }

    @Test
    public void testGetAllCountries(){
        User a = new User();
        a.setName("A");
        a.getAddress().setCity("Oslo");
        a.getAddress().setCountry("Norway");

        User b = new User();
        b.setName("B");
        b.getAddress().setCity("Madrid");
        b.getAddress().setCountry("Spain");

        User b2 = new User();
        b2.setName("B");
        b2.getAddress().setCity("Bergen");
        b2.getAddress().setCountry("Norway");

        User c = new User();
        c.setName("C");
        c.getAddress().setCity("London");
        c.getAddress().setCountry("England");

        assertTrue(updateInATransaction(Operations.PERSIST, em, a, b, b2, c));

        Query query = em.createNamedQuery(User.GET_ALL_COUNTRIES);

        List<String> countries = query.getResultList();

        assertEquals(3, countries.size());

        assertTrue(countries.contains("Spain"));
    }
}
