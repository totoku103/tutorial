package me.totoku103.tutorial.authorization.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_user_authority")
@Getter
@Setter
public class UserAuthorityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;
    private String userId;
    private String authority;
}
