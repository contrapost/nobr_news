package ejb;

import entities.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.DeleterEJB;

import javax.ejb.EJB;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by alexandershipunov on 10/10/2016.
 *
 */
@RunWith(Arquillian.class)
public class RatingEJBTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "ejb")
                .addPackages(true, "test")
                .addPackages(true, "entities")
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private RatingEJB ratingEJB;

    @EJB
    private UserEJB userEJB;

    @EJB
    private NewsEJB newsEJB;

    @EJB
    private CommentEJB commentEJB;

    @EJB
    private DeleterEJB deleterEJB;

    @Before
    public void setUp() {
        deleterEJB.deleteEntities(News.class);
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(Rating.class);
        deleterEJB.deleteEntities(User.class);
        setUserEJBs();
        newsEJB.createNews(userEJB.getUser("alex@alum.com"), "Text", new Date());
    }

    @After
    public void emptyDatabase() {
        deleterEJB.deleteEntities(News.class);
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(Rating.class);
        deleterEJB.deleteEntities(User.class);
    }

    public void setUserEJBs() {
        //Alex's address
        Address addressAlex = new Address();
        addressAlex.setStreet("Street");
        addressAlex.setZipCode("1234");
        addressAlex.setCity("City");
        addressAlex.setCountry("Norway");

        //Bart's address
        Address addressBart = new Address();
        addressBart.setStreet("Street");
        addressBart.setZipCode("1234");
        addressBart.setCity("City");
        addressBart.setCountry("Norway");

        //Conney's address
        Address addressConney = new Address();
        addressConney.setStreet("Street");
        addressConney.setZipCode("1234");
        addressConney.setCity("City");
        addressConney.setCountry("Siberia");

        //Den's address
        Address addressDen = new Address();
        addressDen.setStreet("Street");
        addressDen.setZipCode("1234");
        addressDen.setCity("City");
        addressDen.setCountry("Babel");

        userEJB.createNewUser("Alex", "Alum", "alex@alum.com", "12we34ty", addressAlex);
        userEJB.createNewUser("Bart", "Blum", "bart@blum.com", "12we34ty", addressBart);
        userEJB.createNewUser("Conney", "Clum", "conney@clum.com", "12we34ty", addressConney);
        userEJB.createNewUser("Den", "Dlum", "den@dlum.com", "12we34ty", addressDen);
    }

    @Test
    public void testVoteForNews(){
        News news = newsEJB.getAllNews().get(0);

        ratingEJB.voteForNews(userEJB.getUser("alex@alum.com"), news, Votes.UP );

        assertEquals(1, newsEJB.getAllNews().get(0).getRating().getScore());

        ratingEJB.voteForNews(userEJB.getUser("bart@blum.com"), news, Votes.UP );

        assertEquals(2, newsEJB.getAllNews().get(0).getRating().getScore());
    }

    @Test
    public void testTwoUsersVotesAtTheSameTime() throws InterruptedException {

        Thread threadA = new Thread(() -> {
            try {
                Thread.sleep(4_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ratingEJB.voteForNews(userEJB.getUser("alex@alum.com"), newsEJB.getAllNews().get(0), Votes.UP );
        });

        Thread threadB = new Thread(() -> {
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ratingEJB.voteForNews(userEJB.getUser("bart@blum.com"), newsEJB.getAllNews().get(0), Votes.UP);
        });

        threadA.start();
        threadB.start();

        threadA.join();

        assertEquals(1, newsEJB.getAllNews().get(0).getRating().getScore());

        threadB.join();

        assertEquals(2, newsEJB.getAllNews().get(0).getRating().getScore());
    }
}
