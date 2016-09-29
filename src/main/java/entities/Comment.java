package entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 05.09.16.
 *
 */
@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long commentID;

    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Rating rating;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("date desc")
    private List<Comment> comments;

    public Comment() {
        rating = new Rating();
    }

    public Long getCommentID() {
        return commentID;
    }

    public void setCommentID(Long id) {
        this.commentID = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
