import ejb.NewsEJB;
import ejb.RatingEJB;
import ejb.UserEJB;
import entities.News;
import entities.Votes;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alexandershipunov on 14/10/2016.
 */
@Named
@SessionScoped
public class NewsController implements Serializable {

    @EJB
    private NewsEJB newsEJB;

    @EJB
    private RatingEJB ratingEJB;

    @EJB
    private UserEJB userEJB;

    @Inject
    private LoggingController loggingController;

    public List<News> getNewses() {

        return newsEJB.getAllNews();
    }

    public void vote(int i, long id, String userEmail) {
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
    }
}
