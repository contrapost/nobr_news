package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Alexander Shipunov on 06.10.16.
 *
 */
@Stateless
public class RatingEJB {

    @PersistenceContext(unitName = "DBAutomatic")
    private EntityManager em;

    public RatingEJB() {}



}
