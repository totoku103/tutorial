package me.totoku103.tutorial.authorization.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_web_admin_user")
@Getter
@Setter
public class UserInfoEntity {
    @Id
    @Column(name = "user_login")
    private String userId;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId", targetEntity = UserAuthorityEntity.class, fetch = FetchType.EAGER)
    private Set<UserAuthorityEntity> authorities;
}
