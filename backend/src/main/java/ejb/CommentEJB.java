package ejb;

import entities.Comment;
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
public class CommentEJB {

    @PersistenceContext(unitName = "DBAutomatic")
    private EntityManager em;

    public CommentEJB(){}

    public void createComment(@NotNull User user, @NotNull News news, @NotNull String text, @NotNull Date date) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setText(text);
        comment.setDate(date);

        News foundNews = em.find(News.class, news.getNewsID());
        User foundUser = em.find(User.class, user.getUserID());

        foundNews.getComments().add(comment);

        foundUser.getComments().add(comment);

        em.persist(comment);
        em.persist(foundNews);
        em.persist(foundUser);
    }

    public List<Comment> getAllComments(){
        Query query = em.createNamedQuery(Comment.GET_ALL_COMMENTS);
        return (List<Comment>) query.getResultList();
    }
}
