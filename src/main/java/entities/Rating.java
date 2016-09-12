package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 07.09.16.
 *
 */
@Entity
public class Rating {
    @Version
    private Integer version;

    @Id
    @GeneratedValue
    private Long ratingID;

    private int score;

    @ManyToMany
    private List<User> voters;

    public Rating() {
        voters = new ArrayList<>();
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

    public void setScore(Votes vote, User user) {
        if (voters.stream().map(User::getUserID).anyMatch(id -> id.equals(user.getUserID()))) {
            return;
        }
        switch (vote) {
            case UP:
                score++;
                break;
            case DOWN:
                score--;
        }
        voters.add(user);
    }

    public int getVotersNumber() {
        return voters.size();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setRatingID(Long ratingID) {
        this.ratingID = ratingID;
    }

    public List<User> getVoters() {
        return voters;
    }

    public void setVoters(List<User> voters) {
        this.voters = voters;
    }
}
