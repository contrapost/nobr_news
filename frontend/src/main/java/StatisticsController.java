import ejb.StatisticEJB;
import entities.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alexandershipunov on 16/10/2016.
 */
@Named
@SessionScoped
public class StatisticsController implements Serializable{

    @EJB
    private StatisticEJB statisticEJB;

    public List<User> getTopTenUsers() {
        return (List<User>) statisticEJB.getFromCache("TopTenUsers");
    }

    public long getNumberOfAllUsers() {
        return (long) statisticEJB.getFromCache("NumberOfAllUsers");
    }

    public List<String> getListOfAllCountries() {
        return (List<String>) statisticEJB.getFromCache("ListOfAllCountries");
    }

    public long getNumberOfAllNews() {
        return (long) statisticEJB.getFromCache("NumberOfAllNews");
    }
}
