package nl.tudelft.sem.security.authentication;

import static nl.tudelft.sem.security.controllers.AuthenticationController.getAdminInfo;
import static nl.tudelft.sem.security.controllers.AuthenticationController.getCustomerInfo;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import nl.tudelft.sem.security.users.AppUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Gives the correct role to the user that wants to login.
     *
     * @param username username that is searched for
     * @return customer or admin with their name, password and role
     * @throws UsernameNotFoundException returned when username is not in the database
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser customer = getCustomerInfo(username);
        if (customer != null) {
            List<GrantedAuthority> grantedAuthoritiesCustomer =
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
            return new User(customer.getUsername(), customer.getPassword(),
                grantedAuthoritiesCustomer);
        }

        AppUser admin = getAdminInfo(username);
        if (admin != null) {
            List<GrantedAuthority> grantedAuthoritiesAdmin =
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
            return new User(admin.getUsername(), admin.getPassword(), grantedAuthoritiesAdmin);
        }
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }
}

