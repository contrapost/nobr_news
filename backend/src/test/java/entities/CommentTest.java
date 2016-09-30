package entities;

import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by alex on 10.09.16.
 *
 */
public class CommentTest {

    private EntityManagerFactory factory;
    private EntityManager em;
    private Comment comment;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DBManual");
        em = factory.createEntityManager();
        comment = new Comment();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }


}
