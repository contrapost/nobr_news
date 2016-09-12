package entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;

import static entities.DataProcessor.*;
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
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
        news = new News();
        comment = new Comment();
        user = new User();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    @Test
    public void testNewsCanGetRating(){
        news.getRating().setScore(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));

        assertEquals(1, em.find(News.class, news.getNewsID()).getRating().getScore());
    }

    @Test
    public void testNewlyCreatedNewsHasZeroRating(){
        assertTrue(updateInATransaction(Operations.PERSIST, em, news));

        assertEquals(0, em.find(News.class, news.getNewsID()).getRating().getScore());
    }

    @Test
    public void testNewsRatingCanBeChanged(){
        news.getRating().setScore(Votes.DOWN, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));

        Votes antherVote = Votes.UP;
        User user2 = new User();
        News foundNews = em.find(News.class, news.getNewsID());
        foundNews.getRating().setScore(antherVote, user2);

       assertTrue(updateInATransaction(Operations.PERSIST, em, user2));

        assertTrue(updateInATransaction(Operations.UPDATE, em, foundNews));

        assertEquals(2, foundNews.getRating().getVotersNumber());

        assertEquals(0, em.find(News.class, news.getNewsID()).getRating().getScore());
    }

    @Test
    public void testDeletedNewsHasNoRating(){
        news.getRating().setScore(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));
        assertEquals(1, em.find(News.class, news.getNewsID()).getRating().getScore());

        long ratingId = em.find(News.class, news.getNewsID()).getRating().getRatingID();

        updateInATransaction(Operations.DELETE, em, news);

        assertNull(em.find(Rating.class, ratingId));
    }

    @Test
    public void testNewCommentHasDefaultRating() {
        assertTrue(updateInATransaction(Operations.PERSIST, em, comment));

        Rating rating = em.find(Comment.class, comment.getCommentID()).getRating();

        assertNotNull(rating);
        assertEquals(0, rating.getScore());
    }

    @Test
    public void testCommentRatingCanBeChanged() {
        News news = new News();
        news.setComments(new ArrayList<>());
        news.getComments().add(comment);
        comment.getRating().setScore(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news, comment));

        Comment foundComment = em.find(Comment.class, comment.getCommentID());
        User user2 = new User();
        foundComment.getRating().setScore(Votes.UP, user2);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user2));

        updateInATransaction(Operations.UPDATE, em, news);

        News foundNews = em.find(News.class, news.getNewsID());

        foundNews.getComments().stream().filter(c -> c.getCommentID().equals(foundComment.getCommentID())).forEach(c -> assertEquals(2, c.getRating().getScore()));
    }

    @Test
    public void testDeletedNewsHasNowRatingForComments(){
        news.setComments(new ArrayList<>());
        news.getComments().add(comment);
        comment.getRating().setScore(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news, comment));

        long ratingID = comment.getRating().getRatingID();

        em.find(News.class, news.getNewsID()).getComments().stream().
                filter(c -> comment.getCommentID().equals(c.getCommentID())).
                forEach(c -> assertEquals(1, c.getRating().getScore()));

        assertNotNull(em.find(Rating.class, ratingID));

        updateInATransaction(Operations.DELETE, em, news);

        assertNull(em.find(Rating.class, ratingID));
    }

    @Test
    public void testUserCanVoteJustOnce(){
        news.getRating().setScore(Votes.UP, user);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user, news));

        em.clear();

        News foundNews = em.find(News.class, news.getNewsID());
        assertEquals(1, foundNews.getRating().getVotersNumber());

        foundNews.getRating().setScore(Votes.UP, user);


        assertTrue(updateInATransaction(Operations.UPDATE, em, foundNews));

        assertEquals(1, em.find(News.class, news.getNewsID()).getRating().getScore());
        assertEquals(1, em.find(News.class, news.getNewsID()).getRating().getVotersNumber());

        em.clear();

        News againFoundNews = em.find(News.class, news.getNewsID());
        User user2 = new User();
        againFoundNews.getRating().setScore(Votes.UP, user2);

        assertTrue(updateInATransaction(Operations.PERSIST, em, user2));
        assertTrue(updateInATransaction(Operations.UPDATE, em, againFoundNews));

        assertEquals(2, em.find(News.class, news.getNewsID()).getRating().getScore());
        assertEquals(2, em.find(News.class, news.getNewsID()).getRating().getVotersNumber());
    }
}
