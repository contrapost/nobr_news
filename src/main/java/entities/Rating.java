package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * Created by alex on 07.09.16.
 *
 */
@Entity
class Rating {
    @Version
    private Integer version;

    @Id
    @GeneratedValue
    private Long ratingID;

    private int rating;
    private int votersNumber;

    public Rating() {
    }

    public long getRatingID() {
        return ratingID;
    }

    public void setRatingID(long ratingID) {
        this.ratingID = ratingID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating += rating;
        this.votersNumber++;
    }

    public int getVotersNumber() {
        return votersNumber;
    }
}
