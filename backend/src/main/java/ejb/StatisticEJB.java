package ejb;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexandershipunov on 13/10/2016.
 */
@Singleton
@Startup
public class StatisticEJB {

    private Map<String, Object> cache = new HashMap<>();

    @EJB
    private UserEJB userEJB;

    @EJB
    private NewsEJB newsEJB;

    @Schedule(second = "*/10", minute="*", hour="*", persistent=false)
    public void addTenTopUsers() {
        cache.put("TopTenUsers", userEJB.getXTopUsers(10));
    }

    @Schedule(second = "*/10", minute="*", hour="*", persistent=false)
    public void addNumberOfAllUsers(){
        cache.put("NumberOfAllUsers", userEJB.getNumberOfAllUsers());
    }

    @Schedule(second = "*/10", minute="*", hour="*", persistent=false)
    public void addAllCountries(){
        cache.put("ListOfAllCountries", userEJB.getAllCountries());
    }


    @Schedule(second = "*/10", minute="*", hour="*", persistent=false)
    public void numberOfAllNews(){
        cache.put("NumberOfAllNews", newsEJB.getNumberOfAllNewses());
    }

    public Object getFromCache(String key){
        if(cache.containsKey(key)) return cache.get(key);
        else return null;
    }
}
