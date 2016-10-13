package ejb;

import entities.News;
import entities.Rating;
import entities.User;
import entities.Votes;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

/**
 * Created by Alexander Shipunov on 06.10.16.
 *
 */
@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class RatingEJB {

    @PersistenceContext(unitName = "DBAutomatic")
    private EntityManager em;

    public RatingEJB() {}

    public synchronized void voteForNews(@NotNull User user, @NotNull News post, @NotNull Votes vote) {
        Rating foundRating = em.find(Rating.class, post.getRating().getRatingID());
        foundRating.vote(vote, user);

        em.persist(foundRating);
    }
}
