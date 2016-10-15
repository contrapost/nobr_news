import ejb.CommentEJB;
import ejb.NewsEJB;
import ejb.UserEJB;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alexandershipunov on 14/10/2016.
 */
@Named
@SessionScoped
public class CreateCommentController implements Serializable{

    private String formText;

    @Inject
    private LoggingController loggingController;

    @EJB
    private CommentEJB commentEJB;

    @EJB
    private NewsEJB newsEJB;

    @EJB
    private UserEJB userEJB;

    private long newsID;

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public String saveNewsID(long id){
        newsID = id;
        return "newComment.jsf";
    }

    public long getNewsID() {
        return newsID;
    }

    public String createComment() {
        try {
            commentEJB.createComment(userEJB.getUser(loggingController.getRegisteredUserEmail()),
                    newsEJB.getNews(newsID),
                    formText, new Date());
        } catch (Exception e){
            return "newComment.jsf";
        }

        return "comments.jsf";
    }
}
