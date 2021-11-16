package nl.tudelft.sem.template.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private long id;
    private String username;
    private String password;   // Spring Security
    private boolean premiumUser;




    /**
     *  Empty constructor needed for Spring JPA.
     */
    public User() {

    }

    /** Constructor User.
     * @param id - Long
     * @param username - String
     * @param password - String
     * @param premiumUser - boolean
     */
    public User(long id, String username, String password, boolean premiumUser) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.premiumUser = premiumUser;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPremiumUser() {
        return premiumUser;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, premiumUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id && premiumUser == user.premiumUser && Objects
            .equals(password, user.password);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", password='" + password
            + '\'' + ", premiumUser=" + premiumUser + '}';
    }
}
