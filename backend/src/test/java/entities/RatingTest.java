package entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

import static entities.TestUtil.*;
import static org.junit.Assert.*;

/**
 * Created by alex on 10.09.16.
 *
 */
public class RatingTest {

    private EntityManagerFactory factory;
    private EntityManager em;
    private News news;
    private Comment comment;
    private User user;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DBManual");
        em = factory.createEntityManager();

        user = new User();
        user.setName("Alex");
        user.setSurname("Aurum");
        user.getAddress().setZipCode("1234");
        user.getAddress().setStreet("Street");
        user.getAddress().setCity("Oslo");
        user.getAddress().setCountry("Norway");
        user.setEmail("alex@aurum.com");
        user.setPassword("er78dfe");

        news = new News();
        news.setText("NewsA");
        news.setDate(new Date());
        news.setAuthor(user);

        comment = new Comment();
        comment.setText("CommentA");
        comment.setDate(new Date());
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    @Test
    public void testNewsCanGetRating(){
        news.getRating().vote(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));

        assertEquals(1, em.find(News.class, news.getID()).getRating().getScore());
    }

    @Test
    public void testNewlyCreatedNewsHasZeroRating(){
        news.setAuthor(user);
        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));

        assertEquals(0, em.find(News.class, news.getID()).getRating().getScore());
    }

    @Test
    public void testNewsRatingCanBeChanged(){
        news.getRating().vote(Votes.DOWN, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));

        Votes antherVote = Votes.UP;
        User user2 = new User();
        user2.setName("Alex");
        user2.setSurname("Aurum");
        user2.getAddress().setZipCode("1234");
        user2.getAddress().setStreet("Street");
        user2.getAddress().setCity("Oslo");
        user2.getAddress().setCountry("Norway");
        user2.setEmail("alex@aurum.com");
        user2.setPassword("er78dfe");

        News foundNews = em.find(News.class, news.getID());
        foundNews.getRating().vote(antherVote, user2);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user2));

        assertTrue(updateInATransaction(Operations.UPDATE, em, foundNews));

        assertEquals(2, foundNews.getRating().getVotersNumber());

        assertEquals(0, em.find(News.class, news.getID()).getRating().getScore());
    }

    @Test
    public void testDeletedNewsHasNoRating(){
        news.getRating().vote(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));
        assertEquals(1, em.find(News.class, news.getID()).getRating().getScore());

        long ratingId = em.find(News.class, news.getID()).getRating().getRatingID();

        updateInATransaction(Operations.DELETE, em, news);

        assertNull(em.find(Rating.class, ratingId));
    }

    @Test
    public void testNewCommentHasDefaultRating() {
        comment.setAuthor(user);
        assertTrue(updateInATransaction(Operations.PERSIST, em, user, comment));

        Rating rating = em.find(Comment.class, comment.getID()).getRating();

        assertNotNull(rating);
        assertEquals(0, rating.getScore());
    }

    @Test
    public void testCommentRatingCanBeChanged() {
        comment.setAuthor(user);
        news.getComments().add(comment);
        news.setAuthor(user);
        comment.getRating().vote(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news, comment));

        Comment foundComment = em.find(Comment.class, comment.getID());

        User user2 = new User();
        user2.setName("Alex");
        user2.setSurname("Aurum");
        user2.getAddress().setZipCode("1234");
        user2.getAddress().setStreet("Street");
        user2.getAddress().setCity("Oslo");
        user2.getAddress().setCountry("Norway");
        user2.setEmail("alex@aurum.com");
        user2.setPassword("er78dfe");

        foundComment.getRating().vote(Votes.UP, user2);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user2));

        updateInATransaction(Operations.UPDATE, em, news);

        News foundNews = em.find(News.class, news.getID());

        foundNews.getComments().stream().filter(c -> c.getID() == (foundComment.getID())).forEach(c -> assertEquals(2, c.getRating().getScore()));
    }

    @Test
    public void testDeletedNewsHasNowRatingForComments(){
        comment.setAuthor(user);
        news.setAuthor(user);
        news.getComments().add(comment);
        comment.getRating().vote(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news, comment));

        long ratingID = comment.getRating().getRatingID();

        em.find(News.class, news.getID()).getComments().stream().
                filter(c -> comment.getID() == (c.getID())).
                forEach(c -> assertEquals(1, c.getRating().getScore()));

        assertNotNull(em.find(Rating.class, ratingID));

        updateInATransaction(Operations.DELETE, em, news);

        assertNull(em.find(Rating.class, ratingID));
    }

    @Test
    public void testUserCanVoteJustOnce(){
        news.setAuthor(user);
        news.getRating().vote(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));

        em.clear();

        News foundNews = em.find(News.class, news.getID());
        assertEquals(1, foundNews.getRating().getVotersNumber());

        foundNews.getRating().vote(Votes.UP, user);


        assertTrue(updateInATransaction(Operations.UPDATE, em, foundNews));

        assertEquals(1, em.find(News.class, news.getID()).getRating().getScore());
        assertEquals(1, em.find(News.class, news.getID()).getRating().getVotersNumber());

        em.clear();

        News againFoundNews = em.find(News.class, news.getID());

        User user2 = new User();
        user2.setName("Alex");
        user2.setSurname("Aurum");
        user2.getAddress().setZipCode("1234");
        user2.getAddress().setStreet("Street");
        user2.getAddress().setCity("Oslo");
        user2.getAddress().setCountry("Norway");
        user2.setEmail("alex@aurum.com");
        user2.setPassword("er78dfe");

        againFoundNews.getRating().vote(Votes.UP, user2);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user2));
        assertTrue(updateInATransaction(Operations.UPDATE, em, againFoundNews));

        assertEquals(2, em.find(News.class, news.getID()).getRating().getScore());
        assertEquals(2, em.find(News.class, news.getID()).getRating().getVotersNumber());
    }
}
