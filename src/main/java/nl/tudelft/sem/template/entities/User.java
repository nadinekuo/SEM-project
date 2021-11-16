package nl.tudelft.sem.template.entities;

import java.util.Objects;
import javax.persistence.Entity;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private long ID;
    private String username;
    private String password;   // Spring Security
    private boolean premiumUser;


    public User() {

    }

    public User(long ID, String username, String password, boolean premiumUser) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.premiumUser = premiumUser;
    }


    public long getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPremiumUser() {
        return premiumUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID == user.ID && premiumUser == user.premiumUser && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, password, premiumUser);
    }


    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", premiumUser=" + premiumUser +
                '}';
    }
}
