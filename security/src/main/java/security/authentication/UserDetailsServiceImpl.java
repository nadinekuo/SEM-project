//package security.authentication;
//
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import security.controllers.AuthenticationController;
//import security.users.AppUser;
//
//@Service   // This is for testing purposes. Will be changed later to get users from database
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private BCryptPasswordEncoder encoder;
//
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        AppUser customer = (AppUser) AuthenticationController.getCustomerInfo(username);
//        AppUser admin = (AppUser) AuthenticationController.getAdminInfo(username);
//
//        // If user not found. Throw this exception.
//        if (customer == null && admin == null) {
//            throw new UsernameNotFoundException("Username: " + username + " not found");
//        }
//
//        if (admin != null) {
//            List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//                    .commaSeparatedStringToAuthorityList("ROLE_ADMIN");
//            return new User(admin.getUsername(), admin.getPassword(), grantedAuthorities);
//        }
//
//        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//                .commaSeparatedStringToAuthorityList("ROLE_USER");
//        return new User(customer.getUsername(), customer.getPassword(), grantedAuthorities);
//    }
//
//
//}
