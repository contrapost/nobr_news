package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 05.09.16.
 */
@NamedQueries({
        @NamedQuery(name = User.GET_ALL_COUNTRIES, query = "select DISTINCT u.address.country from User u"),
        @NamedQuery(name = User.GET_NUMBER_OF_ALL_USERS, query = "select count(*) from User u"),
        @NamedQuery(name = User.GET_NUMBER_OF_ALL_USERS_FROM_COUNTRY, query = "select count(*) from User u where u.address.country = :country"),
        @NamedQuery(name = User.GET_TOP_USERS, query = "select u from User u order by (u.comments.size + u.newses.size) desc"),
        @NamedQuery(name = User.IS_EXISTING_USER, query = "select count(*) from User u where u.email = :email")
})
@Entity
public class User {
    public static final String GET_ALL_COUNTRIES = "GET_ALL_COUNTRIES";
    public static final String GET_NUMBER_OF_ALL_USERS = "GET_NUMBER_OF_ALL_USERS";
    public static final String GET_NUMBER_OF_ALL_USERS_FROM_COUNTRY = "GET_NUMBER_OF_ALL_USERS_FROM_COUNTRY";
    public static final String GET_TOP_USERS = "GET_TOP_USERS";
    public static final String IS_EXISTING_USER = "IS_EXISTING_USER";

    @Id
    @GeneratedValue
    private Long userID;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    @Size(min = 2, max = 50)
    private String surname;

    @NotNull
    @Pattern(regexp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @NotNull
    @Size(min = 6, max = 100)
    private String password;

    @NotNull
    private Address address;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author")
    private List<News> newses;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    /*  IF we want to find all ratings user gave to news and comments
    @ManyToMany(mappedBy = "voters")
    private List<Rating> ratingsFromUser;*/

    public User() {
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<News> getNewses() {
        if (newses == null) {
            newses = new ArrayList<>();
        }
        return newses;
    }

    public void setNewses(List<News> newses) {
        this.newses = newses;
    }

    public List<Comment> getComments() {

        if (comments == null) {
            comments = new ArrayList<>();
        }

        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
