package entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by alex on 05.09.16.
 *
 */
@Entity
class User {

    @Id
    @GeneratedValue
    private Long userID;

    private String name;
    private String surname;
    private String email;
    private String password;

    @OneToOne(orphanRemoval = true) //if user is removed, then also the address is removed
    private Address address;

    @OneToMany
    private List<News> newses;

    @OneToMany
    private List<Comment> comments;

    public User() {}

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
