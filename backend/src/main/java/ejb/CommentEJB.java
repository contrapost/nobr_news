package ejb;

import entities.Comment;
import entities.News;
import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by alexandershipunov on 30/09/16.
 */
@Stateless
public class CommentEJB {

    @PersistenceContext
    private EntityManager em;

    public CommentEJB(){}

    public void createComment(@NotNull User user, @NotNull News news, @NotNull String text, @NotNull Date date) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setText(text);
        comment.setDate(date);
        news.getComments().add(comment);

        user.getComments().add(comment);

        em.persist(comment);
        em.persist(news);
        em.persist(user);
    }
}
