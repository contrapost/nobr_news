package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by alex on 05.09.16.
 *
 */
@Embeddable
public class Address {

    private Long id;

    @NotNull
    @Size(min = 4, max = 4)
    private String zipCode;

    @NotNull
    @Size(min = 4, max = 128)
    private String street;

    @NotNull
    @Size(min = 2, max = 128)
    private String city;

    @NotNull
    @Size(min = 3, max = 128)
    private String country;

    public Address(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
