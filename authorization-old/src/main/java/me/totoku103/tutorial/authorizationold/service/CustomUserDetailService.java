package me.totoku103.tutorial.authorizationold.service;

import lombok.RequiredArgsConstructor;
import me.totoku103.tutorial.authorizationold.entity.WebAdminUser;
import me.totoku103.tutorial.authorizationold.repository.WebAdminUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final WebAdminUserRepository webAdminUserRepository;

    @PostConstruct
    public void init() {
        WebAdminUser webAdminUser = new WebAdminUser();
        webAdminUser.setUserLogin("totoku103");
        webAdminUser.setPassword(passwordEncoder.encode("totoku103"));
        webAdminUserRepository.save(webAdminUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WebAdminUser user = webAdminUserRepository.findByUserLogin(username);
        if(user == null) throw new RuntimeException("not found user");

        return User.builder().username(user
                .getUserLogin())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority("resfFul")).build();
    }
}
