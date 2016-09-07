package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by alex on 07.09.16.
 *
 */
@Entity
class Rating {
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
        this.rating = rating;
    }

    public int getVotersNumber() {
        return votersNumber;
    }

    public void setVotersNumber(int votersNumber) {
        this.votersNumber = votersNumber;
    }

    public void vote(int vote) {
        rating += vote;
    }
}
