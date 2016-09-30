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

@NamedQueries({
        @NamedQuery(name = News.TOTAL_NUMBER_OF_NEWS, query = "select count(n) from News n"),
        @NamedQuery(name = News.TOTAL_NUMBER_OF_NEWS_FROM_COUNTRY, query = "select count(n) from News n where n.author.address.country = :country")
})
@Entity
public class News {
    public static final String TOTAL_NUMBER_OF_NEWS = "TOTAL_NUMBER_OF_NEWS";
    public static final String TOTAL_NUMBER_OF_NEWS_FROM_COUNTRY = "TOTAL_NUMBER_OF_NEWS_FROM_COUNTRY";

    @Id
    @GeneratedValue
    private Long newsID;

    @NotNull
    @Size(min = 1, max = 5000)
    private String text;

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
