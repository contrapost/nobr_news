package ejb;

import entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Created by Alexander Shipunov on 06.10.16.
 *
 */
@Stateless
public class RatingEJB {

    @PersistenceContext(unitName = "DBAutomatic")
    private EntityManager em;

    public RatingEJB() {}

    public void voteForNews(@NotNull User user, @NotNull News post, @NotNull Votes vote) {
        News foundPost = em.find(News.class, post.getID());
        Rating foundRating = em.find(Rating.class, foundPost.getRating().getRatingID(), LockModeType.PESSIMISTIC_WRITE);
        foundRating.vote(vote, user);

        em.persist(foundRating);
    }
}
