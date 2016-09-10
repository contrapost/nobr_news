package entities;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by alex on 10.09.16.
 *
 */
class DataLoader {

    static boolean persistInATransaction(EntityManager em, Object... obj)  {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(Object o : obj) {
                em.persist(o);
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
