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
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    private String city;
    private String country;

    @OneToOne(mappedBy = "address")
    private User user;
}
