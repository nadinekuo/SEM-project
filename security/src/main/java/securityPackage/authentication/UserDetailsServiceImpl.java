//package securityPackage.authentication;
//
//import java.util.Arrays;
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
//import securityPackage.controllers.AuthenticationController;
//import securityPackage.users.AppUser;
//
//@Service   // This is for testing purposes. Will be changed later to get users from database
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private BCryptPasswordEncoder encoder;
//
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        AppUser user = (AppUser) AuthenticationController.getUserInfo(username);
//
//        // If user not found. Throw this exception.
//        if (user == null) {
//            throw new UsernameNotFoundException("Username: " + username + " not found");
//        }
//
//
//        return ;
//    }
//
//
//}
