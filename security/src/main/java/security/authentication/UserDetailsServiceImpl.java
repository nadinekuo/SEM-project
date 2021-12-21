package security.authentication;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import security.controllers.AuthenticationController;
import security.users.AppUser;

import static security.controllers.AuthenticationController.getAdminInfo;
import static security.controllers.AuthenticationController.getCustomerInfo;

@Service   // This is for testing purposes. Will be changed later to get users from database
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser customer = getCustomerInfo(username);
        if (customer != null) {
            List<GrantedAuthority> grantedAuthoritiesCustomer = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_USER");
            return new User(customer.getUsername(), customer.getPassword(), grantedAuthoritiesCustomer);
        }

        AppUser admin = getAdminInfo(username);
        if (admin != null) {
            List<GrantedAuthority> grantedAuthoritiesAdmin = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_ADMIN");
            return new User(admin.getUsername(), admin.getPassword(), grantedAuthoritiesAdmin);
        }
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }
}

