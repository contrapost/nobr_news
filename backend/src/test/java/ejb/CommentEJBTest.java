package ejb;

import entities.Address;
import entities.Comment;
import entities.News;
import entities.User;
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
 * Created by alexandershipunov on 30/09/16.
 *
 */
@RunWith(Arquillian.class)
public class CommentEJBTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "ejb")
                .addClass(DeleterEJB.class)
                .addPackages(true, "entities")
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private UserEJB userEJB;

    @EJB
    private NewsEJB newsEJB;

    @EJB
    private CommentEJB commentEJB;

    @EJB
    private DeleterEJB deleterEJB;


    @Before
    @After
    public void emptyDatabase(){
        deleterEJB.deleteEntities(News.class);
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(User.class);
    }

    @Test
    public void createComment(){
        Address address = new Address();
        address.setStreet("Street");
        address.setZipCode("1234");
        address.setCity("City");
        address.setCountry("Country");

        userEJB.createUser("name", "surname", "name@surname.com", "12we34ty", address);

        commentEJB.createComment(userEJB.getUser("name@surname.com"),
                newsEJB.createNews(userEJB.getUser("name@surname.com"), "Text of the news here", new Date()),
                "Comment's text here", new Date());

        assertEquals(1, commentEJB.getAllComments().size());
    }
}
