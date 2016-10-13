package ejb;

import entities.Address;
import entities.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.SecureRandom;
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

    public boolean createUser(@NotNull String name, @NotNull String surname, @NotNull String email, @NotNull String password, @NotNull Address address) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

//        User user = getUser(email);
//        if (user != null) {
//            //a user with same id already exists
//            return false;
//        }

        User user = new User();
        user.setEmail(email);

        //create a "strong" random string of at least 128 bits, needed for the "salt"
        String salt = getSalt();
        user.setSalt(salt);

        String hash = computeHash(password, salt);
        user.setHash(hash);

        user.setName(name);
        user.setSurname(surname);
        user.setPassword(password);
        user.setAddress(address);

        em.persist(user);

        return true;
    }

    public boolean login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        User userDetails = getUser(email);
        if (userDetails == null) {
            return false;
        }

        String hash = computeHash(password, userDetails.getSalt());

        boolean isOK = hash.equals(userDetails.getHash());
        return isOK;
    }

    public User getUser(String email) {
        Query query = em.createNamedQuery(User.GET_USER_BY_EMAIL);
        query.setParameter("email", email);
        User user = null;
        try {
            user = (User) query.getSingleResult();
        } catch (javax.ejb.EJBException e) {
            return null;
        }
        return user;
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
        List<User> usersOrderedByNumberOfCommentsAndNewses = query.getResultList();
        if(usersOrderedByNumberOfCommentsAndNewses.size() < numberOfTopUsers) {
            return usersOrderedByNumberOfCommentsAndNewses;
        } else {
            return usersOrderedByNumberOfCommentsAndNewses.subList(0, numberOfTopUsers);
        }
    }

    private boolean isRegistered(String email) {

        Query query = em.createNamedQuery(User.GET_USER_BY_EMAIL);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return users.size() != 0;
    }


    @NotNull
    protected String computeHash(String password, String salt){
        String combined = password + salt;
        String hash = DigestUtils.sha256Hex(combined);
        return hash;
    }

    @NotNull
    protected String getSalt(){
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32; // 2^5
        int n = 26;
        assert n * bitsPerChar >= 128;

        String salt = new BigInteger(n * bitsPerChar, random).toString(twoPowerOfBits);
        return salt;
    }
}
