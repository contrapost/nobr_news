import ejb.NewsEJB;
import ejb.RatingEJB;
import ejb.UserEJB;
import entities.News;
import entities.Votes;

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

    @EJB
    private RatingEJB ratingEJB;

    @EJB
    private UserEJB userEJB;

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

    // TODO (doesn't implement in RatingEJB
/*    public String vote(int i, long id, String userEmail) {
        switch (i) {
            case 1:
                ratingEJB.voteForNews(userEJB.getUser(userEmail), newsEJB.getNews(id), Votes.UP);
                break;
            case -1:
                ratingEJB.voteForNews(userEJB.getUser(userEmail), newsEJB.getNews(id), Votes.DOWN);
                break;
            default:
                break;
        }
        return "comments.jsf";
    }*/
}
