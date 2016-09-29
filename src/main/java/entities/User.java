package entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by alex on 05.09.16.
 *
 */
@NamedQueries({
        @NamedQuery(name = User.GET_ALL_COUNTRIES, query = "select DISTINCT u.address.country from User u"),
})
@Entity
public class User {
    public static final String GET_ALL_COUNTRIES = "GET_ALL_COUNTRIES";

    @Id
    @GeneratedValue
    private Long userID;

    private String name;
    private String surname;
    private String email;
    private String password;

    private Address address;

    @OneToMany(orphanRemoval = false, fetch = FetchType.EAGER, mappedBy = "user")
    private List<News> newses;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    /*  IF we want to find all ratings user gave to news and comments
    @ManyToMany(mappedBy = "voters")
    private List<Rating> usersRatings;*/

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
        if(address == null){
            address = new Address();
        }
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<News> getNewses() {
        return newses;
    }

    public void setNewses(List<News> newses) {
        this.newses = newses;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
