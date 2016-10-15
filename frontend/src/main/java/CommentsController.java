import ejb.NewsEJB;
import entities.Comment;
import entities.News;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alexandershipunov on 14/10/2016.
 *
 */
@Named
@RequestScoped
public class CommentsController {

    @EJB
    private NewsEJB newsEJB;

    public News getNews(){
        long id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("newsID"));
        return newsEJB.getNews(id);
    }
}
