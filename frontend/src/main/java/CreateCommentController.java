import ejb.CommentEJB;
import ejb.NewsEJB;
import ejb.UserEJB;
import entities.News;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
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

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    private String formNewsID;

    @EJB
    private CommentEJB commentEJB;

    @EJB
    private NewsEJB newsEJB;

    @EJB
    private UserEJB userEJB;


    @Inject
    private LoggingController loggingController;


    public String createComment() {
        long id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("newsID"));

        try {
            commentEJB.createComment(userEJB.getUser(loggingController.getRegisteredUserEmail()),
                    newsEJB.getNews(id),
                    formText, new Date());
        } catch (Exception e){
            return "newComment.jsf";
        }

        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().put("newsID", id + "");
        return "comments.jsf";

    }
}
