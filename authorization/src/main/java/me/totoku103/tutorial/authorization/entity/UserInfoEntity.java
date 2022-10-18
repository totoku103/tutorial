package me.totoku103.tutorial.authorization.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_user_info")
@Getter
@Setter
public class UserInfoEntity {
    @Id
    private String userId;
    private String password;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "userId", targetEntity = UserAuthorityEntity.class, fetch = FetchType.EAGER)
    private Set<UserAuthorityEntity> authorities;
}
