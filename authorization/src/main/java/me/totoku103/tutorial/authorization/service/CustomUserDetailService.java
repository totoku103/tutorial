package me.totoku103.tutorial.authorization.service;

import lombok.RequiredArgsConstructor;
import me.totoku103.tutorial.authorization.entity.UserInfoEntity;
import me.totoku103.tutorial.authorization.repository.UserInfoRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserInfoEntity user = userInfoRepository.findByUserId(username);
        if (user == null) throw new RuntimeException("not found user");

        final List<SimpleGrantedAuthority> authorityList =
                user.getAuthorities()
                        .stream()
                        .map(d -> new SimpleGrantedAuthority(d.getAuthority()))
                        .collect(Collectors.toList());

        return User.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .authorities(authorityList)
                .build();
    }
}
