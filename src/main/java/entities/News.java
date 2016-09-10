package entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 05.09.16.
 *
 */
@Entity
class News {
    @Id
    @GeneratedValue
    private Long newsID;

    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Rating rating;
    @OneToMany(orphanRemoval = true)
    private List<Comment> comments;
    public News() {
        rating = new Rating();
    }

    public Long getNewsID() {
        return newsID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNewsID(Long newsID) {
        this.newsID = newsID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rating getRating() {
        return rating;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
