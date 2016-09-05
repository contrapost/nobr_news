package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by alex on 05.09.16.
 *
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long userID;

    private String name;
    private String surname;


    @OneToOne(orphanRemoval = true) //if user is removed, then also the address is removed
    private Address address;

}
