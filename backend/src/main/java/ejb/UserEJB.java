package ejb;

import entities.Address;
import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by alexandershipunov on 30/09/16.
 *
 */
@Stateless
public class UserEJB {

    @PersistenceContext(unitName = "DBAutomatic")
    private EntityManager em;

    public UserEJB(){}

    public void createNewUser(@NotNull String name, @NotNull String surname, @NotNull String email, @NotNull String password, @NotNull Address address){
        if(isRegistered(email)){
            return;
        }

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        user.setAddress(address);

        em.persist(user);
    }

    public User getUser(String email) {
        Query query = em.createNamedQuery(User.GET_USER_BY_EMAIL);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return users.get(0);
    }

    public List<String> getAllCountries() {
        Query query = em.createNamedQuery(User.GET_ALL_COUNTRIES);
        return query.getResultList();
    }

    public long getNumberOfAllUsers() {
        Query query = em.createNamedQuery(User.GET_NUMBER_OF_ALL_USERS);
        return (long) query.getSingleResult();
    }

    public long getNumberOfUsersFromCountry(String countryName) {
        Query query = em.createNamedQuery(User.GET_NUMBER_OF_ALL_USERS_FROM_COUNTRY);
        query.setParameter("country", countryName);
        return (long) query.getSingleResult();
    }

    public List<User> getXTopUsers(int numberOfTopUsers) {
        Query query = em.createNamedQuery(User.GET_TOP_USERS);
        List<User> usersOrderedByNumberOfCommentsAndNewsws = query.getResultList();
        if(usersOrderedByNumberOfCommentsAndNewsws.size() < numberOfTopUsers) {
            return usersOrderedByNumberOfCommentsAndNewsws;
        } else {
            return usersOrderedByNumberOfCommentsAndNewsws.subList(0, numberOfTopUsers);
        }
    }

    private boolean isRegistered(String email) {

        Query query = em.createNamedQuery(User.GET_USER_BY_EMAIL);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return users.size() != 0;
    }
}
