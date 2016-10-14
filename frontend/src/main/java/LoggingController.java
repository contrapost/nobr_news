import ejb.UserEJB;
import entities.Address;
import entities.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoggingController implements Serializable {

    @EJB
    private UserEJB userEJB;

    private String formEmail;
    private String formPassword;
    private String formConfirmPassword;
    private String formName;
    private String formSurname;
    private String formStreet;
    private String formZipCode;
    private String formCity;
    private String formCountry;

    public UserEJB getUserEJB() {
        return userEJB;
    }

    public void setUserEJB(UserEJB userEJB) {
        this.userEJB = userEJB;
    }

    public String getFormEmail() {
        return formEmail;
    }

    public void setFormEmail(String formEmail) {
        this.formEmail = formEmail;
    }

    public String getFormPassword() {
        return formPassword;
    }

    public void setFormPassword(String formPassword) {
        this.formPassword = formPassword;
    }

    public String getFormConfirmPassword() {
        return formConfirmPassword;
    }

    public void setFormConfirmPassword(String formConfirmPassword) {
        this.formConfirmPassword = formConfirmPassword;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormSurname() {
        return formSurname;
    }

    public void setFormSurname(String formSurname) {
        this.formSurname = formSurname;
    }

    public String getFormStreet() {
        return formStreet;
    }

    public void setFormStreet(String formStreet) {
        this.formStreet = formStreet;
    }

    public String getFormZipCode() {
        return formZipCode;
    }

    public void setFormZipCode(String formZipCode) {
        this.formZipCode = formZipCode;
    }

    public String getFormCity() {
        return formCity;
    }

    public void setFormCity(String formCity) {
        this.formCity = formCity;
    }

    public String getFormCountry() {
        return formCountry;
    }

    public void setFormCountry(String formCountry) {
        this.formCountry = formCountry;
    }

    public void setRegisteredUserEmail(String registeredUserEmail) {
        this.registeredUserEmail = registeredUserEmail;
    }

    /**
     * The current user registered in this session
     */
    private String registeredUserEmail;


    public LoggingController() {
    }

//    public String getUserCountry() {
//        if (registeredUserEmail == null) {
//            return null;
//        }
//
//        return userEJB.getUser(registeredUserEmail).getCountry();
//    }
//
//    public List<String> getCountries() {
//        return Countries.getCountries();
//    }


    public boolean isLoggedIn() {
        return registeredUserEmail != null;
    }

    public String getRegisteredUserEmail() {
        return registeredUserEmail;
    }

    public User getUser() {
        return (User) userEJB.getUser(registeredUserEmail);
    }

    public String logOut() {
        registeredUserEmail = null;
        return "home.jsf";
    }


    public String logIn() {
        boolean valid = userEJB.login(formEmail, formPassword);
        if (valid) {
            registeredUserEmail = formEmail;
            return "home.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew() {

        if (!formPassword.equals(formConfirmPassword)) {
            return "newUser.jsf";
        }

        boolean registered = false;

        Address address = new Address();
        address.setStreet(formStreet);
        address.setZipCode(formZipCode);
        address.setCity(formCity);
        address.setCountry(formCountry);

        try {
            registered = userEJB.createUser(formName, formSurname, formEmail, formPassword, address);
        } catch (Exception e) {
        }

        if (registered) {
            registeredUserEmail = formEmail;
            return "home.jsf";
        } else {
            return "newUser.jsf";
        }
    }

}
