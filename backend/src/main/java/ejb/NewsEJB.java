package ejb;

import entities.News;
import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by alexandershipunov on 30/09/16.
 *
 */
@Stateless
public class NewsEJB {

    @PersistenceContext(unitName = "DBAutomatic")
    private EntityManager em;

    public NewsEJB(){}

    public News createNews(@NotNull User user, @NotNull String text, @NotNull Date date) {
        News news = new News();
        news.setAuthor(user);
        news.setText(text);
        news.setDate(date);

        User foundUser = em.find(User.class, user.getUserID());
        foundUser.getNewses().add(news);

        em.persist(foundUser);
        em.persist(news);

        return news;
    }

    public long getNumberOfAllNewses() {
        Query query = em.createNamedQuery(News.TOTAL_NUMBER_OF_NEWS);
        return (long) query.getSingleResult();
    }

    public long gerNumberOfNewsFromCountry(String ciuntryName) {
        Query query = em.createNamedQuery(News.TOTAL_NUMBER_OF_NEWS_FROM_COUNTRY);
        query.setParameter("country", ciuntryName);
        return (long) query.getSingleResult();
    }

    public List<News> getAllNews() {
        return em.createNamedQuery(News.GET_ALL_NEWS).getResultList();
    }
}
