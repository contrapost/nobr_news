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

    private int score;
    private int votersNumber;

    public Rating() {
    }

    public long getRatingID() {
        return ratingID;
    }

    public void setRatingID(long ratingID) {
        this.ratingID = ratingID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
        this.votersNumber++;
    }

    public int getVotersNumber() {
        return votersNumber;
    }
}
