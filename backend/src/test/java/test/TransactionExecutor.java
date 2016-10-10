package test;

/**
 * Created by alexandershipunov on 10/10/2016.
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;

public class TransactionExecutor {

    private final  EntityManager em;

    public TransactionExecutor(EntityManager em) {
        this.em = em;
    }

    public void syncExe(Consumer<EntityManager> command) {
        Thread t = createThread(command);
        startAndWait(t);
    }

    public Thread asyncExe(Consumer<EntityManager> command){
        Thread t = createThread(command);
        t.start();
        return t;
    }

    private Thread createThread(Consumer<EntityManager> command) {

        return new Thread(() ->{
            EntityTransaction tx = em.getTransaction();

            tx.begin();
            try{
                command.accept(em);
                tx.commit();
            } catch (Exception e){
                tx.rollback();
                System.out.println("\n\nFailed transaction on separated thread: "+e.getCause().toString()+"\n\n");
            }
            em.close();
        });
    }

    private void startAndWait(Thread t){
        //start the thread
        t.start();

        //but then wait for when it is finished
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
