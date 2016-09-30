package entities;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Date;

/**
 * Created by alex on 10.09.16.
 *
 */
public class TestUtil {

    public static boolean updateInATransaction(Operations operation, EntityManager em, Object... obj)  {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(Object o : obj)
                switch (operation) {
                    case PERSIST:
                        em.persist(o);
                        break;
                    case UPDATE:
                        em.merge(o);
                        break;
                    case DELETE:
                        em.remove(o);
                }
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        }

        return true;
    }
}
