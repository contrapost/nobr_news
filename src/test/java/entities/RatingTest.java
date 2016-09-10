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

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
        news = new News();
        comment = new Comment();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    @Test
    public void testNewsCanGetRating(){
        int score = -1;
        news.getRating().setScore(score);

        assertTrue(updateInATransaction(Operations.PERSIST, em, news));

        assertEquals(score, em.find(News.class, news.getNewsID()).getRating().getScore());
    }

    @Test
    public void testNewlyCreatedNewsHasZeroRating(){
        assertTrue(updateInATransaction(Operations.PERSIST, em, news));

        assertEquals(0, em.find(News.class, news.getNewsID()).getRating().getScore());
    }

    @Test
    public void testNewsRatingCanBeChanged(){
        int score = -1;

        news.getRating().setScore(score);

        assertTrue(updateInATransaction(Operations.PERSIST, em, news));

        int anotherScore = 1;
        News foundNews = em.find(News.class, news.getNewsID());
        foundNews.getRating().setScore(anotherScore);

        updateInATransaction(Operations.UPDATE, em, foundNews);

        assertEquals(0, em.find(News.class, news.getNewsID()).getRating().getScore());
    }

    @Test
    public void testDeletedNewsHasNoRating(){
        int score = 1;

        news.getRating().setScore(score);

        assertTrue(updateInATransaction(Operations.PERSIST, em, news));
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
        comment.getRating().setScore(-5);

        assertTrue(updateInATransaction(Operations.PERSIST, em, news, comment));

        Comment foundComment = em.find(Comment.class, comment.getCommentID());
        foundComment.getRating().setScore(10);

        updateInATransaction(Operations.UPDATE, em, news);

        News foundNews = em.find(News.class, news.getNewsID());

        foundNews.getComments().stream().filter(c -> c.getCommentID().equals(foundComment.getCommentID())).forEach(c -> assertEquals(5, c.getRating().getScore()));
    }

    @Test
    public void testDeletedNewsHasNowRatingForComments(){
        news.setComments(new ArrayList<>());
        news.getComments().add(comment);
        comment.getRating().setScore(20);

        assertTrue(updateInATransaction(Operations.PERSIST, em, news, comment));

        long ratingID = comment.getRating().getRatingID();

        em.find(News.class, news.getNewsID()).getComments().stream().
                filter(c -> comment.getCommentID().equals(c.getCommentID())).
                forEach(c -> assertEquals(20, c.getRating().getScore()));

        assertNotNull(em.find(Rating.class, ratingID));

        updateInATransaction(Operations.DELETE, em, news);

        assertNull(em.find(Rating.class, ratingID));
    }
}
