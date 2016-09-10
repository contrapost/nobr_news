package entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.ArrayList;

import static entities.DataLoader.persistInATransaction;
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

        assertTrue(persistInATransaction(em, news));

        assertEquals(score, em.find(News.class, news.getNewsID()).getRating().getScore());
    }

    @Test
    public void testNewlyCreatedNewsHasZeroRating(){
        assertTrue(persistInATransaction(em, news));

        assertEquals(0, em.find(News.class, news.getNewsID()).getRating().getScore());
    }

    @Test
    public void testNewsRatingCanBeChanged(){
        int score = -1;

        news.getRating().setScore(score);

        assertTrue(persistInATransaction(em, news));

        int anotherScore = 1;
        News foundNews = em.find(News.class, news.getNewsID());
        foundNews.getRating().setScore(anotherScore);
        em.getTransaction().begin();
        em.merge(foundNews);
        em.getTransaction().commit();

        assertEquals(0, em.find(News.class, news.getNewsID()).getRating().getScore());
    }

    @Test
    public void testDeletedNewsHasNoRating(){
        int score = 1;

        news.getRating().setScore(score);

        assertTrue(persistInATransaction(em, news));
        assertEquals(1, em.find(News.class, news.getNewsID()).getRating().getScore());

        long ratingId = em.find(News.class, news.getNewsID()).getRating().getRatingID();

        em.getTransaction().begin();
        em.remove(news);
        em.getTransaction().commit();

        assertNull(em.find(Rating.class, ratingId));
    }

    @Test
    public void testNewCommentHasDefaultRating() {
        assertTrue(persistInATransaction(em, comment));

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

        assertTrue(persistInATransaction(em, news, comment));

        Comment foundComment = em.find(Comment.class, comment.getCommentID());
        foundComment.getRating().setScore(10);

        em.getTransaction().begin();
        em.merge(news);
        em.getTransaction().commit();

        News foundNews = em.find(News.class, news.getNewsID());

        foundNews.getComments().stream().filter(c -> c.getCommentID().equals(foundComment.getCommentID())).forEach(c -> assertEquals(5, c.getRating().getScore()));
    }

    @Test
    public void testDeletedNewsHasNowRatingForComments(){
        news.setComments(new ArrayList<>());
        news.getComments().add(comment);
        comment.getRating().setScore(20);

        assertTrue(persistInATransaction(em, news, comment));

        long ratingID = comment.getRating().getRatingID();

        em.find(News.class, news.getNewsID()).getComments().stream().
                filter(c -> comment.getCommentID().equals(c.getCommentID())).
                forEach(c -> assertEquals(20, c.getRating().getScore()));

        assertNotNull(em.find(Rating.class, ratingID));

        em.getTransaction().begin();
        em.remove(news);
        em.getTransaction().commit();

        assertNull(em.find(Rating.class, ratingID));
    }
}
