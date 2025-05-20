package nhn.academy.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("admin")) {
            return new User("admin", passwordEncoder.encode("admin"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }else if (username.equals("member")) {
            return new User("member", passwordEncoder.encode("member"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MEMBER")));
        } throw new UsernameNotFoundException("User not found");

//        return username.equals("admin") ?
//                new User("admin", passwordEncoder.encode("admin"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))) :
//                new User("member", passwordEncoder.encode("member"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MEMBER")));
//        throw new UsernameNotFoundException("User not found");

//        User user = username.equals("admin") ?
//                new User("admin", passwordEncoder.encode("admin"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
//                : username.equals("member") ?
//                new User("member", passwordEncoder.encode("member"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MEMBER"))) : null;
//        if(user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        return user;
    }
}
