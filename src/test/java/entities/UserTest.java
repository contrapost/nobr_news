package entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.ArrayList;

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
}
