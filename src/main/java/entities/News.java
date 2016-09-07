package entities;

import javax.persistence.*;
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

    @OneToOne
    private User user;
    @OneToOne(orphanRemoval = true)
    private Rating rating;
    @OneToMany(orphanRemoval = true)
    private List<Comment> comments;

    public News() {
    }

    public Long getNewsID() {
        return newsID;
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

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
