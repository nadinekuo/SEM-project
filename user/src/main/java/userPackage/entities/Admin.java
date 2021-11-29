package userPackage.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {

    public Admin() {
    }


    /** Constructor with id.
     *
     * @param id - long
     * @param username - String
     * @param password - String
     */
    public Admin(long id, String username, String password) {

        super(id, username, password);
    }


    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public String toString() {
        return "Admin{" + "id=" + super.getId() + ", username='" + super.getUsername() + '\''
            + ", password" + "='" + super.getPassword();
    }

}
