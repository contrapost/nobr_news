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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static entities.DataProcessor.updateInATransaction;

/**
 * Created by alex on 07.09.16.
 */
public class UserTest {

    private User userA;
    private User userB;
    private User userC;
    private User userD;

    private News newsA;
    private News newsB;
    private News newsC;
    private News newsD;

    private Comment commentA;
    private Comment commentB;
    private Comment commentC;
    private Comment commentD;
    private Comment commentE;
    private Comment commentF;
    private Comment commentG;
    private Comment commentH;

    private EntityManagerFactory factory;
    private EntityManager em;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
        setUsers();
        setNewses();
        setComments();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    private void setComments() {
        commentA = new Comment();
        commentB = new Comment();
        commentC = new Comment();
        commentD = new Comment();
        commentE = new Comment();
        commentF = new Comment();
        commentG = new Comment();
        commentH = new Comment();
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

    private void setNewses() {
        newsA = new News();
        newsB = new News();
        newsC = new News();
        newsD = new News();
    }

    private void setNews(User user, News ... newses) {
        for(News news : newses) user.getNewses().add(news);
    }

    private void addCommentToNews(News news, Comment ... comments) {
        for(Comment comment : comments) news.getComments().add(comment);
    }

    private void setAuthorOfComment(User user, Comment ... comments) {
        for(Comment comment : comments) {
            comment.setAuthor(user);
            user.getComments().add(comment);
        }
    }

    @Test
    public void testEmptyUser() {

        User user = new User();
        assertTrue(updateInATransaction(Operations.PERSIST, em, user));
    }

    @Test
    public void testUserCanCreateNews() {
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
    public void testGetAllCountries() {

        assertTrue(updateInATransaction(Operations.PERSIST, em, userA, userB, userC, userD));

        Query query = em.createNamedQuery(User.GET_ALL_COUNTRIES);

        List<String> countries = query.getResultList();

        assertEquals(3, countries.size());

        assertTrue(countries.contains("Spain"));
    }

    @Test
    public void testNumberOfUsers() {
        assertTrue(updateInATransaction(Operations.PERSIST, em, userA, userB, userC, userD));

        assertEquals(4, (long) em.createNamedQuery(User.GET_NUMBER_OF_ALL_USERS).getSingleResult());
    }

    @Test
    public void testNumberOfUsersFromCountry() {
        assertTrue(updateInATransaction(Operations.PERSIST, em, userA, userB, userC, userD));

        Query query = em.createNamedQuery(User.GET_NUMBER_OF_ALL_USERS_FROM_COUNTRY);

        query.setParameter("country", "Norway");

        long countries = (long) query.getSingleResult();

        assertEquals(2, countries);
    }

    @Test
    public void testGetXTopUsers() {
        setNews(userA, newsA, newsB);
        setNews(userB, newsC);
        setNews(userC, newsD);

        // UserA has 2 newses, userB and userC have 1 news each, userD has no newses

        setAuthorOfComment(userA, commentA, commentB, commentC);
        setAuthorOfComment(userB, commentD);
        setAuthorOfComment(userC, commentE, commentF, commentG);
        setAuthorOfComment(userD, commentH);

        // UserA has 3 comments, userB and UserD have 1 comment each and userC has 3 comments
        // UserA has 5, userB has 2, userC has 4, UserD has 1

        addCommentToNews(newsA, commentA, commentB);
        addCommentToNews(newsB, commentC,commentD);
        addCommentToNews(newsC, commentE, commentF);
        addCommentToNews(newsD, commentG, commentH);

        assertTrue(updateInATransaction(Operations.PERSIST, em, userA, userB, userC, userD,
                newsA, newsB, newsC, newsD, commentA, commentB, commentC, commentD, commentE, commentF,
                commentG, commentH));

        assertEquals(5, em.find(User.class, userA.getUserID()).getNewses().size() +
                em.find(User.class, userA.getUserID()).getComments().size());

        assertEquals(4, em.find(User.class, userC.getUserID()).getNewses().size() +
                em.find(User.class, userC.getUserID()).getComments().size());

        Query query = em.createNamedQuery(User.GET_TOP_USERS);
        List<User> topUsers = query.getResultList().subList(0, 2);

        assertTrue(topUsers.contains(userA));
        assertTrue(topUsers.contains(userC));
        assertFalse(topUsers.contains(userD));
        assertFalse(topUsers.contains(userB));

        assertEquals(userA, topUsers.get(0));
    }
}
