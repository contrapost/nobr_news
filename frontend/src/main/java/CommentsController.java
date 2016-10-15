import ejb.NewsEJB;
import entities.News;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by alexandershipunov on 14/10/2016.
 *
 */
@Named
@SessionScoped
public class CommentsController implements Serializable {

    @EJB
    private NewsEJB newsEJB;

    private long newsID;

    public String saveNewsID(long id) {
        newsID = id;
        return "comments.jsf";
    }

    public long getNewsID() {
        return newsID;
    }

    public News getNews(){
        return newsEJB.getNews(newsID);
    }
}
