package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 05.09.16.
 *
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Comment.GET_ALL_COMMENTS, query = "select c from Comment c")
})
public class Comment {
    public static final String GET_ALL_COMMENTS = "GET_ALL_COMMENTS";

    @Id
    @GeneratedValue
    private Long ID;

    @NotNull
    @Size(min = 1, max = 1000)
    private String text;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @NotNull
    @ManyToOne
    private User author;

    @OneToOne(cascade = CascadeType.ALL)
    private Rating rating;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("date desc")
    private List<Comment> comments;

    public Comment() {
        rating = new Rating();
    }

    public long getID() {
        return ID;
    }

    public void setID(Long id) {
        this.ID = id;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Rating getRating() {
        return rating;
    }

    public List<Comment> getComments() {

        if(comments == null) {
            comments = new ArrayList<>();
        }

        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
