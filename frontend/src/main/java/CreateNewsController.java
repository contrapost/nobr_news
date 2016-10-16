import ejb.NewsEJB;
import ejb.UserEJB;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alexandershipunov on 14/10/2016.
 */
@Named
@RequestScoped
public class CreateNewsController {

    private String formText;

    @EJB
    private NewsEJB newsEJB;

    @EJB
    private UserEJB userEJB;

    @Inject
    private LoggingController loggingController;

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public String createNews(){

        try {
            newsEJB.createNews(userEJB.getUser(loggingController.getRegisteredUserEmail()), formText, new Date());
        } catch (Exception e){
            return "newNews.jsf";
        }

        return "home.jsf";
    }
}
