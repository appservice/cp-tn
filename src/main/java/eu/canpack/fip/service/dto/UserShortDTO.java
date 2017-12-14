package eu.canpack.fip.service.dto;

import eu.canpack.fip.domain.User;

/**
 * CP S.A.
 * Created by lukasz.mochel on 02.12.2017.
 */
public class UserShortDTO  {

    private String firstName;
    private String lastName;
    private Long id;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserShortDTO() {
    }

    public UserShortDTO(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.id = user.getId();
    }

    @Override
    public String toString() {
        return "UserShortDTO{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", id=" + id +
            '}';
    }
}
