package entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static entities.TestUtil.updateInATransaction;
import static org.junit.Assert.*;

/**
 * Created by alex on 07.09.16.
 *
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
        factory = Persistence.createEntityManagerFactory("DBManual");
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
        commentA.setText("CommentA");
        commentA.setDate(new Date());

        commentB = new Comment();
        commentB.setText("CommentB");
        commentB.setDate(new Date());

        commentC = new Comment();
        commentC.setText("CommentC");
        commentC.setDate(new Date());

        commentD = new Comment();
        commentD.setText("CommentD");
        commentD.setDate(new Date());

        commentE = new Comment();
        commentE.setText("CommentE");
        commentE.setDate(new Date());

        commentF = new Comment();
        commentF.setText("CommentF");
        commentF.setDate(new Date());

        commentG = new Comment();
        commentG.setText("CommentG");
        commentG.setDate(new Date());

        commentH = new Comment();
        commentH.setText("CommentH");
        commentH.setDate(new Date());
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

        userB = new User();
        userB.setName("Bob");
        userB.setSurname("Brom");
        userB.getAddress().setZipCode("1234");
        userB.getAddress().setStreet("Street");
        userB.getAddress().setCity("Madrid");
        userB.getAddress().setCountry("Spain");
        userB.setEmail("bob@brom.com");
        userB.setPassword("er78dfe");

        userC = new User();
        userC.setName("Chris");
        userC.setSurname("Chlor");
        userC.getAddress().setZipCode("1234");
        userC.getAddress().setStreet("Street");
        userC.getAddress().setCity("Bergen");
        userC.getAddress().setCountry("Norway");
        userC.setEmail("chris@chlor.com");
        userC.setPassword("er78dfe");

        userD = new User();
        userD.setName("David");
        userD.setSurname("Doom");
        userD.getAddress().setZipCode("1234");
        userD.getAddress().setStreet("Street");
        userD.getAddress().setCity("London");
        userD.getAddress().setCountry("England");
        userD.setEmail("david@doom.com");
        userD.setPassword("er78dfe");
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
        assertFalse(updateInATransaction(Operations.PERSIST, em, user));
    }

    @Test
    public void testUserCanCreateNews() {
        setNews(userA, newsA);

        assertTrue(updateInATransaction(Operations.PERSIST, em, userA, newsA));

        assertTrue(em.find(User.class, userA.getUserID()).getNewses().stream().anyMatch(n -> "NewsA".equals(n.getText())));
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
        query.setMaxResults(2); // Test will fail if we call setMaxResults(3)
        List<User> topUsers = query.getResultList(); //Get top 2
        Collections.sort(topUsers, (o1, o2) -> {
            if(o1.getNewses().size() + o1.getComments().size() == o2.getNewses().size() + o2.getComments().size())
                return 0;
            return (o1.getNewses().size() + o1.getComments().size()) > (o2.getNewses().size() + o2.getComments().size()) ? -1 : 1;
        });

        assertTrue(topUsers.contains(userA));
        assertTrue(topUsers.contains(userC));
        assertFalse(topUsers.contains(userD));
        assertFalse(topUsers.contains(userB));

        assertEquals(userA, topUsers.get(0));
    }

    @Test
    public void testExistingUser() {
        updateInATransaction(Operations.PERSIST, em, userA);

        Query query = em.createNamedQuery(User.GET_USER_BY_EMAIL);
        query.setParameter("email", "alex@aurum.com");
        List<User> users = query.getResultList();
        assertFalse(users.size() == 0);

        Query query2 = em.createNamedQuery(User.GET_USER_BY_EMAIL);
        query2.setParameter("email", "alex22@aurum.com");
        List<User> users2 = query2.getResultList();
        assertTrue(users2.size() == 0);
    }
}
